package ams.dev.api.barber_shop.mapper.request;

import ams.dev.api.barber_shop.dto.client.ClientRequestDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientRequestMapper {


    public ClientEntity toEntity(ClientRequestDto clientRequestDto){
        if (clientRequestDto==null) return null;

        return ClientEntity
                .builder()
                .fullName(clientRequestDto.getFullName())
                .phone(clientRequestDto.getPhone())
                .email(clientRequestDto.getEmail())
                .notes(clientRequestDto.getNotes())
                .build()
                ;
    }

    public ClientEntity toEntity(ClientRequestDto clientRequestDto, BarberShopEntity barberShopEntity){
        if (clientRequestDto==null) return null;

        return ClientEntity
                .builder()
                .fullName(clientRequestDto.getFullName())
                .phone(clientRequestDto.getPhone())
                .email(clientRequestDto.getEmail())
                .notes(clientRequestDto.getNotes())
                .barbershop(barberShopEntity)
                .build()
                ;
    }
}
