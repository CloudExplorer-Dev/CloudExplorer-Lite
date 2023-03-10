SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `job_record_resource_mapping`
(
    id            VARCHAR(255)                       NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    resource_id   VARCHAR(255)                       NOT NULL COMMENT '资源ID',
    job_type      INT(255)                           NOT NULL COMMENT '资源类型',
    job_record_id VARCHAR(255)                       NULL COMMENT '任务记录id',
    resource_type VARCHAR(255)                       NULL COMMENT '资源类型',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '任务资源关系';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
