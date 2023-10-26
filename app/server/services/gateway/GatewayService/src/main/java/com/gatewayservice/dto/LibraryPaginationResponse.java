package com.gatewayservice.dto;

import com.gatewayservice.service.LibraryService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class LibraryPaginationResponse {
    int page;
    int pageSize;
    int totalElements;
    ArrayList<LibraryResponse> items;
}
