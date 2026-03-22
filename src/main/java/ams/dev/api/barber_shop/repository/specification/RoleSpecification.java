package ams.dev.api.barber_shop.repository.specification;

import ams.dev.api.barber_shop.dto.role.RoleFilerDto;
import ams.dev.api.barber_shop.entity.RoleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RoleSpecification {

    public static Specification<RoleEntity> withRoleId(String permissionId){
        return (root,query,cb)-> permissionId == null ?
                cb.conjunction() :
                cb.equal(root.get("id"), permissionId);
    }

    public static Specification<RoleEntity> withDeleted(Boolean deleted) {
        return (root,query,cb)->{
            if (deleted == null)
                return cb.conjunction();

            return cb.equal(root.get("isDeleted"), deleted);
        };
    }

    public static Specification<RoleEntity> withActive(Boolean active){
        return (root,query,cb)->{
            if (active == null)
                return cb.conjunction();

            return cb.equal(root.get("isActive"), active);
        };
    }

    public static Specification<RoleEntity> withCreatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            // Nota: createdAt es de tipo Date en tu entidad
            return cb.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<RoleEntity> withCreatedBefore(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            return cb.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<RoleEntity> withSearchTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            // Buscar en fullName (único campo de nombre), email y phone
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(cb.lower(root.get("description")), likePattern)

            );
        };
    }

    /**
     * Método que combina todas las specifications de manera elegante
     */
    public static Specification<RoleEntity> combineFromFilter(RoleFilerDto queryParamDto) {
        return Specification
                .where(withRoleId(queryParamDto != null ? queryParamDto.getRoleId() : null))
                .and(withDeleted(queryParamDto != null ? queryParamDto.getIsDeleted() : null))
                .and(withActive(queryParamDto != null ? queryParamDto.getIsActive() : null))
                .and(withCreatedAfter(queryParamDto != null ? queryParamDto.getCreatedAfter() : null))
                .and(withCreatedBefore(queryParamDto != null ? queryParamDto.getCreatedBefore() : null))
                .and(withSearchTerm(queryParamDto != null ? queryParamDto.getSearchTerm() : null));

    }

}
