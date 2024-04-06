package com.reservation.store.service;

import com.reservation.store.domain.Reservation;
import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.exception.CustomException;
import com.reservation.store.repository.ReservationRepository;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.reservation.store.constant.ReservationStatus.APPROVED;
import static com.reservation.store.constant.ReservationStatus.REJECTED;
import static com.reservation.store.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * @param reservationInfo (매장 id, 예약 시간)
     * 예약할 회원를 확인 후, 예약할 매장을 찾습니다. 그 후, 매장 예약을 합니다.
     */
    @Transactional
    public void createReservation(ReservationInfo reservationInfo) {
        User user = getUser();
        Store store = getStore(reservationInfo);

        reservationRepository.save(Reservation.createReservation(reservationInfo, user, store));
    }

    /**
     * @param reservationId
     * @return 예약 방문시 true, 기본값은 false
     * 예약한 매장의 방문 여부를 나타납니다.
     */
    @Transactional
    public boolean confirmArrival(Long reservationId) {
        return getReservation(reservationId).checkConfirmArrival();
    }

    /**
     * @param reservationId
     * 고객의 매장 예약을 받고 난 후, 점주가 확인 후 예약을 승인합니다.
     */
    @Transactional
    public void approveReservation(Long reservationId) {
        Reservation reservation = getReservation(reservationId);
        reservation.changeApprove(APPROVED);
    }

    /**
     * @param reservationId
     * 고객의 매장 예약을 받고 난 후, 점주가 확인 후 예약을 거절합니다.
     */
    @Transactional
    public void rejectReservation(Long reservationId) {
        Reservation reservation = getReservation(reservationId);
        reservation.changeApprove(REJECTED);
    }

    /**
     *
     * @param reservationId
     * @return reservation
     * 예약 id를 받아 조회 한 후 예약 정보를 반환합니다.
     */
    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(RESERVATION_NOT_FOUND));
    }

    /**
     * @param reservationInfo (매장 id, 예약 시간)
     * @return store
     * 예약 정보를 받아 해당 매장이 있는지 확인 후 매장 정보를 반환합니다.
     */
    private Store getStore(ReservationInfo reservationInfo) {
        return storeRepository.findById(reservationInfo.storeId())
                .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
    }

    /**
     * @return user
     * 로그인 한 회원 정보를 가져와 반환합니다.
     */
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
