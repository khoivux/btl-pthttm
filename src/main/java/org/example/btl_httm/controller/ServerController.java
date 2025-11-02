package org.example.btl_httm.controller;

import java.time.Instant;
import java.util.*;

import org.example.btl_httm.dao.CartDAO;
import org.example.btl_httm.dao.CartDetailDAO;
import org.example.btl_httm.dao.OrderDAO;
import org.example.btl_httm.dao.ProductDAO;
import org.example.btl_httm.model.*;
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
        List<Integer> recommendIdList = new ArrayList<>();
        User user = (User) session.getAttribute("user");

        try {
            String ipServerML = "26.155.72.159";
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://" + ipServerML + ":5000/api/recommend/" + user.getId();
            Integer[] ids = restTemplate.getForObject(apiUrl, Integer[].class);
            if (ids != null) {
                System.out.println("Lấy thành công " + ids.length + " sản phẩm gợi ý");
                recommendIdList = Arrays.asList(ids);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ProductDAO productDAO = new ProductDAO();
        List<Product> recommendList = productDAO.getProductsByIdList(recommendIdList);
        model.addAttribute("products", recommendList);
        model.addAttribute("user", user);
        return "recommendView";
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(HttpSession session, Model model, @PathVariable("id") int productId) {
        ProductDAO productDAO = new ProductDAO();
        Product selectedProduct = productDAO.getById(productId);
        User user = (User) session.getAttribute("user");

        session.setAttribute("selectedProduct", selectedProduct);
        model.addAttribute("product", selectedProduct);
        model.addAttribute("user", user);

        sendLog(user.getId(), selectedProduct.getId(), "view");
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
        orderDAO.insertOrder(order);
        sendLog(user.getId(), product.getId(), "purchase");
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
        sendLog(user.getId(), product.getId(), "cart");
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
        cartDetailDAO.deleteCartDetail(selectedItemIds);

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
        orderDAO.insertOrder(order);

        for(CartDetail cd : selectedDetailList) {
            sendLog(user.getId(), cd.getProduct().getId(), "purchase");
        }

        cartDetailDAO.deleteCartDetail(selectedItemIds);
        redirectAttributes.addFlashAttribute("message", "Mua hàng thành công!");
        return "redirect:/cart";
    }


    private void sendLog(int userId, int productId, String actionType) {
        try {
            String ipServerML = "26.155.72.159"; // ip máy Kiên
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://" + ipServerML + ":5000/api/samples";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", userId);
            requestBody.put("productId", productId);
            requestBody.put("actionType", actionType);

            restTemplate.postForObject(apiUrl, requestBody, Void.class);
            System.out.println("GỬI LOG CHO SERVER ML: " + "userId: " + userId + ", productId: " + productId + ", actionType: " + actionType);
        } catch (Exception e) {
            System.out.println("Gửi log thất bại: " + e.getMessage());
        }
    }
}
