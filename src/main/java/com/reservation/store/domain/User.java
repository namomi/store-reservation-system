package com.reservation.store.domain;

import com.reservation.store.constant.Role;
import com.reservation.store.dto.UserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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

    @NotNull
    private String name;

    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private String phone;

    @Builder.Default
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();
    public List<Store> getStores() {
        return stores;
    }

    public void setStore(Store store) {
        this.stores.add(store);
        store.setManager(this);
    }


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
