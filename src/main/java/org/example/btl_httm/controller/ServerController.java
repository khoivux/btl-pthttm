package org.example.btl_httm.controller;

import java.time.Instant;
import java.util.*;

import org.example.btl_httm.dao.OrderDAO;
import org.example.btl_httm.dao.ProductDAO;
import org.example.btl_httm.model.Order;
import org.example.btl_httm.model.OrderDetail;
import org.example.btl_httm.model.Product;
import org.example.btl_httm.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class ServerController {
    private String ipServerML = "26.155.72.159";
    @GetMapping("/recommend-list")
    public String showRecommendList(HttpSession session, Model model) {
        List<Integer> recommendIdList = new ArrayList<>();
        User user = (User) session.getAttribute("user");
        recommendIdList.add(1004836);
//        recommendIdList.add(4934);
//        recommendIdList.add(4962);
//        recommendIdList.add(4965);
//        recommendIdList.add(4986);
//        recommendIdList.add(5395);
//        recommendIdList.add(6736);
//        recommendIdList.add(6737);
//        recommendIdList.add(6744);
//        recommendIdList.add(6746);


//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String apiUrl = "http://" + ipServerML + ":5000/api/recommend/" + "381380229";
//            Integer[] ids = restTemplate.getForObject(apiUrl, Integer[].class);
//            if (ids != null) {
//                System.out.println(ids.length);
//                recommendIdList = Arrays.asList(ids);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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

        return "productDetailView";
    }


    @PostMapping("/product/buy")
    public String buyProduct(HttpSession session, Model model, int quantity) {

        User user = (User) session.getAttribute("user");
        Product product = (Product) session.getAttribute("selectedProduct");

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setProduct(product);

        Order order = new Order();
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);
        order.setOrderDetails(orderDetails.toArray(new OrderDetail[0]));
        order.setCreatedTime(Date.from(Instant.now()));
        order.setUser(user);

        OrderDAO orderDAO = new OrderDAO();
        orderDAO.insertOrder(order);
//        sendLog(user.getId(), product.getId(), "purchase");
        model.addAttribute("purchaseSuccess", true);
        model.addAttribute("product",product);
        model.addAttribute("user", user);
        return "productDetailView";
    }

    private void sendLog(int userId, int productId, String actionType) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://" + ipServerML + ":5000/api/samples";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", userId);
            requestBody.put("productId", productId);
            requestBody.put("actionType", actionType);

            restTemplate.postForObject(apiUrl, requestBody, Void.class);
            System.out.println("Gửi log cho Server ML thành công " + "userId: " + userId + ", productId: " + productId + ", actionType: " + actionType);
        } catch (Exception e) {
            System.out.println("Gửi log hành động thất bại: " + e.getMessage());
        }
    }
}
