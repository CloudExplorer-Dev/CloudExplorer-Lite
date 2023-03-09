SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `compliance_insurance_statute`
(
    id                   INT(10) AUTO_INCREMENT COMMENT '序号'
        PRIMARY KEY,
    base_clause          VARCHAR(2048)                      NULL COMMENT '等级保护基本要求条款',
    security_level       VARCHAR(255)                       NULL COMMENT '安全层面',
    control_point        VARCHAR(255)                       NULL COMMENT '控制点',
    improvement_proposal VARCHAR(2048)                      NULL COMMENT '改进建议',
    create_time          DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time          DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '合规条例';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
