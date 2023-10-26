package com.gatewayservice.service;

import com.gatewayservice.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

public class ReservationService {
    private final RestTemplate restTemplate;
    private final String ratingServerUrl;
    private final String libServerUrl;
    private final String reservServerUrl;

    public ReservationService(RestTemplate restTemplate, @Value("${library.server.url}") String libServerUrl,
                              @Value("${rating.server.url}") String ratingServerUrl,
                              @Value("${reservations.server.url}") String reservServerUrl) {
        this.restTemplate = restTemplate;
        this.libServerUrl = libServerUrl;
        this.ratingServerUrl = ratingServerUrl;
        this.reservServerUrl = reservServerUrl;
    }

    public ArrayList<BookReservationResponse> getAllReservations(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<ArrayList<ReservationResponse>> reservations = null;
        try {
            reservations = restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ArrayList<ReservationResponse>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        ArrayList<BookReservationResponse> allRes = new ArrayList<>();
        for (ReservationResponse res : reservations.getBody()) {
            ResponseEntity<BookInfo> book = null;
            ResponseEntity<LibraryResponse> lib = null;

            try {
                lib = restTemplate.exchange(
                        libServerUrl + "/api/v1/libraries/" + res.getLibraryUid(),
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<LibraryResponse>() {
                        }
                );

                book = restTemplate.exchange(
                        libServerUrl + "/api/v1/libraries/books/" + res.getBookUid(),
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<BookInfo>() {
                        }
                );
            } catch (HttpClientErrorException e) {
                e.printStackTrace();
            }

            allRes.add(new BookReservationResponse(res.getReservationUid(),
                    res.getStatus(), res.getStartDate(), res.getTillDate(),
                    book.getBody(), lib.getBody()));
        }

        return allRes;
    }
}
