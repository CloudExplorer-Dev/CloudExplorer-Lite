SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `bill_dimension_setting`
(
    id             VARCHAR(64)                        NOT NULL COMMENT '组建id'
        PRIMARY KEY,
    authorize_rule JSON                               NULL COMMENT '账单规则',
    authorize_id   VARCHAR(64)                        NULL COMMENT '组织或者工作空间id',
    type           INT(10)                            NULL COMMENT '0组织1工作空间',
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
