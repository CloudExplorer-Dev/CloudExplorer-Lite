SET SESSION innodb_lock_wait_timeout = 7200;

ALTER TABLE `bill_dimension_setting`
    ADD COLUMN `update_flag` tinyint(1) NOT NULL DEFAULT true COMMENT '规则是否修改' AFTER `authorize_id`;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
