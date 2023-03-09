SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `compliance_rule` (id, `name`, rule_group_id, platform, resource_type, rules, risk_level, `description`, `enable`)
VALUES ('0025b4151ec878fbbff1579652f5ef48', '使用专有网络类型的Redis实例', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'REDIS', '{
    \"rules\": [
      {
        \"field\": \"instance.fit2cloud_ali_platform_REDIS.networkType\",
        \"value\": \"VPC\",
        \"compare\": \"EQ\"
      }
    ],
    \"scanRule\": \"COMPLIANCE\",
    \"conditionType\": \"AND\"
  }', -1,
        '如果未指定参数，检查Redis实例的网络类型为专有网络，视为“合规”；如果指定参数，检查Redis实例的专有网络实例在指定参数范围内，视为“合规”。 如果未指定参数，检查Redis实例的网络类型为经典网络，视为“不合规”；如果指定参数，检查Redis实例的专有网络实例不在指定参数范围内，视为“不合规”。',
        1),
       ('0081197d8d15f4adc4fefae2beace0cf', 'OBS存储空间开启服务端KMS加密', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_huawei_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_OSS.encryption.sseAlgorithm\",
             \"value\": \"KMS\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'OBS存储空间开启服务端的KMS加密，视为“合规”。 OBS存储空间未开启服务端的KMS加密，视为“不合规”。', 1),
       ('04b19aba5ec9076a7348f46225288bb2', 'OSS存储空间开启防盗链', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.referer.refererList.referer\",
             \"compare\": \"EXIST\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'OSS存储空间开启防盗链，视为“合规”。 OSS存储空间未开启防盗链，视为“不合规”。', 1),
       ('09600ccf48e1f703c22a4f4324478971', '高权限的RAM用户开启MFA', '592df9b262f9fcb772fb27e703782592',
        'fit2cloud_ali_platform', 'RAM', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_RAM.loginProfile.mfabindRequired\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '如果您为RAM用户授予了指定的高风险权限策略，且已开启MFA，视为“合规”。 如果您为RAM用户授予了指定的高风险权限策略，但未开启MFA，视为“不合规”。',
        1),
       ('0a7207f8b2668708b409fe401b3d2f3e', '检测闲置弹性公网IP', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_huawei_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_PUBLIC_IP.status\",
             \"value\": \"DOWN\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '所有弹性公网IP均已绑定到ECS实例或NAT网关实例，视为“合规”。 一个或多个弹性公网IP未绑定到ECS实例或NAT网关实例，视为“不合规”。',
        1),
       ('0dac2247cd7a9ec82f709b3c31fe3576', 'RDS实例开启了SSL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_MYSQL.enable_ssl\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例已开启SSL，视为“合规”。 RDS实例未开启SSL，视为“不合规”。', 1),
       ('0f7cdf61bc38b9ff06f83c78aced06c7', 'CVM实例付费类型为包年包月', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.instanceChargeType\",
             \"value\": \"PREPAID\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'CVM实例的付费类型为包年包月，视为“合规”。 CVM实例的付费类型非包年包月，视为“不合规”。', 1),
       ('10f3d9e455fb984fd6b820132eb11417', 'RDS实例存储空间满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_MYSQL.volume.size\",
             \"value\": 40,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('139951351254e739ad2625a03fdd712a', 'RDS实例内存满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MYSQL.memory\",
             \"value\": 1024,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('14731fdfbca64285722777fdf30cd619', 'RDS实例开启删除保护-PostGreSql', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_POST_GRE_SQL.deletionProtection\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例开启删除保护，视为“合规”。 RDS实例未开启删除保护，视为“不合规”。', 1),
       ('14dd01801c8b0d4c0b7887c129c8b605', 'Redis实例满足指定内存容量要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_REDIS.size\",
             \"value\": 2048,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'Redis实例的内存容量大于等于您设置的参数，视为“合规”。 Redis实例的内存容量小于您设置的参数，视为“不合规”。',
        1),
       ('1778bfdee847f41cc36c6e3aa5dfdc11', '待挂载的CVM数据磁盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_DISK.encrypt\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, '待挂载的CVM数据磁盘已开启加密，视为“合规”。', 1),
       ('1bb2977fa98e1a002e4057ec62bb7d67', 'RDS实例内存满足最低要求-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SQL_SERVER.memory\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('1d21118caac44c7a65438225f3e1a221', '使用高可用（主备）的RDS实例-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_SQL_SERVER.type\",
             \"value\": \"Single\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例为主备实例，视为“合规”。 RDS实例为单机实例，视为“不合规”。', 1),
       ('1f89847bb94cbdbd0ac37896e44b2977', 'ELB实例状态为运行中', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_huawei_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_LOAD_BALANCER.operating_status\",
             \"value\": \"ONLINE\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'ELB实例的运行状态为运行中，视为“合规”。 ELB实例的运行状态为已停止，视为“不合规”。', 1),
       ('237569e8019abbe09dca8c2e57eaa34a', 'CVM实例未绑定公网地址', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.internetAccessible.publicIpAssigned\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'CVM实例未直接绑定IPv4公网IP或弹性公网IP，视为“合规”。 CVM实例已绑定IPv4公网IP或弹性公网IP，视为“不合规”。',
        1),
       ('23e16854e277589e94c29802d7dc9dd2', '使用多可用区的RDS实例-PostGreSql', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_POST_GRE_SQL.mutriORsignle\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例支持多个可用区，视为“合规”。 RDS实例支持一个可用区，视为“不合规”。', 1),
       ('250d53ae5f8ca5e6d7a62e81722a59d4', 'RDS实例开启删除保护-MariaDB', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MARIA_DB.deletionProtection\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例开启删除保护，视为“合规”。 RDS实例未开启删除保护，视为“不合规”。', 1),
       ('26b6476f54dcd110e05ee815facad048', 'CVM实例内存满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.memory\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'CVM实例内存满足最低要求，视为“合规”。', 1),
       ('2860f283450ad2fde72827c2aed3b283', '使用多可用区的RDS实例-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.mutriORsignle\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例支持多个可用区，视为“合规”。 RDS实例支持一个可用区，视为“不合规”。', 1),
       ('291f29f05379273d14ac835e9ee5b7ec', '不存在闲置的弹性公网IP', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_PUBLIC_IP.status\",
             \"value\": \"Available\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '弹性公网IP均已绑定到ECS实例或NAT网关实例，视为“合规”。', 1),
       ('306d7b61464d8cf831ba31d9fd54ac3a', 'Redis实例的节点类型为双副本', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_REDIS.nodeType\",
             \"value\": \"double\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'Redis实例的节点类型为双副本，视为“合规”。 Redis实例的节点类型为单副本，视为“不合规”。', 1),
       ('3120aef9bb05cbf75681fde2dbd00790', 'ECS实例CPU核数满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.cpu\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的CPU核数大于等于您设置的期望值，视为“合规”。', 1),
       ('323724ec7c0c065c715716b960e519aa', 'ECS实例内存满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.memory\",
             \"value\": 16000,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的内存大于等于您设置的期望值，视为“合规”。', 1),
       ('34026579ec14df80f82343cec2fcc74b', '运行中的CVM实例未绑定公网地址', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.instanceState\",
             \"value\": \"RUNNING\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.internetAccessible.publicIpAssigned\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0,
        '运行中的CVM实例没有直接绑定IPv4公网IP地址或弹性公网IP地址，视为“合规”。 运行中的CVM实例直接绑定IPv4公网IP地址或弹性公网IP地址，视为“不合规”。',
        1),
       ('349d247d162125bd0dfe26cabfdc6e55', 'SLB实例未绑定公网IP', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_LOAD_BALANCER.addressType\",
             \"value\": \"internet\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'SLB实例未绑定公网IP地址，视为“合规”。 SLB实例已绑定公网IP地址，视为“不合规”。', 1),
       ('355bb1f4e8a858d4fbaceb6401875840', '使用专有网络类型的RDS实例-SqlServer', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.instanceNetworkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，检查RDS实例的网络类型为专有网络，视为“合规”；如果指定参数，检查RDS实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('3575010909c3be137543419024721448', 'OpenStack 安全组入网设置中不能有对所有端口开放的访问规则',
        '8162dc7f8c4ffa84e9b8f1edab28557c', 'fit2cloud_openstack_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"ingress\",
             \"compare\": \"CONTAIN\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.port_range_min.keyword\",
             \"value\": 1,
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.port_range_max.keyword\",
             \"value\": 65535,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '安全组入方向授权策略为允许，当端口范围未设置为1-65535时，视为“合规”； 安全组入方向授权策略为允许，当端口范围设置为1-65535，视为“不合规”。',
        1),
       ('390ed7fd8fc2a211fcde31aa7c5b1549', 'RDS实例存储空间满足最低要求-PostgreSQL',
        '5536b282b21ce92d9b44164ee0f95f50', 'fit2cloud_tencent_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_POST_GRE_SQL.dbinstanceStorage\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('39f13ec0fa452a651b5689fdcc4bc478', '安全组不允许对全部网段开启风险端口', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SECURITY_GROUP.rule.permissions.permission.sourceCidrIp\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '当安全组入方向网段未设置为0.0.0.0/0时，视为“合规”；当安全组入方向网段设置为0.0.0.0/0，但指定的风险端口为关闭状态时，视为“合规”。',
        1),
       ('3dcd51c4fc401fd6936ace5bde90b458', 'ECS实例的系统盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.type\",
             \"value\": \"system\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.encrypted\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'ECS实例的系统盘开启加密，视为“合规”。', 1),
       ('3e4f21abe745bb787be35acfddedb1ff', 'Elasticsearch实例开启公网访问控制', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'ELASTIC_SEARCH', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ELASTIC_SEARCH.elbWhiteList.enableWhiteList\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'Elasticsearch实例开启公网访问控制，视为“合规”； Elasticsearch实例未开启公网访问控制视为“不合规”。', 1),
       ('3f0d1dc06adde1cbfa620ae12c27080f', 'VMware vSphere 云服务器CPU、内存检查', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_vsphere_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_ECS.cpu\",
             \"value\": 4,
             \"compare\": \"GE\"
           },
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_ECS.memory\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', -1, 'VMware vSphere 云服务器CPU、内存满足要求视为“合规”', 1),
       ('3f4550fc093225ea812debb1f9e89f4d', '使用专有网络类型的ECS实例', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.instanceNetworkType\",
             \"value\": \"vpc\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，检查ECS实例的网络类型为专有网络，视为“合规”；如果指定参数，检查ECS实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('42664f7ec5362a0f182232402df0b076', '使用数据库代理模式访问SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.connectionMode\",
             \"value\": \"Safe\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        'RDS实例的SQL Server类型数据库的访问模式为代理模式，视为“合规”。 RDS实例的SQL Server类型数据库的访问模式不为代理模式，视为“不合规”。',
        1),
       ('42b84e32004da6691f4e06dd24a8b8b2', 'OSS存储空间开启服务端的KMS加密', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.encryption.applyServerSideEncryptionByDefault.ssealgorithm\",
             \"value\": \"KMS\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'OSS存储空间开启服务端的KMS加密，视为“合规”。 OSS存储空间未开启服务端的KMS加密，视为“不合规”。', 1),
       ('446b595f180cb73b1d52eabffe07aa97', 'VMware vSphere VM 状态扫描', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_vsphere_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_ECS.instanceStatus\",
             \"value\": \"Running\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'VMware vSphere 检测账号下 VM 是否符合条件，符合视为“合规”，否则属于“不合规”', 1),
       ('4519ed839a77cd414ab7bdb89edaf747', 'CVM包年包月实例开启自动续费', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.renewFlag\",
             \"value\": \"NOTIFY_AND_AUTO_RENEW\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'CVM包年包月的实例开启自动续费，视为“合规”。', 1),
       ('45c05af10766f7c4f991fd83cde885f6', 'CVM固定公网IP实例按固定带宽计费', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.internetAccessible.internetChargeType\",
             \"value\": \"BANDWIDTH_POSTPAID_BY_HOUR\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'CVM实例已分配固定公网IP地址，且公网带宽的计费方式按固定带宽计费，视为“合规”。', 1),
       ('46dc4592524f3d7d5f4cf3f8a721ceb1', 'Elasticsearch实例未开启公网访问', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'ELASTIC_SEARCH', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ELASTIC_SEARCH.publicAccess\",
             \"value\": \"CLOSE\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'Elasticsearch实例未开启公网访问，视为“合规“。 Elasticsearch实例已开启公网访问，视为“不合规”。', 1),
       ('46f1e30711651bbc943b7c08f2761a17', 'SLB实例满足指定带宽要求', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_LOAD_BALANCER.bandwidth\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'SLB实例的可用带宽大于等于规则中指定的参数值，视为“合规”。', 1),
       ('474f22ab3aef675078aeb7503bcd50ac', 'RDS实例网络类型为专有网络-MySQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MYSQL.instanceNetworkType\",
             \"value\": \"Classic\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的网络类型为专有网络，视为“合规”。 RDS实例的网络类型为经典网络，视为“不合规”。', 1),
       ('48c4e0fe3f11615638c1a7da950f449d', 'MongoDB实例未被冻结', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'MONGO_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_MONGO_DB.status\",
             \"value\": \"frozen\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'MongoDB实例为未冻结状态，视为“合规”。 MongoDB实例为冻结状态，视为“不合规”。', 1),
       ('4b89cda6a052240e8331d367fbebcafb', 'OpenStack 安全组入网设置不能有对所有协议开放的访问规则',
        '8162dc7f8c4ffa84e9b8f1edab28557c', 'fit2cloud_openstack_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.protocol.keyword\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"ingress\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '安全组入方向授权策略为允许，当端口的协议类型未设置为ALL时，视为“合规”；如果端口的协议类型设置为ALL，但被优先级更高的授权策略拒绝，视为“合规”。 安全组入方向授权策略为允许，当端口的协议类型设置为ALL，且未被优先级更高的授权策略拒绝时，视为“不合规”。',
        1),
       ('4da0a984adea14b0e8f462fcd33a2d8c', 'RDS实例开启删除保护-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.deletionProtection\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例开启删除保护，视为“合规”。 RDS实例未开启删除保护，视为“不合规”。', 1),
       ('4e8a085cacd032e24a7352f7c2790cb8', '不存在闲置的CVM数据磁盘', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_DISK.diskState\",
             \"value\": \"UNATTACHED\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '所有数据磁盘均已挂载到CVM实例，视为“合规”。 一个或多个数据磁盘未挂载到CVM实例，视为“不合规”。', 1),
       ('5113fd27ef3d4f835dd2a69bab7d5d1b', 'OpenStack 安全组入网设置有效', '8162dc7f8c4ffa84e9b8f1edab28557c',
        'fit2cloud_openstack_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"ingress\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.remote_ip_prefix.keyword\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.port_range_min.keyword\",
             \"value\": 1,
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.port_range_max.keyword\",
             \"value\": 65535,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, '安全组入方向授权策略为允许，当端口范围1-65535和授权对象0.0.0.0/0未同时出现时，视为“合规”。', 1),
       ('51d6db71e7162364740c49eb3445066d', 'RDS实例内存满足最低要求-MariaDB', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MARIA_DB.memory\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('527a0504f64ee6c5d684a3e738562997', 'OpenStack ECS实例CPU核数满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_openstack_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_openstack_platform_ECS.cpu\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的CPU核数大于等于您设置的期望值，视为“合规”。 ECS实例的CPU核数小于您设置的期望值，视为“不合规”。',
        1),
       ('53dedb203f5ac460bbf4f2e55059498d', '使用专有网络类型的MongoDB实例', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MONGO_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MONGO_DB.netType\",
             \"value\": 1,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '检查MongoDB实例的网络类型为专有网络，视为“合规”；如果指定参数，则检查MongoDB实例的专有网络实例在指定参数范围内，视为“合规”。检查MongoDB实例的网络类型为经典网络，视为“不合规”；',
        1),
       ('545f49459b5212decd0528bfdd9337c8', '检测闲置弹性公网IP', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_tencent_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_PUBLIC_IP.addressStatus\",
             \"value\": \"UNBIND\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '所有弹性公网IP均已绑定到CVM实例或NAT网关实例，视为“合规”。 一个或多个弹性公网IP未绑定到CVM实例或NAT网关实例，视为“不合规”。',
        1),
       ('5556e04f7dfc2a29cf211582997d15dc', '不存在闲置的ECS数据磁盘', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.status\",
             \"value\": \"Available\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '您需要定期清理闲置磁盘，避免资源浪费带来不必要的成本增长。', 1),
       ('58669c7d98cb6147020ecefbb67ced37', 'SLB实例开启释放保护', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_LOAD_BALANCER.deleteProtection\",
             \"value\": \"on\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'SLB实例开启释放保护，视为“合规”。 SLB实例未开启释放保护，视为“不合规”。', 1),
       ('588f7f2b223e32b7b4656602b7ba1002', 'VMware vSphere 宿主机CPU、内存满足要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_vsphere_platform', 'HOST', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_HOST.numCpuCores\",
             \"value\": 8,
             \"compare\": \"GE\"
           },
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_HOST.memoryTotal\",
             \"value\": 130000,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'VMware vSphere 宿主机CPU、内存满足要求，视为“合规”', 1),
       ('59ea82a5fbf588767ae44f9b6a0dc291', '弹性IP实例带宽满足最低要求', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_huawei_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_PUBLIC_IP.bandwidth.size\",
             \"value\": 1,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '弹性公网IP实例的可用带宽大于等于您设置的参数，视为“合规”。 弹性公网IP实例的可用带宽小于您设置的参数，视为“不合规”。',
        1),
       ('5aafd97e0cb2ae88759bd8e89b76ada6', '安全组不允许对全部网段开启风险端口', '8162dc7f8c4ffa84e9b8f1edab28557c',
        'fit2cloud_tencent_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SECURITY_GROUP.rule.ingress.cidrBlock\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, '当安全组入方向网段未设置为0.0.0.0/0时，视为“合规”。 当安全组入方向网段未设置为0.0.0.0/0时，视为“合规”。', 1),
       ('5ac19dd47ce87fc9bf8c6b7ece74ebd2', 'RDS实例CPU核数满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_MYSQL.cpu\",
             \"value\": 3,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('5e21f54374e0d532fecb2e416b7db231', '使用专有网络类型的RDS实例-Mongodb', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'MONGO_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MONGO_DB.networkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，检查RDS实例的网络类型为专有网络，视为“合规”；如果指定参数，检查RDS实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('64e83034fe8b75e0938b367c75e7ec3f', '运行中的ECS实例在专有网络', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.status\",
             \"value\": \"Running\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.instanceNetworkType\",
             \"value\": \"vpc\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0,
        '如果ECS实例有归属的专有网络VPC，但未指定参数，视为\"合规\"；如果ECS实例有归属的专有网络VPC且指定参数，则检查专有网络VPC在指定参数范围内，视为“合规”。 如果ECS实例无归属的专有网络VPC，视为\"不合规\"；如果ECS实例有归属的专有网络VPC且指定参数，但检查专有网络VPC不在指定参数范围内，视为“不合规”。关于如何修正该问题，请参见修正指导。 非运行中的ECS实例，视为“不适用”。',
        1),
       ('665b559b4766da22897c5acdc76752f2', 'Elasticsearch实例数据节点开启云盘加密', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'ELASTIC_SEARCH', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ELASTIC_SEARCH.nodeSpec.diskEncryption\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0,
        'Elasticsearch实例数据节点已开启云盘加密，视为“合规”。 Elasticsearch实例数据节点未开启云盘加密，视为“不合规”。', 1),
       ('66717ea497d5907175667710902f1e7b', 'RAM用户开启MFA', '592df9b262f9fcb772fb27e703782592',
        'fit2cloud_ali_platform', 'RAM', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_RAM.loginProfile.mfabindRequired\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RAM用户开启MFA，视为“合规”。 RAM用户未开启MFA，视为“不合规”。', 1),
       ('67f8ea0932afa9bf72df22df52e719ae', '使用专有网络类型的Redis实例', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_REDIS.networkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，检查RDS实例的网络类型为专有网络，视为“合规”；如果指定参数，检查RDS实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('6894075df35864b5daa2781782170706', 'Redis实例满足指定内存容量要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_REDIS.max_memory\",
             \"value\": 126,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'Redis实例的内存容量大于等于您设置的参数，视为“合规”。 Redis实例的内存容量小于您设置的参数，视为“不合规”。',
        1),
       ('6905e36cb1e72785fbbd1bf9060e82f4', '使用高可用（主备）的RDS实例-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_POST_GRE_SQL.type\",
             \"value\": \"Single\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例为主备实例，视为“合规”。 RDS实例为单机实例，视为“不合规”。', 1),
       ('69e33fc3b2b029dd885351756f9c6d6f', 'CLB实例状态为运行中', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_tencent_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_LOAD_BALANCER.status\",
             \"value\": 1,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'CLB实例的运行状态为运行中，视为“合规”。 CLB实例的运行状态为已停止，视为“不合规”。', 1),
       ('6b6b6f5210d42d4f2c01fcad6ad523eb', 'RDS实例CPU核数满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MYSQL.cpu\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('6cbbb5ae64e0b510edb68838b948c388', 'ECS数据磁盘释放时保留自动快照', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.deleteAutoSnapshot\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, '设置ECS数据磁盘释放时保留自动快照，视为“合规”。', 1),
       ('6cbce8a7b023a1bf1ce41d98b7f013d3', 'ECS实例付费类型为包年包月', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.instanceChargeType\",
             \"value\": \"PrePaid\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'ECS实例的付费类型为包年包月，视为“合规”。 ECS实例的付费类型非包年包月，视为“不合规”。', 1),
       ('6e0b99b9667c7912cf4c18255d25c185', 'OSS存储空间ACL禁止公共读', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.acl.accessControlList.grant\",
             \"value\": \"private\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'OSS存储空间的ACL策略禁止公共读，视为“合规”。 OSS存储空间的ACL策略允许公共读，视为“不合规”。', 1),
       ('6e0f0ddd7bc1d8b9675fbecf73d5c656', '弹性IP实例带宽满足最低要求', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_PUBLIC_IP.bandwidth\",
             \"value\": 3,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, '弹性公网IP实例的可用带宽大于等于您设置的参数，视为“合规”。', 1),
       ('6eeafea705bf54e7ed692ea70abccf1e', 'OSS存储空间开启服务端默认加密', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.encryption.applyServerSideEncryptionByDefault.ssealgorithm\",
             \"value\": \"AES256\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'OSS存储空间开启服务端的OSS完全托管加密，视为“合规”。', 1),
       ('71f42f8f9a8af661830cc21c6e58c8af', '按量付费的ECS已停机实例使用节省停机模式',
        '7d0f1173c4d681f81bb2243950c1b9c5', 'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.instanceChargeType\",
             \"value\": \"PostPaid\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.stoppedMode\",
             \"value\": \"StopCharging\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '按量付费的ECS实例停机时使用节省停机模式，视为“合规”。 按量付费的ECS实例停机时未使用节省停机模式，视为“不合规”。关于如何修正该问题，请参见修正指导。 包年包月的实例和运行中的实例不适用本规则，视为“不适用”。',
        1),
       ('78354ed0b7594e3ee4993923c7ef0a45', 'ECS实例开启释放保护', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.deletionProtection\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'ECS实例开启释放保护，视为“合规”。', 1),
       ('788e03669955b4fbf98ce93c27905d54', 'RDS实例未申请外网地址-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', 1, 'RDS实例未申请外网地址，视为“合规”。 RDS实例已申请外网地址，视为“不合规”。', 1),
       ('7bb3bc60526d1bc325660baedffeb5f0', '弹性IP实例带宽满足最低要求', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_tencent_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_PUBLIC_IP.bandwidth\",
             \"value\": 3,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '弹性公网IP实例的可用带宽大于等于您设置的参数，视为“合规”。 弹性公网IP实例的可用带宽小于您设置的参数，视为“不合规”。',
        1),
       ('7bdf1e6f9d78711497ed5f7cec2345fe', 'ECS实例的系统盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.bootable\",
             \"value\": \"true\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.bootable\",
             \"value\": \"true\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'ECS实例的系统盘开启加密，视为“合规”。', 1),
       ('7eb2d7d877be803c94d22f8f9b1f9285', '安全组入网设置有效', '8162dc7f8c4ffa84e9b8f1edab28557c',
        'fit2cloud_huawei_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"ingress\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.action.keyword\",
             \"value\": \"allow\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.remote_ip_prefix.keyword\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.multiport.keyword\",
             \"value\": \"1-65535\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '安全组入方向授权策略为允许，当端口范围-1/-1和授权对象0.0.0.0/0未同时出现时，视为“合规”。', 1),
       ('7f8faaa06525ae77e918511cc19438b1', '使用中的CVM数据磁盘已开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_DISK.encrypt\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '使用中的CVM数据磁盘已开启加密，视为“合规”。', 1),
       ('801f7940c44254031b61e18396788036', 'RDS实例未申请外网地址-PostGreSql', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_POST_GRE_SQL.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_POST_GRE_SQL.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', -1, 'RDS实例未申请外网地址，视为“合规”。 RDS实例已申请外网地址，视为“不合规”。', 1),
       ('80618a99d3ffa767305bf38c4df43ab5', 'RDS实例存储空间满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SQL_SERVER.storage\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('81e2dff7b985b95c285d57c5f2f9168d', 'RDS实例存储空间满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MYSQL.volume\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('8288873fc8648897007cf98ef08a2455', 'CVM实例未被隔离', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.isolatedSource\",
             \"value\": \"ARREAR\",
             \"compare\": \"NOT_EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.isolatedSource\",
             \"value\": \"EXPIRE\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'CVM实例未因欠费或安全等原因而被隔离，视为“合规”。 CVM实例因欠费或安全等原因而被隔离，视为“不合规”。', 1),
       ('84139a94a370f1a4a9c0436befdd06c6', 'OpenStack ECS实例状态不是已停止状态', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_openstack_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_openstack_platform_ECS.instanceStatus\",
             \"value\": \"Running\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'ECS实例的状态为运行中，视为“合规”。 ECS实例的状态为已停止，视为“不合规”。', 1),
       ('89702f4a6f9f4afedee58414db6cebe6', 'COS存储空间开启防盗链', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_tencent_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_OSS.referer.status\",
             \"value\": \"Enabled\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'COS存储空间开启防盗链，视为“合规”。 COS存储空间未开启防盗链，视为“不合规”。', 1),
       ('8c18a7ec59b1ed455487ad97ee40262f', 'Redis实例开启密码认证', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_REDIS.no_password_access\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'Redis实例开启密码认证，视为“合规”。 Redis实例关闭密码认证，视为“不合规”。', 1),
       ('8c57b23760456077380f6250fae9be22', 'Redis实例满足指定带宽要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_REDIS.netLimit\",
             \"value\": 24,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        'Redis实例的可用带宽大于等于您设置的期望值，视为“合规”。 Redis实例的可用带宽小于您设置的期望值，视为“不合规”。', 1),
       ('8c846bf195a06e20ba92e6c0eacf9364', 'SLB实例状态为运行中', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_LOAD_BALANCER.loadBalancerStatus\",
             \"value\": \"active\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'SLB实例的运行状态为运行中，视为“合规”。 SLB实例的运行状态为已停止，视为“不合规”。', 1),
       ('8ea177cf2ddac989d02fff0cd56e949c', 'RAM用户开启MFA', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'RAM', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_RAM.loginProfile.mfabindRequired\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'RAM用户开启MFA，视为“合规”。', 1),
       ('8ea3414dce64b279120f9b5d261449f7', 'RDS实例存储空间满足最低要求-SQL Server',
        '5536b282b21ce92d9b44164ee0f95f50', 'fit2cloud_huawei_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_SQL_SERVER.volume.size\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('91db2857eb24e7b9c19d20854b755c24', 'COS存储空间ACL禁止公共读', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_tencent_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_OSS.access.cannedAccessControl\",
             \"value\": \"Private\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'COS存储空间的ACL策略禁止公共读，视为“合规”。 COS存储空间的ACL策略允许公共读，视为“不合规”。', 1),
       ('929567273ab13d2447e91d0ce6840c7a', '使用中的ECS数据磁盘已开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.status\",
             \"value\": \"in-use\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.bootable\",
             \"value\": \"true\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '使用中的ECS数据磁盘已开启加密，视为“合规”。', 1),
       ('93a222cb2879aa1785a48fc63578929b', 'RDS实例CPU核数满足最低要求-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SQL_SERVER.cpu\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('94e12a77c24c9873600dfa4ba8332453', '使用多可用区的RDS实例', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MYSQL.deployMode\",
             \"value\": 1,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例支持多个可用区，视为“合规”。 RDS实例支持一个可用区，视为“不合规”。', 1),
       ('9579ae3ab4375d3af916b5b24cfe1b4d', 'OpenStack ECS实例内存满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_openstack_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_openstack_platform_ECS.memory\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的内存大于等于您设置的期望值，视为“合规”。 ECS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('95c000a5114452b9eaf13b0f365ef524', '使用专有网络类型的MongoDB实例', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MONGO_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MONGO_DB.networkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，则检查MongoDB实例的网络类型为专有网络，视为“合规”；如果指定参数，则检查MongoDB实例的专有网络实例在指定参数范围内，视为“合规”。 如果未指定参数，则检查MongoDB实例的网络类型为经典网络，视为“不合规”；如果指定参数，则检查MongoDB实例的专有网络实例不在指定参数范围内，视为“不合规”。',
        1),
       ('963cc3d9baedc3987d2bd51887348268', 'ELB实例为独享型实例', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_huawei_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_LOAD_BALANCER.guaranteed\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'ELB实例为独享型实例，视为“合规”。 ELB实例不为独享型实例，视为“不合规”。', 1),
       ('96a9de5ebb5b4b3c199973b427cf9d65', 'RDS实例开启了SSL-SQL-Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_SQL_SERVER.enable_ssl\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例已开启SSL，视为“合规”。 RDS实例未开启SSL，视为“不合规”。', 1),
       ('98bce207759cc52f34b75a82f7ef6610', '弹性IP实例带宽满足最低要求', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_PUBLIC_IP.bandwidth\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '弹性公网IP实例的可用带宽大于等于您设置的参数，视为“合规”。 弹性公网IP实例的可用带宽小于您设置的参数，视为“不合规”。',
        1),
       ('99ce9f90237a323f801a1bef8bd96069', 'Redis实例满足指定内存容量要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_REDIS.capacity\",
             \"value\": 1024,
             \"compare\": \"LE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'Redis实例的内存容量大于等于您设置的参数，视为“合规”。 Redis实例的内存容量小于您设置的参数，视为“不合规”。',
        1),
       ('9c22021af2d94a7c218401eae7720a58', '不存在闲置的ECS数据磁盘', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.status\",
             \"value\": \"available\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '所有ECS数据磁盘均已挂载到ECS实例，视为“合规”。 一个或多个ECS数据磁盘未挂载到ECS实例，视为“不合规”。', 1),
       ('9debddcd6d8b736a9fe35411692c1dab', 'Redis实例满足指定带宽要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_REDIS.bandwidth\",
             \"value\": 10,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        'Redis实例的可用带宽大于等于您设置的期望值，视为“合规”。 Redis实例的可用带宽小于您设置的期望值，视为“不合规”。', 1),
       ('9ec821b3278b5550884b75d6ce6bba05', 'RDS实例未申请外网地址-MariaDB', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MARIA_DB.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_MARIA_DB.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', 1, 'RDS实例未申请外网地址，视为“合规”。', 1),
       ('9ecaf6d22fe4f779df243c62c4366752', 'ECS实例状态不是已停止状态', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.status\",
             \"value\": \"Stopped\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'ECS实例状态不是已停止状态', 1),
       ('9fd6a034574721c3d338ba9422bbe593', 'RDS实例未申请外网地址-MariaDB', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MARIA_DB.wanStatus\",
             \"value\": 0,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'RDS实例未申请外网地址，视为“合规”。 RDS实例已申请外网地址，视为“不合规”。', 1),
       ('a01ad3b41cf83d86cac0ebcd9d983da7', 'RDS实例内存满足最低要求-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_SQL_SERVER.mem\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('a157ab17a7ac0482308fe5d36d3ead6c', '使用专有网络类型的SLB实例', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_LOAD_BALANCER.networkType\",
             \"value\": \"vpc\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_LOAD_BALANCER.addressType\",
             \"value\": \"intranet\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', -1,
        '如果规则中未设置参数值，当SLB实例的网络类型为私网时，视为“合规”；如果规则中已设置参数值，且SLB实例绑定的VPC ID已在参数值中列举，视为“合规”。 如果规则中未设置参数值，当SLB实例的网络类型不为私网时，视为“不合规”；如果规则中已设置参数值，但SLB实例绑定的VPC ID没有在参数值中列举，视为“不合规”。',
        1),
       ('a93ab5a2b4a97a5c9263e9f7a5fae9bc', 'CVM实例的系统盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_DISK.encrypt\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'CVM实例的系统盘开启加密，视为“合规”。', 1),
       ('a93c063af9238d63af7c2a5d79e3e7b1', 'OpenStack 安全组出方向未设置为全通', '8162dc7f8c4ffa84e9b8f1edab28557c',
        'fit2cloud_openstack_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"egress\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.remote_ip_prefix.keyword\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.port_range_min.keyword\",
             \"value\": 1,
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_openstack_platform_SECURITY_GROUP_group_rule.port_range_max.keyword\",
             \"value\": 65535,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, '安全组出网方向未设置为全通，视为“合规”。 安全组出网方向设置为全通，视为“不合规”。', 1),
       ('aafefa48a8ee97ff476b5f6b5172baa4', 'ECS实例CPU核数满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ECS.vcpus\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的CPU核数大于等于您设置的期望值，视为“合规”。 ECS实例的CPU核数小于您设置的期望值，视为“不合规”。',
        1),
       ('ab4a9ec2b4fc7be2e8bf95d4631d59c5', 'ECS实例付费类型为包年包月', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ECS.metadata.charging_mode\",
             \"value\": \"1\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的付费类型为包年包月，视为“合规”。 ECS实例的付费类型非包年包月，视为“不合规”。', 1),
       ('ab53882ec23e0f793e66934cf09fcdcf', '按量付费的CVM已停机实例使用节省停机模式',
        '7d0f1173c4d681f81bb2243950c1b9c5', 'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.stopChargingMode\",
             \"value\": \"STOP_CHARGING\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.instanceChargeType\",
             \"value\": \"POSTPAID_BY_HOUR\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '按量付费的CVM实例停机时使用节省停机模式，视为“合规”。 按量付费的CVM实例停机时未使用节省停机模式，视为“不合规”。 包年包月的实例和运行中的实例不适用本规则，视为“不适用”。',
        1),
       ('ab8dc40af723281d66c7d1dc3465c371', 'RDS实例存储空间满足最低要求-MariaDB', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MARIA_DB.storage\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('b10d8fc2cacfe5ab888a921f446a436c', '安全组入网设置中不能有对所有端口开放的访问规则',
        '8162dc7f8c4ffa84e9b8f1edab28557c', 'fit2cloud_huawei_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"ingress\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.action.keyword\",
             \"value\": \"allow\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.multiport.keyword\",
             \"value\": \"1-65535\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '安全组入方向授权策略为允许，当端口范围未设置为1-65535时，视为“合规”； 安全组入方向授权策略为允许，当端口范围设置为1-65535，视为“不合规”。',
        1),
       ('b16bd94354a69287a48a49ef590913a5', 'RDS实例CPU核数满足最低要求-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_POST_GRE_SQL.dbinstanceCpu\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('b25e6f1a3bb2f431df263ebcae934da2', 'Redis实例未设置公网IP', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_REDIS.enable_publicip\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'Redis实例未设置公网IP地址，视为“合规”。 Redis实例已设置公网IP地址，视为“不合规”。', 1),
       ('b331a402f14547d0a45d292ea84e586a', 'ELB实例开启了删除保护', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_huawei_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_LOAD_BALANCER.deletion_protection_enable\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ELB实例开启了删除保护，视为“合规”； ELB实例未开启删除保护，视为“不合规”', 1),
       ('b64f33fe9c555009069615f0dd978831', '弹性公网IP开启删除保护', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_PUBLIC_IP.deletionProtection\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '弹性公网IP开启删除保护，视为“合规”。', 1),
       ('b8583b00f9de4f8d0ffc3c81c5ffae79', '检测闲置弹性公网IP', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_ali_platform', 'PUBLIC_IP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_PUBLIC_IP.status\",
             \"value\": \"Available\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '所有弹性公网IP均已绑定到ECS实例或NAT网关实例，视为“合规”。 一个或多个弹性公网IP未绑定到ECS实例或NAT网关实例，视为“不合规”。',
        1),
       ('bb8ff9be44427e4b000709c267312341', 'ECS数据磁盘开启加密', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.encrypted\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'ECS数据磁盘开启加密，视为“合规”。', 1),
       ('bba31027fc5d4048112a9988d5fd0c81', '分配了公网IP地址的ECS实例公网出带宽最大值小于指定值',
        '7d0f1173c4d681f81bb2243950c1b9c5', 'fit2cloud_ali_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_ECS.internetMaxBandwidthOut\",
             \"value\": 5,
             \"compare\": \"LE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '已分配公网IP地址的ECS实例的公网出带宽的最大值小于或等于指定值，视为“合规”。', 1),
       ('bc2558c09b717c11288b1ae2620f3210', '使用中的ECS数据磁盘已开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.status\",
             \"value\": \"In_use\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.encrypted\",
             \"value\": true,
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.type\",
             \"value\": \"data\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '使用中的ECS数据磁盘已开启加密，视为“合规”。', 1),
       ('bc8a9b8445436b932949fad5e40b8a79', 'ECS实例状态不是已停止状态', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ECS.status\",
             \"value\": \"SHUTOFF\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ECS.status\",
             \"value\": \"ERROR\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ECS.status\",
             \"value\": \"UNKNOWN\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', -1, 'ECS实例的状态为运行中，视为“合规”。 ECS实例的状态为已停止，视为“不合规”。', 1),
       ('bf37110d036734ddb84517a8a48e670f', 'MongoDB实例未被锁定', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MONGO_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MONGO_DB.lockMode\",
             \"value\": \"Unlock\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'MongoDB实例为未锁定状态，视为“合规”。 MongoDB实例为锁定状态，视为“不合规”。', 1),
       ('c1c425d1955c4ccb0fd1b8a2cc3b2732', '使用专有网络类型的MongoDB实例', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MARIA_DB.instanceNetworkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，则检查MongoDB实例的网络类型为专有网络，视为“合规”；如果指定参数，则检查MongoDB实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('c4198fd37c5179e18169d29d5f43c122', 'CLB实例未绑定公网IP', '2fc5bf68ba7ce91112ba0ec3a73062b4',
        'fit2cloud_tencent_platform', 'LOAD_BALANCER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_LOAD_BALANCER.loadBalancerType\",
             \"value\": \"INTERNAL\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'CLB实例网络类型为内网属性，视为“合规”。 CLB实例网络类型不为内网属性，视为“不合规”。', 1),
       ('c4dc375dad4e6550d6428a68142dfad5', 'RDS实例开启删除保护-MySQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MYSQL.deletionProtection\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例开启删除保护，视为“合规”。 RDS实例未开启删除保护，视为“不合规”。', 1),
       ('c547cf24c23801ee2e61134bfe6dcf57', '使用多可用区的RDS实例-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SQL_SERVER.model\",
             \"value\": 2,
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例支持多个可用区，视为“合规”。 RDS实例支持一个可用区，视为“不合规”。', 1),
       ('c93aa35c91c9e0712fd1f97f72f4fbf4', '待挂载的ECS数据磁盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.status\",
             \"value\": \"available\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.bootable\",
             \"value\": \"true\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '待挂载的ECS数据磁盘已开启加密，视为“合规”。', 1),
       ('c9da6d04e598ecc77dfad17c8170ddbd', 'CVM实例CPU核数满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.cpu\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'CVM实例CPU核数满足最低要求，视为“合规”。', 1),
       ('cbbb2b59c16605d03afaed33241d5a21', 'Elasticsearch实例数据节点开启云盘加密', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'ELASTIC_SEARCH', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ELASTIC_SEARCH.diskEncrypted\",
             \"value\": true,
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        'Elasticsearch实例数据节点已开启云盘加密，视为“合规”。 Elasticsearch实例数据节点未开启云盘加密，视为“不合规”。', 1),
       ('cca04ab28dd456dab67e38fda48e5dfa', 'OSS存储空间ACL禁止公共读', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.acl.accessControlList.grant\",
             \"value\": \"private\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'OSS存储空间的ACL策略禁止公共读，视为“合规”。', 1),
       ('d0b78eb0f39661b73730866b19525b1d', 'VMware vSphere Datastore 可用空间扫描', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_vsphere_platform', 'DATA_STORE', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_DATA_STORE.freeSpace\",
             \"value\": 300,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'VMware vSphere 检测账号下 Datastore 可用空间是否满足条件，满足视为“合规”，否则属于“不合规”', 1),
       ('d3c7771d17d7b57f5495d6e3a5341ffa', 'OSS存储空间开启服务端默认加密', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.encryption.applyServerSideEncryptionByDefault.ssealgorithm\",
             \"value\": \"AES256\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        'OSS存储空间开启服务端的OSS完全托管加密，视为“合规”。 OSS存储空间未开启服务端的OSS完全托管加密，视为“不合规”。', 1),
       ('d468a1fb38c85f1da1bb3f57b6373bab', 'ECS实例内存满足最低要求', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ECS.ram\",
             \"value\": 2048,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'ECS实例的内存大于等于您设置的期望值，视为“合规”。 ECS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('d4c50b59acf90dc3ade1edcc6ed19b58', 'Elasticsearch实例已设置通信加密', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'ELASTIC_SEARCH', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_ELASTIC_SEARCH.httpsEnable\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'Elasticsearch实例已设置通信加密，视为“合规”； Elasticsearch实例未设置通信加密，视为“不合规”。', 1),
       ('d564918d065e44b9ad09ab99516e02dd', 'RDS实例开启了SSL-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_POST_GRE_SQL.enable_ssl\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'RDS实例已开启SSL，视为“合规”。 RDS实例未开启SSL，视为“不合规”。', 1),
       ('dcfd3343dd8ccc8eda7a4d56fd15e374', '安全组出方向未设置为全通', '8162dc7f8c4ffa84e9b8f1edab28557c',
        'fit2cloud_tencent_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SECURITY_GROUP.rule.egress.cidrBlock\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"CONTAIN\"
           },
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SECURITY_GROUP.rule.egress.protocol\",
             \"value\": \"ALL\",
             \"compare\": \"CONTAIN\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '安全组出网方向未设置为全通，视为“合规”。 安全组出网方向设置为全通，视为“不合规”。 除CVM外的云服务（例如：云防火墙、NAT网关等）或虚商所使用的安全组不适用本规则，视为“不适用”。',
        1),
       ('ddb88deec319b4164a51242f0db0a654', '使用多可用区的RDS实例-MariaDB', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MARIA_DB.mutriORsignle\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例支持多个可用区，视为“合规”。 RDS实例支持一个可用区，视为“不合规”。', 1),
       ('df7ad77b9b2c6aeef076bb4cd059e969', 'Redis实例设置SSL加密', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_REDIS.enable_ssl\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'Redis实例已设置SSL加密，视为“合规”。 Redis实例未设置SSL加密，视为“不合规”。', 1),
       ('e183cad94998e0aa3ad3c70417bfaa0c', 'RDS实例内存满足最低要求-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_POST_GRE_SQL.mem\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('e38a9f55d564d4420fc99afff759d936', 'CVM数据磁盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_DISK.encrypt\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'CVM数据磁盘开启加密，视为“合规”。 CVM数据磁盘未开启加密，视为“不合规”。', 1),
       ('e5c3e9e1f69929ff2eb032a21a9f0f05', '使用高可用（主备）的RDS实例', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_MYSQL.type\",
             \"value\": \"Single\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例为主备实例，视为“合规”。 RDS实例为单机实例，视为“不合规”。', 1),
       ('e6c2bc90133b7f77cbd2546f65ee5a37', 'RDS实例存储空间满足最低要求-PostgreSQL',
        '5536b282b21ce92d9b44164ee0f95f50', 'fit2cloud_huawei_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_POST_GRE_SQL.volume.size\",
             \"value\": 100,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的存储空间大于等于您设置的参数，视为“合规”。 RDS实例的存储空间小于您设置的参数，视为“不合规”。', 1),
       ('e7e52803557c4a8fc74ebcb341c7de4b', '使用多可用区的RDS实例', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MYSQL.mutriORsignle\",
             \"value\": true,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'RDS实例支持多个可用区，视为“合规”。', 1),
       ('e7e5e0e05a8325e882197ccd506485d6', 'CVM实例状态不是已停止状态', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_tencent_platform', 'ECS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.instanceState\",
             \"value\": \"STOPPED\",
             \"compare\": \"NOT_EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_tencent_platform_ECS.instanceState\",
             \"value\": \"SHUTDOWN\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'CVM实例的状态为运行中，视为“合规”。 CVM实例的状态为已停止，视为“不合规”。', 1),
       ('ec70cc45f23da8c0a5d79c199dafeffc', 'VMware vSphere ResourcePool CPU内存扫描',
        '7d0f1173c4d681f81bb2243950c1b9c5', 'fit2cloud_vsphere_platform', 'RESOURCE_POOL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_vsphere_platform_RESOURCE_POOL.totalMemory\",
             \"value\": 1,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'VMware vSphere 检测账号下 ResourcePool CPU内存是否分配，分配视为“合规”，否则属于“不合规”', 1),
       ('ed8f89230ce8d6428e100b6777447e4b', '使用数据库代理模式访问SQL Server', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_SQL_SERVER.connectionMode\",
             \"value\": \"Safe\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的SQL Server类型数据库的访问模式为代理模式，视为“合规”。', 1),
       ('edbe4df71ebe50e0b7da5a5213271394', 'RDS实例未申请外网地址-MySQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_ali_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MYSQL.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_MYSQL.networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype\",
             \"value\": \"Public\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', 1, 'RDS实例未申请外网地址，视为“合规”。 RDS实例已申请外网地址，视为“不合规”。', 1),
       ('eddbca5b3b6d818655a5f50c70b40f9d', 'Redis实例开启密码认证', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'REDIS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_REDIS.noAuth\",
             \"value\": false,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'Redis实例开启密码认证，视为“合规”。 Redis实例未开启密码认证，视为“不合规”。', 1),
       ('ef6cd9c119714f145f87858d5ca4f13b', 'RDS实例内存满足最低要求', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_MYSQL.mem\",
             \"value\": 4,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('ef8ec3caf111c8bc2fc131d5196f22a4', 'RDS实例CPU核数满足最低要求-MariaDB', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MARIA_DB', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MARIA_DB.cpu\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('f1de4d699ede5d529976705445087221', 'ECS数据磁盘开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_huawei_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_DISK.bootable\",
             \"value\": \"true\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 0, 'ECS数据磁盘开启加密，视为“合规”。 ECS数据磁盘未开启加密，视为“不合规”。', 1),
       ('f2615aec9acbf990045b399a2c0b1491', 'RDS实例未申请外网地址-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_POST_GRE_SQL.dbinstanceNetInfo.netType\",
             \"value\": \"public\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'RDS实例未申请外网地址，视为“合规”。 RDS实例已申请外网地址，视为“不合规”。', 1),
       ('f5b028f65a971cf47f9fd51315fa6850', 'RDS实例未申请外网地址', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_MYSQL.wanStatus\",
             \"value\": 0,
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'RDS实例未申请外网地址，视为“合规”。 RDS实例已申请外网地址，视为“不合规”。', 1),
       ('f80620ff7fc726a2e09bb4aa6879ceb7', '安全组出方向未设置为全通', '8162dc7f8c4ffa84e9b8f1edab28557c',
        'fit2cloud_huawei_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.direction.keyword\",
             \"value\": \"egress\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.action.keyword\",
             \"value\": \"allow\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.remote_ip_prefix.keyword\",
             \"value\": \"0.0.0.0/0\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"filterArray.fit2cloud_huawei_platform_SECURITY_GROUP_group_rule.multiport.keyword\",
             \"value\": \"1-65535\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '安全组出网方向未设置为全通，视为“合规”。 安全组出网方向设置为全通，视为“不合规”。', 1),
       ('f82d74f5c6524c133e0295a8f4be40b1', 'COS存储空间开启服务端默认加密', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_tencent_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_OSS.encryption.rule.applyServerSideEncryptionByDefault.sseAlgorithm\",
             \"value\": \"KMS\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_tencent_platform_OSS.encryption.rule.applyServerSideEncryptionByDefault.sseAlgorithm\",
             \"value\": \"AES256\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"NOT_COMPLIANCE\",
         \"conditionType\": \"OR\"
       }', 1, 'COS存储空间开启服务端加密，视为“合规”。 COS存储空间未开启服务端加密，视为“不合规”。', 1),
       ('f8a961b6c3ab6f692224befe96354ed9', '推荐使用专有网络类型的RDS实例-PostGreSql',
        'a46779e112b01a0f20b13cfbd73b8400', 'fit2cloud_ali_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_POST_GRE_SQL.instanceNetworkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，检查RDS实例的网络类型为专有网络，视为“合规”；如果指定参数，检查RDS实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('f958ca8df216ad3cd5caf98286a5e723', 'RDS实例内存满足最低要求-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_tencent_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_POST_GRE_SQL.dbinstanceMemory\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的内存大于等于您设置的期望值，视为“合规”。 RDS实例的内存小于您设置的期望值，视为“不合规”。', 1),
       ('f9bfb58205ac1c16151762c6a7a598d1', 'RDS实例CPU核数满足最低要求-SQL Server', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'SQL_SERVER', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_SQL_SERVER.cpu\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('fab47c3fac3fb6f0c3508e4cad4a0342', 'RDS实例CPU核数满足最低要求-PostgreSQL', '5536b282b21ce92d9b44164ee0f95f50',
        'fit2cloud_huawei_platform', 'POST_GRE_SQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_huawei_platform_POST_GRE_SQL.cpu\",
             \"value\": 2,
             \"compare\": \"GE\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, 'RDS实例的CPU核数大于等于您设置的参数，视为“合规”。 RDS实例的CPU核数小于您设置的参数，视为“不合规”。', 1),
       ('fde1fb3796ac8dad1b68ffd561fd8bdc', '安全组入网设置不能有对所有协议开放的访问规则',
        '8162dc7f8c4ffa84e9b8f1edab28557c', 'fit2cloud_tencent_platform', 'SECURITY_GROUP', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_tencent_platform_SECURITY_GROUP.rule.ingress.protocol\",
             \"value\": \"ALL\",
             \"compare\": \"NOT_EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1,
        '安全组入方向授权策略为允许，当端口的协议类型未设置为ALL时，视为“合规”；如果端口的协议类型设置为ALL，但被优先级更高的授权策略拒绝，视为“合规”。 安全组入方向授权策略为允许，当端口的协议类型设置为ALL，且未被优先级更高的授权策略拒绝时，视为“不合规”。关于如何修正该问题，请参见修正指导。 除CVM外的云服务（例如：云防火墙、NAT网关等）或虚商所使用的安全组不适用本规则，视为“不适用”。',
        1),
       ('fe3a3ce5694f58eed2388977777e3819', 'OSS存储空间ACL禁止公共读写', '359876ddb3819c94258d63e7823646b5',
        'fit2cloud_ali_platform', 'OSS', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_OSS.acl.accessControlList.grant\",
             \"value\": \"private\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1, 'OSS存储空间的ACL策略为私有，视为“合规”。 OSS存储空间的ACL策略为公共读或公共读写，视为“不合规”。', 1),
       ('ff9b18d68d70fa83d370a4a941f2e71a', '使用专有网络类型的RDS实例-Mysql', 'a46779e112b01a0f20b13cfbd73b8400',
        'fit2cloud_ali_platform', 'MYSQL', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_MYSQL.instanceNetworkType\",
             \"value\": \"VPC\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', 1,
        '如果未指定参数，检查RDS实例的网络类型为专有网络，视为“合规”；如果指定参数，检查RDS实例的专有网络实例在指定参数范围内，视为“合规”。',
        1),
       ('ffee74e42acc308453ff11e716358cb7', '待挂载的ECS数据磁盘已开启加密', '7d0f1173c4d681f81bb2243950c1b9c5',
        'fit2cloud_ali_platform', 'DISK', '{
         \"rules\": [
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.status\",
             \"value\": \"Available\",
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.encrypted\",
             \"value\": true,
             \"compare\": \"EQ\"
           },
           {
             \"field\": \"instance.fit2cloud_ali_platform_DISK.type\",
             \"value\": \"data\",
             \"compare\": \"EQ\"
           }
         ],
         \"scanRule\": \"COMPLIANCE\",
         \"conditionType\": \"AND\"
       }', -1, '待挂载的ECS数据磁盘已开启加密，视为“合规”。', 1);


SET SESSION innodb_lock_wait_timeout = DEFAULT;
