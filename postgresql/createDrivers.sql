DROP TABLE IF EXISTS Drivers;

CREATE TABLE Drivers (
	id bigint,
	first_name varchar(40),
	last_name varchar(40),
	address varchar(200),
	phone varchar(20),
	created_at timestamp,
	is_active boolean,
	PRIMARY KEY(id)
)
