package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.Dashboard;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DashBoardRepo extends JpaRepository<Dashboard,Long> {
    List<Dashboard> findByUsersId(String name);
    List<Dashboard> findByUsersIdAndAttempDateBetween(String name, LocalDate preDate, LocalDate localDate);
}
