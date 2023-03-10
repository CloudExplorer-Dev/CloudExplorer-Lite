SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `user_role` (id, user_id, role_id, _source)
VALUES ('57832df2656d03fdd08b4583eee2e92b', 'a7087692925ea80d178138522e3edbe1', 'ADMIN', null);

SET SESSION innodb_lock_wait_timeout = DEFAULT;
