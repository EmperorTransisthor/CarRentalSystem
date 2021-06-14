DROP TABLES IF EXISTS tblCarBasic, tblCustomerBasic, tblRental, tblCarInfo;

CREATE TABLE tblCarBasic
(
    car_no			VARCHAR(6),
    make			VARCHAR(20),
    model			VARCHAR(20),
    available		VARCHAR(4),
    rentPricePerDay	INT	NOT NULL,
    PRIMARY KEY (car_no)
);

CREATE TABLE tblCustomerBasic
(
    cust_id		VARCHAR(6) NOT NULL,
    name		VARCHAR(20) NOT NULL,
    address		TEXT,
    phoneNum	VARCHAR(20),
    PRIMARY KEY (cust_id)
);

CREATE TABLE tblRental
(
	id			INT NOT NULL auto_increment,
    car_id		VARCHAR(6) NOT NULL,
    cust_id		VARCHAR(6) NOT NULL,
    fee			INT NOT NULL,
    date		DATE NOT NULL,
    returnDate	DATE NOT NULL,
    state		ENUM ('active', 'rented', 'returned', 'cancelled') NOT NULL,
    PRIMARY KEY(id)
) ENGINE = InnoDB auto_increment = 1;

/*
CREATE TABLE tblCarInfo
(
	car_id			VARCHAR(255) 	NOT NULL,
    enginePower		INT,
    rentPricePerDay	INT				NOT NULL,
    #category		ENUM ('B', 'B1')NOT NULL,
    PRIMARY KEY(car_id)
);
*/

INSERT INTO tblCarBasic (car_no, make, model, available, rentPricePerDay) VALUES
("C0001", "Ford", "Mondeo", "Yes", 100),
("C0002", "Audi", "A4", "No", 150),
("C0003", "Mercedes", "Genz", "No", 1000),
("C0004", "Fiat", "Panda", "Yes", 80),
("C0005", "Porshe", "Carera", "Yes", 70),
("C0006", "BWM", "X3", "No", 300),
("C0007", "Volvo", "XC70", "Yes", 200);


INSERT INTO tblCustomerBasic (cust_id, name, address, phoneNum) VALUES
("C0001", "Jan Dorozd", "Warszawa Piotrusia pana 12", "123456789"),
("C0002", "Kazimierz Tetmajer", "Warszawa Aleje Jerozolimskie 121", "121341789"),
("C0003", "Jan Sobieski", "Kraków Nawojki 56", "123456483"),
("C0004", "Józef Piłsudski", "Sulejówek Dębów 47", "122751381"),
("C0005", "Stefan Batory", "", "122751228"),
("C0006", "Andrzej Czerw", "", "122751075"),
("C0007", "Kazimierz Wielki", "", "122750922"),
("C0008", "Jan Sobieski", "", "122750769"),
("C0009", "Gar Field", "", "999888777"),
("C0010", "Padding Ton", "", "486543164");

