package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.BarberShopRequestDto;
import ams.dev.api.barber_shop.dto.BarberShopResponseDto;

import java.util.List;


public interface BarberShopService {
    ApiResponseDto executeCreateBarberShop(BarberShopRequestDto barberShopRequestDto);
    List<BarberShopResponseDto> executeGetListBarberShop();
    BarberShopResponseDto executeGetBarberShop(String barberShopId);
    ApiResponseDto executeUpdateBarberShop(String barberShopId,BarberShopRequestDto barberShopRequestDto);
    void executeDeletedBarberShop(String barberShopId);
}
