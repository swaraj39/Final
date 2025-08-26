package com.pack.demo.ModelDAO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity
@Table(name = "quizz")
public class QuestionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String cateogry;
    private String level;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    @Column(name = "correctans")
    private String correctans;
    private int usersolved;
    public String getReason() {
        return "Find Yourself";
    }
}
