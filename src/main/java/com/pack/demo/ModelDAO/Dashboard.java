package com.pack.demo.ModelDAO;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "attempt")
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private int noques;
    private String name;
    private int marks;
    private LocalDate attempDate;
    private LocalTime start;
    private LocalTime end;

    @ManyToOne
    private UserModel users;

    
    public Dashboard(){}
    public Dashboard(int noques, String name, int marks, LocalDate localDate, LocalTime start, LocalTime end, UserModel
                      users){
        this.noques = noques;
        this.name = name;
        this.attempDate = localDate;
        this.start = start;
        this.end = end;
        this.users = users;
        this.marks = marks;
    }
}
