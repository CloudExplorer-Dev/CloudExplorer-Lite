SET SESSION innodb_lock_wait_timeout = 7200;

delete from `system_parameter` where `param_key` = 'help.link';
INSERT INTO `system_parameter` (`param_key`, `param_value`, `type`, `sort`, `module`) VALUES ('help.link', 'https://fit2cloud.com/cloudexplorer-lite/docs/', 'text', NULL, 'management-center');

SET SESSION innodb_lock_wait_timeout = DEFAULT;
