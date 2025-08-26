package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.QuestionModel;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<QuestionModel,Long> {

    List<QuestionModel> findByCateogry(String cateogry);

}
