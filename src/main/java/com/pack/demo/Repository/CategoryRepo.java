package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {


    
}
