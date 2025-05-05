package com.aniket.BookMyShow.repository;

import com.aniket.BookMyShow.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowSheatRepository extends JpaRepository<ShowSeat, Long> {
    @Override
    Optional<ShowSeat> findById(Long showSheatId);
    @Override
    ShowSeat save(ShowSeat showSeat);
}
