create table user
(
    id       varchar(128)         not null
        primary key,
    username varchar(128)         not null,
    _name    varchar(128)         not null,
    enabled  tinyint(1) default 1 null,
    email    varchar(512)         null,
    phone    varchar(50)          null,
    password varchar(512)         null
)
    comment '用户';
