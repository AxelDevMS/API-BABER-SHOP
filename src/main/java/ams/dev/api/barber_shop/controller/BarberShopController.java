package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.BarberShopRequestDto;
import ams.dev.api.barber_shop.service.BarberShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shop")
public class BarberShopController {

    @Autowired
    private BarberShopService barberShopService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> executeCreateBarberShop(@RequestBody @Valid  BarberShopRequestDto barberShopRequestDto){
        ApiResponseDto apiResponseDto = this.barberShopService.executeCreateBarberShop(barberShopRequestDto);
        return ResponseEntity.ok(apiResponseDto);
    }
}
