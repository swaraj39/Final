package com.pack.demo.ModelDAO;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferUser {
    @Id
    private String userModel;
    private LocalDateTime localDateTime;
}
