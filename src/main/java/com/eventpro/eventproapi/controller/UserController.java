package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.UserRegistrationRequest;
import com.eventpro.eventproapi.dto.response.UserResponse;
import com.eventpro.eventproapi.service.UserService;
// ⬇️ C'EST CET IMPORT QU'IL FAUT ⬇️
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController { // J'ai mis un U majuscule

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRegistrationRequest request){
        // Vérifiez que la méthode dans UserService s'appelle bien creerUtilisateur (ou createUtilisateur)
        return userService.createUtilisateur(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
}