DROP TABLE IF EXISTS Orders;

CREATE TABLE Orders (
    id bigint,
    trip_id bigint REFERENCES trips (id),
    status integer,
    created_at timestamp,
    updated_at timestamp,
    PRIMARY KEY(id)
);