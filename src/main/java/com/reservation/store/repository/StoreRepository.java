package com.reservation.store.repository;

import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByLocation(String location);

    Optional<Store> findByName(String name);

}
