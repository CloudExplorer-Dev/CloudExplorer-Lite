SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `compliance_scan_resource_result`
(
    `id`                   VARCHAR(255)                           NOT NULL
        PRIMARY KEY,
    `compliance_rule_id`   VARCHAR(32)  DEFAULT NULL COMMENT '合规规则id',
    `resource_type`        VARCHAR(255) DEFAULT NULL COMMENT '资源类型 冗余字段',
    `cloud_account_id`     VARCHAR(32)  DEFAULT NULL COMMENT '云账号id',
    `resource_instance_id` VARCHAR(32)                            NOT NULL COMMENT '资源实例id',
    `status`               INT(11)      DEFAULT NULL COMMENT '是否合规',
    create_time            DATETIME     DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time            DATETIME     DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
