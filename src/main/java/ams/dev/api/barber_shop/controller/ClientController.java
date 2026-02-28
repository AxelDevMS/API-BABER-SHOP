package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.client.ClientRequestDto;
import ams.dev.api.barber_shop.dto.client.ClientResponseDto;
import ams.dev.api.barber_shop.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @PostMapping
    public ResponseEntity<ApiResponseDto> executeCreateClient(
            @RequestBody @Valid ClientRequestDto clientRequestDto
    ){
        ApiResponseDto response = clientService.executeCreateClient(clientRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response) // Asumiendo que response tiene el ID
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{barbershopId}")
    public ResponseEntity<ApiResponseDto> executeUpdateClient(
            @PathVariable String barbershopId,
            @RequestBody @Valid ClientRequestDto clientRequestDto
    ){
        return ResponseEntity.ok(this.clientService.executeUpdateClient(barbershopId,clientRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> executeListClient(
            @RequestParam(required = false) String barbershopId

    ){
        return ResponseEntity.ok(this.clientService.executeGetListClient(barbershopId));
    }

    @GetMapping("/{clientId}/{barbershopId}")
    public ResponseEntity<ClientResponseDto> executeGetClient(
            @PathVariable String clientId,
            @PathVariable String barbershopId
    ){
        return ResponseEntity.ok(this.clientService.executeGetClient(clientId, barbershopId));
    }

    @DeleteMapping("/{clientId}/{barbershopId}")
    public ResponseEntity<Void> executeDeleteClient(
            @PathVariable String clientId,
            @PathVariable String barbershopId
    ){
        this.clientService.executeDeleteClient(clientId, barbershopId);
        return ResponseEntity.noContent().build();
    }
}
