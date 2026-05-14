package uz.codelog.fitnessclubmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "devices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "user_id"})
)
public class Device extends BaseEntity {

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    private String deviceName;

    private String deviceType;

    private LocalDateTime lastLogin;

    @Column(name = "active")
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
