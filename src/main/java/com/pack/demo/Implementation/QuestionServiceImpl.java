package com.pack.demo.Implementation;

import com.pack.demo.ModelDAO.QuestionDTO;
import com.pack.demo.ModelDAO.QuestionModel;
import com.pack.demo.ModelDAO.Category;
import com.pack.demo.ModelDAO.ShowCateogry;
import com.pack.demo.ModelDAO.TimeQuestion;
import com.pack.demo.Repository.CategoryRepo;
import com.pack.demo.Repository.DailyRepo;
import com.pack.demo.Repository.QuestionRepo;
import com.pack.demo.Services.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private CategoryRepo categoryRepository;

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private DailyRepo dailyRepo;

    @Override
    public void saveQuestion(QuestionModel questionModel) {
        List<String> options = Arrays.asList(questionModel.getOption1(), questionModel.getOption2(), questionModel.getOption3(), questionModel.getOption4());
        questionModel.setCorrectans(options.get(Integer.parseInt(questionModel.getCorrectans()) - 1));
        System.out.println("Saving question: " + questionModel.getQuestion());
        questionRepository.save(questionModel);
    }

    @Override
    public List<QuestionModel> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<QuestionModel> getRandomQuestions(int count) {
        List<QuestionModel> allQuestions = questionRepository.findAll();
        Collections.shuffle(allQuestions);
        Set<QuestionModel> uniqueQuestions = new LinkedHashSet<>(allQuestions);
        List<QuestionModel> question = uniqueQuestions.stream().limit(count).toList();
        return question;
    }

    @Override
    public List<QuestionModel> getQuestionsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return questionRepository.findAll();
        }
        return questionRepository.findByCateogry(category);
    }
    
    @Override
    public int evaluateQuiz(List<QuestionModel> questions, Map<String, String> answers, List<QuestionDTO> resultList) {
        int score = 0;

        for (QuestionModel question : questions) {
            String questionId = String.valueOf(question.getId());
            String userAnswer = answers.get("q" + questionId);

            if (userAnswer != null) {
                userAnswer = userAnswer.trim(); // Clean user input
            }

            String correctAnswer = question.getCorrectans();
            if (correctAnswer != null) {
                correctAnswer = correctAnswer.trim(); // Clean correct answer
            }

            boolean isCorrect = correctAnswer != null && correctAnswer.equalsIgnoreCase(userAnswer);
            if (isCorrect) {
                score++;
            }

            // Build DTO
            QuestionDTO dto = new QuestionDTO();
            dto.setId(questionId);
            dto.setQuestion(question.getQuestion());
            dto.setOption1(question.getOption1());
            dto.setOption2(question.getOption2());
            dto.setOption3(question.getOption3());
            dto.setOption4(question.getOption4());
            dto.setCorrectAnswer(correctAnswer);
            dto.setUserAnswer(userAnswer);
            dto.setCorrect(isCorrect);
            resultList.add(dto);
        }

        return score;
    }

    @Override
    public TimeQuestion getDailyOne() {
        LocalDate currentDate = LocalDate.now();
        Optional<TimeQuestion> timequestion = dailyRepo.findByDate(currentDate);
        if (timequestion.isPresent()) {
            return dailyRepo.findById(timequestion.get().getId()).orElse(null);
        } else {
            List<QuestionModel> allQuestions = questionRepository.findAll();
            Collections.shuffle(allQuestions);
            QuestionModel questionModel = allQuestions.get(0);
            TimeQuestion timeQuestion = new TimeQuestion();
            timeQuestion.setId(questionModel.getId());
            timeQuestion.setQuestion(questionModel.getQuestion());
            timeQuestion.setOption1(questionModel.getOption1());
            timeQuestion.setOption2(questionModel.getOption2());
            timeQuestion.setOption3(questionModel.getOption3());
            timeQuestion.setOption4(questionModel.getOption4());
            timeQuestion.setDate(currentDate);
            timeQuestion.setAnswer(questionModel.getCorrectans());
            timeQuestion.setReason("See it and solve it yourself"); // Set a default reason or modify as needed
            dailyRepo.save(timeQuestion); // Save the new question for today
            return timeQuestion;
        }

        // Return a random question
    }

    public List<ShowCateogry> findallbro(){
        List<QuestionModel> list = questionRepository.findAll();
        List<ShowCateogry> showCateogries = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (QuestionModel q: list){
            map.put(q.getCateogry(),map.getOrDefault(q.getCateogry(),0)+1);
        }
        for (Map.Entry<String, Integer> map1: map.entrySet()){
            ShowCateogry s = new ShowCateogry();
            s.setName(map1.getKey());
            s.setNo(map1.getValue());
            showCateogries.add(s);
        }


        return showCateogries;
    }

    @Override
    public List<Category> selectbycateogry() {
        List<Category> list = categoryRepository.findAll();
        return list;
    }

    @Override
    public int getc(String name) {
        return questionRepository.findByCateogry(name).size();
    }

    @Override
    public void increaseDailyQuestionCount(TimeQuestion dailyQuestion) {
        questionRepository.findById(dailyQuestion.getId()).ifPresent(question -> {
            question.setUsersolved(question.getUsersolved() + 1);
            questionRepository.save(question);
        });
    }

    

    @Override
    public List<QuestionModel> findByCateogry(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return questionRepository.findAll();
        }
        return questionRepository.findByCateogry(categoryId);
    }
    
}
