package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    @PostMapping
    public ResponseEntity<ApiResponseDto> executeCreatePermission(@RequestBody @Valid PermissionRequestDto permissionRequestDto) {
        ApiResponseDto responseDto = permissionService.executeCreatePermission(permissionRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDto> executeGetPermission(@PathVariable String id){
        PermissionResponseDto responseDto = permissionService.executeGetPermission(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> executeGetPermissions(){
        List<PermissionResponseDto> responseDto = permissionService.executeGetPermissions();
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> executeUpdatePermission(@PathVariable String id, @RequestBody @Valid PermissionRequestDto permissionRequestDto){
        ApiResponseDto responseDto = permissionService.executeUpdatePermission(id, permissionRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> executeDeletePermission(@PathVariable String id){
        permissionService.executeDeletePermission(id);
        return ResponseEntity.noContent().build();
    }






}
