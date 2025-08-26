package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.Reviwer;

import java.util.List;

@Repository
public interface Review extends JpaRepository<Reviwer, Integer> {
    // Custom query methods can be defined here if needed
    List<Reviwer> findByName(String name);
}
