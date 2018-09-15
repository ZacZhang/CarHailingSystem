DROP TABLE IF EXISTS Trips;

CREATE TABLE Trips (
	id long,
	driverId integer REFERENCES drivers (id),
	riderId integer REFERENCES riders (id),
	origin varchar(40),
	destination varchar(40),
	status integer,
	createdAt timestamp,
	PRIMARY KEY(id)
)