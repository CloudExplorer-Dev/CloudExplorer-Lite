SET SESSION innodb_lock_wait_timeout = 7200;

DELIMITER //
CREATE FUNCTION GET_CHILDREN_ID(orgid VARCHAR(8000)) RETURNS VARCHAR(8000)
BEGIN
    DECLARE oTemp VARCHAR(8000);
    DECLARE oTempChild VARCHAR(8000);
    SET oTemp = '';
    SET oTempChild = CAST(orgid AS CHAR);
    WHILE oTempChild IS NOT NULL
        DO
            SET oTemp = CONCAT(oTemp, ',', oTempChild);
            SELECT GROUP_CONCAT(id) INTO oTempChild FROM `organization` WHERE FIND_IN_SET(pid, oTempChild) > 0;
        END WHILE;
    RETURN oTemp;
END//
DELIMITER ;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
