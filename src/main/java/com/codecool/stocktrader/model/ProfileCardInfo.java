package com.codecool.stocktrader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileCardInfo {
    private Long id;
    private String username;
    private String nickName;
    private String profilePic;
    private Date dateOfRegistration;
}
