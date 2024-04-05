package com.reservation.store.controller;

import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public void createReservation(@RequestBody ReservationInfo reservationInfo) {
        reservationService.createReservation(reservationInfo);
    }

    @PutMapping("/reservation/{reservationId}/confirmArrival")
    public ResponseEntity<String> confirmArrival(@PathVariable Long reservationId) {
        if (reservationService.confirmArrival(reservationId)) {
            return ResponseEntity.ok("방문이 확인되었습니다.");
        } else return ResponseEntity.badRequest().body("방문 확인이 불가능한 시간입니다.");
    }

    @PutMapping("/reservation/{reservationId}/approve")
    public ResponseEntity<String> approveReservation(@PathVariable Long reservationId) {
        reservationService.approveReservation(reservationId);
        return ResponseEntity.ok().body("예약 확정");
    }

    @PutMapping("/reservation/{reservationId}/reject")
    public ResponseEntity<String> rejectReservation(@PathVariable Long reservationId) {
        reservationService.rejectReservation(reservationId);
        return ResponseEntity.ok().body("예약 취소");
    }
}
