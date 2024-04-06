package com.reservation.store.dto;

import com.reservation.store.domain.Review;

public record ReviewResponseDto(Long id, String content, int rating) {
    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(review.getId(), review.getContent(), review.getRating());
    }
}
