SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `compliance_scan_result`
(
    `id`                   VARCHAR(255)                           NOT NULL
        PRIMARY KEY,
    `compliance_rule_id`   VARCHAR(255)                           NOT NULL DEFAULT '' COMMENT '合规规则id',
    `resource_type`        VARCHAR(255)                           NOT NULL COMMENT '资源类型',
    `cloud_account_id`     VARCHAR(255) DEFAULT NULL COMMENT '云账号id',
    `compliance_count`     INT(10)      DEFAULT '0' COMMENT '合规数量',
    `not_compliance_count` INT(10)      DEFAULT NULL COMMENT '不合规数量',
    `resource_count`       INT(10)      DEFAULT '0' COMMENT '资源总数',
    `status`               VARCHAR(255) DEFAULT NULL COMMENT '扫描状态',
    create_time            DATETIME     DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time            DATETIME     DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
