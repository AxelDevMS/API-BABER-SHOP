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
@RequestMapping("/contract")
public class ContractingController {

    @Autowired
    private BarberShopService barberShopService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> executeContractingServiceBarberShop(@RequestBody @Valid BarberShopRequestDto barberShopRequestDto){
        ApiResponseDto apiResponseDto = this.barberShopService.executeCreateBarberShop(barberShopRequestDto);
        return ResponseEntity.ok(apiResponseDto);
    }
}
