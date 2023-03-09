SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `compliance_rule_group` (id, `name`, `description`)
VALUES ('2fc5bf68ba7ce91112ba0ec3a73062b4', '网络合规最佳实践', '网络合规管理最佳实践配置检测'),
       ('359876ddb3819c94258d63e7823646b5', 'OSS合规基线', 'OSS 合规检查为您提供全方位的对象存储资源检查功能。'),
       ('5536b282b21ce92d9b44164ee0f95f50', '数据库合规管理', '数据库合规管理最佳实践合规包中的默认规则。'),
       ('592df9b262f9fcb772fb27e703782592', ' CIS合规检查',
        'CIS（Center for Internet Security）合规检查能力，为您动态且持续地监控您保有在云上的资源是否符合 CIS Control 网络安全架构要求。'),
       ('7d0f1173c4d681f81bb2243950c1b9c5', 'ECS合规管理', 'ECS合规管理最佳实践配置检测'),
       ('8162dc7f8c4ffa84e9b8f1edab28557c', '安全组最佳实践', '安全组最佳配置实践'),
       ('a46779e112b01a0f20b13cfbd73b8400', '等保预检',
        '等保合规检查（全称为等级保护合规检查）为您提供了全面覆盖通信网络、区域边界、计算环境和管理中心的网络安全检查。');

SET SESSION innodb_lock_wait_timeout = DEFAULT;
