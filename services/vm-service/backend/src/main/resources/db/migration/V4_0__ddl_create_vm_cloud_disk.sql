SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `vm_cloud_disk`
(
    id                   VARCHAR(50)                        NOT NULL
        PRIMARY KEY,
    region               VARCHAR(256)                       NULL COMMENT '数据中心/区域',
    zone                 VARCHAR(256)                       NULL COMMENT '集群/可用区',
    disk_id              VARCHAR(128)                       NULL COMMENT '磁盘UUID',
    disk_name            VARCHAR(128)                       NULL COMMENT '磁盘名称',
    disk_type            VARCHAR(45)                        NULL COMMENT '磁盘置备方式',
    category             VARCHAR(45)                        NULL COMMENT '磁盘类型',
    `status`             VARCHAR(45)                        NULL COMMENT '磁盘状态',
    disk_charge_type     VARCHAR(45)                        NULL COMMENT '计费方式',
    `description`        VARCHAR(45)                        NULL COMMENT '描述',
    size                 BIGINT(16)                         NULL COMMENT '大小(GB)',
    device               VARCHAR(255)                       NULL COMMENT '挂载点',
    account_id           VARCHAR(50)                        NOT NULL COMMENT '云账号ID',
    datastore_id         VARCHAR(255)                       NULL COMMENT '存储器ID',
    instance_uuid        VARCHAR(128)                       NULL COMMENT '关联虚拟机UUID',
    source_id            VARCHAR(50)  DEFAULT ''            NOT NULL COMMENT '组织/工作空间 ID',
    project_id           VARCHAR(50)                        NULL COMMENT 'Project ID',
    bootable             TINYINT(1)                         NULL COMMENT '是否启动盘',
    image_id             VARCHAR(50)                        NULL COMMENT '镜像ID',
    delete_with_instance VARCHAR(50)                        NULL COMMENT '随实例释放',
    create_time          DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time          DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '云磁盘';


SET SESSION innodb_lock_wait_timeout = DEFAULT;
