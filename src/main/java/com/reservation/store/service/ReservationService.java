package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createReservation(ReservationInfo reservationInfo) {
        User user = getUser();
        Store store = getStore(reservationInfo);

        reservationRepository.save(Reservation.createReservation(reservationInfo, user, store));
    }

    @Transactional
    public boolean confirmArrival(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다."));

        return reservation.checkConfirmArrival();
    }

    private Store getStore(ReservationInfo reservationInfo) {
        return storeRepository.findById(reservationInfo.getStoreId())
                .orElseThrow(() -> new RuntimeException("일치하는 가계가 업습니다."));
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("일치하는 회원이 없습니다."));
    }
}
