package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.BarberShopRequestDto;
import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import ams.dev.api.barber_shop.service.BarberShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class BarberShopController {

    @Autowired
    private BarberShopService barberShopService;

    @GetMapping
    public ResponseEntity<List<BarberShopResponseDto>> executeListBarberShop(){
        return ResponseEntity.ok(this.barberShopService.executeGetListBarberShop());
    }

    @GetMapping("/{barberShopId}")
    public ResponseEntity<BarberShopResponseDto> executeGetBarberShop(@PathVariable String barberShopId){
        return ResponseEntity.ok(this.barberShopService.executeGetBarberShop(barberShopId));
    }

    @PutMapping("/{barberShopId}")
    public ResponseEntity<ApiResponseDto> executeUpdateBarberShop(@PathVariable String barberShopId, @RequestBody @Valid BarberShopRequestDto barberShopRequestDto){
        return ResponseEntity.ok(this.barberShopService.executeUpdateBarberShop(barberShopId,barberShopRequestDto));
    }

    @DeleteMapping("/{barberShopId}")
    public ResponseEntity<Void> executeDeleteBarberShop(@PathVariable String barberShopId) {
        this.barberShopService.executeDeletedBarberShop(barberShopId);
        return ResponseEntity.noContent().build();
    }
}
