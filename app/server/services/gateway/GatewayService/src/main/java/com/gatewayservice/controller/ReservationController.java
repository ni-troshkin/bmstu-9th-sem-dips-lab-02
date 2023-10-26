package com.gatewayservice.controller;

import com.gatewayservice.dto.BookReservationResponse;
import com.gatewayservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@Tag(name = "RESERVATIONS")
@RequestMapping("/reservations")
public class ReservationController {
    /**
     * Сервис, работающий с прокатом книг
     */
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Получение списка книг, взятых в прокат по имени пользователя
     * @param username имя пользователя, информацию о котором нужно получить
     * @return список книг, взятых в прокат
     */
    @Operation(summary = "Получение списка книг, взятых пользователем в прокат")
    @GetMapping()
    public ResponseEntity<ArrayList<BookReservationResponse>> getReservations(@RequestHeader("X-User-Name") String username) {
        ArrayList<BookReservationResponse> reservations = reservationService.getAllReservations(username);

        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }
}
