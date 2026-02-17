package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.AuthRequestDto;
import ams.dev.api.barber_shop.dto.employee.EmployeeRequestDto;
import ams.dev.api.barber_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar operaciones de usuarios y empleados.
 *
 * Este controlador expone endpoints REST para:
 * - Crear nuevos usuarios/empleados en el sistema
 * - Gestionar la información de usuarios
 *
 * Los endpoints están mapeados bajo el prefijo "/user" y requieren autenticación HTTP Basic.
 * Aunque la ruta esté permitida en SecurityConfig, en producción debería requerir autenticación.
 *
 * Dependencias:
 * - UserService: Servicio de lógica de negocio para operaciones de usuario
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Servicio de usuario inyectado por Spring para manejar la lógica de negocio
     */
    @Autowired
    private UserService userService;

    /**
     * Endpoint para crear un nuevo empleado/usuario en el sistema.
     *
     * Ruta: POST /user/create
     * Autenticación: Configurada como permitida (debería ser autenticada)
     * Content-Type: application/json
     * Status de respuesta: 201 CREATED
     *
     * Flujo técnico:
     * 1. Recibe un DTO con datos del empleado (username, password, isActive)
     * 2. Delega la lógica de creación al UserService
     * 3. El servicio mapea el DTO a entidad, encripta la contraseña y persiste en BD
     * 4. Retorna un DTO de respuesta con mensaje de éxito e ID del nuevo usuario
     *
     * @param employeeRequestDto DTO con los datos del empleado a crear (username, password, isActive)
     * @return ResponseEntity con ApiResponseDto conteniendo mensaje de confirmación y HTTP 201
     */
    @GetMapping("/create")
    public ResponseEntity<ApiResponseDto> executeCreateEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        ApiResponseDto response = userService.executeCreateEmployee(employeeRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponseDto> authenticate(@RequestBody AuthRequestDto authRequestDto){
        ApiResponseDto response  = userService.authenticate(authRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
