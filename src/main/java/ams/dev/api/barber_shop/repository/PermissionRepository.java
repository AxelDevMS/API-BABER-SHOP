package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Permission;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity,String> {

    Optional<PermissionEntity> findByName(String name);
    Optional<PermissionEntity> findByNameAndIdNot(String name, String id);
}
