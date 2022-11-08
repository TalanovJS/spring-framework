--liquibase formatted sql

--changeset rtalanov:1
alter table users
add column password varchar(128) default '{noop}123';

--changeset rtalanov:2
alter table users_aud
add column password varchar(128);