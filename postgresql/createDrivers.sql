DROP TABLE IF EXISTS Drivers;

CREATE TABLE Drivers (
	did integer,
	firstName varchar(40),
	lastName varchar(40),
	address varchar(200),
	phone varchar(20),
	createdAt timestamp,
	isActive boolean,
	PRIMARY KEY(did)
)