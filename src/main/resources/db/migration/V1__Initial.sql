

CREATE TABLE valid_deal (
	id int not null AUTO_INCREMENT PRIMARY KEY, 
	deal_id int,
	from_currency_code char(3),
	to_currency_code char(3),
	file_info_id int,
	amount decimal(15,2),
	deal_timestamp timestamp
);

CREATE TABLE invalid_deal (
	id int not null AUTO_INCREMENT PRIMARY KEY, 
	deal_id varchar(50),
	from_currency_code varchar(50),
	to_currency_code varchar(50),
	file_info_id int,
	amount varchar(100),
	deal_timestamp varchar(255)
);

CREATE TABLE file_import_info (
	id int not null AUTO_INCREMENT PRIMARY KEY, 
	filename varchar(255),
	CONSTRAINT uc_file_import_info UNIQUE (filename)
);

CREATE TABLE currency_deal (
	id int not null AUTO_INCREMENT PRIMARY KEY, 
	currency_code char(3),
	count_of_deals int default 0,
	CONSTRAINT uk_currency_deal UNIQUE (currency_code)
);
