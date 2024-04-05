package edu.tomokoki.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;

import java.util.List;
import java.util.StringJoiner;

@Data
@Entity
@Table(name = "person")
@NoArgsConstructor
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "email", length = 50)
    private String email;

    @Formula(value = "(SELECT COUNT(*) FROM hotels_visits hv WHERE hv.person_id = id)")
    private int visitsCount;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Visit> visits;

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Пользователь{", "}");
        return joiner
                .add("id: " + id)
                .add("ФИО: " + fio)
                .add("email: " + email)
                .toString();
    }
}