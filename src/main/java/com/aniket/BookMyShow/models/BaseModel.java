package com.aniket.BookMyShow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreatedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
