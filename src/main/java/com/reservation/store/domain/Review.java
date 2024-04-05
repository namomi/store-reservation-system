package com.reservation.store.domain;

import com.reservation.store.dto.ReviewInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @Min(1)
    @Max(10)
    private int rating;

    public static Review createReview(ReviewInfo reviewInfo, Reservation reservation, User user) {
        return Review.builder()
                .reservation(reservation)
                .user(user)
                .content(reviewInfo.getContent())
                .rating(reviewInfo.getRating())
                .build();
    }


    public void updateReview(ReviewInfo reviewInfo) {
        this.content = reviewInfo.getContent();
        this.rating = reviewInfo.getRating();
    }
}
