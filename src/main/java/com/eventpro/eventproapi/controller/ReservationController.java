package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.ReservationRequest;
import com.eventpro.eventproapi.dto.response.ReservationResponse;
import com.eventpro.eventproapi.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse creerReservation(@Valid @RequestBody ReservationRequest request){
        return reservationService.createReservation(request);
    }
}
