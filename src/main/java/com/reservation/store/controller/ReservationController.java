package com.reservation.store.controller;

import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public void createReservation(@RequestBody ReservationInfo reservationInfo) {
        reservationService.createReservation(reservationInfo);
    }

    @PostMapping("/{reservationId}/confirmArrival")
    public ResponseEntity<String> confirmArrival(@PathVariable Long reservationId) {
        if (reservationService.confirmArrival(reservationId)) {
            return ResponseEntity.ok("방문이 확인되었습ㄴ다.");
        } else return ResponseEntity.badRequest().body("방문 확인이 불가능한 시간입니다.");
    }
}
