SET SESSION innodb_lock_wait_timeout = 7200;

ALTER TABLE vm_cloud_server ADD auto_renew tinyint ( 1 ) DEFAULT NULL COMMENT '自动续费';

ALTER TABLE `vm_cloud_server`
DROP INDEX `UNIQUE_SERVER`,
ADD UNIQUE INDEX `UNIQUE_SERVER`(`instance_uuid` ASC, `account_id` ASC, `region` ASC) USING BTREE;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
