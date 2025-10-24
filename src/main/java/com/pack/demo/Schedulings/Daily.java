package com.pack.demo.Schedulings;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pack.demo.ModelDAO.QuestionModel;
import com.pack.demo.ModelDAO.TimeQuestion;
import com.pack.demo.Repository.DailyRepo;
import com.pack.demo.Repository.QuestionRepo;
import com.pack.demo.Repository.UserRepo;

@Service
public class Daily {
    
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DailyRepo dailyRepo;

//    // ✅ Runs every day at midnight
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void generateDailyQuestion() {
//        userRepo.findById("Swaraj").ifPresent(user -> {
//            user.setLevel(1);
//            userRepo.save(user);
//        });
//    }

    // ✅ This can be called anytime (e.g., from controller) to get today's question
    @PostConstruct
    public TimeQuestion createOrGetTodayQuestion() {
        System.out.println("creating");
        Optional<TimeQuestion> existing = dailyRepo.findByDate(LocalDate.now());
        
        if (existing.isPresent()) {
            return existing.get();
        } else {
            // Pick a random question
            List<QuestionModel> questions = questionRepo.findAll();
            Collections.shuffle(questions);
            QuestionModel q = questions.get(0);

            // Create a new daily question
            TimeQuestion timeQuestion = new TimeQuestion(
                    q.getId(),
                    q.getQuestion(),
                    q.getOption1(),
                    q.getOption2(),
                    q.getOption3(),
                    q.getOption4(),
                    LocalDate.now(),
                    q.getCorrectans(),
                    q.getReason(),
                    q.getLevel(),
                    0,   // ✅ usersolved always starts fresh
                    0L   // ✅ version always starts at 0
            );

            return dailyRepo.save(timeQuestion);
        }
    }
}
