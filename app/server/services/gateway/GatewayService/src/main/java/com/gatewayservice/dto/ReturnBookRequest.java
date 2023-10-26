package com.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReturnBookRequest {
    String condition;
    String date;
}
