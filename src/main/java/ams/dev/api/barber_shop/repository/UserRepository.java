package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.dto.UserResponseDto;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByUsernameAndIsActive(String username, Boolean isActive);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndIdNot(String username, String id);

    @Query(
            "SELECT new ams.dev.api.barber_shop.dto.UserResponseDto(" +
                    "u.id," +
                    "u.name," +
                    "u.lastName," +
                    "u.phone," +
                    "u.username)" +
                    "FROM UserEntity u JOIN u.barberShops b WHERE b.id = :barberShopId"
    )
    List<UserResponseDto> findAllUserByBarberShopId(@Param("barberShopId") String barberShopId);
}
