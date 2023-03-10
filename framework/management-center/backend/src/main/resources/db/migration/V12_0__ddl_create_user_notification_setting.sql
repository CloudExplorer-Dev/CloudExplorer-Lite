SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `user_notification_setting`
(
    user_id        VARCHAR(50)                        NOT NULL NULL
        PRIMARY KEY,
    wechat_account VARCHAR(255)                       NULL,
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '用户通知设置';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
