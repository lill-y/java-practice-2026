create table product(
    id serial unique,
    name char(42) not null,
    price integer check (price >= 0)
);

insert into product(name, price) values ('Milk', 98);
insert into product(name, price) values ('Bread', 71);
