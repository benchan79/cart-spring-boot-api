-- sudo /etc/init.d/mysql start
-- sudo mysql
show databases;
create database cartdb;
drop database cartdb;
use cartdb;

-- drop database
DROP DATABASE product;
-- drop table
DROP TABLE cart;
-- drop column
ALTER TABLE cart
DROP COLUMN user_id;

-- create table
create table product (
    id int not null AUTO_INCREMENT,
    name varchar(255) not null,
    description text,
    price float not null default 1,
    created_at timestamp default current_timestamp,
    primary key (id)
);

create table product (
    id int not null AUTO_INCREMENT,
    name varchar(255) not null,
    description text,
    price float not null default 1,
    created_at timestamp,
    primary key (id)
);

show tables;
describe product;
insert into product (name, price) values ("Cabbage", 1.0);
select * from product;
alter table product modify created_at timestamp default current_timestamp;
insert into product (name, price) values ("Carrot", 1.5);
select * from product;
SELECT @@global.time_zone, @@session.time_zone;
insert into product (name, price) values ("apple", 3.4), ("orange",3.3);
update product set name = "long bean" where id = 1;
update product set name = "pineapple" where name like "%app%";
delete from product where id = 1;
delete from product where price < 3;

create table cart (
    id int primary key not null auto_increment,
    quantity int not null,
    product_id int not null,
    created_at timestamp default current_timestamp,
    foreign key (product_id) references product(id)
);

-- Select product name, price, and quantity using join keyword
select p.name, p.price, c.quantity 
from product p 
inner join cart c 
  on p.id = c.product_id;

-------------------------------------------------------------------
insert into product (name, price) values ("Pineapple Tart", 10), ("Mango", 7.3), ("Pear", 2.2), ("Carrot", 1.2);

insert into cart (product_id, quantity) values 
    ((select id from product where name = "Pineapple Tart"), 5), 
    ((select id from product where name = "Mango"), 2), 
    ((select id from product where name = "Pear"), 6);

-- Challenge 1
select p.name, p.price, c.quantity 
from product p 
inner join cart c 
  on p.id = c.product_id
where price > 3
order by p.name asc;

-- Challenge 2
select p.name, p.price, c.quantity,
round((p.price * 1.08), 2) as payable_with_gst
from product p 
inner join cart c 
  on p.id = c.product_id
where price > 3
order by c.quantity desc;

-- challenge 3
select distinct name
from product;

-- chalenge 4
select count(name) as total_num_of_products
from product
where price > 5;

-- challenge 6
select round(sum(price), 2) as price_of_all_products
from product;
-------------------------------------------------------------------