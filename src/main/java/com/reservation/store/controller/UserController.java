package com.reservation.store.controller;

import com.reservation.store.dto.UserInfo;
import com.reservation.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입", description = "점주의 정보를 받아 회원가입을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 회원입니다.")
    })
    @PostMapping("/user/register/partner")
    public void registerPartner(@RequestBody
                                @Positive(message = "회원정보는 필수 입니다.")
                                @Schema(description = "점주 정보를 받고 회원가입을 시킵니다.",
                                        example = "userInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)")
                                UserInfo userInfo) {
        userService.savePartner(userInfo);
    }

    @Operation(summary = "회원 가입", description = "유저의 정보를 받아 회원가입을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 회원입니다.")
    })
    @PostMapping("/user/register/customer")
    public void registerCustomer(@RequestBody
                                 @Positive(message = "회원정보는 필수 입니다.")
                                 @Schema(description = "점주 정보를 받고 회원가입을 시킵니다.",
                                         example = "userInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)")
                                 UserInfo userInfo) {
        userService.saveCustomer(userInfo);
    }


    
}
