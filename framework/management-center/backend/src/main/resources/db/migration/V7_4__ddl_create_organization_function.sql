SET SESSION innodb_lock_wait_timeout = 7200;

DELIMITER //
CREATE FUNCTION GET_ROOT_ORG_ID(org_id VARCHAR(200)) RETURNS VARCHAR(200)
BEGIN
    DECLARE rootid VARCHAR(200);
    DECLARE sid VARCHAR(200);
    SET sid = org_id;
    WHILE sid IS NOT NULL
        DO
            SET rootid = sid;
            SELECT pid INTO sid FROM `organization` WHERE id = sid;
        END WHILE;
    return rootid;
END//
DELIMITER ;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
