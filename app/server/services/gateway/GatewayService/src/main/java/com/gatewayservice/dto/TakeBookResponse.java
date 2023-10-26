package com.gatewayservice.dto;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class TakeBookResponse {
    UUID reservationUid;
    String status;
    String startDate;
    String tillDate;
    BookInfo book;
    LibraryResponse library;
    UserRatingResponse rating;
}
