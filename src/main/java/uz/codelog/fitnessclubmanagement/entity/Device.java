package uz.codelog.fitnessclubmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "user_id"})
)

public class Device extends BaseEntity {

    @Column(nullable = false)
    private String deviceId;

    private String deviceName;

    private String deviceType;

    private LocalDateTime lastLogin;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
