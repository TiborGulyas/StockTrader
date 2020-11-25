package com.codecool.stocktrader;

import com.codecool.stocktrader.component.DataInitializer;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Calendar;

@SpringBootApplication
@EnableScheduling
public class StocktraderApplication {

    @Autowired
    private DataInitializer dataInitializer;

    public static void main(String[] args) {
        SpringApplication.run(StocktraderApplication.class, args);
        System.out.println("!!!!!!!!!!!!!running!!!!!!!!!!");
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            System.out.println("init persistance");
            dataInitializer.initData();
        };
    }
}
