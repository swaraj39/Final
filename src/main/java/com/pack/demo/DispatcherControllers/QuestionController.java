package com.pack.demo.DispatcherControllers;

import com.pack.demo.ModelDAO.*;
import com.pack.demo.Repository.*;
import com.pack.demo.Services.QuestionService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ques")
public class QuestionController {

    private final Review review;
    private TimeQuestion timeQuestion;
    private final QuestionService questionService;
    private final UserRepo userRepo;
    private final DashBoardRepo dashBoardRepo;
    private final DailyRepo dailyRepo;
    private final StreakRepo streakRepo;
    String[] arr = new String[10];
    String arr1[] = new String[10];
    String[] a = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

    // public QuestionController(Review review) {
    // this.review = review;
    // arr[1] = "1";
    // arr[0] = "0";
    // }
    @Autowired
    public QuestionController(Review review, QuestionService questionService,
            UserRepo userRepo, DashBoardRepo dashBoardRepo, DailyRepo dailyRepo, StreakRepo streakRepo) {
        this.review = review;
        this.questionService = questionService;
        this.userRepo = userRepo;
        this.dashBoardRepo = dashBoardRepo;
        this.dailyRepo = dailyRepo;
        this.streakRepo = streakRepo;
    }

    // @Autowired
    // private DashBoardRepo dashBoardRepo;
    // @Autowired
    // private UserRepo userRepo;
    // @Autowired
    // private QuestionService questionService;
    //
    // @Autowired
    // private TimeQuestion timeQuestion;
    // @Autowired
    // private Review review;

