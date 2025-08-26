package com.pack.demo.ModelDAO;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.time.LocalDateTime;

import jakarta.persistence.Column;

@Entity
@Data
public class TemporaryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long code;

    @ManyToOne
    private UserModel userModel;

    @Column(name = "created")
    private LocalDateTime date;

    public TemporaryCode() {
        
    }
}
