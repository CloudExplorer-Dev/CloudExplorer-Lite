SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `role` (id, _type, _name, _description, parent_role_id, _sort)
VALUES ('ADMIN', 0, '系统管理员', '系统管理员', 'ADMIN', 1);
INSERT INTO `role` (id, _type, _name, _description, parent_role_id, _sort)
VALUES ('ORGADMIN', 0, '组织管理员', '组织管理员', 'ORGADMIN', 2);
INSERT INTO `role` (id, _type, _name, _description, parent_role_id, _sort)
VALUES ('USER', 0, '普通用户', '普通用户', 'USER', 3);


SET SESSION innodb_lock_wait_timeout = DEFAULT;
