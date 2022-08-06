package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.base.common.bean.Page;
import org.springframework.base.system.dao.TaskDao;
import org.springframework.base.system.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService
{
    @Autowired
    private TaskDao taskDao;
    
    public Page<Map<String, Object>> taskList(Page<Map<String, Object>> page)
    {
        return taskDao.taskList(page);
    }
    
    public boolean deleteTask(String[] ids)
    {
        return taskDao.deleteTask(ids);
    }
    
    public Map<String, Object> getTask(String id)
    {
        return taskDao.getTask(id);
    }
    
    public boolean taskUpdate(Task task)
    {
        return taskDao.taskUpdate(task);
    }
    
    public boolean taskUpdateStatus(String taskId, String status)
    {
        return taskDao.taskUpdateStatus(taskId, status);
    }
    
    public List<Map<String, Object>> getTaskListById(String[] ids)
    {
        return taskDao.getTaskListById(ids);
    }
    
    public Page<Map<String, Object>> taskLogList(Page<Map<String, Object>> page, String taskId)
    {
        return taskDao.taskLogList(page, taskId);
    }
    
    public List<Map<String, Object>> getTaskList2(String state)
    {
        return taskDao.getTaskList2(state);
    }
    
    public boolean deleteTaskLog(String[] ids)
    {
        return taskDao.deleteTaskLog(ids);
    }
    
    public boolean taskLogSave(String status, String comments, String taskId)
    {
        return taskDao.taskLogSave(status, comments, taskId);
    }
    
    public boolean deleteTaskLogByDS(String id)
    {
        return taskDao.deleteTaskLogByDS(id);
    }
}
