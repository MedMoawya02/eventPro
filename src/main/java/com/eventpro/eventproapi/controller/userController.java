package com.eventpro.eventproapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class userController {
    @GetMapping("/")
    public String  helloWorld(){
        return "hello";
    }
}
