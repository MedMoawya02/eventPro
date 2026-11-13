package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.ReservationRequest;
import com.eventpro.eventproapi.dto.response.ReservationResponse;
import com.eventpro.eventproapi.entity.Reservation;
import com.eventpro.eventproapi.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //controller du mise en situation
    @GetMapping("/recherche")
    public ResponseEntity<List<Reservation>> rechercherReservation(@RequestParam String email ,@RequestParam String categorieName,@RequestParam(defaultValue = "1") Integer quantiteMin){
        List<Reservation> reservations=reservationService.findReser(email,categorieName,quantiteMin);
        return ResponseEntity.ok(reservations);
    }

}
