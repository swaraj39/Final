package com.pack.demo.ModelDAO;



import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "daily")
public class TimeQuestion {
    @Id
    private Long id;
    private String question;
    private String option1;
    private String option2; 
    private String option3;
    private String option4;
    private LocalDate date;
    private String answer;
    private String reason;
    private String level;
    private int usersolved;

    @Version
    private Long version;
}
