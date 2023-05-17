SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE  IF NOT EXISTS `data_convert_version`
(
    `id`                 varchar(255) COLLATE utf8mb4_bin NOT NULL,
    `convert_class_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '执行器函数',
    `module_name`        varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '模块名称',
    `success`            tinyint(4) unsigned zerofill DEFAULT NULL COMMENT '是否执行成功',
    `create_time`        timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_unique` (`convert_class_name`,`module_name`) USING HASH
) ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE utf8mb4_general_ci
    COMMENT '数据转换';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
