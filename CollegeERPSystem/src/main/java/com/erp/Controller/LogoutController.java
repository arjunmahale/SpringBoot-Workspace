package com.erp.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    // ✅ Logout (accessible to all logged-in users)
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        if (session != null) {
            // remove all attributes
            session.removeAttribute("loggedInUser");
            session.removeAttribute("user");
            session.removeAttribute("role");
            session.removeAttribute("admin_course");
            session.removeAttribute("student");

            // completely kill the session
            session.invalidate();
        }

        // redirect to login/index page
        return "redirect:/index";
    }
}
