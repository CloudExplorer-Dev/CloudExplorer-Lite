SET SESSION innodb_lock_wait_timeout = 7200;

CREATE OR REPLACE VIEW `org_workspace` AS
SELECT `organization`.`id`   AS `id`,
       `organization`.`name` AS `NAME`,
       `organization`.`id`   AS `organization_id`,
       `organization`.`name` AS `organization_name`,
       `organization`.`pid`  AS `pid`,
       'org'                 AS `type`
FROM `organization`
UNION
SELECT `workspace`.`id`              AS `id`,
       `workspace`.`_name`           AS `_name`,
       `workspace`.`organization_id` AS `organization_id`,
       `organization`.`name`         AS `organization_name`,
       `workspace`.`organization_id` AS `pid`,
       'workspace'                   AS `workspace`
FROM (`workspace` LEFT JOIN `organization`
      ON (`workspace`.`organization_id` = `organization`.`id`));

SET SESSION innodb_lock_wait_timeout = DEFAULT;
