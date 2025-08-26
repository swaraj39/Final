package com.pack.demo.ModelDAO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int currentStreak;
    private int longestStreak;
    @OneToOne
    private UserModel user;
    
}
