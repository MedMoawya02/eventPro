package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.PaymentRequest;
import com.eventpro.eventproapi.dto.response.PaymentResponse;
import com.eventpro.eventproapi.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse payer(@Valid @RequestBody PaymentRequest request){
        return paymentService.payer(request);
    }
}
