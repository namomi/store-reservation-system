package com.reservation.store.domain;

import com.reservation.store.dto.ReservationInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {

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

    public static Reservation createReservation(ReservationInfo reservationInfo, User user, Store store) {
        return Reservation.builder()
                .user(user)
                .store(store)
                .reservationTime(reservationInfo.getReservationTime())
                .isConfirmed(false)
                .build();
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean checkConfirmArrival() {
        if (this.getReservationTime() == null) return false;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesBefore = this.getReservationTime().minusMinutes(10);

        if (!now.isAfter(tenMinutesBefore)) {
            this.setConfirmed(true);
            return true;
        }else return false;
    }
}
