package com.reservation.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewInfo {

    private Long reservationId;

    private String content;

    private int rating;
}
