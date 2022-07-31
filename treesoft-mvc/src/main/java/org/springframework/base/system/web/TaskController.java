package org.springframework.base.system.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.base.common.bean.Page;
import org.springframework.base.common.web.BaseController;
import org.springframework.base.system.entity.Task;
import org.springframework.base.system.scheduler.QuartzJobFactoryForTask;
import org.springframework.base.system.scheduler.QuartzManager;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.TaskService;
import org.springframework.base.system.web.dto.IdsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"system/task"})
public class TaskController extends BaseController
{
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private PermissionService permissionService;
    
    @Resource
    private QuartzManager quartzManager;
    
    @Autowired
    HttpServletRequest request;
    
    @RequestMapping(value = {"i/task"}, method = {RequestMethod.GET})
    public String task(Model model)
    {
        return "system/taskList";
    }
    
    @RequestMapping(value = {"i/taskList"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> taskList()
    {
        Page<Map<String, Object>> page = getPage(request);
        try
        {
            page = taskService.taskList(page);
        }
        catch (Exception e)
        {
            return getEasyUIData(page);
        }
        return getEasyUIData(page);
    }
    
    @RequestMapping(value = {"i/addTaskForm"}, method = {RequestMethod.GET})
    public String addTaskForm(Model model)
    {
        List<Map<String, Object>> configList = permissionService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/taskForm";
    }
    
    @RequestMapping(value = {"i/editTaskForm/{id}"}, method = {RequestMethod.GET})
    public String editTaskForm(@PathVariable String id, Model model)
    {
        List<Map<String, Object>> configList = permissionService.getAllConfigList();
        Map<String, Object> map = taskService.getTask(id);
        model.addAttribute("configList", configList);
        model.addAttribute("task", map);
        return "system/taskForm";
    }
    
    @RequestMapping(value = {"i/taskUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> taskUpdate(@ModelAttribute @RequestBody Task task, Model model)
    {
        String mess;
        String status;
        try
        {
            String taskId = task.getId();
            String status2 = task.getStatus();
            String cron = task.getCron();
            String state = task.getState();
            if (state.equals("1"))
            {
                task.setStatus("0");
            }
            taskService.taskUpdate(task);
            if (!taskId.equals(""))
            {
                if (status2.equals("1"))
                {
                    Map<String, Object> job = taskService.getTask(taskId);
                    QuartzManager.removeJob(taskId);
                    quartzManager.addJob(taskId, QuartzJobFactoryForTask.class, cron, job);
                }
                if (state.equals("1"))
                {
                    QuartzManager.removeJob(taskId);
                }
            }
            mess = "修改成功";
            status = "success";
        }
        catch (Exception e)
        {
            mess = "error:" + e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
    
    @RequestMapping(value = {"i/deleteTask"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteTask(@RequestBody IdsDto tem)
    {
        String[] ids = tem.getIds();
        String mess;
        String status;
        try
        {
            taskService.deleteTask(ids);
            for (String taskId : ids)
            {
                QuartzManager.removeJob(taskId);
                taskService.deleteTaskLogByDS(taskId);
            }
            mess = "删除成功";
            status = "success";
        }
        catch (Exception e)
        {
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
    
    @RequestMapping(value = {"i/startTask"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> startTask(@RequestBody IdsDto tem)
    {
        String[] ids = tem.getIds();
        String mess;
        String status;
        String state;
        String taskId = "";
        try
        {
            List<Map<String, Object>> list = taskService.getTaskListById(ids);
            for (Map<String, Object> job : list)
            {
                taskId = (String)job.get("id");
                state = (String)job.get("state");
                if (state.equals("1"))
                {
                    throw new Exception("启用状态的任务才能运行！");
                }
                QuartzManager.removeJob(taskId);
                quartzManager.addJob(taskId, QuartzJobFactoryForTask.class, job.get("cron").toString(), job);
                taskService.taskUpdateStatus(taskId, "1");
            }
            mess = "操作成功!";
            status = "success";
        }
        catch (Exception e)
        {
            taskService.taskUpdateStatus(taskId, "0");
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
    
    @RequestMapping(value = {"i/startTaskOne"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> startTaskOne(@RequestBody IdsDto tem)
    {
        String[] ids = tem.getIds();
        String mess;
        String status;
        String taskId = "";
        try
        {
            List<Map<String, Object>> list = taskService.getTaskListById(ids);
            for (Map<String, Object> job : list)
            {
                taskId = (String)job.get("id");
                quartzManager.addJobOne(taskId, QuartzJobFactoryForTask.class, job);
            }
            mess = "操作成功!";
            status = "success";
        }
        catch (Exception e)
        {
            taskService.taskUpdateStatus(taskId, "0");
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
    
    @RequestMapping(value = {"i/stopTask"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> stopTask(@RequestBody IdsDto tem)
    {
        String[] ids = tem.getIds();
        String mess;
        String status;
        String taskId = "";
        try
        {
            List<Map<String, Object>> list = taskService.getTaskListById(ids);
            for (Map<String, Object> job : list)
            {
                taskId = (String)job.get("id");
                QuartzManager.removeJob(taskId);
                taskService.taskUpdateStatus(taskId, "0");
            }
            mess = "操作成功!";
            status = "success";
        }
        catch (Exception e)
        {
            taskService.taskUpdateStatus(taskId, "0");
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
    
    @RequestMapping(value = {"i/taskLogForm/{taskId}"}, method = {RequestMethod.GET})
    public String taskLogForm(@PathVariable String taskId)
    {
        request.setAttribute("taskId", taskId);
        return "system/taskLogForm";
    }
    
    @RequestMapping(value = {"i/taskLogList/{taskId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> taskLogList(@PathVariable String taskId)
    {
        Page<Map<String, Object>> page = getPage(request);
        try
        {
            page = taskService.taskLogList(page, taskId);
        }
        catch (Exception e)
        {
            return getEasyUIData(page);
        }
        return getEasyUIData(page);
    }
    
    @RequestMapping(value = {"i/deleteTaskLog"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteTaskLog(@RequestBody IdsDto tem)
    {
        String[] ids = tem.getIds();
        String mess;
        String status;
        try
        {
            taskService.deleteTaskLog(ids);
            mess = "删除成功";
            status = "success";
        }
        catch (Exception e)
        {
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
