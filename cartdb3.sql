-- Create from scratch
DROP DATABASE cartdb;
create database cartdb;
use cartdb;

create table product (
    id int not null AUTO_INCREMENT,
    name varchar(255) not null,
    description text,
    price float not null default 1,
    created_at timestamp default current_timestamp,
    primary key (id)
);

insert into product (name, price) values 
  ("Pineapple Tart", 10), 
  ("Mango", 7.3), 
  ("Pear", 2.2), 
  ("Carrot", 1.2);

create table cart (
    id int primary key not null auto_increment,
    quantity int not null,
    product_id int not null,
    created_at timestamp default current_timestamp,
    foreign key (product_id) references product(id)
);

insert into cart (product_id, quantity) values 
    ((select id from product where name = "Pineapple Tart"), 5), 
    ((select id from product where name = "Mango"), 2), 
    ((select id from product where name = "Pear"), 6);

-------------------------------------------------------------------------

-- Create user table and relationship from existing tables (last lesson)

CREATE TABLE user (
  id int primary key not null auto_increment,
  uid varchar(255) not null unique,
  email varchar(255) not null,
  created_at timestamp default current_timestamp
);

INSERT INTO user (uid, email) VALUES 
  (100, "john@mail.com"), 
  (101, "mary@mail.com"), 
  (103, "peter@mail.com");

ALTER TABLE cart
ADD user_id int NOT NULL;

UPDATE cart SET user_id = 3 WHERE id = 1;
UPDATE cart SET user_id = 1 WHERE id = 2;
UPDATE cart SET user_id = 2 WHERE id = 3;

ALTER TABLE cart
ADD FOREIGN KEY (user_id) REFERENCES user(id);

-------------------------------------------------------------------------

CREATE TABLE payment (
  id int primary key not null auto_increment,
  user_id int not null,
  created_at timestamp default current_timestamp,
  foreign key (user_id) references user(id)
);

CREATE TABLE payment_details (
  product_id int not null,
  payment_id int not null,
  foreign key (product_id) references product(id),
  foreign key (payment_id) references payment(id)
);

INSERT INTO payment (user_id) VALUES (2),(3),(1);

INSERT INTO payment_details (product_id, payment_id) VALUES 
  (3, 1), 
  (2, 3), 
  (1, 2);