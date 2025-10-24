package com.pack.demo.Repository;

import com.pack.demo.ModelDAO.OfferUser;
import com.pack.demo.ModelDAO.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OfferRepo extends JpaRepository<OfferUser, String> {
}

