package ams.dev.api.barber_shop.mapper.response;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import ams.dev.api.barber_shop.dto.client.ClientResponseDto;
import ams.dev.api.barber_shop.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientResponseMapper {

    public ClientResponseDto toDto(ClientEntity clientEntity){
        if (clientEntity == null) return null;

        return ClientResponseDto
                .builder()
                .id(clientEntity.getId())
                .fullName(clientEntity.getFullName())
                .phone(clientEntity.getPhone())
                .email(clientEntity.getEmail())
                .isVip(clientEntity.getIsVip())
                .isActive(clientEntity.getIsActive())
                .isDeleted(clientEntity.getIsDeleted())
                .notes(clientEntity.getNotes())
                .createdAt(clientEntity.getCreatedAt())
                .build();
    }

    public ClientResponseDto toDto(ClientEntity clientEntity, BarberShopResponseDto barberShopDto){
        if (clientEntity == null) return null;

        return ClientResponseDto
                .builder()
                .id(clientEntity.getId())
                .fullName(clientEntity.getFullName())
                .phone(clientEntity.getPhone())
                .email(clientEntity.getEmail())
                .isVip(clientEntity.getIsVip())
                .isActive(clientEntity.getIsActive())
                .isDeleted(clientEntity.getIsDeleted())
                .notes(clientEntity.getNotes())
                .createdAt(clientEntity.getCreatedAt())
                .barberShop(barberShopDto)
                .build();
    }
}
