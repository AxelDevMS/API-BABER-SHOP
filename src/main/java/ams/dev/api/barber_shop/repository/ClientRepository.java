package ams.dev.api.barber_shop.repository;

import ams.dev.api.barber_shop.entity.ClientEntity;
import ams.dev.api.barber_shop.enums.Constants;
import ams.dev.api.barber_shop.enums.ConstantsSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,String>, JpaSpecificationExecutor<ClientEntity> {

   // 1. Buscar por email y barbershop_id
    @Query(name = ConstantsSQL.SELECT_CLIENT_EMAIL_AND_BARBERSHOP_ID, nativeQuery = true)
    Optional<ClientEntity> findByEmailAndBarbershopId(@Param(Constants.PARAM_EMAIL) String email,
                                                      @Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId);


    // 2. Buscar por email y barbershop_id excluyendo un cliente específico
    @Query(name = ConstantsSQL.SELECT_CLIENT_EMAIL_AND_BARBERSHOP_AND_IDNOT, nativeQuery = true)
    Optional<ClientEntity> findByEmailAndBarbershopIdAndIdNot( @Param(Constants.PARAM_EMAIL) String email,
                                                               @Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId,
                                                               @Param(Constants.PARAM_CLIENT_ID) String clientId);

    // 3. Buscar por ID de cliente y barbershop_id
    @Query(name = ConstantsSQL.SELECT_CLIENT_ID_AND_BARBERSHOP_ID, nativeQuery = true)
    Optional<ClientEntity> findByIdAndBarbershopId(@Param(Constants.PARAM_CLIENT_ID) String clientId,
                                                   @Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId);

    // 4. Buscar todos los clientes por barbershop_id
    @Query(name = ConstantsSQL.SELECT_CLIENTS_BY_BARBERSHOP_ID, nativeQuery = true)
    List<ClientEntity> findAllByBarbershopId(@Param(Constants.PARAM_BARBERSHOP_ID) String barbershopId);
}