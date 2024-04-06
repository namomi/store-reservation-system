package com.reservation.store.domain;

import com.reservation.store.constant.ReservationStatus;
import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.reservation.store.constant.ReservationStatus.*;
import static com.reservation.store.exception.ErrorCode.RESERVATION_TOO_SOON;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalDateTime reservationTime;
    private boolean isConfirmed;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public static Reservation createReservation(ReservationInfo reservationInfo, User user, Store store) {
        return Reservation.builder()
                .user(user)
                .store(store)
                .reservationTime(validateReservationTime(reservationInfo.reservationTime()))
                .isConfirmed(false)
                .reservationStatus(PENDING)
                .build();
    }


    protected static LocalDateTime validateReservationTime(LocalDateTime reservationTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesLater = now.plusMinutes(10);

        if (reservationTime.isBefore(tenMinutesLater)) {
            throw new CustomException(RESERVATION_TOO_SOON);
        }
        return reservationTime;
    }
    public void isConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public void changeApprove(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public boolean checkConfirmArrival() {
        if (this.getReservationTime() == null) return false;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesBefore = this.getReservationTime().minusMinutes(10);

        if (!now.isAfter(tenMinutesBefore)) {
            this.isConfirmed(true);
            return true;
        }else return false;
    }
}
