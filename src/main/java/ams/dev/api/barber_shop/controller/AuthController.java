package ams.dev.api.barber_shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/hey")
    public String hey() {
        return "Hey, World!";
    }

    @GetMapping("/hi")
    public String hi() {
        return "Hi";
    }
}
