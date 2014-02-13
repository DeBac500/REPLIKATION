drop table if exists log_123;
CREATE TABLE log_123(
	act VARCHAR(255),
	tab VARCHAR(255),
	col VARCHAR(255),
	val VARCHAR(255),
	tim timestamp,
	PRIMARY KEY (tim))
ENGINE=INNODB;