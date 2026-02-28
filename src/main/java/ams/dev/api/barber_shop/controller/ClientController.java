package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.client.ClientFilterDto;
import ams.dev.api.barber_shop.dto.client.ClientRequestDto;
import ams.dev.api.barber_shop.dto.client.ClientResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
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
    public ResponseEntity<PageResponseDto<ClientResponseDto>> executeListClient(
            @RequestParam(required = true) String barbershopId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) Boolean vip,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ){

        PageParamRequestDto pageParam = new PageParamRequestDto(page, size, sortBy, sortDirection);
        ClientFilterDto clientFilterDto = ClientFilterDto.builder()
                .barbershopId(barbershopId)
                .active(active)
                .deleted(deleted)
                .vip(vip)
                .createdAfter(createdAfter)
                .createdBefore(createdBefore)
                .searchTerm(search)
                .pageParam(pageParam)
                .build();

        return ResponseEntity.ok(this.clientService.executeGetListClient(clientFilterDto));
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
