package com.reservation.store.controller;

import com.reservation.store.dto.ReviewInfo;
import com.reservation.store.dto.ReviewResponseDto;
import com.reservation.store.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "매장 리뷰를 답니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "매장 이용 후에 리뷰를 달 수 있습니다."),
            @ApiResponse(responseCode = "409", description = "이미 리뷰를 작성했습니다.")
    })
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@Validated @RequestBody
                                                          @Positive(message = "예약 id와 점수는 1부터 10사이입니다.")
                                                          @Schema(description = "예약 id와 내용, 점수를 적어주세요.",
                                                                  example = "reviewInfo (예약 id, 내용, 점수)")
                                                          ReviewInfo reviewInfo) {
        ReviewResponseDto review = reviewService.createReview(reviewInfo);
        return ResponseEntity.status(CREATED).body(review);
    }

    @Operation(summary = "리뷰 수정", description = " 리뷰 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "리뷰 수정 권한이 없습니다.")
    })
    @PutMapping("/reviews/{reviewId}")
    public void updateReview(@PathVariable
                             @Positive(message = "리뷰 id를 입력해주세요.")
                             @Schema(description = "리뷰 작성시 생긴 id 값을 입력해주세요",
                                     example = "1")
                             Long reviewId,
                             @RequestBody
                             @Positive(message = "")
                             @Schema(description = "수정할 리뷰 내용을 적어주세요.",
                                     example = "reviewInfo (예약 id, 내용, 점수)")
                             ReviewInfo reviewInfo) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewService.updateReview(reviewId, reviewInfo, email);
    }


    @Operation(summary = "리뷰 삭제", description = " 리뷰 내용을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "리뷰 삭제 권한이 없습니다.")
    })
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable
                                 @Positive(message = "리뷰 id를 입력해주세요.")
                                 @Schema(description = "리뷰 id로 해당 리뷰를 찾고 삭제 시도를 합니다.",
                                         example = "1")
                                 Long reviewId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewService.deleteReview(reviewId, email);
    }
}
