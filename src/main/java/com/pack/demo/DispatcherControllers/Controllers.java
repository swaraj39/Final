package com.pack.demo.DispatcherControllers;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.pack.demo.Implementation.QuestionServiceImpl;
import com.pack.demo.Implementation.UserServiceIpl;
import com.pack.demo.ModelDAO.*;
import com.pack.demo.Repository.DailyRepo;
import com.pack.demo.Repository.DashBoardRepo;
import com.pack.demo.Repository.Review;
import com.pack.demo.Repository.StreakRepo;
import com.pack.demo.Repository.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;


@Controller
public class Controllers {

    @Autowired
    private com.pack.demo.Services.UserService userService;
    @Autowired
    private DashBoardRepo dashBoardRepo;
    @Autowired
    private UserServiceIpl userService1;
    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private TokenRepo tokenRepo;
    @Autowired
    private Review review;
    @Autowired
    private DailyRepo daily;
    @Autowired
    private StreakRepo streakRepo;
    @Autowired
    private TemplateEngine templateEngine;

//TODO For Every request this executes
    @ModelAttribute
    public void setModelAttributes(Authentication authentication, Model model) {
        // If authentication is null, try reading from the security context
        if (authentication == null) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }

        // Only set name if we are really logged in
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            model.addAttribute("name", authentication.getName());
            model.addAttribute("reviews",review.findAll()
                    .stream().limit(3).toList());
            Optional<TimeQuestion> d = daily.findByDate(LocalDate.now());
            TimeQuestion t = d.get();
            model.addAttribute("daily", t);
        } else {
            model.addAttribute("name", null);
        }
    }
    //TODO Used to show the welcome page along with the model attributes
    @RequestMapping({"/", "/welcome"})
    public String home(Model model) {
        System.out.println("Home page accessed" + userService1.findallusers().size());
        model.addAttribute("user", userService1.findallusers().stream().filter(u->u.isVerified()).toList().size());
        model.addAttribute("codingQuestions", questionService.getAllQuestions().size());
        model.addAttribute("programmingLanguages", questionService.findallbro().size());
        model.addAttribute("satisfactionRate", 90);
        return "About";
    }

    @RequestMapping("/us")
    public String usPage() {
        return "Us";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        String name = authentication.getName();
        UserModel user = userService1.loadUserByUsername(name);
        model.addAttribute("user", user);
        return "Update";
    }
    
    @RequestMapping("/demo")
    public String demoPage() {
        return "demo0";
    }
    @RequestMapping("/new")
    public String loginPage() {
        return "new";
    }

    @RequestMapping("/signup")
    public String signupPage(Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "signup";
    }
    
    @RequestMapping("/denied")
    public String denied(){
        return "Denied";
    }
    @RequestMapping("/prize")
    public String prize(){
        return "Prize";
    }
    @RequestMapping("/forgot")
    public String forgot(){
        return "Forgot";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserModel user, Model model, Authentication authentication) {
        if( userService1.updateUser(user, authentication.getName())) {
            model.addAttribute("successMessage", "Profile updated successfully!");
            return "signup";
        }
        return "Update";
    }

    //TODO You should set the password mails selecting the token from the database and
    // check whether it is correct or not Or valid or not
    @RequestMapping("/setpassword")
    public String set(@RequestParam("token") String token, Model model){
        Optional<Token> optionalToken = tokenRepo.findById(token);
        if (optionalToken.isPresent()) {
            Token tokenObj = optionalToken.get();
            LocalDateTime l = LocalDateTime.now().minusMinutes(3);
            if(tokenObj.getLocalDateTime().isBefore(l)){
                tokenRepo.deleteById(token);
                System.out.println("executing");
                return "Limit";
            }
            // Proceed with token verification and password reset
        }
        else{
        return "Limit";
        }
        model.addAttribute("token",token);
        return "setpassword";
    }

    //TODO It is used to change the password after token is validate
    @RequestMapping("/conform")
    public String change(@RequestParam("token") String token,
                         @RequestParam("password") String password){

        String s = tokenRepo.findById(token).get().getEmail();
        userService.changeit(s,password);
        
        return "signup";
    }


    //TODO Used to send the forgot link to their respective email
    @GetMapping("/forgotlink")
    public String forgotlink(@RequestParam("email") String email, Model model){
        if(userService.forgotlink(email).equals("successful")) {
            model.addAttribute("email", true);
            return "signup";
        }
        return "new";
    }


    //TODO Used to return the emails HTML page
    @RequestMapping("/Email")
    public String emailPage() {
        return "Email";
    }

    //TODO This is the process that what happens after the user write their credential after the Spring Security
    @RequestMapping("/home")
    public String home(Principal principal, Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("name", authentication.getName());
            List<Reviwer> allReviews = review.findAll();
            Collections.shuffle(allReviews);
            List<Reviwer> limitedReviews = allReviews.stream().limit(3).toList();
            model.addAttribute("reviews", limitedReviews);
        }
        return "new"; // or your home page
    }

    //TODO Used to show the review page
    @RequestMapping("/review")
    public String requestMethodName() {
        return "Review";
    }

    //TODO Saving the review of the OF THE RESPECTIVE user
    @GetMapping("/saving")
    public String reviews(Authentication authentication, HttpServletRequest request, HttpServletResponse response,Model model, @RequestParam("text") String text){
        Reviwer r = new Reviwer(authentication.getName(),text);
        review.save(r);
        model.addAttribute("name",authentication.getName());
        List<Reviwer> allReviews = review.findAll();
        Collections.shuffle(allReviews);
        List<Reviwer> limitedReviews = allReviews.stream().limit(3).toList();
        model.addAttribute("reviews", limitedReviews);
        return "new";

    }

    //TODO Usually show the activity of users
    @RequestMapping("/progress")
    public String progress(Authentication authentication, Model model) {
        String name = authentication.getName();
        List<Dashboard> list = dashBoardRepo.findByUsersId(name);
        int avgs = dashBoardRepo.findByUsersId(name).stream().map(Dashboard::getMarks).reduce(0,Integer::sum);
        int total = dashBoardRepo.findByUsersId(name).stream().map(Dashboard::getNoques).reduce(0,Integer::sum);
        float avg = avgs / (float) total * 100;
        if(avg == 0){ avg = 0; }
        model.addAttribute("name", authentication.getName());
        model.addAttribute("avg", avg);
        model.addAttribute("number",list.size());
        model.addAttribute("memberSince", userService1.loadUserByUsername(name).getJoinDate());
        //model.addAttribute("lists",userService1.findallusers().stream().filter(u->u.isVerified()).toList().size());
        //model.addAttribute("role", userService1.loadUserByUsername(name).getRole().name());
        //model.addAttribute("avatar", userService1.loadUserByUsername(name).getAvatar());
        model.addAttribute("current", streakRepo.findByUserId(name).getCurrentStreak());
        model.addAttribute("longest", streakRepo.findByUserId(name).getLongestStreak());
        model.addAttribute("list", list);
        return "History";
    }

    //TODO Used to show the dashboard page
    @GetMapping("/dashboard")
    public String dash(Authentication authentication, Model model,HttpSession session){
        String name = authentication.getName();
        List<Dashboard> list = dashBoardRepo.findByUsersId(name);

        List<UserModel> userModelList = userService1.findallusers();
        session.setAttribute("user1",name);
        for(UserModel u : userModelList){
            u.setNoquiz(dashBoardRepo.findByUsersId(u.getId()).size());
        }
        int avgs = dashBoardRepo.findByUsersId(name).stream().map(Dashboard::getMarks).reduce(0,Integer::sum);
        int total = dashBoardRepo.findByUsersId(name).stream().map(Dashboard::getNoques).reduce(0,Integer::sum);
        float avg = avgs / (float) total * 100;
        if(avg == 0){ avg = 0; }
        List<ShowCateogry> showCateogries = questionService.findallbro();
        model.addAttribute("reviews",review.findByName(name).size());
        model.addAttribute("show",showCateogries.stream().limit(5).toList());
        model.addAttribute("users",userModelList);
        model.addAttribute("name", authentication.getName());
        model.addAttribute("number",list.size());
        model.addAttribute("lists",userService1.findallusers().stream().filter(u->u.isVerified()).toList().size());
        model.addAttribute("avg", avg);
        model.addAttribute("role", userService1.loadUserByUsername(name).getRole().name());
        model.addAttribute("current", streakRepo.findByUserId(name).getCurrentStreak());
        model.addAttribute("longest", streakRepo.findByUserId(name).getLongestStreak());
        model.addAttribute("avatar", userService1.loadUserByUsername(name).getAvatar());
        return "DashBoard";
    }

    //TODO Used to generate OTP and send to the email along send In the Database
    @PostMapping("/signups")
    public String signup(@ModelAttribute UserModel userModel, HttpSession session, Model model) {
        return userService.processSignup(userModel, session,model);
    }

    //TODO It is used to send the new OTP to the email
    @RequestMapping(value = "/resend", method = { RequestMethod.GET, RequestMethod.POST })
    public String resendOtp(HttpSession session, RedirectAttributes redirectAttributes) {
        return userService.resendOtp(session, redirectAttributes);
    }

    //TODO Used to verify the CODE
    @PostMapping("/codewala")
    public String verifyOtp(@RequestParam("codes") Long code, @RequestParam("email") String email,
                            HttpSession session, Model model) {
        System.out.println("Verifying OTP for email: " + email);
        System.out.println("OTP code received: " + code);
        return userService.verifyOtp(code,email, session, model);
    }

    //TODO Used to send mail to the admin for user Query
    @PostMapping("/contact")
    public String contact(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("message") String message,
                          Model model) {
        return userService.handleContact(name, email, message, model);
    }

    //TODO Used to save the user
    @GetMapping("/usersave")
    public String userSaved(@ModelAttribute UserModel userModel) {
        return userService.saveUser(userModel);
    }

    //TODO Tell the user let us use by me firstly
    @GetMapping("/UserEnter")
    public String enter(HttpSession session) {
        return userService.handleUserEntry(session);
    }

    //TODO Used to log out that invalidate the session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "logout";
    }


    @GetMapping("/history/pdf")
public void generatePdfReport(Authentication auth, HttpServletResponse response) throws IOException {
    String username = auth.getName();
    List<Dashboard> list = dashBoardRepo.findByUsersId(username);
    int totalMarks = list.stream().mapToInt(Dashboard::getMarks).sum();
    int totalQuizzes = list.size();
    float avg = totalQuizzes == 0 ? 0 : (totalMarks * 100f / totalQuizzes);

    Context ctx = new Context();
    ctx.setVariable("name", username);
    ctx.setVariable("number", totalQuizzes);
    ctx.setVariable("avg", avg);
    ctx.setVariable("list", list);

    String html = templateEngine.process("report-pdf", ctx);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    PdfRendererBuilder builder = new PdfRendererBuilder();
    builder.withHtmlContent(html, null);
    builder.toStream(baos);
    builder.run();

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=quiz_history.pdf");
    response.getOutputStream().write(baos.toByteArray());
}

}
