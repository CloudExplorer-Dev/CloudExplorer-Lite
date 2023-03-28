SET SESSION innodb_lock_wait_timeout = 7200;
ALTER TABLE vm_cloud_datastore ADD allocated_space BIGINT ( 20 ) DEFAULT NULL COMMENT '已分配容量';
ALTER TABLE vm_cloud_host ADD cpu_used BIGINT ( 20 ) DEFAULT NULL COMMENT 'CPU已使用MHZ';
ALTER TABLE vm_cloud_host ADD memory_used BIGINT ( 20 ) DEFAULT NULL COMMENT '内存已使用MB';
SET SESSION innodb_lock_wait_timeout = DEFAULT;
