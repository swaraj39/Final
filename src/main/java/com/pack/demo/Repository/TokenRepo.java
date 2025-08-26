package com.pack.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pack.demo.ModelDAO.Token;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token,String> {

    Optional<Token> findByToken(String token);
}
