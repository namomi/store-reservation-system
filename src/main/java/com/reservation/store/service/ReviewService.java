package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Review;
import com.reservation.store.domain.User;
import com.reservation.store.dto.ReviewInfo;
import com.reservation.store.dto.ReviewResponseDto;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.ReviewRepository;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewInfo reviewInfo) {
        Reservation reservation = getReservation(reviewInfo);
        User user = getUser(reservation.getUser().getEmail());
        Review review = Review.createReview(reviewInfo, reservation, user);
        reviewRepository.save(review);

        return ReviewResponseDto.toDto(review);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewInfo reviewInfo, String email) {
        Review review = getReview(reviewId);

        updateValidation(!review.getReservation().getUser().getEmail().equals(email), "리뷰 수정 권한이 없습니다.");

        review.updateReview(reviewInfo);

        return ReviewResponseDto.toDto(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, String email) {
        Review review = getReview(reviewId);
        User user = getUser(email);

        authorityValidation(review, user);

        reviewRepository.delete(review);
    }

    private void authorityValidation(Review review, User user) {
        boolean isOwner = review.getReservation().getUser().equals(user);
        boolean isManager = storeRepository.findById(review.getReservation().getStore().getId())
                .map(store -> store.getManager().equals(user))
                .orElse(false);

        if(!isOwner && !isManager) throw new RuntimeException("리뷰 삭제 권한이 없브니다.");
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

    }

    private static void updateValidation(boolean review, String message) {
        if (review) {
            throw new RuntimeException(message);
        }
    }

    private Reservation getReservation(ReviewInfo reviewInfo) {
        Reservation reservation = reservationRepository.findById(reviewInfo.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 업습니다."));

        if(!reservation.isConfirmed()) throw new RuntimeException("매장 이용후에 리부를 달 수 있습니다.");

        updateValidation(reviewRepository.existsByReservationId(reviewInfo.getReservationId()), "이미 리뷰를 작성했습니다.");
        return reservation;
    }


}
