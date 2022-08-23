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


create table if not exists role
(
    _role        varchar(64)                          not null
        primary key,
    _type        enum ('origin', 'inherit') not null,
    _name        varchar(64)                          not null,
    _description varchar(255)                         null,
    parent_role  varchar(64)                          null
);

INSERT INTO role (_role, _type, _name, _description, parent_role)
VALUES ('ADMIN', 'origin', '管理员', null, null);
INSERT INTO role (_role, _type, _name, _description, parent_role)
VALUES ('ORGADMIN', 'origin', '组织管理员', null, null);
INSERT INTO role (_role, _type, _name, _description, parent_role)
VALUES ('USER', 'origin', '普通用户', null, null);

create table user_role
(
    id      varchar(64)  not null
        primary key,
    user_id varchar(128) not null,
    role_id varchar(64)  not null,
    _source varchar(64)  null
);


