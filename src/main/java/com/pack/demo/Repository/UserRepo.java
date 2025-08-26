package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel,String> {
    UserModel findByEmail(String email);

}
