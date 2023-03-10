SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `cloud_account`
(
    id          VARCHAR(128)                       NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    `name`      VARCHAR(255)                       NULL COMMENT '云账号名称',
    platform    VARCHAR(255)                       NULL COMMENT '云平台',
    credential  VARCHAR(5000)                      NULL COMMENT '凭证字段',
    state       TINYINT(1)                         NULL COMMENT '云账号状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '云账号';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
