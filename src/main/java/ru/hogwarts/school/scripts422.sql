CREATE TABLE cars
(
    id SERIAL PRIMARY KEY,
    brand VARCHAR,
    model VARCHAR,
    price NUMERIC CHECK (price > 0)
);

CREATE TABLE people
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR,
    age         INTEGER,
    has_license BOOLEAN DEFAULT 0,
    car_id      INTEGER REFERENCES cars (id)
);
