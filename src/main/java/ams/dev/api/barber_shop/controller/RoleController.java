package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.RoleRequestDto;
import ams.dev.api.barber_shop.dto.RoleResponseDto;
import ams.dev.api.barber_shop.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> executeCreateRole(@RequestBody @Valid RoleRequestDto roleRequestDto){
        return ResponseEntity.ok(roleService.executeCreateRole(roleRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> executeGetRoles(){
        return ResponseEntity.ok(roleService.executeGetRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> executeGetRole(@PathVariable String id){
        return ResponseEntity.ok(roleService.executeGetRole(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> executeUpdateRole(@PathVariable String id, @RequestBody @Valid RoleRequestDto roleRequestDto){
        return ResponseEntity.ok(roleService.executeUpdateRole(id, roleRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> executeDeleteRole(@PathVariable String id) {
        roleService.executeDeleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
