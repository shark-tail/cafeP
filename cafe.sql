drop database if exists cafedb;
create database cafedb;
use cafedb;
create table coffee(
	ccode int primary key not null auto_increment,
    cName varchar(10) not null,
    cPrice int not null
);
create table beverage(
	bcode int primary key not null auto_increment,
    bName varchar(10) not null,
    bPrice int not null
);
create table dessert(
	dcode int primary key not null auto_increment,
    dName varchar(10) not null,
    dPrice int not null
);
create table customers(
	customer_id varchar(20) not null primary key,
    customer_name varchar(10) not null,
    customer_pwd varchar(20) not null,
    customer_phone varchar(20) not null,
    customer_coupon int default 0,
    customer_couponcheck int default 0
);
create table orderCart(
	ocode int primary key not null auto_increment,
    customer_id varchar(20) not null,
    ccode int,
    bcode int,
    dcode int,
    oamount int not null,
    oprice int not null,
    odate datetime,
    foreign key (customer_id) references customers(customer_id),
    foreign key (ccode) references coffee(ccode),
    foreign key (bcode) references beverage(bcode),
    foreign key (dcode) references dessert(dcode)
);

insert into customers(customer_id, customer_name, customer_pwd, customer_phone, customer_coupon) value('admin','관리자','123123', '010-1234-5678', '3000');
insert into customers(customer_id, customer_name, customer_pwd, customer_phone) value('asd','아무개','pw', '010-1234-5678'); -- 테스트용.. 사용하시고 주석처리 하세요.
#update customers set customer_coupon = 5, customer_couponcheck = 8 where customer_id ='asd';
select * from customers;

create table backupUpdateCustomers(
	bcustomer_id varchar(20) not null,
    bcustomer_name varchar(10) not null,
    bcustomer_pwd varchar(20) not null,
    bcustomer_phone varchar(20) not null,
    bcustomer_coupon int default 0,
    bcustomer_couponcheck int default 0,
    bcustomer_date datetime
);
#select * from backupUpdateCustomers;
create table backupDeleteCustomers(
	bdcustomer_id varchar(20) not null,
    bdcustomer_name varchar(10) not null,
    bdcustomer_pwd varchar(20) not null,
    bdcustomer_phone varchar(20) not null,
    bdcustomer_coupon int default 0,
    bdcustomer_couponcheck int default 0,
    bdcustomer_date datetime
);
#select * from backupDeleteCustomers;
#drop trigger if exists backupUpdateCustomers;
delimiter $$
create trigger backupUpdateCustomers
	after update
    on customers
    for each row
begin
	insert into backupUpdateCustomers values(old.customer_id, old.customer_name, old.customer_pwd, old.customer_phone, old.customer_coupon, old.customer_couponcheck, current_timestamp());
end $$
delimiter ;
#select * from backupUpdateCustomers;
#drop trigger if exists backupDeleteCustomers;
delimiter $$
create trigger backupDeleteCustomers
	after delete
    on customers
    for each row
begin
	insert into backupDeleteCustomers values(old.customer_id, old.customer_name, old.customer_pwd, old.customer_phone, old.customer_coupon, old.customer_couponcheck, current_timestamp());
end $$
delimiter ;