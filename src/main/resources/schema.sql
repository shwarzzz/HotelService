BEGIN;
DROP TABLE IF EXISTS hotel CASCADE;
CREATE TABLE IF NOT EXISTS hotel
(
    id      serial,
    name    varchar(80),
    rating  integer,
    address varchar(100),
    PRIMARY KEY (id),
    CONSTRAINT ch_hotel_rating CHECK (rating > 0 AND rating <= 100)
);

DROP TABLE IF EXISTS room_type CASCADE;
CREATE TABLE IF NOT EXISTS room_type
(
    name varchar(50),
    PRIMARY KEY (name)
);

DROP TABLE IF EXISTS room CASCADE;
CREATE TABLE IF NOT EXISTS room
(
    id                   serial,
    hotel_id             bigint,
    type                 varchar(50),
    cost                 integer,
    available            bool,
    current_guests_count integer DEFAULT 0,
    max_guest            integer,
    PRIMARY KEY (id),
    CONSTRAINT ch_current_guest_count CHECK (room.current_guests_count >= 0),
    CONSTRAINT ch_max_guest_count CHECK (max_guest > 0 AND max_guest < 5),
    CONSTRAINT fk_room_hotel_id FOREIGN KEY (hotel_id) REFERENCES hotel (id),
    CONSTRAINT fk_room_type_id FOREIGN KEY (type) REFERENCES room_type (name)
);


DROP TABLE IF EXISTS person CASCADE;
CREATE TABLE IF NOT EXISTS person
(
    id      serial,
    fio     varchar(120),
    email   varchar(50),
    room_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_guest_room_id FOREIGN KEY (room_id) REFERENCES room (id)
);

DROP TABLE IF EXISTS visits CASCADE;
CREATE TABLE IF NOT EXISTS hotels_visits
(
    person_id  serial,
    hotel_id   bigint,
    visit_date date,
    CONSTRAINT fk_visits_person_id FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT fk_visits_hotel_id FOREIGN KEY (hotel_id) REFERENCES hotel (id)
);
COMMIT;