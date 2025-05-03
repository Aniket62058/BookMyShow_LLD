package com.aniket.BookMyShow.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Ticket extends BaseModel{
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;
    @ManyToOne
    private Show show;
    @ManyToMany
    private List<ShowSeat> showSeats;
    @OneToMany
    private List<Payment> payments;
    @ManyToOne
    private User user;
    private Data bookedAt;
    private double amount;
}
