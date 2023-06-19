SET SESSION innodb_lock_wait_timeout = 7200;

UPDATE vm_cloud_server
SET instance_charge_type='PostPaid'
WHERE instance_charge_type IS NULL;

ALTER TABLE vm_cloud_server
    MODIFY COLUMN instance_charge_type varchar (255) NOT NULL DEFAULT 'PostPaid' COMMENT '计费类型';

UPDATE vm_cloud_disk
SET disk_charge_type='PostPaid'
WHERE disk_charge_type IS NULL;

UPDATE vm_cloud_disk  SET disk_charge_type='PostPaid' WHERE disk_charge_type IS NULL;

ALTER TABLE vm_cloud_disk
    MODIFY COLUMN disk_charge_type varchar (45) NOT NULL DEFAULT 'PostPaid' COMMENT '计费方式';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
