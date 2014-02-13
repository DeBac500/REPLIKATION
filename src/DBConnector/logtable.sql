drop table if exists log_123;
CREATE TABLE log_123(
	act VARCHAR(255),
	tab VARCHAR(255),
	col VARCHAR(255),
	valNEW TEXT,
	valOLD TEXT,
	tim timestamp,
	PRIMARY KEY (tim))
ENGINE=INNODB;