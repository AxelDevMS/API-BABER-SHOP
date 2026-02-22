package ams.dev.api.barber_shop.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_ADD')") // Verifica que el usuario tenga el permiso específico para agregar productos
    public String addProduct() {
        return "Producto agregado";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')") // Verifica que el usuario tenga el permiso específico para ver un producto
    public String getProductById(@PathVariable Long id) {
        return "Producto encontrado: "+ id;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_VIEW_ALL')") // Verifica que el usuario tenga el permiso específico para ver todos los productos
    public String getAllProducts() {
        return "Lista de productos";
    }
}
