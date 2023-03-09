SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `system_log`
(
    id       BIGINT       NOT NULL COMMENT '主键id'
        PRIMARY KEY,
    `host`   VARCHAR(125) NULL COMMENT '主机',
    _level   INT          NULL COMMENT '级别：(0)ERROR、(1)INFO、(2)WARN、(3)DEBUG',
    _time    DATETIME     NULL COMMENT '时间',
    _module  VARCHAR(125) NULL COMMENT '模块',
    _message TEXT         NULL COMMENT '日志详情',
    _thread  VARCHAR(255) NULL COMMENT '线程',
    _logger  VARCHAR(255) NULL COMMENT '方法'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '系统日志';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
