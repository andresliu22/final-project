package com.example.authserver.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class UserDomain {
    Integer userId;
    String userName;
    String userPassword;
    String userRole;
}
