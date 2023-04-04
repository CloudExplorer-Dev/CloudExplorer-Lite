SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `recycle_bin`
(
    id            varchar(50)                        NOT NULL COMMENT '主键'
        PRIMARY KEY,
    resource_id   varchar(50)                        NOT NULL COMMENT '资源 ID',
    resource_type varchar(50)                        NULL COMMENT '资源类型：云主机、云磁盘',
    `status`      varchar(50)                        NOT NULL COMMENT '资源状态：待回收、已删除、已恢复',
    recover_time  datetime                           NULL COMMENT '恢复时间',
    delete_time   datetime                           NULL COMMENT '删除时间',
    user_id       varchar(50)                        NOT NULL COMMENT '发起回收、恢复或者删除操作的用户ID',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间（资源放入回收站时间）',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '回收站';



SET SESSION innodb_lock_wait_timeout = DEFAULT;
