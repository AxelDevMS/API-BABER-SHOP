package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.permission.PermissionFilterDto;
import ams.dev.api.barber_shop.dto.permission.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.permission.PermissionResponseDto;
import ams.dev.api.barber_shop.service.PermissionService;
import jakarta.validation.Valid;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/permission")
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
    public ResponseEntity<PageResponseDto<PermissionResponseDto>> executeGetListPermission(
            @RequestParam(required = false) String permissionId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
            )
    {
        PageParamRequestDto pageParamDto = new PageParamRequestDto(page,size,sortBy,sortDirection);
        PermissionFilterDto queryParamDto = new PermissionFilterDto();
        queryParamDto.setPermissionId(permissionId);
        queryParamDto.setModule(module);
        queryParamDto.setDeleted(deleted);
        queryParamDto.setActive(active);
        queryParamDto.setCreatedAfter(createdAfter);
        queryParamDto.setCreatedBefore(createdBefore);
        queryParamDto.setSearchTerm(search);
        queryParamDto.setPageParam(pageParamDto);

        return ResponseEntity.ok(permissionService.executeGetListPermission(queryParamDto));
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
