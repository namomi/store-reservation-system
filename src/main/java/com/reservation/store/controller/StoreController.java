package com.reservation.store.controller;

import com.reservation.store.domain.Store;

import com.reservation.store.dto.StoreInfo;
import com.reservation.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "매장 등록", description = "매장 정보와 점주의 이메일을 받아 매장 등록을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "회원가입에 실패하였습니다.")
    })
    @PostMapping("/store/register/{email}")
    public void registerStore(@RequestBody
                              @Positive(message = "매장 정보는 필수입니다.")
                              @Schema(description = "매장을 등록할려면 매장정보는 필요합니다.",
                                      example = "userInfo (매장이름, 정보, 설명)")
                              StoreInfo storeInfo,
                              @PathVariable
                              @Positive(message = "점주 확인을 위해 이메일 정보를 필수입니다.")
                              @Schema(description = "유저가 점주인지 확인을 위해 가입하신 이메일 정보가 필요합니다.",
                                      example = "test@naver.com")
                              String email) {
        storeService.registerStore(storeInfo, email);
    }


    @Operation(summary = "매장 정보 조회", description = "등록하신 모든 매장정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "등록하신 매장정보가 없습니다.")
    })
    @GetMapping("/store")
    public List<Store> findAllStores(@RequestBody
                                     @Positive(message = "이메일은 필수입니다.")
                                     @Schema(description = "등록하신 매장 정보 조회를 위해 등록하신 이메일 정보가 필요합니다..",
                                             example = "test@naver.com")
                                     String email) {
        return storeService.findStores(email);
    }

    @Operation(summary = "매장명으로 매장 정보 조회", description = "매장명으로 해당 매장을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 매장정보는 없습니다.")
    })
    @GetMapping("/store/search")
    public List<Store> searchStores(@RequestParam
                                    @Positive(message = "매장명 입력해주세요.")
                                    @Schema(description = "매장명으로 해당 매장을 조회합니다.",
                                            example = "커피빈")
                                    String name) {
        return storeService.searchStore(name);
    }


    @Operation(summary = "해당 매장 상세정보 조회", description = "등록된 매장 id로 매장의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 매장정보는 없습니다.")
    })
    @GetMapping("/store/{id}")
    public Store getStoreDetails(@PathVariable
                                     @Positive(message = "등론된 매장 id를 입력해주세요.")
                                     @Schema(description = "매장id로 해당 매장의 상세 정보를 조회합니다.",
                                             example = "1")
                                     Long id) {
        return storeService.getStoreDetails(id)
                .orElseThrow(() -> new RuntimeException("해당 가게의 정보가 없습니다."));
    }
}
