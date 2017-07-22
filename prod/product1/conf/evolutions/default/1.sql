# --- First database schema

# --- !Ups

set ignorecase true;

create sequence user_test_id_seq;

create table user_test (
  id integer not null default nextval('user_test_id_seq'),
  email VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  cpf VARCHAR(255) NOT NULL,
  date_of_birth VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);


create sequence user_id_seq;

create table user (
  id integer not null default nextval('user_id_seq'),
  email VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  pw VARCHAR(255) NOT NULL
);


create table product (
  id            bigint not null,
  name          varchar(255) not null,
  description   varchar(255),
  imgkey         varchar(255),
  publish       timestamp,
  category_id   bigint,
  constraint pk_product primary key (id))
;

create table category (
  id        bigint not null,
  name      varchar(255) not null,
  url       varchar(64) unique,
  description varchar(255),
  constraint pk_category primary key(id))
;


create table company (
  id                        bigint not null,
  name                      varchar(255) not null,
  constraint pk_company primary key (id))
;

create table computer (
  id                        bigint not null,
  name                      varchar(255) not null,
  introduced                timestamp,
  discontinued              timestamp,
  company_id                bigint,
  constraint pk_computer primary key (id))
;

create sequence product_seq start with 1000;

create sequence category_seq start with 1000;


create sequence company_seq start with 1000;

create sequence computer_seq start with 1000;


alter table product add constraint fk_product_category_1 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_1 on product (category_id);


alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_computer_company_1 on computer (company_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;


# --- new user_test

drop table if exists user_test;

drop sequence if exists user_test_id_seq;

# --- end user_test

drop table if exists user;

drop sequence if exists user_id_seq;

drop table if exists product;

drop table if exists category;


drop table if exists company;

drop table if exists computer;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists product_seq;

drop sequence if exists category_seq;


drop sequence if exists company_seq;

drop sequence if exists computer_seq;

