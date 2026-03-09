package ams.dev.api.barber_shop.entity;
import ams.dev.api.barber_shop.enums.BarberShopStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "barbershop")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SQLRestriction("is_deleted = false")
public class BarberShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(name = "commercial_name")
    private String commercialName;

    @Column(name = "tax_id")
    private String taxId;

    private String address;

    private String phone;

    private String email;

    @Column(name = "opening_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private BarberShopStatus status;

    // Relación con usuarios a través de user_barbershops
    @OneToMany(mappedBy = "barbershop", fetch = FetchType.LAZY)
    private List<UserBarberShopEntity> userAssignments;

    @OneToMany(mappedBy = "barbershop", fetch = FetchType.LAZY)
    private List<ClientEntity> clients;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
