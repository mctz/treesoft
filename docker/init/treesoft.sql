/*
 Navicat Premium Data Transfer

 Source Server         : didi-tree
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 116.85.24.154:3316
 Source Schema         : treesoft

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 06/04/2022 11:16:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dms_config
-- ----------------------------
DROP TABLE IF EXISTS `dms_config`;
CREATE TABLE `dms_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `updateDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isdefault` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `databaseType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `valid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `license` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `driver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `databaseName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `userName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_config
-- ----------------------------
INSERT INTO `dms_config` VALUES (1, '2018-10-21 19:00:30', NULL, 'mysql', '1', 'MySql', NULL, NULL, NULL, '3306', NULL, '172.88.88.11', NULL, 'treesoft', 'jdbc:mysql://172.88.88.11:3306/treesoft', 'treesoft', 'dHJlZXNvZnRgdHJlZXNvZnQxMjM=');

-- ----------------------------
-- Table structure for dms_data_synchronize
-- ----------------------------
DROP TABLE IF EXISTS `dms_data_synchronize`;
CREATE TABLE `dms_data_synchronize`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `updateDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `souceConfig_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `souceDataBase` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doSql` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `targetConfig_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `targetDataBase` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `targetTable` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `updateUser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qualification` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_data_synchronize
-- ----------------------------

-- ----------------------------
-- Table structure for dms_data_synchronize_log
-- ----------------------------
DROP TABLE IF EXISTS `dms_data_synchronize_log`;
CREATE TABLE `dms_data_synchronize_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data_synchronize_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_data_synchronize_log
-- ----------------------------

-- ----------------------------
-- Table structure for dms_license
-- ----------------------------
DROP TABLE IF EXISTS `dms_license`;
CREATE TABLE `dms_license`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `personNumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `license` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mess` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_license
-- ----------------------------

-- ----------------------------
-- Table structure for dms_log
-- ----------------------------
DROP TABLE IF EXISTS `dms_log`;
CREATE TABLE `dms_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `log` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_log
-- ----------------------------

-- ----------------------------
-- Table structure for dms_searchhistory
-- ----------------------------
DROP TABLE IF EXISTS `dms_searchhistory`;
CREATE TABLE `dms_searchhistory`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` date NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sqls` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_searchhistory
-- ----------------------------
INSERT INTO `dms_searchhistory` VALUES (1, '2018-10-21', 'dual', '1', 'SELECT 1 from DUAL', '2');

-- ----------------------------
-- Table structure for dms_study
-- ----------------------------
DROP TABLE IF EXISTS `dms_study`;
CREATE TABLE `dms_study`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sort` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pid` int(11) NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_study
-- ----------------------------
INSERT INTO `dms_study` VALUES (1, 'icon-berlin-billing', NULL, 'SQL', NULL, 0, NULL);
INSERT INTO `dms_study` VALUES (2, 'icon-berlin-calendar', NULL, 'å¸¸ç”¨SQL', NULL, 1, NULL);
INSERT INTO `dms_study` VALUES (3, 'icon-berlin-project', NULL, 'select', 'SELECT col_name,...\n  FROM table_name\n WHERE where_condition\n GROUP BY col_name,... \nHAVING where_condition\n ORDER BY col_name,...\n LIMIT offset,row_count', 2, NULL);
INSERT INTO `dms_study` VALUES (4, 'icon-berlin-project', NULL, 'insert', 'INSERT INTO table_name(col_name,...) values(expr,...)', 2, NULL);
INSERT INTO `dms_study` VALUES (5, 'icon-berlin-project', NULL, 'update', 'UPDATE table_name SET col_name=expr,... WHERE where_condition\n', 2, NULL);
INSERT INTO `dms_study` VALUES (6, 'icon-berlin-project', NULL, 'delete', 'DELETE FROM table_name WHERE where_condition', 2, NULL);
INSERT INTO `dms_study` VALUES (7, 'icon-berlin-project', NULL, 'replace', 'REPLACE INTO table_name(col_name,...) values(expr,...)', 2, NULL);
INSERT INTO `dms_study` VALUES (8, 'icon-berlin-calendar', NULL, 'è¡¨/ç´¢å¼•', NULL, 1, NULL);
INSERT INTO `dms_study` VALUES (9, 'icon-berlin-project', NULL, 'alter table', 'ALTER [ONLINE | OFFLINE] [IGNORE] TABLE tbl_name\n    alter_specification [, alter_specification] ...\n\nalter_specification:\n    table_option ...\n  | ADD [COLUMN] col_name column_definition\n        [FIRST | AFTER col_name ]\n  | ADD [COLUMN] (col_name column_definition,...)\n  | ADD {INDEX|KEY} [index_name]\n        [index_type] (index_col_name,...) [index_option] ...\n  | ADD [CONSTRAINT [symbol]] PRIMARY KEY\n        [index_type] (index_col_name,...) [index_option] ...\n  | ADD [CONSTRAINT [symbol]]\n        UNIQUE [INDEX|KEY] [index_name]\n        [index_type] (index_col_name,...) [index_option] ...\n  | ADD FULLTEXT [INDEX|KEY] [index_name]\n        (index_col_name,...) [index_option] ...\n  | ADD SPATIAL [INDEX|KEY] [index_name]\n        (index_col_name,...) [index_option] ...\n  | ADD [CONSTRAINT [symbol]]\n        FOREIGN KEY [index_name] (index_col_name,...)\n        reference_definition\n  | ALTER [COLUMN] col_name {SET DEFAULT literal | DROP DEFAULT}\n  | CHANGE [COLUMN] old_col_name new_col_name column_definition\n        [FIRST|AFTER col_name]\n  | MODIFY [COLUMN] col_name column_definition\n        [FIRST | AFTER col_name]\n  | DROP [COLUMN] col_name\n  | DROP PRIMARY KEY\n  | DROP {INDEX|KEY} index_name\n  | DROP FOREIGN KEY fk_symbol\n  | DISABLE KEYS\n  | ENABLE KEYS\n  | RENAME [TO] new_tbl_name\n  | ORDER BY col_name [, col_name] ...\n  | CONVERT TO CHARACTER SET charset_name [COLLATE collation_name]\n  | [DEFAULT] CHARACTER SET [=] charset_name [COLLATE [=] collation_name]\n  | DISCARD TABLESPACE\n  | IMPORT TABLESPACE\n  | partition_options\n  | ADD PARTITION (partition_definition)\n  | DROP PARTITION partition_names\n  | COALESCE PARTITION number\n  | REORGANIZE PARTITION partition_names INTO (partition_definitions)\n  | ANALYZE PARTITION partition_names\n  | CHECK PARTITION partition_names\n  | OPTIMIZE PARTITION partition_names\n  | REBUILD PARTITION partition_names\n  | REPAIR PARTITION partition_names\n  | REMOVE PARTITIONING\n\nindex_col_name:\n    col_name [(length)] [ASC | DESC]\n\nindex_type:\n    USING {BTREE | HASH | RTREE}\n\nindex_option:\n    KEY_BLOCK_SIZE [=] value\n  | index_type\n  | WITH PARSER parser_name\n  | COMMENT \'string\'', 8, NULL);
INSERT INTO `dms_study` VALUES (10, 'icon-berlin-project', NULL, 'create table', 'CREATE TABLE tbl_name\n(\ncol_name data_type NOT NULL DEFAULT default_value AUTO_INCREMENT COMMENT \'string\',\n...\nKEY index_name index_type (index_col_name,...),\n...\nPRIMARY KEY(index_col_name,...),\nUNIQUE KEY(index_col_name,...)\n) ENGINE=engine_name CHARACTER SET=charset_name COMMENT=\'string\'', 8, NULL);
INSERT INTO `dms_study` VALUES (11, 'icon-berlin-project', NULL, 'create index', 'CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name\n    [USING index_type]\n    ON tbl_name (index_col_name,...)\n \nindex_col_name:\n    col_name [(length)] [ASC | DESC]\n', 8, NULL);
INSERT INTO `dms_study` VALUES (12, 'icon-berlin-project', NULL, 'drop table', 'DROP [TEMPORARY] TABLE [IF EXISTS]\n    tbl_name [, tbl_name] ...\n    [RESTRICT | CASCADE]\n', 8, NULL);
INSERT INTO `dms_study` VALUES (13, 'icon-berlin-project', NULL, 'drop index', 'DROP INDEX index_name ON tbl_name', 8, NULL);
INSERT INTO `dms_study` VALUES (14, 'icon-berlin-project', NULL, 'rename table', 'RENAME TABLE tbl_name TO new_tbl_name\n', 8, NULL);
INSERT INTO `dms_study` VALUES (15, 'icon-berlin-project', NULL, 'truncate table', 'TRUNCATE [TABLE] tbl_name', 8, NULL);
INSERT INTO `dms_study` VALUES (16, 'icon-berlin-calendar', NULL, 'è§†å›¾', NULL, 1, NULL);
INSERT INTO `dms_study` VALUES (17, 'icon-berlin-project', NULL, 'create view', 'CREATE [OR REPLACE] [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]\n    VIEW view_name [(column_list)]\n    AS select_statement\n    [WITH [CASCADED | LOCAL] CHECK OPTION]\n', 16, NULL);
INSERT INTO `dms_study` VALUES (18, 'icon-berlin-project', NULL, 'alter view', 'ALTER\n    [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]\n    [DEFINER = { user | CURRENT_USER }]\n    [SQL SECURITY { DEFINER | INVOKER }]\n    VIEW view_name [(column_list)]\n    AS select_statement\n    [WITH [CASCADED | LOCAL] CHECK OPTION]', 16, NULL);
INSERT INTO `dms_study` VALUES (19, 'icon-berlin-project', NULL, 'drop view', 'DROP VIEW [IF EXISTS]\n    view_name [, view_name] ...\n    [RESTRICT | CASCADE]\n', 16, NULL);
INSERT INTO `dms_study` VALUES (20, 'icon-berlin-calendar', NULL, 'å‡½æ•°/å­˜å‚¨è¿‡ç¨‹', NULL, 1, NULL);
INSERT INTO `dms_study` VALUES (21, 'icon-berlin-project', NULL, 'alter function', 'ALTER FUNCTION sp_name [characteristic ...]\n \ncharacteristic:\n    { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'', 20, NULL);
INSERT INTO `dms_study` VALUES (22, 'icon-berlin-project', NULL, 'alter procedure', 'ALTER PROCEDURE sp_name [characteristic ...]\n \ncharacteristic:\n    { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'\n', 20, NULL);
INSERT INTO `dms_study` VALUES (23, 'icon-berlin-project', NULL, 'call procedure', 'CALL sp_name([parameter[,...]])', 20, NULL);
INSERT INTO `dms_study` VALUES (24, 'icon-berlin-project', NULL, 'create function', 'CREATE FUNCTION sp_name ([func_parameter[,...]])\n    RETURNS type\n    [characteristic ...]\n routine_body\n\ncharacteristic:\n    LANGUAGE SQL\n  | [NOT] DETERMINISTIC\n  | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'\n', 20, NULL);
INSERT INTO `dms_study` VALUES (25, 'icon-berlin-project', NULL, 'create procedure', 'CREATE PROCEDURE sp_name ([proc_parameter[,...]])\n    [characteristic ...]\n routine_body\ncharacteristic:\n    LANGUAGE SQL\n  | [NOT] DETERMINISTIC\n  | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'\n', 20, NULL);
INSERT INTO `dms_study` VALUES (26, 'icon-berlin-project', NULL, 'drop function', 'DROP FUNCTION [IF EXISTS] sp_name', 20, NULL);
INSERT INTO `dms_study` VALUES (27, 'icon-berlin-project', NULL, 'drop procedure', 'DROP PROCEDURE [IF EXISTS] sp_name', 20, NULL);
INSERT INTO `dms_study` VALUES (28, 'icon-berlin-project', NULL, 'MongoDB shellå‘½ä»¤', NULL, 1, NULL);
INSERT INTO `dms_study` VALUES (29, 'icon-berlin-project', NULL, 'åˆ›å»ºæ•°æ®åº“use', 'use <databaseName> ', 28, NULL);
INSERT INTO `dms_study` VALUES (30, 'icon-berlin-project', NULL, 'åˆ›å»ºæ•°æ®é›†åˆdb.createCollection', 'db.createCollection(<collectionName>)', 28, NULL);
INSERT INTO `dms_study` VALUES (31, 'icon-berlin-project', NULL, 'æŸ¥çœ‹æ–‡æ¡£db.users.find()', 'db.users.find({})', 28, NULL);
INSERT INTO `dms_study` VALUES (32, 'icon-berlin-project', NULL, 'æ’å…¥æ•°æ®db.users.insert()', 'db.users.insert(<data>)', 28, NULL);
INSERT INTO `dms_study` VALUES (33, 'icon-berlin-project', NULL, 'åˆ é™¤æ•°æ®db.users.remove()', 'db.users.remove({})', 28, NULL);
INSERT INTO `dms_study` VALUES (34, 'icon-berlin-project', NULL, 'æ›´æ–°æ•°æ®db.users.update()', 'db.users.update(query,update)', 28, NULL);
INSERT INTO `dms_study` VALUES (35, 'icon-berlin-calendar', NULL, 'Oracleå¸¸ç”¨æŸ¥è¯¢', NULL, 1, NULL);
INSERT INTO `dms_study` VALUES (36, 'icon-berlin-project', NULL, 'æŸ¥çœ‹è¿žæŽ¥ç”¨æˆ·ä¿¡æ¯', 'SELECT s.Osuser Os_User_Name,Decode(Sign(48 - Command),1,To_Char(Command),\r\n\'Action Code #\' || To_Char(Command)) Action,\r\np.Program Oracle_Process, Status Session_Status, s.Terminal Terminal,\r\ns.Program Program, s.Username User_Name,\r\ns.Fixed_Table_Sequence Activity_Meter, \'\' Query, 0 Memory,\r\n0 Max_Memory, 0 Cpu_Usage, s.Sid, s.Serial# Serial_Num\r\nFROM V$session s, V$process p\r\nWHERE s.Paddr = p.Addr\r\nAND s.TYPE = \'USER\'\r\nORDER BY s.Username, s.Osuser', 35, NULL);
INSERT INTO `dms_study` VALUES (37, 'icon-berlin-project', NULL, 'é«˜é€Ÿç¼“å†²åŒºå‘½ä¸­çŽ‡', 'select 1 - sum(decode(name, \'physical reads\', value, 0)) /\r\n(sum(decode(name, \'db block gets\', value, 0)) +\r\nsum(decode(name, \'consistent gets\', value, 0))) hit_ratio\r\nfrom v$sysstat t\r\nwhere name in (\'physical reads\', \'db block gets\', \'consistent gets\')', 35, NULL);
INSERT INTO `dms_study` VALUES (38, 'icon-berlin-project', NULL, 'æŸ¥çœ‹è¡¨ç©ºé—´ä½¿ç”¨æƒ…å†µ', 'SELECT Upper(F.TABLESPACE_NAME)         \"è¡¨ç©ºé—´å\",\r\n       D.TOT_GROOTTE_MB                 \"è¡¨ç©ºé—´å¤§å°(M)\",\r\n       D.TOT_GROOTTE_MB - F.TOTAL_BYTES \"å·²ä½¿ç”¨ç©ºé—´(M)\",\r\n       To_char(Round(( D.TOT_GROOTTE_MB - F.TOTAL_BYTES ) / D.TOT_GROOTTE_MB * 100, 2), \'990.99\')\r\n       || \'%\'                           \"ä½¿ç”¨æ¯”\",\r\n       F.TOTAL_BYTES                    \"ç©ºé—²ç©ºé—´(M)\",\r\n       F.MAX_BYTES                      \"æœ€å¤§å—(M)\"\r\nFROM   (SELECT TABLESPACE_NAME,\r\n               Round(Sum(BYTES) / ( 1024 * 1024 ), 2) TOTAL_BYTES,\r\n               Round(Max(BYTES) / ( 1024 * 1024 ), 2) MAX_BYTES\r\n        FROM   SYS.DBA_FREE_SPACE\r\n        GROUP  BY TABLESPACE_NAME) F,\r\n       (SELECT DD.TABLESPACE_NAME,\r\n               Round(Sum(DD.BYTES) / ( 1024 * 1024 ), 2) TOT_GROOTTE_MB\r\n        FROM   SYS.DBA_DATA_FILES DD\r\n        GROUP  BY DD.TABLESPACE_NAME) D\r\nWHERE  D.TABLESPACE_NAME = F.TABLESPACE_NAME\r\nORDER  BY 1', 35, NULL);
INSERT INTO `dms_study` VALUES (39, 'icon-berlin-project', NULL, 'å…±äº«æ± çš„å‘½ä¸­çŽ‡', 'select sum(pinhits)/sum(pins)*100 \"hit radio\" from v$librarycache;', 35, NULL);
INSERT INTO `dms_study` VALUES (40, 'icon-berlin-project', NULL, 'æŸ¥çœ‹è¿˜æ²¡æäº¤çš„äº‹åŠ¡', 'select * from v$locked_object;', 35, NULL);
INSERT INTO `dms_study` VALUES (41, 'icon-berlin-project', NULL, 'è€—èµ„æºçš„è¿›ç¨‹', 'select s.schemaname schema_name, decode(sign(48 - command), 1,\r\nto_char(command), \'Action Code #\' || to_char(command) ) action, status\r\nsession_status, s.osuser os_user_name, s.sid, p.spid , s.serial# serial_num,\r\nnvl(s.username, \'[Oracle process]\') user_name, s.terminal terminal,\r\ns.program program, st.value criteria_value from v$sesstat st, v$session s , v$process p\r\nwhere st.sid = s.sid and st.statistic# = to_number(\'38\') and (\'ALL\' = \'ALL\'\r\nor s.status = \'ALL\') and p.addr = s.paddr order by st.value desc, p.spid asc, s.username asc, s.osuser asc', 35, NULL);
INSERT INTO `dms_study` VALUES (42, 'icon-berlin-project', NULL, 'æŸ¥çœ‹é”ï¼ˆlockï¼‰æƒ…å†µ', 'select /* RULE */ ls.osuser os_user_name, ls.username user_name,\r\ndecode(ls.type, \'RW\', \'Row wait enqueue lock\', \'TM\', \'DML enqueue lock\', \'TX\',\r\n\'Transaction enqueue lock\', \'UL\', \'User supplied lock\') lock_type,\r\no.object_name object, decode(ls.lmode, 1, null, 2, \'Row Share\', 3,\r\n\'Row Exclusive\', 4, \'Share\', 5, \'Share Row Exclusive\', 6, \'Exclusive\', null)\r\nlock_mode, o.owner, ls.sid, ls.serial# serial_num, ls.id1, ls.id2\r\nfrom sys.dba_objects o, ( select s.osuser, s.username, l.type,\r\nl.lmode, s.sid, s.serial#, l.id1, l.id2 from v$session s,\r\nv$lock l where s.sid = l.sid ) ls where o.object_id = ls.id1 and o.owner\r\n<> \'SYS\' order by o.owner, o.object_name', 35, NULL);
INSERT INTO `dms_study` VALUES (43, 'icon-berlin-project', NULL, 'æŸ¥çœ‹ç­‰å¾…ï¼ˆwaitï¼‰æƒ…å†µ', 'SELECT v$waitstat.class, v$waitstat.count count, SUM(v$sysstat.value) sum_value\r\nFROM v$waitstat, v$sysstat WHERE v$sysstat.name IN (\'db block gets\',\r\n\'consistent gets\') group by v$waitstat.class, v$waitstat.count', 35, NULL);
INSERT INTO `dms_study` VALUES (44, 'icon-berlin-project', NULL, 'æŸ¥çœ‹sgaæƒ…å†µ', 'SELECT NAME, BYTES FROM SYS.V_$SGASTAT ORDER BY NAME ASC', 35, NULL);
INSERT INTO `dms_study` VALUES (45, 'icon-berlin-project', NULL, 'æŸ¥çœ‹è¡¨ç©ºé—´çš„ç¢Žç‰‡ç¨‹åº¦', 'select tablespace_name,count(tablespace_name) from dba_free_space group by tablespace_name\r\nhaving count(tablespace_name)>10;\r\nalter tablespace name coalesce;\r\nalter table name deallocate unused;\r\ncreate or replace view ts_blocks_v as\r\nselect tablespace_name,block_id,bytes,blocks,\'free space\' segment_name from dba_free_space\r\nunion all\r\nselect tablespace_name,block_id,bytes,blocks,segment_name from dba_extents;\r\nselect * from ts_blocks_v;\r\nselect tablespace_name,sum(bytes),max(bytes),count(block_id) from dba_free_space\r\ngroup by tablespace_name;', 35, NULL);

