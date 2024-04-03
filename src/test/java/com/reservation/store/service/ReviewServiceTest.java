package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Review;
import com.reservation.store.dto.ReviewInfo;
import com.reservation.store.dto.ReviewResponseDto;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReviewServiceTest {

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    @InjectMocks
    private ReviewService reviewService;

    private ReviewInfo reviewInfo;
    private Reservation reservation;


    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setConfirmed(true);
        reviewInfo = new ReviewInfo(1L, "좋아요", 8);

        when(reservationRepository.findById(reviewInfo.getReservationId())).thenReturn(Optional.of(reservation));
        when(reviewRepository.existsByReservationId(reviewInfo.getReservationId())).thenReturn(false);
    }

    @Test
    void createReviewTest() {
        // given
        ReviewResponseDto result = reviewService.createReview(reviewInfo);

        // when, then
        assertNotNull(result); // 생성된 리뷰의 응답이 null이 아닌지 확인
        verify(reviewRepository, times(1)).save(any(Review.class)); // 리뷰가 저장되었는지 검증
    }

}