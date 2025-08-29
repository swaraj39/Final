package com.pack.demo.Handler;

import com.pack.demo.ModelDAO.UserModel;
import com.pack.demo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class SessionHandler implements ApplicationListener<SessionDestroyedEvent>{

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        for(SecurityContext context : event.getSecurityContexts()){
            if(context.getAuthentication().getName() != null){
                UserModel u = userRepo.findById(context.getAuthentication().getName())
                        .get();
                u.setLastlogin(LocalDateTime.now());
                userRepo.save(u);
            }
        }
        System.out.println("Deleted");

    }
}
