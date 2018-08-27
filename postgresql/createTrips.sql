DROP TABLE IF EXISTS Trips;

CREATE TABLE Trips (
	tid integer,
	driverId integer REFERENCES drivers (did),
	riderId integer REFERENCES riders (rid),
	origin varchar(40),
	destination varchar(40),
	status integer,
	createdAt timestamp,
	PRIMARY KEY(tid)
)