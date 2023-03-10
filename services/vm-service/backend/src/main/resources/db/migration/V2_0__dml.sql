SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `system_parameter` (`param_key`, `param_value`, `type`, `sort`, `module`)
VALUES ('recycle_bin.enable', 'true', 'boolean', NULL, 'vm-service');


SET SESSION innodb_lock_wait_timeout = DEFAULT;
