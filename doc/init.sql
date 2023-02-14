CREATE DATABASE `ce` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE `quartz` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
use `ce`;
create table `bill_dimension_setting`
(
    id             varchar(64)                        not null comment '组建id'
        primary key,
    authorize_rule json                               null comment '账单规则',
    authorize_id   varchar(64)                        null comment '组织或者工作空间id',
    type           int(10)                            null comment '0组织1工作空间',
    create_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);
create table `bill_rule`
(
    id          varchar(64)                        not null comment '主键'
        primary key,
    `name`        varchar(255)                       null comment '规则名称',
    `groups`    json                               null comment '分组规则',
    filters     json                               null comment '过滤规则',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
);

create table `cloud_account`
(
    id          varchar(128)                       not null comment '主键id'
        primary key,
    `name`        varchar(255)                       null comment '云账号名称',
    platform    varchar(255)                       null comment '云平台',
    credential  varchar(5000)                      null comment '凭证字段',
    state       tinyint(1)                         null comment '云账号状态',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

create table `compliance_insurance_statute`
(
    id                   int(10) auto_increment comment '序号'
        primary key,
    base_clause          varchar(2048)                      null comment '等级保护基本要求条款',
    security_level       varchar(255)                       null comment '安全层面',
    control_point        varchar(255)                       null comment '控制点',
    improvement_proposal varchar(2048)                      null comment '改进建议',
    create_time          datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

create table `compliance_rule`
(
    id            varchar(32)                        not null comment '主键id'
        primary key,
    `name`          varchar(255)                       not null comment '规则名称',
    rule_group_id varchar(32)                        not null comment '规则组id',
    platform      varchar(255)                       not null comment '云平台',
    resource_type varchar(255)                       not null comment '资源类型',
    rules         json                               not null comment '规则条件',
    risk_level    int(10)                            not null comment '风险等级',
    `description`   varchar(255)                       not null comment '规则描述',
    `enable`        tinyint(1)                         not null comment '是否启用',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '修改时间'
);

create table `compliance_rule_group`
(
    id          varchar(32)                        not null comment '主键id'
        primary key,
    `name`        varchar(255)                       null comment '规则组名称',
    `description` varchar(255)                       null comment '规则组描述',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

create table `compliance_rule_insurance_statute_mapping`
(
    id                              varchar(32)                        null comment '主键id',
    compliance_rule_id              varchar(32)                        null comment '合规规则id',
    compliance_insurance_statute_id int(10)                            null comment '等保条例id',
    create_time                     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time                     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

create table `compliance_scan_result`
(
    id                   varchar(255)                           not null
        primary key,
    resource_id          varchar(255) default ''                not null comment '资源id',
    resource_type        int(10)                                not null comment '资源类型',
    cloud_account_id     varchar(255)                           null comment '云账号id',
    compliance_count     int(10)      default 0                 null comment '合规数量',
    not_compliance_count int(10)                                null comment '不合规数量',
    resource_count       int(10)      default 0                 null comment '资源总数',
    `status`               varchar(255)                           null comment '扫描状态',
    create_time          datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time          datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

create table `job_record`
(
    id            varchar(64)                        not null comment '主键id'
        primary key,
    type          int(255)                           null comment '任务类型',
    `status`        int(255)                           null comment '任务状态',
    `description`   varchar(255)                       null comment '任务描述',
    params        json                               null comment '任务参数',
    create_time   datetime                           null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    result        text                               null comment '任务结果',
    resource_type varchar(255)                       null comment '资源类型',
    finish_time   datetime                           null comment '任务结束时间'
);

create table `job_record_resource_mapping`
(
    id            varchar(255)                       not null comment '主键id'
        primary key,
    resource_id   varchar(255)                       not null comment '资源ID',
    job_type      int(255)                           not null comment '资源类型',
    job_record_id varchar(255)                       null comment '任务记录id',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    resource_type varchar(255)                       null comment '资源类型'
);

create index `resource_id`
    on `job_record_resource_mapping` (resource_id);

create table `organization`
(
    id          varchar(32)                        not null comment '主键id'
        primary key,
    `name`        varchar(255)                       null comment '组织名称',
    `description` varchar(255)                       null comment '描述',
    pid         varchar(32)                        null comment '父id',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

create table `recycle_bin`
(
    id            varchar(50) default '' not null comment '主键'
        primary key,
    resource_id   varchar(50)            not null comment '资源 ID',
    resource_type varchar(50)            null comment '资源类型：云主机、云磁盘',
    `status`        varchar(50)            not null comment '资源状态：待回收、已删除、已恢复',
    create_time   datetime               not null on update CURRENT_TIMESTAMP comment '创建时间（资源放入回收站时间）',
    delete_time   datetime               null comment '删除时间（资源彻底删除时间）',
    recover_time  datetime               null comment '恢复时间',
    user_id       varchar(50)            not null comment '发起回收、恢复或者删除操作的用户ID'
);

create table `role`
(
    id             varchar(64)                        not null comment '角色ID'
        primary key,
    _type          int      default 1                 null comment '角色分类：(0)系统内置角色、(1)继承角色',
    _name          varchar(64)                        not null,
    _description   varchar(255)                       null,
    parent_role_id varchar(64)                        null comment '父角色ID',
    create_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    _sort          int      default 0                 null
);

create table `role_permission`
(
    role_id     varchar(64)                        not null
        primary key,
    permissions json                               null,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table `sync_setting`
(
    id          varchar(128)                       not null comment '主键id'
        primary key,
    amount      varchar(255)                       null comment '时间数量',
    unit        varchar(255)                       null comment '同步时间单位',
    region      json                               null comment '同步区域',
    `source`      varchar(255)                       null comment '同步资源',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
);

create table `system_log`
(
    id       bigint       not null comment '主键id'
        primary key,
    `host`     varchar(125) null comment '主机',
    _level   int          null comment '级别：(0)ERROR、(1)INFO、(2)WARN、(3)DEBUG',
    _time    datetime     null comment '时间',
    _module  varchar(125) null comment '模块',
    _message text         null comment '日志详情',
    _thread  varchar(255) null comment '线程',
    _logger  varchar(255) null comment '方法'
);

create table `system_parameter`
(
    param_key   varchar(64)                 not null comment '参数名称'
        primary key,
    param_value varchar(255)                null comment '参数值',
    type        varchar(100) default 'text' not null comment '参数类型',
    sort        int(5)                      null comment '排序',
    module      varchar(100)                null comment '模块'
);

create table `user`
(
    id          varchar(128)                         not null
        primary key,
    username    varchar(128)                         not null,
    _name       varchar(128)                         not null,
    enabled     tinyint(1) default 1                 null,
    email       varchar(512)                         null,
    phone       varchar(50)                          null,
    `password`    varchar(512)                         null,
    `source`      varchar(50)                          not null comment '用户来源:本地/第三方',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '用户' ;

create table `user_notification_setting`
(
    user_id        varchar(50)  not null
        primary key,
    wechat_account varchar(255) null
);

create table `user_role`
(
    id          varchar(64)                        not null
        primary key,
    user_id     varchar(128)                       not null,
    role_id     varchar(64)                        not null,
    _source     varchar(64)                        null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create table `vm_cloud_datastore`
(
    id             varchar(50) default ''                not null
        primary key,
    account_id     varchar(50) default ''                not null comment '云账号 ID',
    datastore_id   varchar(256)                          null comment '存储 ID',
    datastore_name varchar(256)                          null comment '存储名称',
    capacity       bigint                                null comment '容量',
    free_space     bigint                                null comment '剩余',
    `status`         varchar(45)                           null comment '状态',
    type           varchar(45)                           null comment '存储类型',
    region         varchar(256)                          null comment '数据中心',
    zone           varchar(256)                          null comment '集群',
    create_time    datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    datetime    default CURRENT_TIMESTAMP null comment '更新时间'
);

create table `vm_cloud_disk`
(
    id                   varchar(50) default ''                not null
        primary key,
    region               varchar(256)                          null comment '数据中心/区域',
    zone                 varchar(256)                          null comment '集群/可用区',
    disk_id              varchar(128)                          null comment '磁盘UUID',
    disk_name            varchar(128)                          null comment '磁盘名称',
    disk_type            varchar(45)                           null comment '磁盘置备方式',
    category             varchar(45)                           null comment '磁盘类型',
    `status`               varchar(45)                           null comment '磁盘状态',
    disk_charge_type     varchar(45)                           null comment '计费方式',
    `description`          varchar(45)                           null comment '描述',
    size                 bigint(16)                            null comment '大小(GB)',
    device               varchar(255)                          null comment '挂载点',
    account_id           varchar(50) default ''                not null comment '云账号ID',
    datastore_id         varchar(255)                          null comment '存储器ID',
    instance_uuid        varchar(128)                          null comment '关联虚拟机UUID',
    source_id            varchar(50) default ''                not null comment '组织/工作空间 ID',
    project_id           varchar(50)                           null comment 'Project ID',
    bootable             tinyint(1)                            null comment '是否启动盘',
    image_id             varchar(50)                           null comment '镜像ID',
    delete_with_instance varchar(50)                           null comment '随实例释放',
    create_time          datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime    default CURRENT_TIMESTAMP not null comment '更新时间'
);

create table `vm_cloud_host`
(
    id                    varchar(50) default ''                not null
        primary key,
    account_id            varchar(50) default ''                not null comment '云账号 ID',
    region                varchar(256)                          null comment '数据中心',
    zone                  varchar(256)                          null comment '集群',
    host_id               varchar(256)                          null comment '主机ID',
    host_name             varchar(256)                          null comment '主机名',
    cpu_total             bigint                                null comment 'CPU 总量',
    cpu_allocated         bigint                                null comment 'CPU 已分配',
    memory_total          bigint                                null comment '内存总量',
    memory_allocated      bigint                                null comment '内存已分配',
    vm_total              bigint                                null comment '虚拟机总数',
    vm_running            bigint                                null comment '运行中虚拟机数',
    vm_stopped            bigint                                null comment '已停止虚拟机数',
    host_ip               varchar(45)                           null comment '主机 IP',
    `status`                varchar(45)                           null comment '主机状态',
    hypervisor_type       varchar(64)                           null comment '虚拟化类型',
    hypervisor_version    varchar(64)                           null comment '虚拟化版本',
    host_vendor           varchar(128)                          null comment '主机提供商',
    host_model            varchar(128)                          null comment '主机型号',
    cpu_model             varchar(128)                          null comment 'CPU 型号',
    cpu_mHz_per_one_core  int         default 0                 null comment '单核 CPU 容量',
    num_cpu_cores         int         default 0                 null comment 'CPU 总数',
    maintenance_timestamp bigint      default 0                 null comment '维保日期',
    vm_cpu_cores          int         default 0                 null comment '虚拟核数',
    create_time           datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_time           datetime    default CURRENT_TIMESTAMP null comment '更新时间'
);

create table `vm_cloud_image`
(
    id          varchar(50)  default ''                not null
        primary key,
    account_id  varchar(50)  default ''                not null comment '云账号 ID',
    image_id    varchar(512)                           null comment '镜像 ID',
    region      varchar(100)                           null comment '数据中心/区域',
    region_name varchar(100)                           null comment '数据中心/区域',
    image_name  varchar(512) default ''                not null comment '镜像名',
    `description` text                                   null comment '描述',
    os          varchar(512)                           null comment '操作系统',
    disk_size   bigint(13)                             not null comment '镜像磁盘大小',
    disk_infos  json                                   null,
    `status`      varchar(255)                           null comment '磁盘状态,normal:正常，delete:删除',
    create_time datetime     default CURRENT_TIMESTAMP null comment '更新时间',
    update_time datetime     default CURRENT_TIMESTAMP not null comment '上次同步时间',
    image_type  varchar(50)                            null comment '镜像类型',
    constraint UNQ_IMAGE
        unique (image_id, account_id, region)
);

create index IDX_ACCOUNT
    on `vm_cloud_image` (account_id);

create index IDX_IMAGE
    on `vm_cloud_image` (image_id);

create index IDX_LAST_SYC
    on `vm_cloud_image` (update_time);

create index IDX_REGION
    on `vm_cloud_image` (region);

create table `vm_cloud_server`
(
    id                        varchar(50) default ''                not null
        primary key,
    instance_uuid             varchar(50)                           null comment '实例唯一标识',
    source_id                 varchar(50) default ''                not null comment '组织/工作空间 ID',
    project_id                varchar(50)                           null comment 'Project ID',
    account_id                varchar(50)                           not null comment '云账号 ID',
    instance_id               varchar(256)                          null comment '实例 ID',
    instance_name             varchar(256)                          null comment '实例名称',
    image_id                  varchar(255)                          null comment '镜像 ID',
    instance_status           varchar(45)                           null comment '实例状态',
    instance_type             varchar(64)                           null comment '实例类型',
    instance_type_description varchar(64)                           null comment '实例类型描述',
    region                    varchar(256)                          null comment '数据中心/区域',
    zone                      varchar(256)                          null comment '可用区/集群',
    host_id                   varchar(256)                          null comment '宿主机ID',
    `host`                      varchar(256)                          null comment '宿主机',
    resource_pool_id          varchar(256)                          null comment '资源池ID',
    resource_pool             varchar(256)                          null comment '资源池',
    remote_ip                 varchar(64)                           null comment '公网 IP',
    ip_array                  json                                  null comment 'IP 地址',
    os                        varchar(50)                           null comment '操作系统Key',
    os_version                varchar(50)                           null comment '操作系统版本',
    `cpu`                       int(8)      default 0                 null comment 'CPU 核数',
    `memory`                    int(8)      default 0                 null comment '内存容量',
    `disk`                      int(16)     default 0                 null comment '磁盘容量',
    hostname                  varchar(256)                          null comment '主机名',
    management_ip             varchar(64)                           null comment '管理 IP',
    management_port           int                                   null comment '管理端口',
    os_info                   varchar(128)                          null comment '云平台操作系统信息',
    remark                    varchar(128)                          null comment '备注',
    network                   varchar(255)                          null comment '网络',
    vpc_id                    varchar(255)                          null comment 'VPC ID',
    subnet_id                 varchar(255)                          null comment '子网 ID',
    network_interface_id      varchar(255)                          null comment '网络接口 ID',
    management_ipv6           varchar(64)                           null comment '管理ip:ipv6',
    remote_ipv6               varchar(64)                           null comment '公网ipv6',
    local_ipv6                varchar(64)                           null comment '内网ipv6',
    ip_type                   varchar(50)                           null comment 'ip类型: ipv4、 ipv6 、DualStack',
    snap_shot                 int(8)      default 0                 null comment '快照',
    create_time               datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time               datetime    default CURRENT_TIMESTAMP not null comment '更新时间',
    last_operate_time         datetime    default CURRENT_TIMESTAMP null comment '最近关机时间',
    vm_tools_version          varchar(255)                          null,
    vm_tools_status           varchar(255)                          null,
    instance_charge_type      varchar(255)                          null comment '计费类型',
    security_group_ids        json                                  null comment '安全组',
    apply_user                varchar(50)                           null comment '申请人',
    expired_time              datetime                              null comment '包年包月机器到期时间',
    delete_time               datetime                              null comment '删除时间',
    constraint UNIQUE_SERVER
        unique (instance_uuid, account_id)
);

create index IDX_ACCOUNT
    on vm_cloud_server (account_id);

create index IDX_HOST
    on vm_cloud_server (host);

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

create table `workspace`
(
    id              varchar(64)  not null
        primary key,
    _name           varchar(128) not null,
    _description    varchar(255) null,
    organization_id varchar(64)  not null,
    create_time     datetime     not null comment '创建时间',
    update_time     datetime     not null comment '更新时间'
);

create or replace view `org_workspace` as
select `organization`.`id`   AS `id`,
       `organization`.`name` AS `NAME`,
       `organization`.`id`   AS `organization_id`,
       `organization`.`name` AS `organization_name`,
       `organization`.`pid`  AS `pid`,
       'org'                 AS `type`
from `organization`
union
select `workspace`.`id`              AS `id`,
       `workspace`.`_name`           AS `_name`,
       `workspace`.`organization_id` AS `organization_id`,
       `organization`.`name`         AS `organization_name`,
       `workspace`.`organization_id` AS `pid`,
       'workspace'                   AS `workspace`
from (`workspace` left join `organization`
      on (`workspace`.`organization_id` = `organization`.`id`));

create function GET_CHILDREN_ID(orgid varchar(8000)) returns varchar(8000)
BEGIN
    DECLARE oTemp VARCHAR(8000);
    DECLARE oTempChild VARCHAR(8000);
    SET oTemp = '';
    SET oTempChild = CAST(orgid AS CHAR);
    WHILE oTempChild IS NOT NULL
        DO
            SET oTemp = CONCAT(oTemp, ',', oTempChild);
            SELECT GROUP_CONCAT(id) INTO oTempChild FROM `organization` WHERE FIND_IN_SET(pid, oTempChild) > 0;
        END WHILE;
    RETURN oTemp;
END;

create function GET_ROOT_ORG_ID(org_id varchar(200)) returns varchar(200)
BEGIN
    DECLARE rootid VARCHAR(200);
    DECLARE sid VARCHAR(200);
    SET sid = org_id;
    WHILE sid IS NOT NULL
        DO
            SET rootid = sid;
            SELECT pid INTO sid from `organization` where id = sid;
        END WHILE;
    return rootid;
END;

INSERT INTO `user` (id, username, _name, enabled, email, phone, `password`, `source`)
VALUES ('a7087692925ea80d178138522e3edbe1', 'admin', '管理员', 1, 'admin@fit2cloud.com', null,
        '92d7ddd2a010c59511dc2905b7e14f64', 'local');
INSERT INTO `role` (id, _type, _name, _description, parent_role_id, _sort)
VALUES ('ADMIN', 0, '系统管理员', '系统管理员', 'ADMIN', 1);
INSERT INTO `role` (id, _type, _name, _description, parent_role_id, _sort)
VALUES ('ORGADMIN', 0, '组织管理员', '组织管理员', 'ORGADMIN', 2);
INSERT INTO `role` (id, _type, _name, _description, parent_role_id, _sort)
VALUES ('USER', 0, '普通用户', '普通用户', 'USER', 3);
INSERT INTO `user_role` (id, user_id, role_id, _source)
VALUES ('57832df2656d03fdd08b4583eee2e92b', 'a7087692925ea80d178138522e3edbe1', 'ADMIN', null);


