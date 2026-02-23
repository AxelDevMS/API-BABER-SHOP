package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.RoleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRespository  extends JpaRepository<RoleEntity,String> {

    @EntityGraph(attributePaths = "permissions")
    Optional<RoleEntity> findByName(String name);

    Optional<RoleEntity> findByNameAndIdNot(String name, String id);
}
