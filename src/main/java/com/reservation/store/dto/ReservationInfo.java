package com.reservation.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
public class ReservationInfo {

    private Long storeId;
    private LocalDateTime ReservationTime;
}
