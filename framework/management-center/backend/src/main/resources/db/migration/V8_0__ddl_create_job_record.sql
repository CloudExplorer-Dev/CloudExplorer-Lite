SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `job_record`
(
    id              VARCHAR(64)                        NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    type            INT(255)                           NULL COMMENT '任务类型',
    `status`        INT(255)                           NULL COMMENT '任务状态',
    `description`   VARCHAR(255)                       NULL COMMENT '任务描述',
    params          JSON                               NULL COMMENT '任务参数',
    result          TEXT                               NULL COMMENT '任务结果',
    finish_time     DATETIME                           NULL COMMENT '任务结束时间',
    operate_user_id VARCHAR(126)                       NULL COMMENT '操作人ID',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '任务';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
