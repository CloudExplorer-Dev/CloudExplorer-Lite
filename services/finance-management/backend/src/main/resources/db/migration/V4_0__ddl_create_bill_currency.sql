SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE `bill_currency` (
                                 `code` varchar(255)  NOT NULL COMMENT '币种code',
                                 `message` varchar(255)  DEFAULT NULL COMMENT '提示',
                                 `symbol` varchar(255) DEFAULT NULL COMMENT '符号',
                                 `unit` varchar(255)  DEFAULT NULL COMMENT '单位',
                                 `exchangeRate` double(20,10) NOT NULL DEFAULT '1.0000000000' COMMENT '汇率',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
