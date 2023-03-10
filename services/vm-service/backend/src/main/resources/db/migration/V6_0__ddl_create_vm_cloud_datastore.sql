SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `vm_cloud_datastore`
(
    id             VARCHAR(50)                        NOT NULL
        PRIMARY KEY,
    account_id     VARCHAR(50)                        NOT NULL COMMENT '云账号 ID',
    datastore_id   VARCHAR(256)                       NULL COMMENT '存储 ID',
    datastore_name VARCHAR(256)                       NULL COMMENT '存储名称',
    capacity       BIGINT                             NULL COMMENT '容量',
    free_space     BIGINT                             NULL COMMENT '剩余',
    `status`       VARCHAR(45)                        NULL COMMENT '状态',
    type           VARCHAR(45)                        NULL COMMENT '存储类型',
    region         VARCHAR(256)                       NULL COMMENT '数据中心',
    zone           VARCHAR(256)                       NULL COMMENT '集群',
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time    DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '数据中心';


SET SESSION innodb_lock_wait_timeout = DEFAULT;
