package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.EventCreateRequest;
import com.eventpro.eventproapi.dto.response.EventResponse;
import com.eventpro.eventproapi.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    //1.créer un evenement
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvenement(@Valid @RequestBody EventCreateRequest request){
        return eventService.creerEvenement(request);
    }
    //2.publier un événement
    @PatchMapping("/{id}/publish")
    public EventResponse publierEvenement(@PathVariable Long id){
        return eventService.publierEvenement(id);
    }

}
