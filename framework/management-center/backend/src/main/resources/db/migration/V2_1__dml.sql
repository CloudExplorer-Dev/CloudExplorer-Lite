SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `user` (id, username, _name, enabled, email, phone, `password`, `source`)
VALUES ('a7087692925ea80d178138522e3edbe1', 'admin', '管理员', 1, 'admin@fit2cloud.com', NULL,
        '4a8a7cc4be17fdd1b33036c9fdbf2b5a', 'local');

SET SESSION innodb_lock_wait_timeout = DEFAULT;
