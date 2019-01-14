drop database  if exists stocks;

create database stocks;

use stocks;

drop table if exists stock;

create table stock (
  ticker varchar(16) not null ,
  exchange enum('shanghai', 'shenzhen'),
  primary key (ticker)
);

create table quote (
                     id varchar(64) not null ,
                     price varchar(16) not null ,
                     timestamp integer,
                     ticker varchar(16) not null ,
                     exchange enum('shanghai', 'shenzhen'),
                     primary key (id)
)
