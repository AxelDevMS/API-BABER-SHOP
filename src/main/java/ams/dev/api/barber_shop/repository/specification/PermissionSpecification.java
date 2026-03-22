package ams.dev.api.barber_shop.repository.specification;

import ams.dev.api.barber_shop.dto.permission.PermissionFilterDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PermissionSpecification {

    public static Specification<PermissionEntity> withPermissionId(String permissionId){
        return (root,query,cb)-> permissionId == null ?
                cb.conjunction() :
                cb.equal(root.get("id"), permissionId);
    }

    public static Specification<PermissionEntity> withModule(String module){
        return  (root,query,cb)-> module == null ?
                cb.conjunction() :
                cb.equal(root.get("module"), module);
    }

    public static Specification<PermissionEntity> withDeleted(Boolean deleted) {
        return (root,query,cb)->{
            if (deleted == null)
                return cb.conjunction();

            return cb.equal(root.get("isDeleted"), deleted);
        };
    }

    public static Specification<PermissionEntity> withActive(Boolean active){
        return (root,query,cb)->{
            if (active == null)
                return cb.conjunction();

            return cb.equal(root.get("isActive"), active);
        };
    }

    public static Specification<PermissionEntity> withCreatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            // Nota: createdAt es de tipo Date en tu entidad
            return cb.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<PermissionEntity> withCreatedBefore(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            return cb.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<PermissionEntity> withSearchTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            // Buscar en fullName (único campo de nombre), email y phone
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(cb.lower(root.get("description")), likePattern),
                    cb.like(cb.lower(root.get("module")), likePattern),
                    cb.like(cb.lower(root.get("action")), likePattern)
            );
        };
    }

    /**
     * Método que combina todas las specifications de manera elegante
     */
    public static Specification<PermissionEntity> combineFromFilter(PermissionFilterDto paramsDto) {
        return Specification
                .where(withPermissionId(paramsDto != null ? paramsDto.getPermissionId() : null))
                .and(withModule(paramsDto != null ? paramsDto.getModule() : null))
                .and(withDeleted(paramsDto != null ? paramsDto.getDeleted() : null))
                .and(withActive(paramsDto != null ? paramsDto.getActive() : null))
                .and(withCreatedAfter(paramsDto != null ? paramsDto.getCreatedAfter() : null))
                .and(withCreatedBefore(paramsDto != null ? paramsDto.getCreatedBefore() : null))
                .and(withSearchTerm(paramsDto != null ? paramsDto.getSearchTerm() : null));

    }
}
