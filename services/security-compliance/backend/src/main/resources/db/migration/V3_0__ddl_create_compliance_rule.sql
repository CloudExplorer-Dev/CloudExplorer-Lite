SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `compliance_rule`
(
    id            VARCHAR(32)                        NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    `name`        VARCHAR(255)                       NOT NULL COMMENT '规则名称',
    rule_group_id VARCHAR(32)                        NOT NULL COMMENT '规则组id',
    platform      VARCHAR(255)                       NOT NULL COMMENT '云平台',
    resource_type VARCHAR(255)                       NOT NULL COMMENT '资源类型',
    rules         JSON                               NOT NULL COMMENT '规则条件',
    risk_level    INT(10)                            NOT NULL COMMENT '风险等级',
    `description` VARCHAR(255)                       NOT NULL COMMENT '规则描述',
    `enable`      TINYINT(1)                         NOT NULL COMMENT '是否启用',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '合规条例';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
