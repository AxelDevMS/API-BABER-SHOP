package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.ServiceEntity;
import ams.dev.api.barber_shop.enums.Constants;
import ams.dev.api.barber_shop.enums.ConstantsSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, String>, JpaSpecificationExecutor<ServiceEntity> {

    @Query(name = ConstantsSQL.SELECT_SERVICE_ID_AND_BARBERSHOP_ID, nativeQuery = true)
    Optional<ServiceEntity> findByIdAndBarbershopId(@Param(Constants.PARAM_STATIC_SERVICE) String serviceId, @Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId);

    @Query(name = ConstantsSQL.SELECT_LIST_SERVICE_BY_BARBERSHOP_ID, nativeQuery = true)
    List<ServiceEntity> findAllByBarbershopId(@Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId);

    @Query(name = ConstantsSQL.SELECT_SERVICE_BY_NAME_AND_BARBERSHOP_ID, nativeQuery = true)
    List<ServiceEntity> findByNameAndBarbershopId(@Param(Constants.NAME_SERVICE)String name, @Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId);

}
