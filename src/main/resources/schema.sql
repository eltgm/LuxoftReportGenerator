create table users
(
    username    varchar(50)  not null primary key,
    password    varchar(500) not null,
    missed_days int          not null,
    enabled     boolean      not null
);
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);
create table overtimes
(
    username    varchar(50)   not null,
    task_number varchar(50)   not null,
    overtime    decimal(2, 1) not null,
    is_weekend  boolean       not null,
    constraint fk_overtimes_users foreign key (username) references users (username)
);