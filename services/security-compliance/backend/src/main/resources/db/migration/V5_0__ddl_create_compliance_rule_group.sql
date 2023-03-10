SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `compliance_rule_group`
(
    id            VARCHAR(32)                        NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    `name`        VARCHAR(255)                       NULL COMMENT '规则组名称',
    `description` VARCHAR(255)                       NULL COMMENT '规则组描述',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '合规规则组';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
