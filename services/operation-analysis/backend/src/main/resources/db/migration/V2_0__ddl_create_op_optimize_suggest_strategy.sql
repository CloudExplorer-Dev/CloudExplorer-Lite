SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS `op_optimize_suggest_strategy`
(
    `id` bigint
(
    11
) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `strategy_type` varchar
(
    255
) DEFAULT NULL COMMENT '建议策略类型',
    `strategy_content` json DEFAULT NULL COMMENT '策略内容',
    `operate_user_id` varchar
(
    126
) DEFAULT NULL COMMENT '操作人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY
(
    `id`
),
    UNIQUE KEY `IDX_TYPE`
(
    `strategy_type`
)
                                                     USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='优化建议策略';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
