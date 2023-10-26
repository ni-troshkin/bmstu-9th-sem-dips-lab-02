package com.gatewayservice.dto;

import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class LibraryBookPaginationResponse {
    int page;
    int pageSize;
    int totalElements;
    ArrayList<LibraryBookResponse> items;
}
