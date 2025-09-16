package com.pack.demo.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.pack.demo.ModelDAO.Category;
import com.pack.demo.ModelDAO.QuestionDTO;
import com.pack.demo.ModelDAO.QuestionModel;
import com.pack.demo.ModelDAO.TimeQuestion;
import com.pack.demo.ModelDAO.UserModel;

public interface QuestionService {

    void saveQuestion(QuestionModel questionModel);

    List<QuestionModel> getAllQuestions();

    List<QuestionModel> getRandomQuestions(int count);

    List<QuestionModel> getQuestionsByCategory(String category);

    int evaluateQuiz(List<QuestionModel> questions, Map<String, String> answers, List<QuestionDTO> resultList);

    TimeQuestion getDailyOne();

    List<Category> selectbycateogry();

    int getc(String name);

    void increaseDailyQuestionCount(TimeQuestion dailyQuestion);

    List<QuestionModel> findByCateogry(String categoryId);

    void saveDailyUser(String userId, LocalDate now, String question, boolean b);
}
