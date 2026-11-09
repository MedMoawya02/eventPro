package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.RefundRequest;
import com.eventpro.eventproapi.entity.Remboursement;
import com.eventpro.eventproapi.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Remboursement demanderRemboursement(@Valid @RequestBody RefundRequest request) {
        return refundService.demanderRemboursement(request);
    }
}