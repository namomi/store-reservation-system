package com.reservation.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserInfo {
    private String name;
    private String email;
    private String password;
    private String phone;
}
