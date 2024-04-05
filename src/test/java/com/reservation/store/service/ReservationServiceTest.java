package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.reservation.store.constant.ReservationStatus.APPROVED;
import static com.reservation.store.constant.ReservationStatus.REJECTED;
import static com.reservation.store.constant.Role.PARTNER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReservationServiceTest {
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private StoreRepository storeRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
        Mockito.lenient().when(mockUserDetails.getUsername()).thenReturn("test@name.com");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUserDetails, null,
                        Collections.singletonList(new SimpleGrantedAuthority("PARTNER")));

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = createUser();
        Mockito.lenient().when(userRepository.findByEmail("test@name.com")).thenReturn(Optional.of(user));
    }

    @Test
    void createReservationTest() {
        // given
        ReservationInfo reservationInfo = new ReservationInfo();
        reservationInfo.setStoreId(1L);
        reservationInfo.setReservationTime(LocalDateTime.now().plusDays(1));

        Store store = new Store();
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        // when
        reservationService.createReservation(reservationInfo);

        // then
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void confirmArrivalTest() {
        // given
        Long reservationId = 1L;
        Reservation mockReservation = Mockito.mock(Reservation.class);
        when(mockReservation.checkConfirmArrival()).thenReturn(true);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        // when
        boolean result = reservationService.confirmArrival(reservationId);

        // then
        assertTrue(result);
        verify(reservationRepository).findById(reservationId);
        verify(mockReservation).checkConfirmArrival();
    }


    @Test
    void approveReservationTest() {
        // given
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when
        reservationService.approveReservation(reservationId);

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(APPROVED);
    }

    @Test
    void rejectReservationTest() {
        // 예시 코드
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.rejectReservation(reservationId);

        assertThat(reservation.getReservationStatus()).isEqualTo(REJECTED);
    }

    public User createUser() {
        return User.createUser(new UserInfo("정히나", "test@name.com", "1234", "010-1111-3333"), PARTNER, passwordEncoder);

    }
}