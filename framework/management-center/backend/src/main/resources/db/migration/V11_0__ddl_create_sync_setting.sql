SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `sync_setting`
(
    id          VARCHAR(128)                       NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    amount      VARCHAR(255)                       NULL COMMENT '时间数量',
    unit        VARCHAR(255)                       NULL COMMENT '同步时间单位',
    region      JSON                               NULL COMMENT '同步区域',
    `source`    VARCHAR(255)                       NULL COMMENT '同步资源',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '同步设置';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
