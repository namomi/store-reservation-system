package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Review;
import com.reservation.store.domain.User;
import com.reservation.store.dto.ReviewInfo;
import com.reservation.store.dto.ReviewResponseDto;
import com.reservation.store.exception.CustomException;
import com.reservation.store.exception.ErrorCode;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.ReviewRepository;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.reservation.store.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * @param reviewInfo (예약 id, 내용, 점수)
     * @return 리뷰 쓴 내용
     * 리뷰 정보를 받고 해당 예약이 있는지 확인한 후,
     * 예약한 사용자를 확인하여 리보를 작성합니다.
     * 그 후 작성한 내용을 반환합니다.
     */
    @Transactional
    public ReviewResponseDto createReview(ReviewInfo reviewInfo) {
        Reservation reservation = getReservation(reviewInfo);
        User user = getUser(reservation.getUser().getEmail());
        Review review = Review.createReview(reviewInfo, reservation, user);
        reviewRepository.save(review);

        return ReviewResponseDto.toDto(review);
    }

    /**
     *
     * @param reviewId
     * @param reviewInfo (예약 id, 내용, 점수)
     * @param email
     * @return 리뷰 정보
     * 사용자 리뷰를 찾고, 해당 리뷰 작성자인지 확인 후 리뷰 내용을 변경을 합니다.
     * 변경 후 변경된 내용을 반환합니다.
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewInfo reviewInfo, String email) {
        Review review = getReview(reviewId);

        updateValidation(!review.getReservation().getUser().getEmail().equals(email), UNAUTHORIZED_REVIEW_UPDATE);

        review.updateReview(reviewInfo);

        return ReviewResponseDto.toDto(review);
    }

    /**
     *
     * @param reviewId
     * @param email
     * 리뷰 id로 해당 리뷰를 찾고, 이메일로 해당 사용자를 찾습니다.
     * 해당 리뷰를 삭제합니다.
     */
    @Transactional
    public void deleteReview(Long reviewId, String email) {
        Review review = getReview(reviewId);
        User user = getUser(email);

        authorityValidation(review, user);

        reviewRepository.delete(review);
    }

    /**
     *
     * @param review
     * @param user
     * 리뷰와 사용자를 받아 리뷰를 작성한 작성자인지 또는 해당 매장의 점주인지 확인 후
     * 둘 다 아니면 삭제 권환이 없다고 에러를 반환합니다.
     */
    private void authorityValidation(Review review, User user) {
        boolean isOwner = review.getReservation().getUser().equals(user);
        boolean isManager = storeRepository.findById(review.getReservation().getStore().getId())
                .map(store -> store.getManager().equals(user))
                .orElse(false);

        if(!isOwner && !isManager) throw new CustomException(UNAUTHORIZED_REVIEW_DELETE);
    }

    /**
     * @param email
     * @return user
     * 이메일을 받아 해당 유저를 찾아 반환합니다.
     */
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    /**
     * @param reviewId
     * @return review
     * 리뷰 id를 받아 해당 id의 리뷰를 찾아 반환합니다.
     */
    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

    }

    /**
     * @param review
     * @param errorCode
     * 업데이트 검증을 하기 위해 만든 메소드입니다. 리뷰와 에러 메시지를 받습니다.
     */
    private static void updateValidation(boolean review, ErrorCode errorCode) {
        if (review) {
            throw new RuntimeException(errorCode.message());
        }
    }

    /**
     * @param reviewInfo (예약 id, 내용, 점수)
     * @return reservation
     * 리뷰정보를 받아 예약이 있는지 와 매장 이용 했는지 여부 와 이미 작성한 리뷰인지 확인 후,
     * 해당 예약정보를 반환합니다.
     */
    private Reservation getReservation(ReviewInfo reviewInfo) {
        Reservation reservation = reservationRepository.findById(reviewInfo.reservationId())
                .orElseThrow(() -> new CustomException(RESERVATION_NOT_FOUND));

        if(!reservation.isConfirmed()) throw new CustomException(REVIEW_NOT_ALLOWED);

        updateValidation(reviewRepository.existsByReservationId(reviewInfo.reservationId()), REVIEW_ALREADY_EXISTS);
        return reservation;
    }


}
