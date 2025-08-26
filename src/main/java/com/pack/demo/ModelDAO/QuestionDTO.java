package com.pack.demo.ModelDAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private String Id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctAnswer;
    private String userAnswer;
    private boolean isCorrect;
     // Assuming this is the ID of the question in the database

    // Constructors, getters, and setters
    
}
