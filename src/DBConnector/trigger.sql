DELIMITER |
CREATE TRIGGER logINS AFTER INSERT ON genre2 FOR EACH ROW
BEGIN
	INSERT INTO log_123 (act,tab,col,val,tim) 
	VALUES('INSERT','genre2','gbez',NEW.gbez,NOW());
END
|
DELIMITER ;
DELIMITER |
CREATE TRIGGER logUPD AFTER UPDATE ON genre2 FOR EACH ROW
BEGIN
	INSERT INTO log_123 (act,tab,col,val,tim)
	VALUES('UPDATE','genre2','gbez',NEW.gbez,NOW());
END;
|
DELIMITER ;
DELIMITER |
CREATE TRIGGER logDEL BEFORE DELETE ON genre2 FOR EACH ROW
BEGIN
	INSERT INTO log_123 (act,tab,col,val,tim)
	VALUES('DELETE','genre2','gbez',OLD.gbez,NOW());
END;
|
DELIMITER ;


Tabelle test
a1 , a2 , a3
12 , 34 , 54

'INSERT','genre2','gbez',12,NOW()
'INSERT','genre2','gbez',34,NOW()
'INSERT','genre2','gbez',54,NOW()