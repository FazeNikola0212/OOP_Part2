CREATE TABLE hotel (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255)
);

CREATE TABLE room (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      number VARCHAR(50) NOT NULL,
                      capacity INT,
                      price_per_night DECIMAL,
                      rating DOUBLE,
                      room_category VARCHAR(50),
                      room_status VARCHAR(50),
                      hotel_id BIGINT,
                      CONSTRAINT uq_room UNIQUE (number, hotel_id)
);
