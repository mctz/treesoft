package org.springframework.base.system.service;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.entity.NewQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/SQLParserService.class */
public class SQLParserService {
    @Autowired
    private ConfigService configService;

    public List<Map<String, String>> operationSQL(NewQueryDTO dto) throws Exception {
        List<Map<String, String>> sqlList;
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        String databaseType = (String) map.get("databaseType");
        List<Map<String, String>> sqlList2 = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getSql())) {
            String tempStr = dto.getSql().toLowerCase().replace(" ", "");
            if (tempStr.indexOf("createprocedure") == 0 || tempStr.indexOf("createorreplaceprocedure") == 0) {
                Map<String, String> mapTemp = new HashMap<>();
                mapTemp.put("sql", dto.getSql());
                mapTemp.put("sqlType", "createProcedure");
                sqlList2.add(mapTemp);
                return sqlList2;
            } else if (tempStr.indexOf("createfunction") == 0 || tempStr.indexOf("createorreplacefunction") == 0) {
                Map<String, String> mapTemp2 = new HashMap<>();
                mapTemp2.put("sql", dto.getSql());
                mapTemp2.put("sqlType", "createProcedure");
                sqlList2.add(mapTemp2);
                return sqlList2;
            } else if (tempStr.indexOf("renametable") == 0) {
                String[] sqls = dto.getSql().split(";");
                Map<String, String> mapTemp3 = new HashMap<>();
                mapTemp3.put("sql", sqls[0]);
                mapTemp3.put("sqlType", "other");
                sqlList2.add(mapTemp3);
                return sqlList2;
            } else if (tempStr.indexOf("show") == 0) {
                String[] sqls2 = dto.getSql().split(";");
                for (String str : sqls2) {
                    Map<String, String> mapTemp4 = new HashMap<>();
                    mapTemp4.put("sql", str);
                    mapTemp4.put("sqlType", "other");
                    sqlList2.add(mapTemp4);
                }
                return sqlList2;
            }
        }
        if (databaseType.equals("MySQL") || databaseType.equals("TiDB")) {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser);
        } else if (databaseType.equals("MariaDB")) {
            SQLStatementParser parser2 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser2);
        } else if (databaseType.equals("MySQL8.0")) {
            SQLStatementParser parser3 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser3);
        } else if (databaseType.equals("Oracle")) {
            SQLStatementParser parser4 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "oracle");
            sqlList = parserSQL(parser4);
        } else if (databaseType.equals("HANA2")) {
            SQLStatementParser parser5 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser5);
        } else if (databaseType.equals("PostgreSQL")) {
            SQLStatementParser parser6 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "postgresql");
            sqlList = parserSQL(parser6);
        } else if (databaseType.equals("SQL Server")) {
            SQLStatementParser parser7 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "sqlserver");
            sqlList = parserSQL(parser7);
        } else if (databaseType.equals("MongoDB")) {
            sqlList = parserMongoSQL(dto.getSql());
        } else if (databaseType.equals("Hive2")) {
            SQLStatementParser parser8 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser8);
        } else if (databaseType.equals("Impala")) {
            SQLStatementParser parser9 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser9);
        } else if (databaseType.equals("Cache")) {
            SQLStatementParser parser10 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser10);
        } else if (databaseType.equals("DB2")) {
            SQLStatementParser parser11 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "db2");
            sqlList = parserSQL(parser11);
        } else if (databaseType.equals("DM7")) {
            SQLStatementParser parser12 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "sqlserver");
            sqlList = parserSQL(parser12);
        } else if (databaseType.equals("ShenTong")) {
            SQLStatementParser parser13 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser13);
        } else if (databaseType.equals("Sybase")) {
            SQLStatementParser parser14 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "sybase");
            sqlList = parserSQL(parser14);
        } else if (databaseType.equals("Kingbase")) {
            SQLStatementParser parser15 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "kingbase");
            sqlList = parserSQL(parser15);
        } else if (databaseType.equals("Informix")) {
            SQLStatementParser parser16 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "informix");
            sqlList = parserSQL(parser16);
        } else if (databaseType.equals("ClickHouse")) {
            SQLStatementParser parser17 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "clickhouse");
            sqlList = parserSQL(parser17);
        } else if (databaseType.equals("Redshift")) {
            SQLStatementParser parser18 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser18);
        } else if (databaseType.equals("ES")) {
            SQLStatementParser parser19 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser19);
        } else {
            SQLStatementParser parser20 = SQLParserUtils.createSQLStatementParser(dto.getSql(), "mysql");
            sqlList = parserSQL(parser20);
        }
        return sqlList;
    }

    public List<Map<String, String>> parserSQL(SQLStatementParser parser) {
        String str;
        List<Map<String, String>> sqlList = new ArrayList<>();
        List<SQLStatement> stmtList = parser.parseStatementList();
        for (int i = 0; i < stmtList.size(); i++) {
            Map<String, String> mapTemp = new HashMap<>();
            SQLStatement stmt = stmtList.get(i);
            mapTemp.put("sql", stmt.toString());
            if (stmt instanceof SQLSelectStatement) {
                str = "select";
            } else if (stmt instanceof SQLShowTablesStatement) {
                str = "select";
            } else if (stmt instanceof SQLExplainStatement) {
                str = "select";
            } else if (stmt instanceof MySqlShowStatement) {
                str = "select";
            } else if (stmt instanceof SQLUpdateStatement) {
                str = "update";
            } else if (stmt instanceof SQLDeleteStatement) {
                str = "delete";
            } else if (stmt instanceof SQLInsertStatement) {
                str = "insert";
            } else if (stmt instanceof SQLDropStatement) {
                str = "drop";
            } else if (stmt instanceof SQLCreateStatement) {
                str = "create";
            } else if (stmt instanceof SQLCreateFunctionStatement) {
                str = "createFunction";
            } else if (stmt instanceof SQLCreateProcedureStatement) {
                str = "createProcedure";
            } else if (stmt instanceof SQLCallStatement) {
                str = "call";
            } else {
                str = "other";
            }
            String sqlType = str;
            mapTemp.put("sqlType", sqlType);
            sqlList.add(mapTemp);
        }
        return sqlList;
    }

    public List<Map<String, String>> parserMongoSQL(String sql) {
        List<Map<String, String>> sqlList = new ArrayList<>();
        String[] sqlArray = sql.split(";");
        for (int i = 0; i < sqlArray.length; i++) {
            if (!StringUtils.isEmpty(sqlArray[i].trim())) {
                Map<String, String> mapTemp = new HashMap<>();
                mapTemp.put("sql", sqlArray[i].trim());
                if (sql.toLowerCase().indexOf(".find(") > 0) {
                    mapTemp.put("sqlType", "select");
                } else if (sql.indexOf(".insert(") > 0) {
                    mapTemp.put("sqlType", "insert");
                } else if (sql.indexOf(".update(") > 0) {
                    mapTemp.put("sqlType", "update");
                } else if (sql.indexOf(".remove(") > 0) {
                    mapTemp.put("sqlType", "delete");
                } else {
                    mapTemp.put("sqlType", "other");
                }
                sqlList.add(mapTemp);
            }
        }
        return sqlList;
    }

    public String parserTableNames(String sql, String databaseType) {
        SQLStatementParser parser;
        String talbeName = "";
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("TiDB") || databaseType.equals("MariaDB") || databaseType.equals("MySQL8.0")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
            } else if (databaseType.equals("Oracle")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "oracle");
            } else if (databaseType.equals("SQL Server")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "sqlserver");
            } else if (databaseType.equals("Hive2")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
            } else if (databaseType.equals("Impala")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
            } else if (databaseType.equals("DB2")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "db2");
            } else if (databaseType.equals("DM7")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "dm");
            } else if (databaseType.equals("ShenTong")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
            } else if (databaseType.equals("Sybase")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "sybase");
            } else if (databaseType.equals("Kingbase")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "kingbase");
            } else if (databaseType.equals("Informix")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "informix");
            } else if (databaseType.equals("ClickHouse")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "clickhouse");
            } else if (databaseType.equals("Redshift")) {
                parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
            } else {
                parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
            }
            if (parser != null) {
                List<SQLStatement> stmtList = parser.parseStatementList();
                SQLStatement sQLStatement = stmtList.get(0);
                if (sQLStatement instanceof SQLSelectStatement) {
                    SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sQLStatement;
                    SQLSelectQueryBlock query = sqlSelectStatement.getSelect().getQuery();
                    if (query instanceof SQLSelectQueryBlock) {
                        SQLSelectQueryBlock sqlSelectQueryBlock = query;
                        SQLTableSource tableSource = sqlSelectQueryBlock.getFrom();
                        if (tableSource instanceof SQLExprTableSource) {
                            talbeName = tableSource.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return talbeName;
    }
}
