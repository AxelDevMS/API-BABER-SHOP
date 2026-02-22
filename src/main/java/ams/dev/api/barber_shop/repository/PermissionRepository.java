package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.projection.PermissionBasicProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.Permission;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity,String> {

    Optional<PermissionEntity> findByName(String name);
    Optional<PermissionEntity> findByNameAndIdNot(String name, String id);
    List<PermissionEntity> findAllByIdIn(List<String> ids);


    @Query("SELECT new ams.dev.api.barber_shop.dto.PermissionResponseDto(p.id, p.name) " +
            "FROM PermissionEntity p JOIN p.roles r WHERE r.id = :roleId")
    List<PermissionResponseDto> findBasicDtoByRoleId(@Param("roleId") String roleId);

}
