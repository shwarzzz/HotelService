INSERT INTO hotel (name, rating, address)
VALUES ('Grand Hotel', 100, 'Russia, Moscow, Leninskiy prospect, 15'),
       ('Camping Welgelegen', 80, 'Netherlands, Workum, Lange Lean, 11'),
       ('MEININGER', 100, 'Netherlands, Amsterdam, Julianaplein, 6');

INSERT INTO room_type(name)
VALUES ('Пентхаус'),
       ('Семейный'),
       ('Двухместный'),
       ('Трехместный');

INSERT INTO room (hotel_id, type, cost, available, max_guest)
VALUES (1, 'Пентхаус', 20000, true, 3),
       (1, 'Пентхаус', 50000, true, 4),
       (1, 'Семейный', 20000, true, 4),
       (1, 'Двухместный', 35000, true, 2),
       (1, 'Трехместный', 30000, true, 3),
       (2, 'Двухместный', 30000, true, 2),
       (2, 'Двухместный', 25000, true, 2),
       (2, 'Трехместный', 35000, true, 3),
       (2, 'Двухместный', 25000, true, 2),
       (3, 'Пентхаус', 100000, true, 1);