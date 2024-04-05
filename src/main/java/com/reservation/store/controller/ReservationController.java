package com.reservation.store.controller;

import com.reservation.store.dto.ReservationInfo;
import com.reservation.store.exception.CustomException;
import com.reservation.store.exception.ErrorCode;
import com.reservation.store.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약 등록", description = "매장 예약을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 가게의 정보가 없습니다."),
            @ApiResponse(responseCode = "400", description = "예약은 현재 시간으로부터 최소 10분 후부터 가능합니다.")
    })
    @PostMapping("/reservation")
    public void createReservation(@RequestBody
                                  @Positive(message = "매장 id와 예약 시간은 필수 입니다..")
                                  @Schema(description = "매장 id와 예약 시간을 적어주세요.",
                                          example = "reservationInfo (매장 id, 예약시간)")
                                  ReservationInfo reservationInfo) {
        reservationService.createReservation(reservationInfo);
    }

    @Operation(summary = "매장 방문", description = "매장 방문시 방문처리가 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "방문이 확인되었습니다."),
            @ApiResponse(responseCode = "400", description = "방문 확인이 불가능한 시간입니다.")
    })
    @PutMapping("/reservation/{reservationId}/confirmArrival")
    public ResponseEntity<String> confirmArrival(@PathVariable
                                                 @Positive(message = "예약 id는 필수 입니다..")
                                                 @Schema(description = "예약 id를 적어주세요.",
                                                         example = "1")
                                                 Long reservationId) {
        if (reservationService.confirmArrival(reservationId)) {
            return ResponseEntity.ok("방문이 확인되었습니다.");
        } else {
            throw new CustomException(ErrorCode.INVALID_ARRIVAL_TIME);
        }
    }

    @Operation(summary = "예약 승인", description = "예약을 승인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 확정"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다.")
    })
    @PutMapping("/reservation/{reservationId}/approve")
    public ResponseEntity<String> approveReservation(@PathVariable
                                                     @Positive(message = "예약 id는 필수 입니다..")
                                                     @Schema(description = "예약 id를 적어주세요.",
                                                             example = "1")
                                                     Long reservationId) {
        reservationService.approveReservation(reservationId);
        return ResponseEntity.ok().body("예약 확정");
    }


    @Operation(summary = "예약 취소", description = "예약을 취소시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 취소"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다.")
    })
    @PutMapping("/reservation/{reservationId}/reject")
    public ResponseEntity<String> rejectReservation(@PathVariable
                                                    @Positive(message = "예약 id는 필수 입니다..")
                                                    @Schema(description = "예약 id를 적어주세요.",
                                                            example = "1")
                                                    Long reservationId) {
        reservationService.rejectReservation(reservationId);
        return ResponseEntity.ok().body("예약 취소");
    }
}
