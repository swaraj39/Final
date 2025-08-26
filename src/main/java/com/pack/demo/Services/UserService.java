package com.pack.demo.Services;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pack.demo.ModelDAO.UserModel;

public interface UserService {
    String processSignup(UserModel userModel, HttpSession session,Model model);
    String resendOtp(HttpSession session, RedirectAttributes redirectAttributes);
    String verifyOtp(Long code, String email, HttpSession session, Model model);
    String handleContact(String name, String email, String message, Model model);
    String saveUser(UserModel userModel);
    String handleUserEntry(HttpSession session);
    String forgotlink(String email);

    void changeit(String s, String password);
    
}
