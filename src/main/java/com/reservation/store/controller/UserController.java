package com.reservation.store.controller;

import com.reservation.store.config.UserPrincipal;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user/register/partner")
    public void registerPartner(@RequestBody UserInfo userInfo) {
        userService.savePartner(userInfo);
    }

    @PostMapping("/user/register/customer")
    public void registerCustomer(@RequestBody UserInfo userInfo) {
        userService.saveCustomer(userInfo);
    }


    
}
