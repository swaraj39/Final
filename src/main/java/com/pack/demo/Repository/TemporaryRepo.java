package com.pack.demo.Repository;

import com.pack.demo.ModelDAO.TemporaryCode;
import com.pack.demo.ModelDAO.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TemporaryRepo extends JpaRepository<TemporaryCode,Long> {

    TemporaryCode findByUserModel(UserModel userModel);


    long deleteByDateBefore(LocalDateTime localDateTime);
}
