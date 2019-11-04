DROP DATABASE TAXI;
CREATE DATABASE TAXI;

USE TAXI;
show tables;

CREATE TABLE id(
id_code VARCHAR(20) primary key,
id_type varchar(20)
);

CREATE TABLE mem(
mem_id VARCHAR(20) NOT NULL,
mem_password VARCHAR(255) NOT NULL,
mem_name VARCHAR(20) NOT NULL,
mem_phonenumber VARCHAR(20) NOT NULL,
mem_code VARCHAR(20) NOT NULL,
isLogin boolean,
PRIMARY KEY(mem_id),
foreign key(mem_code) references id(id_code)
);

CREATE TABLE driver(
driver_id varchar(20) NOT NULL primary key,
longitude double,
latitude double,
isDrive boolean,
customer_id varchar(20),
customer_on boolean,
foreign key(driver_id) references mem(mem_id),
foreign key(customer_id) references mem(mem_id)
);

CREATE TABLE customer(
customer_id varchar(20) NOT NULL primary key,
longitude double,
latitude double,
destination_longitude double,
destination_latitude double,
foreign key(customer_id) references mem(mem_id)
);

CREATE TABLE negativeList(
customer_id varchar(20) not null,
driver_id varchar(20) not null,
foreign key(customer_id) references customer(customer_id),
foreign key(driver_id) references driver(driver_id)
);

insert into id(id_code,id_type) values("0","driver");
insert into id(id_code,id_type) values("1","customer");
update mem set longitude = 3.0, latitude = 5.0 where mem_id="1234";
insert into mem(mem_id,mem_password,mem_name,mem_code,mem_phonenumber) 
values ('1234','1234','high','1','01046103240');
insert into driver(driver_id,isDrive) values("2345",true);
insert into negativeList(driver_id,customer_id) values("22","111");
update driver set customer_id = null , customer_on = false;
select * from mem;
select * from customer;
select * from driver;
SELECT customer_id from driver where driver_id = "22";
select * from negativeList;
select customer_id from driver;
select * from driver where customer_id = "22";
insert into negativeList(customer_id, driver_id) values ("111","123");
delete from negativeList where customer_id = "111";
delete from driver where driver_id = "234";
delete from mem where mem_id = "22";
delete from negativeList;
update driver set customer_id = "22" where driver_id = "11";
select * from customer;
update customer set longitude = 30, latitude = 30 where customer_id=55;
select driver.driver_id, driver.longitude, driver.latitude from driver left outer join negativeList on driver.driver_id = negativeList.driver_id and negativeList.customer_id = "111" where negativeList.driver_id is null;
select driver_id from driver where customer_id = "111";
update driver set longitude = null where driver_id = "22";
SELECT customer_id,longitude,latitude,destination_longitude,destination_latitude from customer where customer_id = "111";
update mem set isLogin = false where mem_id = "11";
/*
DROP TABLE MEM;
DROP TABLE driver;
DROP TABLE customer;
DROP TABLE negativeList;
DROP TABLE id;*/