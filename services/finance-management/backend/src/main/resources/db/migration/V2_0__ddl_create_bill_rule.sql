SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `bill_rule`
(
    id          VARCHAR(64)                        NOT NULL COMMENT '主键'
        PRIMARY KEY,
    `name`      VARCHAR(255)                       NULL COMMENT '规则名称',
    `groups`    JSON                               NULL COMMENT '分组规则',
    filters     JSON                               NULL COMMENT '过滤规则',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
