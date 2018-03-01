CREATE TABLE server_illegal_accesses (
	id int NOT NULL AUTO_INCREMENT,
    start_date DATETIME,
    end_date DATETIME,
    ip varchar(15),
    access_count int,
    PRIMARY KEY (id)
 );