package com.aniket.BookMyShow.repository;

import com.aniket.BookMyShow.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ShowSheatRepository extends JpaRepository<ShowSeat, Long> {
    @Override
    Optional<ShowSeat> findById(Long showSheatId);
    @Override
    ShowSeat save(ShowSeat showSeat);
}
