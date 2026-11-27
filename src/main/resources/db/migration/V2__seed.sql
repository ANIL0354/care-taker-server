INSERT INTO users (id, name, email, phone, password, role)
VALUES (1, 'CareRide Admin', 'admin@careride.com', '9999999999',
        '$2a$10$usUmAATARU4glOEeVgZSI.lyY69khEsTlFTB4h4Jj9VY389EUXitC', 'ADMIN');

INSERT INTO hospitals (id, name, address, city)
VALUES (1, 'City General Hospital', '100 Health Avenue', 'Bengaluru'),
       (2, 'St. Mary Medical Center', '22 Church Street', 'Bengaluru'),
       (3, 'Green Valley Hospital', '8 Park Road', 'Mumbai');

WITH RECURSIVE day_numbers AS (
    SELECT 1 AS day_offset
    UNION ALL
    SELECT day_offset + 1 FROM day_numbers WHERE day_offset < 14
),
window_numbers AS (
    SELECT 0 AS window_offset
    UNION ALL
    SELECT window_offset + 1 FROM window_numbers WHERE window_offset < 27
)
INSERT INTO time_slots (slot_start, slot_end, capacity, booked_count)
SELECT CURRENT_DATE + day_offset * INTERVAL '1 day' + (360 + window_offset * 30) * INTERVAL '1 minute',
       CURRENT_DATE + day_offset * INTERVAL '1 day' + (390 + window_offset * 30) * INTERVAL '1 minute',
       3,
       0
FROM day_numbers
CROSS JOIN window_numbers;

SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('hospitals', 'id'), (SELECT MAX(id) FROM hospitals));
