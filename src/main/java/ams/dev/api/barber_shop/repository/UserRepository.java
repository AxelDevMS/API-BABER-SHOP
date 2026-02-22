package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByUsernameAndIsActive(String username, Boolean isActive);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndIdNot(String username, String id);
}
