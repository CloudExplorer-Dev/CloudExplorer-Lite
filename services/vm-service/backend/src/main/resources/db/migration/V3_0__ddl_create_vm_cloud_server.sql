SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `vm_cloud_server`
(
    id                        VARCHAR(50)                        NOT NULL
        PRIMARY KEY,
    instance_uuid             VARCHAR(50)                        NULL COMMENT '实例唯一标识',
    source_id                 VARCHAR(50)  DEFAULT ''            NOT NULL COMMENT '组织/工作空间 ID',
    project_id                VARCHAR(50)                        NULL COMMENT 'Project ID',
    account_id                VARCHAR(50)                        NOT NULL COMMENT '云账号 ID',
    instance_id               VARCHAR(256)                       NULL COMMENT '实例 ID',
    instance_name             VARCHAR(256)                       NULL COMMENT '实例名称',
    image_id                  VARCHAR(255)                       NULL COMMENT '镜像 ID',
    instance_status           VARCHAR(45)                        NULL COMMENT '实例状态',
    instance_type             VARCHAR(64)                        NULL COMMENT '实例类型',
    instance_type_description VARCHAR(64)                        NULL COMMENT '实例类型描述',
    region                    VARCHAR(256)                       NULL COMMENT '数据中心/区域',
    zone                      VARCHAR(256)                       NULL COMMENT '可用区/集群',
    host_id                   VARCHAR(256)                       NULL COMMENT '宿主机ID',
    `host`                    VARCHAR(256)                       NULL COMMENT '宿主机',
    resource_pool_id          VARCHAR(256)                       NULL COMMENT '资源池ID',
    resource_pool             VARCHAR(256)                       NULL COMMENT '资源池',
    remote_ip                 VARCHAR(64)                        NULL COMMENT '公网 IP',
    ip_array                  JSON                               NULL COMMENT 'IP 地址',
    os                        VARCHAR(50)                        NULL COMMENT '操作系统Key',
    os_version                VARCHAR(50)                        NULL COMMENT '操作系统版本',
    `cpu`                     INT(8)   DEFAULT 0                 NULL COMMENT 'CPU 核数',
    `memory`                  INT(8)   DEFAULT 0                 NULL COMMENT '内存容量',
    `disk`                    INT(16)  DEFAULT 0                 NULL COMMENT '磁盘容量',
    hostname                  VARCHAR(256)                       NULL COMMENT '主机名',
    management_ip             VARCHAR(64)                        NULL COMMENT '管理 IP',
    management_port           INT                                NULL COMMENT '管理端口',
    os_info                   VARCHAR(128)                       NULL COMMENT '云平台操作系统信息',
    remark                    VARCHAR(128)                       NULL COMMENT '备注',
    network                   VARCHAR(255)                       NULL COMMENT '网络',
    vpc_id                    VARCHAR(255)                       NULL COMMENT 'VPC ID',
    subnet_id                 VARCHAR(255)                       NULL COMMENT '子网 ID',
    network_interface_id      VARCHAR(255)                       NULL COMMENT '网络接口 ID',
    management_ipv6           VARCHAR(64)                        NULL COMMENT '管理ip:ipv6',
    remote_ipv6               VARCHAR(64)                        NULL COMMENT '公网ipv6',
    local_ipv6                VARCHAR(64)                        NULL COMMENT '内网ipv6',
    ip_type                   VARCHAR(50)                        NULL COMMENT 'ip类型: ipv4、 ipv6 、DualStack',
    snap_shot                 INT(8)   DEFAULT 0                 NULL COMMENT '快照',
    vm_tools_version          VARCHAR(255)                       NULL,
    vm_tools_status           VARCHAR(255)                       NULL,
    instance_charge_type      VARCHAR(255)                       NULL COMMENT '计费类型',
    security_group_ids        JSON                               NULL COMMENT '安全组',
    apply_user                VARCHAR(50)                        NULL COMMENT '申请人',
    expired_time              DATETIME                           NULL COMMENT '包年包月机器到期时间',
    delete_time               DATETIME                           NULL COMMENT '删除时间',
    last_operate_time         DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '最近关机时间',
    create_time               DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time               DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    constraint UNIQUE_SERVER
        unique (instance_uuid, account_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '云主机';


create index IDX_ACCOUNT
    on vm_cloud_server (account_id);

create index IDX_HOST
    on vm_cloud_server (`host`);

create index IDX_LAST_SYNC
    on vm_cloud_server (update_time);

create index IDX_REGION
    on vm_cloud_server (region);

create index IDX_REMARK
    on vm_cloud_server (remark);

create index IDX_STATUS
    on vm_cloud_server (instance_status);

create index IDX_UUID
    on vm_cloud_server (instance_uuid);

create index IDX_WORKSPACE
    on vm_cloud_server (source_id);

create index IDX_ZONE
    on vm_cloud_server (zone);


SET SESSION innodb_lock_wait_timeout = DEFAULT;
