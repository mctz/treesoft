package org.springframework.base.system.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.common.bean.Page;
import org.springframework.base.common.utils.DateUtil;
import org.springframework.base.system.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDao
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    NamedParameterJdbcTemplate namedJdbcTemplate;
    
    public Page<Map<String, Object>> taskList(Page<Map<String, Object>> page)
    {
        String sql =
            " select t1.id, t1.state, t1.name, t1.createDate, t1.updateDate, t1.souceConfig_id as souceConfigId, t1.souceDataBase, t1.doSql, t1.targetConfig_id as targetConfigId, t1.targetDataBase, t1.targetTable, t1.cron, t1.operation, t1.comments, t1. status, t2.name||', '||t2.ip||':'||t2.port as souceConfig, t3.ip||':'||t3.port as targetConfig from treesoft_task t1 left join treesoft_config t2 on t1.souceConfig_id = t2.id LEFT JOIN treesoft_config t3 on t1.targetConfig_id = t3.id ";
        int rowCount = jdbcTemplate.queryForList(sql).size();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql + " limit ?, ?", (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }
    
    public List<Map<String, Object>> getTaskListById(String[] ids)
    {
        String sql =
            "select id, name, souceConfig_id as souceConfigId, souceDataBase, doSql, targetConfig_id as targetConfigId, targetDataBase, targetTable, cron, operation, comments, status, state, qualification from treesoft_task where id in(:ids)";
        return namedJdbcTemplate.queryForList(sql, Collections.singletonMap("ids", ids));
    }
    
    public boolean deleteTask(String[] ids)
    {
        String sql = "delete from treesoft_task where id in (:ids)";
        return namedJdbcTemplate.update(sql, Collections.singletonMap("ids", ids)) > 0;
    }
    
    public boolean taskUpdate(Task task)
    {
        String id = task.getId();
        String sql = "";
        String status = task.getStatus();
        if (StringUtils.isEmpty(status))
        {
            task.setStatus("0");
        }
        String doSql = task.getDoSql();
        doSql = doSql.replaceAll("'", "''");
        doSql = doSql.replaceAll(";", "");
        doSql = StringEscapeUtils.unescapeHtml4(doSql);
        task.setDoSql(doSql);
        if (!id.equals(""))
        {
            sql =
                "update treesoft_task set name=:name, souceConfig_id=:souceConfigId, souceDataBase=:souceDataBase, doSql=:doSql, targetConfig_id=:targetConfigId, targetDataBase =:targetDataBase, targetTable=:targetTable, cron=:cron, status=:status, state=:state, qualification=:qualification, comments=:comments, operation=:operation where id=:id";
            return namedJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(task)) > 0;
        }
        else
        {
            sql =
                " insert into treesoft_task (name, createDate, updateDate, souceConfig_id, souceDataBase, doSql, targetConfig_id, targetDataBase, targetTable, cron, operation, comments, status, qualification, state ) values ( :name, '"
                    + DateUtil.getDateTime() + "', '" + DateUtil.getDateTime()
                    + "', :souceConfigId , :souceDataBase, :doSql, :targetConfigId, :targetDataBase, :targetTable, :cron, :operation, :comments, :status, :qualification, :state) ";
            return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(task)) > 0;
        }
    }
    
    public boolean taskUpdateStatus(String taskId, String status)
    {
        String sql = "update treesoft_task set status=? where id=?";
        return jdbcTemplate.update(sql, status, taskId) > 0;
    }
    
    public Map<String, Object> getTask(String id)
    {
        String sql =
            "select id, name, souceConfig_id as souceConfigId, souceDataBase, doSql, targetConfig_id as targetConfigId, targetDataBase, targetTable, cron, operation, comments, status, state, qualification from treesoft_task where id=?";
        return jdbcTemplate.queryForMap(sql, id);
    }
    
    public Page<Map<String, Object>> taskLogList(Page<Map<String, Object>> page, String taskId)
    {
        String sql = " select id, createDate, status, comments from treesoft_task_log where task_id =? order by createdate desc";
        int rowCount = jdbcTemplate.queryForList(sql, taskId).size();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql + " limit ?, ?", (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }
    
    public List<Map<String, Object>> getTaskList2(String state)
    {
        String sql = "";
        if (StringUtils.isBlank(state))
        {
            sql =
                "select t1.id, t1.state, t1.name, t1.createDate, t1.updateDate, t1.souceConfig_id as souceConfigId, t1.souceDataBase, t1.doSql, t1.targetConfig_id as targetConfigId, t1.targetDataBase, t1.targetTable, t1.cron, t1.operation, t1.comments, t1.status, t2.ip||':'||t2.port as souceConfig, t3.ip||':'||t3.port as targetConfig from treesoft_task t1 left join treesoft_config t2 on t1.souceConfig_id = t2.id LEFT JOIN treesoft_config t3 on t1.targetConfig_id = t3.id ";
            return jdbcTemplate.queryForList(sql);
        }
        else
        {
            sql =
                "select t1.id, t1.state, t1.name, t1.createDate, t1.updateDate, t1.souceConfig_id as souceConfigId, t1.souceDataBase, t1.doSql, t1.targetConfig_id as targetConfigId, t1.targetDataBase, t1.targetTable, t1.cron, t1.operation, t1.comments, t1.status, t2.ip||':'||t2.port as souceConfig, t3.ip||':'||t3.port as targetConfig from treesoft_task t1 left join treesoft_config t2 on t1.souceConfig_id = t2.id LEFT JOIN treesoft_config t3 on t1.targetConfig_id = t3.id where t1.state=?";
            return jdbcTemplate.queryForList(sql, state);
        }
    }
    
    public boolean deleteTaskLog(String[] ids)
    {
        String sql = "delete from treesoft_task_log where id in (:ids)";
        return namedJdbcTemplate.update(sql, Collections.singletonMap("ids", ids)) > 0;
    }
    
    public boolean deleteTaskLogByDS(String id)
    {
        return jdbcTemplate.update("delete from treesoft_task_log where task_id =?", id) > 0;
    }
    
    public boolean taskLogSave(String status, String comments, String taskId)
    {
        String comments2 = comments.replaceAll("'", "''");
        String sql = "insert into treesoft_task_log (createDate, status, comments, task_id) values (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, DateUtil.getDateTime(), status, comments2, taskId) > 0;
    }
}
