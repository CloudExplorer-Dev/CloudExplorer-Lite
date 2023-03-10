SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `workspace`
(
    id              VARCHAR(64)                        NOT NULL
        PRIMARY KEY,
    _name           VARCHAR(128)                       NOT NULL,
    _description    VARCHAR(255)                       NULL,
    organization_id VARCHAR(64)                        NOT NULL,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '工作空间';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
