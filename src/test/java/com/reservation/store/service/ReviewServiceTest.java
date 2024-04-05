package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Review;
import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.*;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.ReviewRepository;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.reservation.store.constant.Role.PARTNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReviewServiceTest {

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private ReviewService reviewService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private ReviewInfo reviewInfo;
    private Reservation reservation;


    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.isConfirmed(true);
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

    @Test
    void updateReviewTest() {
        // given
        Long reviewId = 1L;
        String userEmail = "test@naver.com";
        ReviewInfo updatedReviewInfo = new ReviewInfo(reviewId, "맛 없어요", 3);

        User user = createUser();
        Store store = createStore(user);
        ReservationInfo reservationInfo = new ReservationInfo();
        reservationInfo.setStoreId(1L);
        reservationInfo.setReservationTime(LocalDateTime.now());

        Reservation reservation = createReservation(reservationInfo, user, store);
        Review review = createReview(reservation, user);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // when
        ReviewResponseDto result = reviewService.updateReview(reviewId, updatedReviewInfo, userEmail);

        // then
        assertNotNull(result);
        assertEquals("맛 없어요", review.getContent());
        assertEquals(3, review.getRating());
    }

    @Test
    void deleteReviewTest() {
        // given
        Long reviewId = 1L;
        String userEmail = "user@example.com";


        User user = createUser();
        Store store = createStore(user);
        ReservationInfo reservationInfo = new ReservationInfo();
        reservationInfo.setStoreId(1L);
        reservationInfo.setReservationTime(LocalDateTime.now());
        Reservation reservation = createReservation(reservationInfo, user, store);
        Review review = Review.createReview(reviewInfo, reservation, user);


        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // when
        reviewService.deleteReview(reviewId, userEmail);

        // then
        verify(reviewRepository).delete(review);

    }


    public User createUser() {
        return User.createUser(new UserInfo("정히나", "test@naver.com", "1234", "010-1111-3333"), PARTNER, passwordEncoder);

    }

    public Store createStore(User user) {
        return Store.registerStore(new StoreInfo("토스트", "서울 중구", "토스트 가게"), user);
    }

    public Reservation createReservation(ReservationInfo reservationInfo, User user, Store store) {
        return Reservation.createReservation(reservationInfo, user, store);
    }

    public Review createReview(Reservation reservation, User user) {
        return Review.createReview(new ReviewInfo(1L, "좋아요", 8), reservation, user);
    }
}