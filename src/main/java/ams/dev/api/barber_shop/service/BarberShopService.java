package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.BarberShopRequestDto;


public interface BarberShopService {
    ApiResponseDto executeCreateBarberShop(BarberShopRequestDto barberShopRequestDto);
}