-- ----------------------------
-- Table structure for dms_task
-- ----------------------------
DROP TABLE IF EXISTS `dms_task`;
CREATE TABLE `dms_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `updateDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `souceConfig_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `souceDataBase` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doSql` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `targetConfig_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `targetDataBase` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `targetTable` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `updateUser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qualification` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_task
-- ----------------------------

-- ----------------------------
-- Table structure for dms_task_log
-- ----------------------------
DROP TABLE IF EXISTS `dms_task_log`;
CREATE TABLE `dms_task_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for dms_users
-- ----------------------------
DROP TABLE IF EXISTS `dms_users`;
CREATE TABLE `dms_users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `datascope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `expiration` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_users
-- ----------------------------
INSERT INTO `dms_users` VALUES (1, '2015-08-08', 'admin', '7c4ad84514ff2c2c664f7b1255f640e4', '0', 'ç®¡ç†å‘˜', '0', '475b8556f5f6ab0a8a7fcc86e105aab5', 'ç‰ˆæƒæ‰€æœ‰ï¼Œè¯·å°Šé‡å¹¶æ”¯æŒå›½äº§è½¯ä»¶', 'synchronize,monitor,backdatabase,person,config,json,task', '1', '2099-12-30 23:59:59');
INSERT INTO `dms_users` VALUES (2, '2015-08-08', 'treesoft', '0aeb0993855641272bce26eed6017aff', '0', 'ç®¡ç†å‘˜', '0', 'cebc228fc46e893b35a9c032ce7e800f', 'ç‰ˆæƒæ‰€æœ‰ï¼Œè¯·å°Šé‡å¹¶æ”¯æŒå›½äº§è½¯ä»¶', 'synchronize,monitor,backdatabase,person,config,json,task', '1', '2099-12-30 23:59:59');

SET FOREIGN_KEY_CHECKS = 1;
