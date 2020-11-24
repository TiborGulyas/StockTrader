package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String userName);
}
