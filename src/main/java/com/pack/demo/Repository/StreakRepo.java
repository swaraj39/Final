package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.Streak;

@Repository
public interface StreakRepo extends JpaRepository<Streak, Long> {
    Streak findByUserId(String username);
}
