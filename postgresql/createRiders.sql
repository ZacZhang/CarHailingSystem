DROP TABLE IF EXISTS Riders;

CREATE TABLE Riders (
	id bigint,
	first_name varchar(40),
	last_name varchar(40),
	address varchar(200),
	phone varchar(20),
	email varchar(40),
	payment varchar(200),
	created_at timestamp,
	PRIMARY KEY(id)
);