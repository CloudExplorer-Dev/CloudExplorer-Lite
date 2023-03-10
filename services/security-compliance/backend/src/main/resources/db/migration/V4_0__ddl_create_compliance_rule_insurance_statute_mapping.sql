SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `compliance_rule_insurance_statute_mapping`
(
    id                              VARCHAR(32)                        NULL COMMENT '主键id'
        PRIMARY KEY,
    compliance_rule_id              VARCHAR(32)                        NULL COMMENT '合规规则id',
    compliance_insurance_statute_id INT(10)                            NULL COMMENT '等保条例id',
    create_time                     DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time                     DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '合规规则与等保条例';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
