package com.reservation.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class ReviewInfo {

    private Long reservationId;

    private String content;

    private int rating;
}
