DROP TABLE IF EXISTS Trips;

CREATE TABLE Trips (
  id bigint,
  driver_id bigint,
  rider_id bigint REFERENCES riders (id),
  origin varchar(40),
  destination varchar(40),
  status integer,
  created_at timestamp,
  updated_at timestamp,
  PRIMARY KEY(id)
);