package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.client.ClientFilterDto;
import ams.dev.api.barber_shop.dto.pagination.DataFilterDto;
import ams.dev.api.barber_shop.dto.client.ClientRequestDto;
import ams.dev.api.barber_shop.dto.client.ClientResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.service.ClientService;
import ams.dev.api.barber_shop.util.GenerateExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/client")
@Tag(name = "Clientes", description = "API para la gestión de clientes de la barbería")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private GenerateExcelUtil generateExcelUtil;

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Registra un nuevo cliente en el sistema con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos del cliente inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Conflicto - El cliente ya existe")
    })
    public ResponseEntity<ApiResponseDto> executeCreateClient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del cliente a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientRequestDto.class))
            )
            @RequestBody @Valid ClientRequestDto clientRequestDto
    ){
        ApiResponseDto response = clientService.executeCreateClient(clientRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{barbershopId}")
    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza la información de un cliente específico por ID de barbería")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ApiResponseDto> executeUpdateClient(
            @Parameter(description = "ID de la barbería", required = true, example = "barber123")
            @PathVariable String barbershopId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientRequestDto.class))
            )
            @RequestBody @Valid ClientRequestDto clientRequestDto
    ){
        return ResponseEntity.ok(this.clientService.executeUpdateClient(barbershopId, clientRequestDto));
    }

    @GetMapping
    @Operation(summary = "Listar clientes con filtros", description = "Obtiene una lista paginada de clientes aplicando múltiples filtros opcionales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente", content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos")
    })
    public ResponseEntity<PageResponseDto<ClientResponseDto>> executeListClient(
            @Parameter(description = "ID de la barbería (obligatorio)", required = true, example = "barber123")
            @RequestParam(required = true) String barbershopId,

            @Parameter(description = "Filtrar por clientes activos", example = "true")
            @RequestParam(required = false) Boolean active,

            @Parameter(description = "Filtrar por clientes eliminados", example = "false")
            @RequestParam(required = false) Boolean deleted,

            @Parameter(description = "Filtrar por clientes VIP", example = "true")
            @RequestParam(required = false) Boolean vip,

            @Parameter(description = "Filtrar clientes creados después de esta fecha (ISO 8601)",
                    example = "2024-01-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,

            @Parameter(description = "Filtrar clientes creados antes de esta fecha (ISO 8601)",
                    example = "2024-12-31T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,

            @Parameter(description = "Término de búsqueda (nombre, email, teléfono)", example = "Juan")
            @RequestParam(required = false) String search,

            @Parameter(description = "Número de página (0-indexado)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo por el cual ordenar", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Dirección de ordenamiento", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDirection
    ){
        PageParamRequestDto pageParam = new PageParamRequestDto(page, size, sortBy, sortDirection);

        ClientFilterDto paramsDto = new ClientFilterDto();
        paramsDto.setBarbershopId(barbershopId);
        paramsDto.setActive(active);
        paramsDto.setDeleted(deleted);
        paramsDto.setVip(vip);
        paramsDto.setCreatedAfter(createdAfter);
        paramsDto.setCreatedBefore(createdBefore);
        paramsDto.setSearchTerm(search);
        paramsDto.setPageParam(pageParam);

        return ResponseEntity.ok(this.clientService.executeGetListClient(paramsDto));
    }

    @GetMapping("/{clientId}/{barbershopId}")
    @Operation(summary = "Obtener un cliente específico", description = "Obtiene los detalles de un cliente por su ID y el ID de la barbería")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado exitosamente", content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClientResponseDto> executeGetClient(
            @Parameter(description = "ID del cliente", required = true, example = "client456")
            @PathVariable String clientId,

            @Parameter(description = "ID de la barbería", required = true, example = "barber123")
            @PathVariable String barbershopId
    ){
        return ResponseEntity.ok(this.clientService.executeGetClient(clientId, barbershopId));
    }

    @DeleteMapping("/{clientId}/{barbershopId}")
    @Operation(summary = "Eliminar un cliente", description = "Elimina (soft delete) un cliente por su ID y el ID de la barbería")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente (sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> executeDeleteClient(
            @Parameter(description = "ID del cliente", required = true, example = "client456")
            @PathVariable String clientId,

            @Parameter(description = "ID de la barbería", required = true, example = "barber123")
            @PathVariable String barbershopId
    ){
        this.clientService.executeDeleteClient(clientId, barbershopId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report")
    @Operation(summary = "Generar reporte de clientes en Excel", description = "Genera y descarga un reporte en formato Excel con la lista de clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = "application/vnd.ms-excel")),
            @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
    })
    public ResponseEntity<byte[]> executeGenerateReportClient(
            @Parameter(description = "ID de la barbería (opcional)", example = "barber123")
            @RequestParam(required = false) String barbershopId
    ) {
        try {
            byte[] excelContent = clientService.executeGenerateReportClient(barbershopId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData(
                    "attachment",
                    generateExcelUtil.generateFileName("reporte_clientes")
            );

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}