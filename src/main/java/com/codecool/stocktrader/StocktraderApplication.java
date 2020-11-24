package com.codecool.stocktrader;

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
    UserAccountRepository userAccountRepository;

    @Autowired
    private StockRepository stockRepository;

    public static void main(String[] args) {
        SpringApplication.run(StocktraderApplication.class, args);
        System.out.println("!!!!!!!!!!!!!running!!!!!!!!!!");
    }

    @Bean
    public CommandLineRunner init(){
        return args -> {
            System.out.println("init persistance");
            UserAccount userAccount = UserAccount.builder()
                    .capital(1000000)
                    .username("Mr.T")
                    .build();
            Stock stock = Stock.builder()
                    .symbol("AAPL")
                    .name("Apple")
                    .build();
            stockRepository.save(stock);
            Stock savedAAPL = stockRepository.findBySymbol("AAPL");
            System.out.println(userAccount.toString());
            StockPurchase stockPurchase = StockPurchase.builder()
                    .purchaseDate(Calendar.getInstance().getTime())
                    .stock(savedAAPL)
                    .purchasePrice(150.23)
                    .quantity(100)
                    .userAccount(userAccount)
                    .build();
            userAccount.getStockPurchaseList().add(stockPurchase);
            System.out.println(userAccount.toString());
            userAccountRepository.save(userAccount);


            UserAccount savedUserAccount = userAccountRepository.findByUsername("Mr.T");
            StockPurchase stockPurchase2 = StockPurchase.builder()
                    .purchaseDate(Calendar.getInstance().getTime())
                    .stock(savedAAPL)
                    .purchasePrice(170.23)
                    .quantity(200)
                    .userAccount(savedUserAccount)
                    .build();
            savedUserAccount.getStockPurchaseList().add(stockPurchase2);
            userAccountRepository.save(savedUserAccount);
        };

    }

}
