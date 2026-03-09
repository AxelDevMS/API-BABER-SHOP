package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.role.RoleFilerDto;
import ams.dev.api.barber_shop.dto.role.RoleRequestDto;
import ams.dev.api.barber_shop.dto.role.RoleResponseDto;
import ams.dev.api.barber_shop.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> executeCreateRole(@RequestBody @Valid RoleRequestDto roleRequestDto){
        return ResponseEntity.ok(roleService.executeCreateRole(roleRequestDto));
    }

    /*@GetMapping
    public ResponseEntity<List<RoleResponseDto>> executeGetRoles(){
        return ResponseEntity.ok(roleService.executeGetRoles());
    }*/

    @GetMapping
    public ResponseEntity<PageResponseDto<RoleResponseDto>> executeGetListRole(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) Boolean active
    ){
        PageParamRequestDto pageParamDto = new PageParamRequestDto(page,size,sortBy,sortDirection);
        RoleFilerDto queryParamDto = new RoleFilerDto();
        queryParamDto.setCreatedAfter(createdAfter);
        queryParamDto.setCreatedBefore(createdBefore);
        queryParamDto.setSearchTerm(search);
        queryParamDto.setRoleId(roleId);
        queryParamDto.setIsDeleted(deleted);
        queryParamDto.setIsActive(active);
        queryParamDto.setPageParam(pageParamDto);

        return ResponseEntity.ok(roleService.executeGetListRole(queryParamDto));
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
