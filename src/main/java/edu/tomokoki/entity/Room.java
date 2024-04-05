package edu.tomokoki.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Check;

import java.util.List;
import java.util.StringJoiner;

@Data
@Entity
@Table(name = "room")
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "available")
    private Boolean isAvailable;

    @Check(constraints = "max_guest > 0 AND max_guest < 5")
    @Column(name = "max_guest")
    private Integer maxGuests;

    @Check(constraints = "current_guests_count >= 0")
    @Column(name = "current_guests_count")
    private int currentGuestsCount;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Person> persons;

    @ManyToOne
    @JoinColumn(name = "type")
    private RoomType type;

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Номер{", "}");
        joiner.add("id: " + id)
                .add("отель: " + hotel.getName())
                .add("макс. кол-во гостей: " + maxGuests)
                .add("текущее кол-во гостей: " + currentGuestsCount)
                .add("тип: " + type.getType())
                .add("свободна ли комната: " + isAvailable);
        return joiner.toString();
    }
}