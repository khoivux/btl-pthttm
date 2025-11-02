package org.example.btl_httm.controller;
import jakarta.servlet.http.HttpSession;
import org.example.btl_httm.dao.CartDAO;
import org.example.btl_httm.dao.UserDAO;
import org.example.btl_httm.model.Cart;
import org.example.btl_httm.model.RegisterDTO;
import org.example.btl_httm.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;

@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping("/login")
    public String showLoginView(Model model) {
        model.addAttribute("loginInfo", new User());
        return "loginView";
    }

    @GetMapping("/register")
    public String showRegisterView(Model model) {
        model.addAttribute("registerInfo", new RegisterDTO());
        return "registerView";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginInfo") User loginInfo,
            HttpSession session,
            Model model) {
        UserDAO userDAO = new UserDAO();
        User authenticatedUser = userDAO.authenticate(loginInfo);
        System.out.println(authenticatedUser);
        if (authenticatedUser != null) {
            session.setAttribute("user", authenticatedUser);
            model.addAttribute("user", authenticatedUser);
            return "recommendView";
        } else {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "loginView";
        }
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("registerInfo") RegisterDTO registerInfo,
            HttpSession session,
            Model model) {
        UserDAO userDAO = new UserDAO();
        if(!registerInfo.getPassword().equals(registerInfo.getConfirmPassword())) {
            System.out.println("mật khẩu không khớp");
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "registerView";
        }

        int userId = userDAO.register(registerInfo);

        if (userId > 0) {
            Cart cart = new Cart();
            cart.setId(userId);
            CartDAO cartDAO = new CartDAO();
            cartDAO.insertCart(cart);
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Đăng ký không thành công!");
            return "registerView";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
