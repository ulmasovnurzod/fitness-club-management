package uz.codelog.fitnessclubmanagement.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.codelog.fitnessclubmanagement.enums.Role;
import uz.codelog.fitnessclubmanagement.enums.UserStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;
}
