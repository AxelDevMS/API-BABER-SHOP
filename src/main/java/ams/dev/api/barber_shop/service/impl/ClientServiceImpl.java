package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.client.ClientRequestDto;
import ams.dev.api.barber_shop.dto.client.ClientResponseDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.ClientEntity;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.DuplicateResourceException;
import ams.dev.api.barber_shop.exceptions.ResourceNotFoundException;
import ams.dev.api.barber_shop.mapper.request.ClientRequestMapper;
import ams.dev.api.barber_shop.mapper.response.ClientResponseMapper;
import ams.dev.api.barber_shop.repository.ClientRepository;
import ams.dev.api.barber_shop.service.BarberShopService;
import ams.dev.api.barber_shop.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientRequestMapper clientRequestMapper;

    @Autowired
    private ClientResponseMapper clientResponseMapper;

    @Autowired
    private BarberShopService barberShopService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public ApiResponseDto executeCreateClient(ClientRequestDto clientRequestDto) {
        LOGGER.info("ENTRA AL METODO CREAR CLIENTE");
        String barbershopId = clientRequestDto.getBarberShop().getId();

        this.validateDuplicateClient(clientRequestDto.getEmail(),barbershopId,null);
        BarberShopEntity barberShopBD = this.barberShopService.findById(clientRequestDto.getBarberShop().getId());

        ClientEntity client = clientRequestMapper.toEntity(clientRequestDto,barberShopBD);
        client.setIsVip(false);
        client.setIsActive(true);
        client.setIsDeleted(false);
        client = clientRepository.save(client);
        return new ApiResponseDto("Cliente registrado con éxito con id: "+client.getId());
    }

    @Override
    public ApiResponseDto executeUpdateClient(String clientId, ClientRequestDto clientRequestDto) {
        this.validateId(clientId);
        String barbershopId = clientRequestDto.getBarberShop().getId();

        ClientEntity clientBD = this.findById(clientId,barbershopId);

        clientBD.setFullName(clientRequestDto.getFullName());
        clientBD.setPhone(clientRequestDto.getPhone());
        clientBD.setEmail(clientRequestDto.getEmail());
        clientBD.setNotes(clientRequestDto.getNotes());

        return new ApiResponseDto("Cliente actualizado con éxito con id: "+clientBD.getId());
    }

    @Override
    public ClientEntity findById(String clientId, String barbershopId) {
        return this.clientRepository
                .findByIdAndBarbershopId(clientId,barbershopId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cliente","Id",barbershopId)
                );
    }

    @Override
    public List<ClientResponseDto> executeGetListClient(String barbershopId) {
        LOGGER.info("INGRESA A LA FUNCION PARA TRAE LISTA DE CLIENTES");
        this.validateId(barbershopId);

        List<ClientEntity> clientListBD = this.clientRepository.findAllByBarbershopId(barbershopId);

        if (clientListBD.isEmpty())
            throw new ResourceNotFoundException("No hay clients en el site");

        return clientListBD
                .stream()
                .map(clientBD ->
                        this.clientResponseMapper.toDto(clientBD)
                )
                .toList();
    }

    @Override
    public ClientResponseDto executeGetClient(String clientId, String barbershopId) {
        LOGGER.info("INGRESA AL METODO PARA BUSCAR EL CLIENTE POR SU ID");
        ClientEntity clientBD = this.findById(clientId,barbershopId);
        return this.clientResponseMapper.toDto(clientBD);
    }

    @Override
    public void executeDeleteClient(String clientId, String barbershopId) {
        ClientEntity clientBD = this.findById(clientId,barbershopId);

        clientBD.setIsDeleted(true);
        clientBD.setIsActive(true);
        this.clientRepository.save(clientBD);
    }

    /*
    ==============================================================================
                        MÉTODOS PRIVADOS USADOS PARA ESTA CLASE
    ==============================================================================
    */

    private void validateId(String id){
        if (id == null || id.trim().isEmpty()){
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("id",List.of("El ID no puede estar vacío"));
            throw new BusinessException("Error de validación", errors);
        }
    }

    private void validateDuplicateClient(String email, String barbershopId, String clientId){
        LOGGER.info("ENTRA A VALIDAR SI ES CLIENTE DUPLICADO");
        Optional<ClientEntity> existingClient;

        if (clientId == null)
            existingClient = clientRepository.findByEmailAndBarbershopId(email,barbershopId);
        else
            existingClient = clientRepository.findByEmailAndBarbershopIdAndIdNot(email,barbershopId,clientId);

        if (existingClient.isPresent())
            throw new DuplicateResourceException("Cliente","correo", email);
    }
}
