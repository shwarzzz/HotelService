package edu.tomokoki.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class VisitKey implements Serializable {
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "hotel_id")
    private Long hotelId;
}