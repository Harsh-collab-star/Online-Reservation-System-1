CREATE DATABASE harsh_kumar;
USE harsh_kumar;

CREATE TABLE reservations (
    pnr_number INT PRIMARY KEY,
    passenger_name VARCHAR(100),
    train_number VARCHAR(20),
    class_type VARCHAR(50),
    journey_date DATE,
    from_location VARCHAR(50),
    to_location VARCHAR(50)
);
