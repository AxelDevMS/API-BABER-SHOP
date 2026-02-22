package ams.dev.api.barber_shop.entity;
import ams.dev.api.barber_shop.enums.BarberShopStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "barbershop")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    private String mobile;

    private String email;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "opening_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    private String timezone;

    @Enumerated(EnumType.STRING)
    private BarberShopStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_barbershop",
            joinColumns = @JoinColumn(name = "barbershop_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
