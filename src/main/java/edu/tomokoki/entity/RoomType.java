package edu.tomokoki.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "room_type")
@NoArgsConstructor
public class RoomType {
    @Id
    @Column(name = "name", length = 50)
    private String type;
}
