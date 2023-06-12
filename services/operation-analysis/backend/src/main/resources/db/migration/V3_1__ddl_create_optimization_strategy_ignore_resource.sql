SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `operation_analysis_optimization_strategy_ignore_resource`
(
    `id` varchar(64) NOT NULL COMMENT '主键id',
    `optimization_strategy_id` varchar(50) NOT NULL COMMENT '优化策略ID',
    `resource_id` varchar(50) NOT NULL COMMENT '资源ID',
    PRIMARY KEY (`id`)
    ) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci COMMENT '优化策略忽略资源';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
