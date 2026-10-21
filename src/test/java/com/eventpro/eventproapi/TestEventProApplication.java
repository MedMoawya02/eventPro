package com.eventpro.eventproapi;

import org.springframework.boot.SpringApplication;

public class TestEventProApplication {

    public static void main(String[] args) {
        SpringApplication.from(EventProApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
