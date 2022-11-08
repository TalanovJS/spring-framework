--liquibase formatted sql

--changeset rtalanov:1
alter table users
add column image varchar(64);

--changeset rtalanov:2
alter table users_aud
add column image varchar(64);