package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.RoleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,String>, JpaSpecificationExecutor<RoleEntity> {

    @EntityGraph(attributePaths = "permissions")
    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findByNameAndIdNot(String name, String id);
}
