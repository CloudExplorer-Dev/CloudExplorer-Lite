SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `vm_cloud_server_status_timing` (
    `id` INT ( 11 ) NOT NULL AUTO_INCREMENT COMMENT '序号' PRIMARY KEY,
    `cloud_server_id` VARCHAR ( 50 ) NOT NULL COMMENT '云主机ID',
    `on_time` datetime DEFAULT NULL COMMENT '开机时间',
    `off_time` datetime DEFAULT NULL COMMENT '关机时间',
    `shutdown_duration` BIGINT ( 11 ) DEFAULT 0 COMMENT '关机时长秒',
    `running_duration` BIGINT ( 11 ) DEFAULT 0 COMMENT '运行时长秒'
    ) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci COMMENT '云主机运行关机状态计时';


SET SESSION innodb_lock_wait_timeout = DEFAULT;
