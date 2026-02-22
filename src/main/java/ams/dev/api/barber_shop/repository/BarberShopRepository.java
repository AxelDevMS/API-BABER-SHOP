package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.BarberShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberShopRepository extends JpaRepository<BarberShopEntity,String> {
}
