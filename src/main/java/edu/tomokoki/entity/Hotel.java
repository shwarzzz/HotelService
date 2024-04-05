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
@Table(name = "hotel")
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 80)
    private String name;

    @Check(constraints = "rating > 0 AND rating <= 100")
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "address", length = 100)
    private String address;

    @OneToMany(mappedBy = "hotel")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Visit> visits;

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Отель{", "}");
        joiner.add("id: " + id)
                .add("название: " + name)
                .add("рейтинг: " + rating)
                .add("адрес: " + address);
        return joiner.toString();
    }
}
