package com.pack.demo.Implementation;

import com.pack.demo.ModelDAO.Role;
import com.pack.demo.ModelDAO.Streak;
import com.pack.demo.ModelDAO.TemporaryCode;
import com.pack.demo.Repository.StreakRepo;
import com.pack.demo.Repository.TemporaryRepo;
import com.pack.demo.Repository.UserRepo;
import com.pack.demo.EmailService.EmailService;
import com.pack.demo.ModelDAO.UserModel;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceIpl implements com.pack.demo.Services.UserService {

    @Autowired
    private UserRepo userRepo;
    // @Autowired
    // private DaoAuthenticationProvider authenticationProvider;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TemporaryRepo temporaryRepo;
    @Autowired
    private StreakRepo streakRepo;
    private final Random random = new Random();


    @Override
    public String processSignup(UserModel userModel, HttpSession session,Model model) {
        long code = 0;
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        UserModel user = new UserModel();
        System.out.println(userModel.getAvatar());
        user.setId(userModel.getId());
        user.setName(userModel.getName());
        user.setPassword(userModel.getPassword());
        user.setEmail(userModel.getEmail());
        user.setPhoneno(userModel.getPhoneno());
        user.setJoinDate(LocalDate.now());
        user.setAvatar(userModel.getAvatar());
        user.setVerified(false);
        user.setLevel(1);
        userRepo.save(user);

        //todo generating code
        code = random.nextLong(10000, 99999);


        TemporaryCode tempCode = new TemporaryCode();
        tempCode.setUserModel(user);
        tempCode.setCode(code);
        tempCode.setDate(LocalDateTime.now());
        temporaryRepo.save(tempCode);

        String result = emailService.sendMail(code, userModel);
        model.addAttribute("email", userModel.getEmail());
        return result.equals("successful") ? "Email" : "signup";
    }

    @Override
    public String resendOtp(HttpSession session, RedirectAttributes redirectAttributes) {
        
        UserModel user = (UserModel) session.getAttribute("user");
        if (user == null) return "signup";

        int code = random.nextInt(10000, 99999);
        session.setAttribute("codes", code);
        session.setAttribute("otpTime", System.currentTimeMillis());

        if (emailService.sendMail(code, user).equals("successful")) {
            redirectAttributes.addFlashAttribute("otpMsg", "OTP has been resent successfully.");
            return "Email";
        }

        return "signup";
    }

    
    @Override
    public String verifyOtp(Long code, String email, HttpSession session, Model model) {
       List<TemporaryCode> t = temporaryRepo.findAll();
       if(t.size() > 0) {
        //delete date before 1 min
           temporaryRepo.deleteAll(t.stream()
           .filter(tc->tc.getDate().isBefore(LocalDateTime.now().minusMinutes(1)))
           .toList());
       }
        //!important
        //TODO check the email in otp and validate
        UserModel userModel = userRepo.findByEmail(email);
        if (userModel == null) {
            model.addAttribute("error", "User not found.");
            System.out.println("User not found");
            return "signup";
        }
        TemporaryCode tempCode = temporaryRepo.findByUserModel(userModel);
        if (tempCode == null) {
            model.addAttribute("error", "Temporary code not found.");
            System.out.println("Temporary code not found");
            return "About";
        }
        Long otpCode = (Long) code;
        LocalDateTime expiryTime = tempCode.getDate().plusMinutes(1);
        if(otpCode.equals(tempCode.getCode()) && LocalDateTime.now().isBefore(expiryTime)) {
            // OTP is valid
            userModel.setVerified(true);
            userModel.setRole(Role.USER);
            userRepo.save(userModel);
            Streak streak = new Streak();
            streak.setUser(userModel);
            streakRepo.save(streak);
            temporaryRepo.delete(tempCode);

            System.out.println("User verified");
            return "redirect:/signup";
        }
        return "signup";
}


    @Override
    public String handleContact(String name, String email, String message, Model model) {
        if (emailService.sendMail(name, email, message).equals("successful")) {
            return "contact";
        }
        return "new";
    }

    @Override
    public String saveUser(UserModel userModel) {
        userRepo.save(userModel);
        return "Saved";
    }

    @Override
    public String handleUserEntry(HttpSession session) {
        String loggedInUser = (String) session.getAttribute("User");
        return "Swaraj Ravindra Gujar".equals(loggedInUser) ? "UserEntry" : "Saved";
    }

    @Override
    public String forgotlink(String email) {
        UserModel u = userRepo.findByEmail(email);
        if(u != null){
            emailService.forgotwala(u);
        }
        return "successful";
    }

    @Override
    public void changeit(String s, String password) {
        UserModel u = userRepo.findByEmail(s);
        u.setPassword(passwordEncoder.encode(password));
        userRepo.save(u);
    }

    public int allusers(String str){
        return Math.toIntExact(userRepo.findAll().stream().count());
    }

    public List<UserModel> findallusers(){
        return userRepo.findAll().stream().limit(5).toList();
    }

    public UserModel loadUserByUsername(String id) {
        return userRepo.findById(id).orElse(null);
    }

    public boolean updateUser(UserModel user, String name) {
        UserModel existingUser = userRepo.findById(name).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneno(user.getPhoneno());
            existingUser.setAvatar(user.getAvatar());
            userRepo.save(existingUser);
            return true;
        }
        return false;
    }
}
