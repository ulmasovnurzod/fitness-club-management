package uz.codelog.fitnessclubmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.codelog.fitnessclubmanagement.enums.TokenType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    private boolean revoked;

    private boolean expired;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
}
