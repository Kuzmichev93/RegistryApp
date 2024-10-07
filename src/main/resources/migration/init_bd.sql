create schema registry


if not exists create table registry.Departments(
    id serial primary key,
    deptname varchar(10) unique,
    prefix char(1) unique
)

if not exists create table registry.Users(
    id serial  primary key ,
    first_name varchar(15),
    patronomic varchar(15),
    last_name  varchar(25),
    serial_number varchar(10) unique,
    department_fk varchar(10),
    foreign key (department_fk) references registry.Departments(deptname) on delete cascade
)