    //todo executes Every time when controller calls
    @ModelAttribute
    public void set(Authentication authentication, Model model, HttpSession session) {
        // If authentication is null, try reading from the security context
        //session.setAttribute("started", null);
        if (authentication == null) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        // Only set name if we are really logged in
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            model.addAttribute("name", authentication.getName());
            model.addAttribute("reviews", review.findAll()
                    .stream().limit(2));
        } else {
            model.addAttribute("name", null);
        }
    }
    //todo To give new HTML page
    @RequestMapping("/home")
    public String home() {
        return "new";
    }

    @RequestMapping("/go")
    public String goToQuiz(@RequestParam("categoryValue") String categoryId, Model model) {
        //todo Logic to start the quiz
        model.addAttribute("message", categoryId);
        return "show";
    }

    //todo Executive before start a quiz
    @RequestMapping("/before")
    public String index(Authentication authentication, Model model, HttpSession session) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Month month = LocalDate.now().getMonth();
        long count = dashBoardRepo.findByUsersId(name).stream()
                .filter(dashboards -> dashboards.getAttempDate().getMonth() == month)
                .count();
        boolean hasMoreThanFive = count == 5;
        if (hasMoreThanFive) {
            return "Prize";
        }
        model.addAttribute("categories", questionService.selectbycateogry());
        //return "Startbefore";
        session.setAttribute("started", null);
        return "demo";
    }

    //todo Execute for question adding
    @RequestMapping("/add")
    public String addQuestion(Model model) {
        List<Category> categories = questionService.selectbycateogry();
        model.addAttribute("categories", categories);
        return "QuestionAdding";
    }

    //todo Used to show category wise question
    @RequestMapping("/select")
    public String select(Model model) {
        List<Category> categories = questionService.selectbycateogry();
        List<Integer> counts = categories.stream().map(c -> questionService.getc(c.getName())).toList();
        model.addAttribute("categories", categories);
        model.addAttribute("counts", counts);
        return "Cateogry";
    }

    //todo For saving the questions
    @RequestMapping("/save")
    public String add(@ModelAttribute QuestionModel questionModel) {
        questionService.saveQuestion(questionModel);
        return "Saved";
    }

    //todo Shuffle the question and show it
    @RequestMapping("/getall")
    public String getAll(Model model) {
        List<QuestionModel> list = questionService.getAllQuestions();
        Collections.shuffle(list);
        model.addAttribute("result", list);
        return "QuestionShow";
    }

    //todo To show the questions only of that category
    @GetMapping("/getall/{name}")
    public String getAllByCategory(@PathVariable("name") String name, Model model) {
        List<QuestionModel> list = questionService.getQuestionsByCategory(name);
        Collections.shuffle(list);
        model.addAttribute("result", list);
        return "QuestionShow";
    }

    //todo It is used for setting the random questions then used to start quiz
    @GetMapping(value = "/test")
    public String showRandomQuestions(HttpSession session, Model model, @RequestParam("categoryValue") String categoryId) {
        
        if ("yes".equals(session.getAttribute("started"))) {
            session.setAttribute("started", null);
        return "redirect:/ques/before";  // Optional page saying "You cannot reload the quiz"
        }
        List<QuestionModel> questions = questionService.findByCateogry(categoryId);
        Collections.shuffle(questions);
        Set<QuestionModel> questionModels = questions.stream().limit(10).collect(Collectors.toSet());
        List<QuestionModel> questionModels1 = new ArrayList<>(questionModels);
        System.out.println("Random questions selected: " + questionModels.size());
        session.setAttribute("randomQuestions", questionModels1);
        model.addAttribute("category", categoryId);
        model.addAttribute("result", questionModels);
        model.addAttribute("start", LocalTime.now());
        session.setAttribute("started", "yes");
        return "quizz";
    }

    //todo Get it by Category
    @RequestMapping(value = "/questionOne", method = { RequestMethod.GET, RequestMethod.POST })
    public String getByCategory(@RequestParam(value = "questionType", required = false) String type, Model model) {
        List<QuestionModel> resultList = questionService.getQuestionsByCategory(type);
        model.addAttribute("result", resultList);
        return "QuestionShow";
    }

    //todo Used to show the daily question also check for the user's streak and also increase the count of the questions that users solve
    @RequestMapping(value = "/dailyQuestion", method = RequestMethod.POST)
    public String dailyquiz(Model model, HttpSession session, Authentication authentication) {
        Optional<UserModel> user = userRepo.findById(authentication.getName());
        Optional<TimeQuestion> timeQuestion1 = dailyRepo.findByDate(LocalDate.now());

        if (user.isPresent() && timeQuestion1.isPresent()) {
            TimeQuestion dailyQuestion = timeQuestion1.get();
            UserModel userModel = user.get();
            if(userModel.getDailyquestion() != null && userModel.getDailyquestion().equals(dailyQuestion.getDate())){
                //model.addAttribute("solved",true);
                System.out.println("User has already attempted today's question.");
                return "redirect:/home";
            }
            else{
                System.out.println("User is attempting today's question.");
                if(userModel.getDailyquestion() == null){
                    Streak i = streakRepo.findByUserId(userModel.getId());
                    i.setCurrentStreak(i.getCurrentStreak()+1);
                    i.setLongestStreak(Math.max(i.getLongestStreak(), i.getCurrentStreak()));
                    streakRepo.save(i);
                    userModel.setDailyquestion(LocalDate.now());
                    userRepo.save(userModel);
                }
                else if(userModel.getDailyquestion().plusDays(1).equals(LocalDate.now())){
                    Streak i = streakRepo.findByUserId(userModel.getId());
                    if(i == null) {
                        i = new Streak();
                        i.setUser(userModel);
                        i.setCurrentStreak(1);
                        i.setLongestStreak(1);
                    }else{
                        i.setCurrentStreak(i.getCurrentStreak()+1);
                        i.setLongestStreak(Math.max(i.getLongestStreak(), i.getCurrentStreak()));
                    }
                    streakRepo.save(i);
                }else 
                {
                    Streak i = streakRepo.findByUserId(userModel.getId());
                    if (i == null) {
                        i = new Streak();
                        i.setUser(userModel);
                        i.setCurrentStreak(1);
                        i.setLongestStreak(1);
                    }
                    else {
                        i.setCurrentStreak(1);
                        i.setLongestStreak(Math.max(i.getLongestStreak(), i.getCurrentStreak()));
                    }
                    streakRepo.save(i);
                }
                userModel.setDailyquestion(LocalDate.now());
                userRepo.save(userModel);
                questionService.increaseDailyQuestionCount(dailyQuestion);
                dailyQuestion.setUsersolved(dailyQuestion.getUsersolved()+1);
                dailyRepo.save(dailyQuestion);
            }
        }

        timeQuestion = questionService.getDailyOne();
        model.addAttribute("question", timeQuestion);
        return "DailyQuiz";
        
    }

    //TODO Evaluate the quiz and produce the score of the user
    @RequestMapping(value = "/submitQuiz", method = RequestMethod.POST)
    public String submitQuiz(@RequestParam Map<String, String> answers, Model model, HttpSession session,
            Authentication authentication, @RequestParam("category") String category, @RequestParam("start") String start) {
        
        List<QuestionModel> randomQuestions = (List<QuestionModel>) session.getAttribute("randomQuestions");

        if (randomQuestions == null || randomQuestions.isEmpty()) {
            return "redirect:/ques/test";
        }
        System.out.println(category + answers);
        List<QuestionDTO> results = new ArrayList<>();
        int score = questionService.evaluateQuiz(randomQuestions, answers, results);
        model.addAttribute("rate", score);
        int total = randomQuestions.size();
        int percentage = (score * 100) / total;
        LocalDate currentTime = LocalDate.now();
        String u = authentication.getName();
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.now();
        Dashboard dashboard = new Dashboard(answers.size()-2, category,
                score, currentTime, startTime, endTime, userRepo.findById(u).get());
        dashBoardRepo.save(dashboard);

        model.addAttribute("result", score);
        model.addAttribute("total", total);
        model.addAttribute("percentage", percentage);
        model.addAttribute("results", results);

        session.setAttribute("started", null);
        return "Result";
    }
}
