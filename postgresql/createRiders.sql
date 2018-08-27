DROP TABLE IF EXISTS Riders;

CREATE TABLE Riders (
	rid integer,
	firstName varchar(40),
	lastName varchar(40),
	address varchar(200),
	phone varchar(20),
	payment varchar(200),
	createdAt timestamp,
	PRIMARY KEY(rid)
)