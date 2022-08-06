/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50559
Source Host           : localhost:3306
Source Database       : treesoft

Target Server Type    : MYSQL
Target Server Version : 50559
File Encoding         : 65001

Date: 2018-10-28 12:30:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for treesoft_config
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_config`;
CREATE TABLE `treesoft_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) DEFAULT NULL,
  `updateDate` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `isdefault` varchar(255) DEFAULT NULL,
  `databaseType` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `valid` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `port` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `driver` varchar(255) DEFAULT NULL,
  `databaseName` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_config
-- ----------------------------
INSERT INTO `treesoft_config` VALUES ('1', '2018-10-21 19:00:30', null, 'mysql', '1', 'MySql', null, null, null, '3306', null, '127.0.0.1', null, 'test', 'jdbc:mysql://127.0.0.1:3306/test', 'root', 'cm9vdGByb290');
INSERT INTO `treesoft_config` VALUES ('2', '2018-10-21 22:02:18', null, 'dbutils', '0', 'MySql', null, null, null, '3306', null, '127.0.0.1', null, 'dbutils', 'jdbc:mysql://127.0.0.1:3306/dbutils', 'root', 'cm9vdGByb290');

-- ----------------------------
-- Table structure for treesoft_data_synchronize
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_data_synchronize`;
CREATE TABLE `treesoft_data_synchronize` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) DEFAULT NULL,
  `updateDate` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `souceConfig_id` varchar(255) DEFAULT NULL,
  `souceDataBase` varchar(255) DEFAULT NULL,
  `doSql` varchar(255) DEFAULT NULL,
  `targetConfig_id` varchar(255) DEFAULT NULL,
  `targetDataBase` varchar(255) DEFAULT NULL,
  `targetTable` varchar(255) DEFAULT NULL,
  `cron` varchar(255) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `updateUser` varchar(255) DEFAULT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_data_synchronize
-- ----------------------------

-- ----------------------------
-- Table structure for treesoft_data_synchronize_log
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_data_synchronize_log`;
CREATE TABLE `treesoft_data_synchronize_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `data_synchronize_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_data_synchronize_log
-- ----------------------------

-- ----------------------------
-- Table structure for treesoft_license
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_license`;
CREATE TABLE `treesoft_license` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `personNumber` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `mess` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_license
-- ----------------------------

-- ----------------------------
-- Table structure for treesoft_log
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_log`;
CREATE TABLE `treesoft_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `log` varchar(20000) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_log
-- ----------------------------

-- ----------------------------
-- Table structure for treesoft_searchhistory
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_searchhistory`;
CREATE TABLE `treesoft_searchhistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `database` varchar(255) DEFAULT NULL,
  `sqls` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_searchhistory
-- ----------------------------
INSERT INTO `treesoft_searchhistory` VALUES ('1', '2018-10-21', 'dual', '1', 'SELECT 1 from DUAL', '2');

-- ----------------------------
-- Table structure for treesoft_study
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_study`;
CREATE TABLE `treesoft_study` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `icon` varchar(255) DEFAULT NULL,
  `sort` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(20000) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_study
