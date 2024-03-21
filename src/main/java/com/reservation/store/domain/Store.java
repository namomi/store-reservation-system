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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 1024)
    private String description;

    @ManyToOne
    private User manager;

    public void setManager(User manager) {
        this.manager = manager;
    }

    public static Store registerStore(StoreInfo storeInfo, User user) {
        return Store.builder()
                .name(storeInfo.getName())
                .location(storeInfo.getLocation())
                .description(storeInfo.getDescription())
                .manager(user)
                .build();
    }
}
