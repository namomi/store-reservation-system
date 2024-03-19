package com.reservation.store.domain;

import com.reservation.store.constant.Role;
import com.reservation.store.dto.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "users")
@Entity
public class User extends BaseEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phone;

    public static User createUser(UserInfo userInfo, Role role, PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .password(passwordEncoder.encode(userInfo.getPassword()))
                .role(role)
                .phone(userInfo.getPhone())
                .build();
    }
}
