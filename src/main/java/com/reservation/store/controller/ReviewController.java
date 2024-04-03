package com.reservation.store.controller;

import com.reservation.store.dto.ReviewInfo;
import com.reservation.store.dto.ReviewResponseDto;
import com.reservation.store.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@Validated  @RequestBody ReviewInfo reviewInfo) {
        ReviewResponseDto review = reviewService.createReview(reviewInfo);
        return ResponseEntity.status(CREATED).body(review);
    }
}
