package ams.dev.api.barber_shop.repository.specification;

import ams.dev.api.barber_shop.dto.client.ClientFilterDto;
import ams.dev.api.barber_shop.entity.ClientEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ClientSpecification {

    public static Specification<ClientEntity> withBarbershopId(String barbershopId) {
        return (root, query, cb) -> barbershopId == null ?
                cb.conjunction() :
                cb.equal(root.get("barbershop").get("id"), barbershopId);
    }

    public static Specification<ClientEntity> withActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            // Mapeo correcto: isActive es el nombre del campo en la entidad
            return cb.equal(root.get("isActive"), active);
        };
    }

    public static Specification<ClientEntity> withDeleted(Boolean deleted) {
        return (root, query, cb) -> {
            if (deleted == null) {
                return cb.conjunction();
            }
            // Mapeo correcto: isDeleted es el nombre del campo en la entidad
            return cb.equal(root.get("isDeleted"), deleted);
        };
    }

    public static Specification<ClientEntity> withVip(Boolean vip) {
        return (root, query, cb) -> {
            if (vip == null) {
                return cb.conjunction();
            }
            // Mapeo correcto: isVip es el nombre del campo en la entidad
            return cb.equal(root.get("isVip"), vip);
        };
    }

    public static Specification<ClientEntity> withCreatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            // Nota: createdAt es de tipo Date en tu entidad
            return cb.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<ClientEntity> withCreatedBefore(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            // Convertir LocalDateTime a Date si es necesario
            return cb.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<ClientEntity> withSearchTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            // Buscar en fullName (único campo de nombre), email y phone
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), likePattern),
                    cb.like(cb.lower(root.get("email")), likePattern),
                    cb.like(root.get("phone"), "%" + searchTerm + "%"),
                    cb.like(cb.lower(root.get("notes")), likePattern) // También buscar en notas si existe
            );
        };
    }

    /**
     * Método que combina todas las specifications de manera elegante
     */
    public static Specification<ClientEntity> combineFromFilter(ClientFilterDto filter) {
        return Specification
                .where(withBarbershopId(filter != null ? filter.getBarbershopId() : null))
                .and(withActive(filter != null ? filter.getActive() : null))
                .and(withDeleted(filter != null ? filter.getDeleted() : null))
                .and(withVip(filter != null ? filter.getVip() : null))
                .and(withCreatedAfter(filter != null ? filter.getCreatedAfter() : null))
                .and(withCreatedBefore(filter != null ? filter.getCreatedBefore() : null))
                .and(withSearchTerm(filter != null ? filter.getSearchTerm() : null));
    }

}