package edu.tomokoki.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "hotels_visits")
@NoArgsConstructor
public class Visit {
    @EmbeddedId
    private VisitKey visitKey;

    @Column(name = "visit_date")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date date;

    @ManyToOne
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private Hotel hotel;
}
