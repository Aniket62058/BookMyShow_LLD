package com.aniket.BookMyShow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Show extends BaseModel{
    @ManyToOne
    private Movie movie;
    private Date startTime;
    private Date endTime;
    private Screen screen;
    @Enumerated(EnumType.ORDINAL)
    @ElementCollection  // mapping table for screen to features
    private List<Feature> features;
    private List<ShowSeat> showSeats;
}
