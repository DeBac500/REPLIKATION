drop table if exists log_123;
CREATE TABLE log_123(
	act VARCHAR(255),
	tab VARCHAR(255),
	col VARCHAR(255),
	valNEW VARCHAR(255),
	valOLD VARCHAR(255),
	tim timestamp,
	PRIMARY KEY (act,tab,col,valNEW,valOLD,tim))
ENGINE=INNODB;