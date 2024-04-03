package com.reservation.store.dto;

import com.reservation.store.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewResponseDto {

    private Long id;

    private String content;

    private int rating;

    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(review.getId(), review.getContent(), review.getRating());
    }
}
