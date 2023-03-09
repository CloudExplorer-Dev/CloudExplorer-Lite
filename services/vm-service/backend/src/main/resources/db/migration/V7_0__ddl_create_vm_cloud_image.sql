SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `vm_cloud_image`
(
    id            VARCHAR(50)                        NOT NULL
        PRIMARY KEY,
    account_id    VARCHAR(50)                        NOT NULL COMMENT '云账号 ID',
    image_id      VARCHAR(512)                       NULL COMMENT '镜像 ID',
    region        VARCHAR(100)                       NULL COMMENT '数据中心/区域',
    region_name   VARCHAR(100)                       NULL COMMENT '数据中心/区域',
    image_name    VARCHAR(512)                       NOT NULL COMMENT '镜像名',
    `description` TEXT                               NULL COMMENT '描述',
    os            VARCHAR(512)                       NULL COMMENT '操作系统',
    disk_size     BIGINT(13)                         NOT NULL comment '镜像磁盘大小',
    disk_infos    JSON                               NULL,
    `status`      VARCHAR(255)                       NULL COMMENT '磁盘状态,normal:正常，delete:删除',
    image_type    VARCHAR(50)                        NULL COMMENT '镜像类型',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    constraint UNQ_IMAGE
        unique (image_id, account_id, region)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '镜像';


create index IDX_ACCOUNT
    on `vm_cloud_image` (account_id);

create index IDX_IMAGE
    on `vm_cloud_image` (image_id);

create index IDX_LAST_SYC
    on `vm_cloud_image` (update_time);

create index IDX_REGION
    on `vm_cloud_image` (region);


SET SESSION innodb_lock_wait_timeout = DEFAULT;
