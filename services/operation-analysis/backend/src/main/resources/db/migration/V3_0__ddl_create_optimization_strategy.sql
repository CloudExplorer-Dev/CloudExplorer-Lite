SET SESSION innodb_lock_wait_timeout = 7200;

-- ----------------------------
-- Table structure for operation_analysis_optimization_strategy
-- ----------------------------
DROP TABLE IF EXISTS `operation_analysis_optimization_strategy`;
CREATE TABLE `operation_analysis_optimization_strategy` (
                                                            `id` varchar(64) NOT NULL COMMENT '主键id',
                                                            `name` varchar(255) NOT NULL COMMENT '优化策略名称',
                                                            `strategy_type` varchar(50) NOT NULL COMMENT '策略类型，监控策略or云主机相关mysql表字段策略',
                                                            `resource_type` varchar(50) NOT NULL COMMENT '资源类型',
                                                            `days` bigint(11) DEFAULT NULL COMMENT '分析数据范围,过去多少天',
                                                            `optimization_rules` json DEFAULT NULL COMMENT '优化规则',
                                                            `optimization_scope` tinyint(1) DEFAULT '1' COMMENT '是否针对所有资源',
                                                            `authorize_id` varchar(64) DEFAULT NULL COMMENT '组织或者工作空间id',
                                                            `enabled` tinyint(1) DEFAULT '1' COMMENT '策略是否启用',
                                                            `update_flag` tinyint(1) DEFAULT '1' COMMENT '策略是否可以编辑',
                                                            `create_user_id` varchar(50) NOT NULL COMMENT '创建用户ID',
                                                            `update_user_id` varchar(50) NOT NULL COMMENT '更新用户ID',
                                                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源优化策略';

-- ----------------------------
-- Records of operation_analysis_optimization_strategy
-- ----------------------------

INSERT INTO `operation_analysis_optimization_strategy` VALUES ('075669c1303c4667819a91b152912c49', '建议升配', 'MONITORING', 'VIRTUAL_MACHINE', 10, '{\"children\": [], \"conditions\": [{\"field\": \"CPU_USED_UTILIZATION@MAX\", \"value\": \"90\", \"compare\": \"GE\", \"esField\": true}, {\"field\": \"MEMORY_USED_UTILIZATION@MAX\", \"value\": \"90\", \"compare\": \"GE\", \"esField\": true}], \"conditionType\": \"OR\"}', 1, NULL, 1, 1, '69549a1d4ffa1078f163b339087949ad', '69549a1d4ffa1078f163b339087949ad', '2023-06-09 18:16:49', '2023-06-12 02:21:23');
INSERT INTO `operation_analysis_optimization_strategy` VALUES ('3277cf5ada8d4b6397149c9761ca4223', '建议变更付费方式', 'OTHER', 'VIRTUAL_MACHINE', NULL, '{\"children\": [{\"children\": [], \"conditions\": [{\"field\": \"instanceChargeType\", \"value\": \"PrePaid\", \"compare\": \"EQ\", \"esField\": false}, {\"field\": \"shutdownDuration\", \"value\": \"30\", \"compare\": \"GE\", \"esField\": false}], \"conditionType\": \"AND\"}, {\"children\": [], \"conditions\": [{\"field\": \"instanceChargeType\", \"value\": \"PostPaid\", \"compare\": \"EQ\", \"esField\": false}, {\"field\": \"runningDuration\", \"value\": \"30\", \"compare\": \"GE\", \"esField\": false}], \"conditionType\": \"AND\"}], \"conditions\": [], \"conditionType\": \"AND\"}', 1, NULL, 1, 1, '69549a1d4ffa1078f163b339087949ad', '69549a1d4ffa1078f163b339087949ad', '2023-06-10 11:03:49', '2023-06-11 22:09:49');
INSERT INTO `operation_analysis_optimization_strategy` VALUES ('b05eb0dc5ae64b7089a19536f172f95b', '建议回收', 'OTHER', 'VIRTUAL_MACHINE', NULL, '{\"children\": [], \"conditions\": [{\"field\": \"shutdownDuration\", \"value\": \"30\", \"compare\": \"GE\", \"esField\": false}, {\"field\": \"instanceStatus\", \"value\": \"ToBeRecycled\", \"compare\": \"EQ\", \"esField\": false}], \"conditionType\": \"OR\"}', 1, NULL, 1, 1, '69549a1d4ffa1078f163b339087949ad', '69549a1d4ffa1078f163b339087949ad', '2023-06-10 11:05:15', '2023-06-12 02:04:40');
INSERT INTO `operation_analysis_optimization_strategy` VALUES ('dec7649367b04d2b943edec4cbd4cad2', '建议降配', 'MONITORING', 'VIRTUAL_MACHINE', 10, '{\"children\": [], \"conditions\": [{\"field\": \"CPU_USED_UTILIZATION@MAX\", \"value\": \"1\", \"compare\": \"LE\", \"esField\": true}, {\"field\": \"MEMORY_USED_UTILIZATION@MAX\", \"value\": \"1\", \"compare\": \"LE\", \"esField\": true}], \"conditionType\": \"OR\"}', 1, NULL, 1, 1, '69549a1d4ffa1078f163b339087949ad', '69549a1d4ffa1078f163b339087949ad', '2023-06-09 15:01:04', '2023-06-12 02:21:32');

SET SESSION innodb_lock_wait_timeout = DEFAULT;
