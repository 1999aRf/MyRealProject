CREATE TABLE Car (
    car_id SERIAL PRIMARY KEY,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    cost DECIMAL(10, 2)
);

CREATE TABLE Person (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT CHECK (age > 0),
    has_license BOOLEAN NOT NULL
);

SELECT
    p.person_id,
    p.name,
    p.age,
    p.has_license,
    c.car_id,
    c.make,
    c.model,
    c.cost
FROM
    Person p
JOIN
    PersonCar pc ON p.person_id = pc.person_id
JOIN
    Car c ON pc.car_id = c.car_id;
