package ams.dev.api.barber_shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST de autenticación y endpoints de prueba.
 *
 * Este controlador maneja endpoints públicos (sin autenticación requerida) que sirven para:
 * - Validar que la aplicación esté en funcionamiento
 * - Proporcionar endpoints de prueba para verificar la conectividad
 *
 * Todos los endpoints en este controlador están permitidos en la configuración de seguridad
 * (@see SecurityConfig#basicAuth)
 */
@RestController
public class AuthController {

    /**
     * Endpoint de prueba que retorna un saludo simple.
     *
     * Ruta: GET /hello
     * Autenticación: NO requerida
     * Respuesta: String "Hello, World!"
     *
     * @return mensaje de saludo
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    /**
     * Endpoint de prueba alternativo.
     *
     * Ruta: GET /hey
     * Autenticación: NO requerida
     * Respuesta: String "Hey, World!"
     *
     * @return mensaje de saludo alternativo
     */
    @GetMapping("/hey")
    public String hey() {
        return "Hey, World!";
    }

    /**
     * Endpoint de prueba adicional.
     *
     * Ruta: GET /hi
     * Autenticación: NO requerida
     * Respuesta: String "Hi"
     *
     * @return mensaje de saludo corto
     */
    @GetMapping("/hi")
    public String hi() {
        return "Hi";
    }
}
