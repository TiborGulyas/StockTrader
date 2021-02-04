package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    void deleteById(long id);
    void removeOfferById(long id);
    List<Offer> findAllByUserAccount(UserAccount userAccount);
}
