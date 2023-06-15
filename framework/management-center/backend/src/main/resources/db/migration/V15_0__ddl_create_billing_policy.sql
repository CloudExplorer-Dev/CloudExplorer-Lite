SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE `bill_policy` (
                               `id` varchar(255) NOT NULL COMMENT '主键id',
                               `name` varchar(255) DEFAULT NULL COMMENT '策略名称',
                               `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `bill_policy_cloud_account_mapping` (
                                                     `id` varchar(255) NOT NULL,
                                                     `bill_policy_id` varchar(255) DEFAULT NULL COMMENT '策略id',
                                                     `cloud_account_id` varchar(255) DEFAULT NULL COMMENT '云账号id',
                                                     `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                                     `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `bill_policy_details` (
                                       `id` varchar(255) NOT NULL,
                                       `bill_policy_id` varchar(255) DEFAULT NULL COMMENT '策略id',
                                       `resource_type` varchar(255) DEFAULT NULL COMMENT '资源类型',
                                       `unit_price_on_demand_billing_policy` json DEFAULT NULL COMMENT '单价(按需)计费策略',
                                       `unit_price_monthly_billing_policy` json DEFAULT NULL COMMENT '单价(包年包月)计费策略',
                                       `package_price_billing_policy` json DEFAULT NULL COMMENT '套餐计费策略',
                                       `global_config_meta` json DEFAULT NULL,
                                       `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                       `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS `resource_instance_state` (
                                           `id` varchar(255) NOT NULL COMMENT '资源类型',
                                           `cloud_account_id` varchar(255) DEFAULT NULL COMMENT '云账号id',
                                           `resource_type` varchar(255) DEFAULT NULL COMMENT '资源类型',
                                           `resource_id` varchar(255) DEFAULT NULL COMMENT '资源id',
                                           `instance_state` text  COMMENT '实例状态',
                                           `month` varchar(255)   DEFAULT NULL COMMENT '月份',
                                           `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                           `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS `resource_instance` (
                                     `id` varchar(255) NOT NULL DEFAULT '' COMMENT '主键id 用于区别实例',
                                     `resource_id` varchar(255) NOT NULL COMMENT '资源id',
                                     `resource_name` varchar(255) DEFAULT NULL COMMENT '资源名称',
                                     `resource_type` varchar(255) DEFAULT NULL COMMENT '资源类型',
                                     `cloud_account_id` varchar(255) DEFAULT NULL COMMENT '云账号id',
                                     `product_id` varchar(255) DEFAULT NULL COMMENT '产品id',
                                     `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
                                     `product_detail` varchar(255) DEFAULT NULL COMMENT '产品详情',
                                     `region` varchar(255) DEFAULT NULL COMMENT '区域',
                                     `zone` varchar(255) DEFAULT NULL COMMENT '可用区',
                                     `workspace_id` varchar(255) DEFAULT NULL COMMENT '工作空间id',
                                     `workspace_name` varchar(255) DEFAULT NULL COMMENT '工作空间名称',
                                     `organization_id` varchar(255) DEFAULT NULL COMMENT '组织id',
                                     `organization_name` varchar(255) DEFAULT NULL COMMENT '组织名称',
                                     `bill_mode` varchar(255) DEFAULT NULL COMMENT '计费模式 ON_DEMAND 按需 MONTHLY包年包月',
                                     `meta` json DEFAULT NULL COMMENT '元数据',
                                     `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                     `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


SET SESSION innodb_lock_wait_timeout = DEFAULT;
