SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `role`
(
    id             VARCHAR(64)                        NOT NULL COMMENT '角色ID'
        PRIMARY KEY,
    _type          INT      DEFAULT 1                 NULL COMMENT '角色分类：(0)系统内置角色、(1)继承角色',
    _name          VARCHAR(64)                        NOT NULL,
    _description   VARCHAR(255)                       NULL,
    parent_role_id VARCHAR(64)                        NULL COMMENT '父角色ID',
    _sort          INT      DEFAULT 0                 NULL COMMENT '排序',
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '角色';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
