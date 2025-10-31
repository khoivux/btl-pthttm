package org.example.btl_httm.controller;
import jakarta.servlet.http.HttpSession;
import org.example.btl_httm.dao.UserDAO;
import org.example.btl_httm.model.RegisterDTO;
import org.example.btl_httm.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping("/login")
    public String showLoginView(Model model) {
        model.addAttribute("user", new User());
        return "loginView";
    }

    @GetMapping("/register")
    public String showRegisterView(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "registerView";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("user") User loginInfo,
            HttpSession session,
            Model model) {
        UserDAO userDAO = new UserDAO();
        User authenticatedUser = userDAO.authenticate(loginInfo);
        System.out.println(authenticatedUser);
        if (authenticatedUser != null) {
            session.setAttribute("user", authenticatedUser);
            return "recommendView";
        } else {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "loginView";
        }
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
