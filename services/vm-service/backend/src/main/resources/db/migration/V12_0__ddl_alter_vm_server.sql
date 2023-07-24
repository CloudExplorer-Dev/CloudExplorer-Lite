SET SESSION innodb_lock_wait_timeout = 7200;

ALTER TABLE vm_cloud_server MODIFY COLUMN `instance_uuid` VARCHAR ( 256 ) NULL DEFAULT NULL COMMENT '实例唯一标识' AFTER `id`;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
