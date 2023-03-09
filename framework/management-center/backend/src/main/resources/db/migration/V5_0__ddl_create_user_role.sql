SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `user_role`
(
    id          VARCHAR(64)                        NOT NULL
        PRIMARY KEY,
    user_id     VARCHAR(128)                       NOT NULL,
    role_id     VARCHAR(64)                        NOT NULL,
    _source     VARCHAR(64)                        NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '用户角色';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
