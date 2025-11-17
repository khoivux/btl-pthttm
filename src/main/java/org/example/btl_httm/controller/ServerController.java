package org.example.btl_httm.controller;

import java.time.Instant;
import java.util.*;

import org.example.btl_httm.dao.*;
import org.example.btl_httm.exception.OutOfStockException;
import org.example.btl_httm.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class ServerController {

    @GetMapping("/recommend-list")
    public String showRecommendList(HttpSession session, Model model) {
        List<Integer> recommendProductIds = new ArrayList<>();
        User user = (User) session.getAttribute("user");

        try {
            RestTemplate restTemplate = new RestTemplate();
            String URL = "http://26.155.72.159:5000/api/recommend";

            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            List<OrderDetail> lastOrderDetails = orderDetailDAO.getLastOrderDetailsByUserId(user.getId(), 5);
            List<InteractionLog> interactionHistory = new ArrayList<>();
            for (OrderDetail od : lastOrderDetails) {
                InteractionLog log = new InteractionLog(String.valueOf(od.getProduct().getId()), "purchase", od.getProduct().getBrand(), od.getProduct().getCategoryCode());
                interactionHistory.add(log);
                System.out.println(log);
            }

            RecommendRequest recommendRequest = new RecommendRequest(String.valueOf(user.getId()), 15, interactionHistory);

            Integer[] ids = restTemplate.postForObject(URL, recommendRequest, Integer[].class);
            if (ids != null) {
                System.out.println("Lấy thành công " + ids.length + " sản phẩm gợi ý");
                recommendProductIds = Arrays.asList(ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProductDAO productDAO = new ProductDAO();
        List<Product> recommendList = productDAO.getProductsByIdList(recommendProductIds);
        model.addAttribute("products", recommendList);
        model.addAttribute("user", user);
        return "recommendView";
    }

    @GetMapping("/product")
    public String searchProduct(HttpSession session, Model model,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.getByName(keyword);
        User user = (User) session.getAttribute("user");
        model.addAttribute("products", productList);
        model.addAttribute("user", user);
        return "searchResultView";
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(HttpSession session, Model model, @PathVariable("id") int productId) {
        ProductDAO productDAO = new ProductDAO();
        Product selectedProduct = productDAO.getById(productId);
        User user = (User) session.getAttribute("user");

        session.setAttribute("selectedProduct", selectedProduct);
        model.addAttribute("product", selectedProduct);
        model.addAttribute("user", user);

        sendLog(new InteractionLog(user.getId() + "", productId + "", "view", selectedProduct.getBrand(), selectedProduct.getCategoryCode()));
        return "productDetailView";
    }


    @PostMapping("/product/purchase")
    public String buyProduct(HttpSession session, int quantity, RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Product product = (Product) session.getAttribute("selectedProduct");

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setUnitPrice(product.getPrice());


        Order order = new Order();
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);
        order.setOrderDetailList(orderDetails);
        order.setCreatedTime(Date.from(Instant.now()));
        order.setUser(user);

        OrderDAO orderDAO = new OrderDAO();
        try {
            orderDAO.insertOrder(order);
        } catch (OutOfStockException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/product/" + product.getId();
        }
        sendLog(new InteractionLog(user.getId() + "", product.getId() + "", "purchase", product.getBrand(), product.getCategoryCode()));
        redirectAttributes.addFlashAttribute("message", "Mua hàng thành công!");
        return "redirect:/product/" + product.getId();
    }

    @PostMapping("/product/add-to-cart")
    public String addToCart(HttpSession session, int quantity, RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        int cartId = user.getId();
        Product product = (Product) session.getAttribute("selectedProduct");

        CartDetail cartDetail  = new CartDetail();
        cartDetail.setProduct(product);
        cartDetail.setQuantity(quantity);
        cartDetail.setCartId(cartId);

        CartDetailDAO cartDetailDAO = new CartDetailDAO();
        cartDetailDAO.addOrUpdateCartDetail(cartDetail);
        sendLog(new InteractionLog(user.getId() + "", product.getId() + "", "cart", product.getBrand(), product.getCategoryCode()));
        redirectAttributes.addFlashAttribute("message", "Thêm giỏ hàng thành công!");
        return "redirect:/product/" + product.getId();
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        int cartId = user.getId();

        CartDAO cartDAO = new CartDAO();
        Cart cart = cartDAO.getCartById(cartId);

        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeCartDetail(HttpSession session,  @RequestParam(value = "selectedItems") List<Integer> selectedItemIds) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        CartDetailDAO cartDetailDAO = new CartDetailDAO();
        List<CartDetail> selectedDetailList = cartDetailDAO.getByIdList(selectedItemIds);
        cartDetailDAO.deleteCartDetail(selectedItemIds);
        for (CartDetail cd : selectedDetailList) {
            sendLog(new InteractionLog());
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/edit")
    public String editCartDetail(
            @RequestParam("cartDetailId") int cartDetailId,
            @RequestParam("quantity") int quantity) {

       CartDetailDAO cartDetailDAO = new CartDetailDAO();
        CartDetail cartDetail = new CartDetail();
        cartDetail.setId(cartDetailId);
        cartDetail.setQuantity(quantity);

        cartDetailDAO.editCartDetail(cartDetail);
        return "redirect:/cart";
    }


    @PostMapping("/cart/checkout")
    public String checkout( HttpSession session,
                            @RequestParam(value = "selectedItems") List<Integer> selectedItemIds,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        CartDetailDAO cartDetailDAO = new CartDetailDAO();
        List<CartDetail> selectedDetailList = cartDetailDAO.getByIdList(selectedItemIds);

        Order order = new Order();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(CartDetail cd : selectedDetailList) {
            OrderDetail od = new OrderDetail();
            od.setProduct(cd.getProduct());
            od.setQuantity(cd.getQuantity());
            od.setUnitPrice(cd.getProduct().getPrice());
            orderDetailList.add(od);
        }
        order.setCreatedTime(Date.from(Instant.now()));
        order.setUser(user);
        order.setOrderDetailList(orderDetailList);

        OrderDAO orderDAO = new OrderDAO();

        try {
            orderDAO.insertOrder(order);
        } catch (OutOfStockException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/cart";
        }


        for(CartDetail cd : selectedDetailList) {
            Product product = cd.getProduct();
            sendLog(new InteractionLog(user.getId() + "", product.getId() + "", "purchase", product.getBrand(), product.getCategoryCode()));
        }

        cartDetailDAO.deleteCartDetail(selectedItemIds);
        redirectAttributes.addFlashAttribute("message", "Mua hàng thành công!");
        return "redirect:/cart";
    }


    private void sendLog(InteractionLog log) {
        try {
            String ipServerML = "26.155.72.159";
            String URL = "http://" + ipServerML + ":5000/api/samples";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(URL, log, Void.class);
            System.out.println("GỬI LOG CHO SERVER ML: " + log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
