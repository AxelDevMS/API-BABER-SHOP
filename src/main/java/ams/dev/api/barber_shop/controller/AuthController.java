package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.AuthRequestDto;
import ams.dev.api.barber_shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDto> authenticate(@RequestBody AuthRequestDto authRequestDto){
        ApiResponseDto response  = authService.authenticate(authRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
