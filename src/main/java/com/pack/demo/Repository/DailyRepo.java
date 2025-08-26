package com.pack.demo.Repository;


import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.TimeQuestion;

@Repository
public interface DailyRepo extends JpaRepository<TimeQuestion,Long> {
    Optional<TimeQuestion> findByDate(LocalDate currentDate);

    
} 