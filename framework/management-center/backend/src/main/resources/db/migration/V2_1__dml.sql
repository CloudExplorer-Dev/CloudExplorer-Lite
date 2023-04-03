SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `user` (id, username, _name, enabled, email, phone, `password`, `source`)
VALUES ('a7087692925ea80d178138522e3edbe1', 'admin', '管理员', 1, 'admin@fit2cloud.com', NULL,
        'ebadccf6e9ee14b064f57678e30b75fe', 'local');

SET SESSION innodb_lock_wait_timeout = DEFAULT;
