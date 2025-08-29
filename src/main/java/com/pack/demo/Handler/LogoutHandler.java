package com.pack.demo.Handler;

import com.pack.demo.ModelDAO.UserModel;
import com.pack.demo.Repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

    @Autowired
    private UserRepo userRepo;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication != null && authentication.getName() != null){
            UserModel userModel = userRepo.findById(authentication.getName()).get();
            userModel.setLastlogin(LocalDateTime.now());
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
