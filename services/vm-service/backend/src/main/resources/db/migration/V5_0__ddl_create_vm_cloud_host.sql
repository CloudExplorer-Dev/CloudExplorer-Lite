SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `vm_cloud_host`
(
    id                    VARCHAR(50)                        NOT NULL
        PRIMARY KEY,
    account_id            VARCHAR(50)                        NOT NULL COMMENT '云账号 ID',
    region                VARCHAR(256)                       NULL COMMENT '数据中心',
    zone                  VARCHAR(256)                       NULL COMMENT '集群',
    host_id               VARCHAR(256)                       NULL COMMENT '主机ID',
    host_name             VARCHAR(256)                       NULL COMMENT '主机名',
    cpu_total             BIGINT                             NULL COMMENT 'CPU 总量',
    cpu_allocated         BIGINT                             NULL COMMENT 'CPU 已分配',
    memory_total          BIGINT                             NULL COMMENT '内存总量',
    memory_allocated      BIGINT                             NULL COMMENT '内存已分配',
    vm_total              BIGINT                             NULL COMMENT '虚拟机总数',
    vm_running            BIGINT                             NULL COMMENT '运行中虚拟机数',
    vm_stopped            BIGINT                             NULL COMMENT '已停止虚拟机数',
    host_ip               VARCHAR(45)                        NULL COMMENT '主机 IP',
    `status`              VARCHAR(45)                        NULL COMMENT '主机状态',
    hypervisor_type       VARCHAR(64)                        NULL COMMENT '虚拟化类型',
    hypervisor_version    VARCHAR(64)                        NULL COMMENT '虚拟化版本',
    host_vendor           VARCHAR(128)                       NULL COMMENT '主机提供商',
    host_model            VARCHAR(128)                       NULL COMMENT '主机型号',
    cpu_model             VARCHAR(128)                       NULL COMMENT 'CPU 型号',
    cpu_mHz_per_one_core  INT      DEFAULT 0                 NULL COMMENT '单核 CPU 容量',
    num_cpu_cores         INT      DEFAULT 0                 NULL COMMENT 'CPU 总数',
    maintenance_timestamp BIGINT   DEFAULT 0                 NULL COMMENT '维保日期',
    vm_cpu_cores          INT      DEFAULT 0                 NULL COMMENT '虚拟核数',
    create_time           DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time           DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci
    COMMENT '宿主机';


SET SESSION innodb_lock_wait_timeout = DEFAULT;
