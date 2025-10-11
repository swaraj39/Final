package com.pack.demo.DispatcherControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.pack.demo.Implementation.UserServiceIpl;
import com.pack.demo.ModelDAO.UserModel;
import com.pack.demo.Repository.UserRepo;

@Controller
public class SockController {
    

    @Autowired
    private UserServiceIpl userService;
    @Autowired
    private UserRepo userRepo;

    @MessageMapping("/updateProfile")
    @SendTo("/topic/profile-updates")
    public UserModel updateProfile(UserModel updatedUser, Authentication authentication) {
        boolean a = userService.updateUser(updatedUser, authentication.getName());
        return userRepo.findById(authentication.getName()).get(); // sent to all subscribers
    }
}
