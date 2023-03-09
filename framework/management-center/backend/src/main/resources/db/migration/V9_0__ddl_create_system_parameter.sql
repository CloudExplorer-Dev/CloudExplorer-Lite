SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `system_parameter`
(
    param_key   VARCHAR(64)                            NOT NULL COMMENT '参数名称'
        PRIMARY KEY,
    param_value VARCHAR(255)                           NULL COMMENT '参数值',
    type        VARCHAR(100) DEFAULT 'text'            NOT NULL COMMENT '参数类型',
    sort        INT(5)                                 NULL COMMENT '排序',
    module      VARCHAR(100)                           NULL COMMENT '模块',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '系统参数';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
