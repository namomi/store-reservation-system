package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Review;
import com.reservation.store.dto.ReviewInfo;
import com.reservation.store.dto.ReviewResponseDto;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewResponseDto createReview(ReviewInfo reviewInfo) {
        Reservation reservation = reservationRepository.findById(reviewInfo.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 업습니다."));

        writeValidation(reviewInfo, reservation);

        Review review = Review.createReview(reviewInfo, reservation);
        reviewRepository.save(review);

        return ReviewResponseDto.toDto(review);
    }

    private void writeValidation(ReviewInfo reviewInfo, Reservation reservation) {
        if(!reservation.isConfirmed()) throw new RuntimeException("매장 이용후에 리부를 달 수 있습니다.");

        if (reviewRepository.existsByReservationId(reviewInfo.getReservationId())) {
            throw new RuntimeException("이미 리뷰를 작성했습니다.");
        }
    }
}
