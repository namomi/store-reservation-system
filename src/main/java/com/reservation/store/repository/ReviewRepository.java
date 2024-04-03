package com.reservation.store.repository;

import com.reservation.store.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByReservationId(Long id);
}
