package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    void deleteById(long id);
    void removeOfferById(long id);
}
