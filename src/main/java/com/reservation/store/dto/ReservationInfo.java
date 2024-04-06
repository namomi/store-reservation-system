package com.reservation.store.dto;

import java.time.LocalDateTime;



public record ReservationInfo(Long storeId, LocalDateTime reservationTime) { }