-- ----------------------------
INSERT INTO `treesoft_study` VALUES ('1', 'icon-berlin-billing', null, 'SQL', null, '0', null);
INSERT INTO `treesoft_study` VALUES ('2', 'icon-berlin-calendar', null, '常用SQL', null, '1', null);
INSERT INTO `treesoft_study` VALUES ('3', 'icon-berlin-project', null, 'select', 'SELECT col_name,...\n  FROM table_name\n WHERE where_condition\n GROUP BY col_name,... \nHAVING where_condition\n ORDER BY col_name,...\n LIMIT offset,row_count', '2', null);
INSERT INTO `treesoft_study` VALUES ('4', 'icon-berlin-project', null, 'insert', 'INSERT INTO table_name(col_name,...) values(expr,...)', '2', null);
INSERT INTO `treesoft_study` VALUES ('5', 'icon-berlin-project', null, 'update', 'UPDATE table_name SET col_name=expr,... WHERE where_condition\n', '2', null);
INSERT INTO `treesoft_study` VALUES ('6', 'icon-berlin-project', null, 'delete', 'DELETE FROM table_name WHERE where_condition', '2', null);
INSERT INTO `treesoft_study` VALUES ('7', 'icon-berlin-project', null, 'replace', 'REPLACE INTO table_name(col_name,...) values(expr,...)', '2', null);
INSERT INTO `treesoft_study` VALUES ('8', 'icon-berlin-calendar', null, '表/索引', null, '1', null);
INSERT INTO `treesoft_study` VALUES ('9', 'icon-berlin-project', null, 'alter table', 'ALTER [ONLINE | OFFLINE] [IGNORE] TABLE tbl_name\n    alter_specification [, alter_specification] ...\n\nalter_specification:\n    table_option ...\n  | ADD [COLUMN] col_name column_definition\n        [FIRST | AFTER col_name ]\n  | ADD [COLUMN] (col_name column_definition,...)\n  | ADD {INDEX|KEY} [index_name]\n        [index_type] (index_col_name,...) [index_option] ...\n  | ADD [CONSTRAINT [symbol]] PRIMARY KEY\n        [index_type] (index_col_name,...) [index_option] ...\n  | ADD [CONSTRAINT [symbol]]\n        UNIQUE [INDEX|KEY] [index_name]\n        [index_type] (index_col_name,...) [index_option] ...\n  | ADD FULLTEXT [INDEX|KEY] [index_name]\n        (index_col_name,...) [index_option] ...\n  | ADD SPATIAL [INDEX|KEY] [index_name]\n        (index_col_name,...) [index_option] ...\n  | ADD [CONSTRAINT [symbol]]\n        FOREIGN KEY [index_name] (index_col_name,...)\n        reference_definition\n  | ALTER [COLUMN] col_name {SET DEFAULT literal | DROP DEFAULT}\n  | CHANGE [COLUMN] old_col_name new_col_name column_definition\n        [FIRST|AFTER col_name]\n  | MODIFY [COLUMN] col_name column_definition\n        [FIRST | AFTER col_name]\n  | DROP [COLUMN] col_name\n  | DROP PRIMARY KEY\n  | DROP {INDEX|KEY} index_name\n  | DROP FOREIGN KEY fk_symbol\n  | DISABLE KEYS\n  | ENABLE KEYS\n  | RENAME [TO] new_tbl_name\n  | ORDER BY col_name [, col_name] ...\n  | CONVERT TO CHARACTER SET charset_name [COLLATE collation_name]\n  | [DEFAULT] CHARACTER SET [=] charset_name [COLLATE [=] collation_name]\n  | DISCARD TABLESPACE\n  | IMPORT TABLESPACE\n  | partition_options\n  | ADD PARTITION (partition_definition)\n  | DROP PARTITION partition_names\n  | COALESCE PARTITION number\n  | REORGANIZE PARTITION partition_names INTO (partition_definitions)\n  | ANALYZE PARTITION partition_names\n  | CHECK PARTITION partition_names\n  | OPTIMIZE PARTITION partition_names\n  | REBUILD PARTITION partition_names\n  | REPAIR PARTITION partition_names\n  | REMOVE PARTITIONING\n\nindex_col_name:\n    col_name [(length)] [ASC | DESC]\n\nindex_type:\n    USING {BTREE | HASH | RTREE}\n\nindex_option:\n    KEY_BLOCK_SIZE [=] value\n  | index_type\n  | WITH PARSER parser_name\n  | COMMENT \'string\'', '8', null);
INSERT INTO `treesoft_study` VALUES ('10', 'icon-berlin-project', null, 'create table', 'CREATE TABLE tbl_name\n(\ncol_name data_type NOT NULL DEFAULT default_value AUTO_INCREMENT COMMENT \'string\',\n...\nKEY index_name index_type (index_col_name,...),\n...\nPRIMARY KEY(index_col_name,...),\nUNIQUE KEY(index_col_name,...)\n) ENGINE=engine_name CHARACTER SET=charset_name COMMENT=\'string\'', '8', null);
INSERT INTO `treesoft_study` VALUES ('11', 'icon-berlin-project', null, 'create index', 'CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name\n    [USING index_type]\n    ON tbl_name (index_col_name,...)\n \nindex_col_name:\n    col_name [(length)] [ASC | DESC]\n', '8', null);
INSERT INTO `treesoft_study` VALUES ('12', 'icon-berlin-project', null, 'drop table', 'DROP [TEMPORARY] TABLE [IF EXISTS]\n    tbl_name [, tbl_name] ...\n    [RESTRICT | CASCADE]\n', '8', null);
INSERT INTO `treesoft_study` VALUES ('13', 'icon-berlin-project', null, 'drop index', 'DROP INDEX index_name ON tbl_name', '8', null);
INSERT INTO `treesoft_study` VALUES ('14', 'icon-berlin-project', null, 'rename table', 'RENAME TABLE tbl_name TO new_tbl_name\n', '8', null);
INSERT INTO `treesoft_study` VALUES ('15', 'icon-berlin-project', null, 'truncate table', 'TRUNCATE [TABLE] tbl_name', '8', null);
INSERT INTO `treesoft_study` VALUES ('16', 'icon-berlin-calendar', null, '视图', null, '1', null);
INSERT INTO `treesoft_study` VALUES ('17', 'icon-berlin-project', null, 'create view', 'CREATE [OR REPLACE] [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]\n    VIEW view_name [(column_list)]\n    AS select_statement\n    [WITH [CASCADED | LOCAL] CHECK OPTION]\n', '16', null);
INSERT INTO `treesoft_study` VALUES ('18', 'icon-berlin-project', null, 'alter view', 'ALTER\n    [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]\n    [DEFINER = { user | CURRENT_USER }]\n    [SQL SECURITY { DEFINER | INVOKER }]\n    VIEW view_name [(column_list)]\n    AS select_statement\n    [WITH [CASCADED | LOCAL] CHECK OPTION]', '16', null);
INSERT INTO `treesoft_study` VALUES ('19', 'icon-berlin-project', null, 'drop view', 'DROP VIEW [IF EXISTS]\n    view_name [, view_name] ...\n    [RESTRICT | CASCADE]\n', '16', null);
INSERT INTO `treesoft_study` VALUES ('20', 'icon-berlin-calendar', null, '函数/存储过程', null, '1', null);
INSERT INTO `treesoft_study` VALUES ('21', 'icon-berlin-project', null, 'alter function', 'ALTER FUNCTION sp_name [characteristic ...]\n \ncharacteristic:\n    { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'', '20', null);
INSERT INTO `treesoft_study` VALUES ('22', 'icon-berlin-project', null, 'alter procedure', 'ALTER PROCEDURE sp_name [characteristic ...]\n \ncharacteristic:\n    { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'\n', '20', null);
INSERT INTO `treesoft_study` VALUES ('23', 'icon-berlin-project', null, 'call procedure', 'CALL sp_name([parameter[,...]])', '20', null);
INSERT INTO `treesoft_study` VALUES ('24', 'icon-berlin-project', null, 'create function', 'CREATE FUNCTION sp_name ([func_parameter[,...]])\n    RETURNS type\n    [characteristic ...]\n routine_body\n\ncharacteristic:\n    LANGUAGE SQL\n  | [NOT] DETERMINISTIC\n  | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'\n', '20', null);
INSERT INTO `treesoft_study` VALUES ('25', 'icon-berlin-project', null, 'create procedure', 'CREATE PROCEDURE sp_name ([proc_parameter[,...]])\n    [characteristic ...]\n routine_body\ncharacteristic:\n    LANGUAGE SQL\n  | [NOT] DETERMINISTIC\n  | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }\n  | SQL SECURITY { DEFINER | INVOKER }\n  | COMMENT \'string\'\n', '20', null);
INSERT INTO `treesoft_study` VALUES ('26', 'icon-berlin-project', null, 'drop function', 'DROP FUNCTION [IF EXISTS] sp_name', '20', null);
INSERT INTO `treesoft_study` VALUES ('27', 'icon-berlin-project', null, 'drop procedure', 'DROP PROCEDURE [IF EXISTS] sp_name', '20', null);
INSERT INTO `treesoft_study` VALUES ('28', 'icon-berlin-project', null, 'MongoDB shell命令', null, '1', null);
INSERT INTO `treesoft_study` VALUES ('29', 'icon-berlin-project', null, '创建数据库use', 'use <databaseName> ', '28', null);
INSERT INTO `treesoft_study` VALUES ('30', 'icon-berlin-project', null, '创建数据集合db.createCollection', 'db.createCollection(<collectionName>)', '28', null);
INSERT INTO `treesoft_study` VALUES ('31', 'icon-berlin-project', null, '查看文档db.users.find()', 'db.users.find({})', '28', null);
INSERT INTO `treesoft_study` VALUES ('32', 'icon-berlin-project', null, '插入数据db.users.insert()', 'db.users.insert(<data>)', '28', null);
INSERT INTO `treesoft_study` VALUES ('33', 'icon-berlin-project', null, '删除数据db.users.remove()', 'db.users.remove({})', '28', null);
INSERT INTO `treesoft_study` VALUES ('34', 'icon-berlin-project', null, '更新数据db.users.update()', 'db.users.update(query,update)', '28', null);
INSERT INTO `treesoft_study` VALUES ('35', 'icon-berlin-calendar', null, 'Oracle常用查询', null, '1', null);
INSERT INTO `treesoft_study` VALUES ('36', 'icon-berlin-project', null, '查看连接用户信息', 'SELECT s.Osuser Os_User_Name,Decode(Sign(48 - Command),1,To_Char(Command),\r\n\'Action Code #\' || To_Char(Command)) Action,\r\np.Program Oracle_Process, Status Session_Status, s.Terminal Terminal,\r\ns.Program Program, s.Username User_Name,\r\ns.Fixed_Table_Sequence Activity_Meter, \'\' Query, 0 Memory,\r\n0 Max_Memory, 0 Cpu_Usage, s.Sid, s.Serial# Serial_Num\r\nFROM V$session s, V$process p\r\nWHERE s.Paddr = p.Addr\r\nAND s.TYPE = \'USER\'\r\nORDER BY s.Username, s.Osuser', '35', null);
INSERT INTO `treesoft_study` VALUES ('37', 'icon-berlin-project', null, '高速缓冲区命中率', 'select 1 - sum(decode(name, \'physical reads\', value, 0)) /\r\n(sum(decode(name, \'db block gets\', value, 0)) +\r\nsum(decode(name, \'consistent gets\', value, 0))) hit_ratio\r\nfrom v$sysstat t\r\nwhere name in (\'physical reads\', \'db block gets\', \'consistent gets\')', '35', null);
INSERT INTO `treesoft_study` VALUES ('38', 'icon-berlin-project', null, '查看表空间使用情况', 'SELECT Upper(F.TABLESPACE_NAME)         \"表空间名\",\r\n       D.TOT_GROOTTE_MB                 \"表空间大小(M)\",\r\n       D.TOT_GROOTTE_MB - F.TOTAL_BYTES \"已使用空间(M)\",\r\n       To_char(Round(( D.TOT_GROOTTE_MB - F.TOTAL_BYTES ) / D.TOT_GROOTTE_MB * 100, 2), \'990.99\')\r\n       || \'%\'                           \"使用比\",\r\n       F.TOTAL_BYTES                    \"空闲空间(M)\",\r\n       F.MAX_BYTES                      \"最大块(M)\"\r\nFROM   (SELECT TABLESPACE_NAME,\r\n               Round(Sum(BYTES) / ( 1024 * 1024 ), 2) TOTAL_BYTES,\r\n               Round(Max(BYTES) / ( 1024 * 1024 ), 2) MAX_BYTES\r\n        FROM   SYS.DBA_FREE_SPACE\r\n        GROUP  BY TABLESPACE_NAME) F,\r\n       (SELECT DD.TABLESPACE_NAME,\r\n               Round(Sum(DD.BYTES) / ( 1024 * 1024 ), 2) TOT_GROOTTE_MB\r\n        FROM   SYS.DBA_DATA_FILES DD\r\n        GROUP  BY DD.TABLESPACE_NAME) D\r\nWHERE  D.TABLESPACE_NAME = F.TABLESPACE_NAME\r\nORDER  BY 1', '35', null);
INSERT INTO `treesoft_study` VALUES ('39', 'icon-berlin-project', null, '共享池的命中率', 'select sum(pinhits)/sum(pins)*100 \"hit radio\" from v$librarycache;', '35', null);
INSERT INTO `treesoft_study` VALUES ('40', 'icon-berlin-project', null, '查看还没提交的事务', 'select * from v$locked_object;', '35', null);
INSERT INTO `treesoft_study` VALUES ('41', 'icon-berlin-project', null, '耗资源的进程', 'select s.schemaname schema_name, decode(sign(48 - command), 1,\r\nto_char(command), \'Action Code #\' || to_char(command) ) action, status\r\nsession_status, s.osuser os_user_name, s.sid, p.spid , s.serial# serial_num,\r\nnvl(s.username, \'[Oracle process]\') user_name, s.terminal terminal,\r\ns.program program, st.value criteria_value from v$sesstat st, v$session s , v$process p\r\nwhere st.sid = s.sid and st.statistic# = to_number(\'38\') and (\'ALL\' = \'ALL\'\r\nor s.status = \'ALL\') and p.addr = s.paddr order by st.value desc, p.spid asc, s.username asc, s.osuser asc', '35', null);
INSERT INTO `treesoft_study` VALUES ('42', 'icon-berlin-project', null, '查看锁（lock）情况', 'select /* RULE */ ls.osuser os_user_name, ls.username user_name,\r\ndecode(ls.type, \'RW\', \'Row wait enqueue lock\', \'TM\', \'DML enqueue lock\', \'TX\',\r\n\'Transaction enqueue lock\', \'UL\', \'User supplied lock\') lock_type,\r\no.object_name object, decode(ls.lmode, 1, null, 2, \'Row Share\', 3,\r\n\'Row Exclusive\', 4, \'Share\', 5, \'Share Row Exclusive\', 6, \'Exclusive\', null)\r\nlock_mode, o.owner, ls.sid, ls.serial# serial_num, ls.id1, ls.id2\r\nfrom sys.dba_objects o, ( select s.osuser, s.username, l.type,\r\nl.lmode, s.sid, s.serial#, l.id1, l.id2 from v$session s,\r\nv$lock l where s.sid = l.sid ) ls where o.object_id = ls.id1 and o.owner\r\n<> \'SYS\' order by o.owner, o.object_name', '35', null);
INSERT INTO `treesoft_study` VALUES ('43', 'icon-berlin-project', null, '查看等待（wait）情况', 'SELECT v$waitstat.class, v$waitstat.count count, SUM(v$sysstat.value) sum_value\r\nFROM v$waitstat, v$sysstat WHERE v$sysstat.name IN (\'db block gets\',\r\n\'consistent gets\') group by v$waitstat.class, v$waitstat.count', '35', null);
INSERT INTO `treesoft_study` VALUES ('44', 'icon-berlin-project', null, '查看sga情况', 'SELECT NAME, BYTES FROM SYS.V_$SGASTAT ORDER BY NAME ASC', '35', null);
INSERT INTO `treesoft_study` VALUES ('45', 'icon-berlin-project', null, '查看表空间的碎片程度', 'select tablespace_name,count(tablespace_name) from dba_free_space group by tablespace_name\r\nhaving count(tablespace_name)>10;\r\nalter tablespace name coalesce;\r\nalter table name deallocate unused;\r\ncreate or replace view ts_blocks_v as\r\nselect tablespace_name,block_id,bytes,blocks,\'free space\' segment_name from dba_free_space\r\nunion all\r\nselect tablespace_name,block_id,bytes,blocks,segment_name from dba_extents;\r\nselect * from ts_blocks_v;\r\nselect tablespace_name,sum(bytes),max(bytes),count(block_id) from dba_free_space\r\ngroup by tablespace_name;', '35', null);

-- ----------------------------
-- Table structure for treesoft_task
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_task`;
CREATE TABLE `treesoft_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) DEFAULT NULL,
  `updateDate` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `souceConfig_id` varchar(255) DEFAULT NULL,
  `souceDataBase` varchar(255) DEFAULT NULL,
  `doSql` varchar(255) DEFAULT NULL,
  `targetConfig_id` varchar(255) DEFAULT NULL,
  `targetDataBase` varchar(255) DEFAULT NULL,
  `targetTable` varchar(255) DEFAULT NULL,
  `cron` varchar(255) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `updateUser` varchar(255) DEFAULT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_task
-- ----------------------------

-- ----------------------------
-- Table structure for treesoft_task_log
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_task_log`;
CREATE TABLE `treesoft_task_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdate` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `task_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for treesoft_users
-- ----------------------------
DROP TABLE IF EXISTS `treesoft_users`;
CREATE TABLE `treesoft_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `datascope` varchar(255) DEFAULT NULL,
  `expiration` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of treesoft_users
-- ----------------------------
INSERT INTO `treesoft_users` VALUES ('1', '2015-08-08', 'admin', '7c4ad84514ff2c2c664f7b1255f640e4', '0', '管理员', '0', '475b8556f5f6ab0a8a7fcc86e105aab5', '版权所有，请尊重并支持国产软件', 'synchronize,monitor,backdatabase,person,config,json,task', '1,2,3', '2099-12-30 23:59:59');
INSERT INTO `treesoft_users` VALUES ('2', '2015-08-08', 'treesoft', '0aeb0993855641272bce26eed6017aff', '0', '管理员', '0', 'cebc228fc46e893b35a9c032ce7e800f', '版权所有，请尊重并支持国产软件', 'synchronize,monitor,backdatabase,person,config,json,task', '1,2,3', '2099-12-30 23:59:59');
