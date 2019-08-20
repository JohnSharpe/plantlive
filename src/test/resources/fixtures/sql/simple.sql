INSERT INTO plants (id, user_id, password, type) VALUES (1, 'a483df51-e18e-4053-9875-6c753ca14997', 'a', 'cactus');
INSERT INTO plants (id, user_id, password, type) VALUES (2, '8159e4e2-700b-406c-865c-65f08d341572', 'b', 'rose');
INSERT INTO plants (id, user_id, password, type) VALUES (3, '03adf61a-fada-45e8-a88e-af0c2f97b9d3', 'c', 'dandelion');
INSERT INTO plants (id, user_id, password, type) VALUES (4, '4f1f1997-4922-4828-9858-2b15f291b6c6', 'd', 'lily');

ALTER SEQUENCE plants_id_seq RESTART WITH 5;

-- Reports once a day
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (1, 1, '2018-10-01 12:00:00', 21, 88, 44, 23);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (2, 1, '2018-10-02 12:00:00', 22, 89, 42, 20);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (3, 1, '2018-10-03 12:00:00', 21, 89, 41, 18);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (4, 1, '2018-10-04 12:00:00', 20, 80, 45, 12);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (5, 1, '2018-10-05 12:00:00', 19, 79, 47, 28);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (6, 1, '2018-10-06 12:00:00', 23, 84, 49, 24);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (7, 1, '2018-10-07 12:00:00', 27, 83, 42, 23);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (8, 1, '2018-10-08 12:00:00', 26, 80, 38, 22);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (9, 1, '2018-10-09 12:00:00', 20, 77, 42, 15);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (10, 1, '2018-10-10 12:00:00', 21, 83, 48, 26);

-- Reports once an hour
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (11, 2, '2018-11-21 09:00:00', 21, 88, 44, 23);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (12, 2, '2018-11-21 10:00:00', 22, 89, 42, 20);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (13, 2, '2018-11-21 11:00:00', 21, 89, 41, 18);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (14, 2, '2018-11-21 12:00:00', 20, 80, 45, 12);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (15, 2, '2018-11-21 13:00:00', 19, 79, 47, 28);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (16, 2, '2018-11-21 14:00:00', 23, 84, 49, 24);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (17, 2, '2018-11-21 15:00:00', 27, 83, 42, 23);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (18, 2, '2018-11-21 16:00:00', 26, 80, 38, 22);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (19, 2, '2018-11-21 17:00:00', 20, 77, 42, 15);
INSERT INTO details (id, plant_id, in_timestamp, temperature, humidity, light, conductivity)
    VALUES (20, 2, '2018-11-21 18:00:00', 21, 83, 48, 26);

ALTER SEQUENCE details_id_seq RESTART WITH 21;