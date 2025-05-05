package com.aniket.BookMyShow.repository;

import com.aniket.BookMyShow.models.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {
    @Override
    Optional<SeatType> findById(Long aLong);
    @Override
    SeatType save(SeatType seatType);
}
