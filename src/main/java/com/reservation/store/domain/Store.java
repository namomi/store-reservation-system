package com.reservation.store.domain;

import com.reservation.store.dto.StoreInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String location;

    @Column(unique = true, nullable = false, length = 1024)
    private String description;

    @ManyToOne
    private User manager;

    public void setManager(User manager) {
        this.manager = manager;
    }

    public static Store registerStore(StoreInfo storeInfo, User user) {
        return Store.builder()
                .name(storeInfo.name())
                .location(storeInfo.location())
                .description(storeInfo.description())
                .manager(user)
                .build();
    }
}
