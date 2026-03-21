package ams.dev.api.barber_shop.repository.specification;

import ams.dev.api.barber_shop.dto.service.ServiceFilterDto;
import ams.dev.api.barber_shop.entity.ServiceEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ServiceSpecification {

    public static Specification<ServiceEntity> withServiceId(String serviceId){
        return (root,query,cb)-> serviceId == null ?
                cb.conjunction() :
                cb.equal(root.get("id"), serviceId);
    }

    public static Specification<ServiceEntity> withBarbershopId(String barbershopId) {
        return (root, query, cb) -> barbershopId == null ?
                cb.conjunction() :
                cb.equal(root.get("barbershop").get("id"), barbershopId);
    }

    public static Specification<ServiceEntity> withActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            // Mapeo correcto: isActive es el nombre del campo en la entidad
            return cb.equal(root.get("isActive"), active);
        };
    }

    public static Specification<ServiceEntity> withDeleted(Boolean deleted) {
        return (root, query, cb) -> {
            if (deleted == null) {
                return cb.conjunction();
            }
            // Mapeo correcto: isDeleted es el nombre del campo en la entidad
            return cb.equal(root.get("isDeleted"), deleted);
        };
    }


    public static Specification<ServiceEntity> withCreatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            // Nota: createdAt es de tipo Date en tu entidad
            return cb.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<ServiceEntity> withCreatedBefore(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            return cb.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<ServiceEntity> withSearchTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            // Buscar en fullName (único campo de nombre), email y phone
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(root.get("phone"), "%" + searchTerm + "%")
            );
        };
    }

    /**
     * Método que combina todas las specifications de manera elegante
     */
    public static Specification<ServiceEntity> combineFromFilter(ServiceFilterDto queryParamDto) {
        return Specification
                .where(withBarbershopId(queryParamDto != null ? queryParamDto.getBarbershopId() : null))
                .and(withServiceId(queryParamDto != null ? queryParamDto.getServiceId() : null))
                .and(withActive(queryParamDto != null ? queryParamDto.getIsActive() : null))
                .and(withDeleted(queryParamDto != null ? queryParamDto.getIsDeleted() : null))
                .and(withCreatedAfter(queryParamDto != null ? queryParamDto.getCreatedAfter() : null))
                .and(withCreatedBefore(queryParamDto != null ? queryParamDto.getCreatedBefore() : null))
                .and(withSearchTerm(queryParamDto != null ? queryParamDto.getSearchTerm() : null));
    }



}
