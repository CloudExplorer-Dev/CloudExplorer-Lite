SET SESSION innodb_lock_wait_timeout = 7200;
ALTER TABLE vm_cloud_server ADD auto_renew tinyint ( 1 ) DEFAULT NULL COMMENT '自动续费';
SET SESSION innodb_lock_wait_timeout = DEFAULT;
