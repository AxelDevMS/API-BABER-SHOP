package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.client.ClientFilterDto;
import ams.dev.api.barber_shop.dto.client.ClientRequestDto;
import ams.dev.api.barber_shop.dto.client.ClientResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.entity.ClientEntity;

import java.util.List;

public interface ClientService {

    ApiResponseDto executeCreateClient(ClientRequestDto clientRequestDto);

    ApiResponseDto executeUpdateClient(String clientId, ClientRequestDto clientRequestDto);

    ClientEntity findById(String clientId, String barbershopId);

    //List<ClientResponseDto> executeGetListClient(ClientFilterDto clientFilterDto);

    PageResponseDto<ClientResponseDto> executeGetListClient(ClientFilterDto filterDto);


    ClientResponseDto executeGetClient(String clientId, String barbershopId);

    void executeDeleteClient(String clientId, String barbershopId);


}
