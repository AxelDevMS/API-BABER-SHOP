package ams.dev.api.barber_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "permission")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SQLRestriction("is_deleted = false")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private String module;

    private String action;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RoleEntity> roles;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;




}
