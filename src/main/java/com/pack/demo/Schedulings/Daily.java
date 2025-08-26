package com.pack.demo.Schedulings;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pack.demo.ModelDAO.QuestionModel;
import com.pack.demo.ModelDAO.TimeQuestion;
import com.pack.demo.Repository.DailyRepo;
import com.pack.demo.Repository.QuestionRepo;

import jakarta.annotation.PostConstruct;

@Service
public class Daily {
    
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private DailyRepo dailyRepo;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void DailyQuestion() {
        List<QuestionModel> questions = questionRepo.findAll();
        Collections.shuffle(questions);
    }

    @PostConstruct
    public TimeQuestion questions() {
       Optional<TimeQuestion> timOptional = dailyRepo.findByDate(LocalDate.now());
       if (timOptional.isPresent()) {
           TimeQuestion timeQuestion = timOptional.get();
           // Do something with timeQuestion
           return timeQuestion;
       }
       else{
           // Handle the case where no TimeQuestion is found
              List<QuestionModel> questions = questionRepo.findAll();
                Collections.shuffle(questions);
                TimeQuestion timeQuestion = new TimeQuestion(questions.get(0).getId(), questions.get(0).getQuestion(),
                        questions.get(0).getOption1(), questions.get(0).getOption2(), questions.get(0).getOption3(),
                        questions.get(0).getOption4(), LocalDate.now(), questions.get(0).getCorrectans(),
                        questions.get(0).getReason(), questions.get(0).getLevel(),questions.get(0).getUsersolved());
                dailyRepo.save(timeQuestion);
                return timeQuestion;
       }
    }
}
