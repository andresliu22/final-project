drop database if exists db_fp;
create database db_fp;

use db_fp;

CREATE TABLE IF NOT EXISTS user (
	user_id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_name varchar(20) NOT NULL,
    user_password varchar(20) NOT NULL,
    Constraint uq_user_name Unique (user_name)
);

CREATE TABLE IF NOT EXISTS employee (
	employee_id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_id int NOT NULL,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    cell_phone int NOT NULL
);
	
CREATE TABLE IF NOT EXISTS address (
	address_id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    employee_id int NOT NULL,
    address_line1 varchar(30) NOT NULL,
    address_line2 varchar(30),
    city varchar(30) NOT NULL,
    zipcode varchar(30) NOT NULL,
    state varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS emergency_contact (
	emergency_contact_id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    employee_id int NOT NULL,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    cell_phone int NOT NULL
);
