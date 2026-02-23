package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

   @Autowired
    private UserService userService;

}
