SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `user`
(
    id          VARCHAR(128)                         NOT NULL
        PRIMARY KEY,
    username    VARCHAR(128)                         NOT NULL,
    _name       VARCHAR(128)                         NOT NULL,
    enabled     TINYINT(1) DEFAULT 1                 NULL,
    email       VARCHAR(512)                         NULL,
    phone       VARCHAR(50)                          NULL,
    `password`  VARCHAR(512)                         NULL,
    `source`    VARCHAR(50)                          NOT NULL COMMENT '用户来源:本地/第三方',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '用户';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
