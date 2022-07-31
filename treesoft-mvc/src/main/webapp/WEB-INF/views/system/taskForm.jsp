<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>定时任务</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>

<body>
<div>
    <div style="float:left;">
	<form id="mainform" action="${ctx }/system/task/i/taskUpdate" method="post">
	    <input id="id" name="id" type="hidden" value="${task.id }"   />
	    <input id="status" name="status" type="hidden" value="${task.status }"   />
		<table class="formTable">
			
			<tr>
				<td>任务名称：</td>
				<td colspan="3" ><input id="name"  name="name" type="text" value="${task.name }"   class="easyui-validatebox"   data-options="width:415,required:'required'"/>	</td>
		    </tr>
		    
			<tr>
				<td>数据库配置：</td>
				<td>
				<select id="souceConfigId" name="souceConfigId" class="esayui-combobox"  style="width:150px;"    >
				   <option value="" > </option>
		           <c:forEach var="config" items="${ configList }"   >
		                 <option value="${config.id}" title="${config.ip}:${config.port}"  <c:if test="${ task.souceConfigId == config.id}"> selected </c:if>  >${config.name}</option>
		           </c:forEach>
				</select>
				</td>
				
				<td>&nbsp;&nbsp;&nbsp;&nbsp;数据库名：</td>
				<td><input id="souceDataBase"  name="souceDataBase" type="text" value="${task.souceDataBase }"   class="easyui-validatebox"   data-options="width: 150,required:'required'"/>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="Oracle请填写实例名"></a>
				</td>				
		    </tr>
			 
			 <tr>
			  <td>SQL语句：</td>
			  <td colspan="3"><textarea id="doSql" name="doSql" type="text"   style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${task.doSql }</textarea>   </td>
			 </tr>
			 
			 <tr>
				<td> 调度计划： </td>
				<td> <input id="cron"  name="cron" type="text" value="${task.cron }" class="easyui-validatebox"  data-options="width: 150,required:'required'" />
				  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="调度计划使用Cron表达式,&#13;Cron表达式的格式：秒 分 时 日 月 周 年(可选)。&#13;Cron表达式范例：&#13;  每隔20秒执行一次：*/20 * * * * ?&#13;  每隔1分钟执行一次：0 */1 * * * ?&#13;  每天23点执行一次：0 0 23 * * ?&#13;  每天凌晨1点执行一次：0 0 1 * * ?&#13;  每月1号凌晨1点执行一次：0 0 1 1 * ?&#13;  每月最后一天23点执行一次：0 0 23 L * ?
				  "></a>
			    </td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;是否启用：</td>
				<td>
				 <select id="state" name="state"  class="esayui-combobox"   style="width:150px;"  >
				   <option value="0" <c:if test="${task.state=='0' }"> selected </c:if>  >启用</option> 
				   <option value="1" <c:if test="${task.state=='1' }"> selected </c:if>  >禁用</option>  
				</select>  </td>
			</tr>
			 
			 
			 <tr>
			  <td>备注说明：</td>
			  <td colspan="3"><textarea id="comments" name="comments" type="text" value="${task.comments }" style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${task.comments}</textarea>  </td>
			 </tr>
			 
			  <tr>
				<td> &nbsp;</td>
				<td> </td>
				<td> </td>
				<td> </td>
			</tr>
			 
			 <tr>
			  <td>功能说明：</td>
			  <td colspan="3"> 1、按调度计划定时运行SQL语句，使用企业级任务调度框架quartz。 <br>
                               3、支持MySql，Oracle，PostgreSQL，SQL Server等定时运行SQL语句。 <br>
                               4、支持支持新增、删除、更新操作。 <br>
                               5、系统启动时，<font color="red">将自动运行“启用”状态的任务，</font>闲置的任务请设为“禁用”。 <br>
              </td>
			 </tr>
			 
			 <tr>
				<td> </td>
				<td> <span id="mess2">  </span> </td>
				<td> </td>
				<td>   </td>
			</tr>
		</table>
	</form>
	</div>
	<div id="dlgg" ></div> 
</div>

<script type="text/javascript">
 
var taskLog;
//提交表单
$('#mainform').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
    	
    	var str = $("#operation").val();
	    if(str =='1' || str =='2'|| str =='4'){
		   if($("#qualification").val()==""){
			   // alert("请输入约束条件！");
			   parent.$.messager.show({ title : "提示",msg: "请输入约束条件！" , position: "bottomRight" });
			  $("#qualification").focus();
			  isValid= false; 
		   }
	    }
    	
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	var obj = eval('(' + data + ')');
    	parent.$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
    	setTimeout(function () {
           task.panel('close');
           $("#dg3").datagrid('reload');
            
        }, 2000);
    	// successTip(data,dg,d);
    	
    }    
});   


 function changeOperation(){
	 var str = $("#operation").val();
	// alert( $("#operation").val()  );
	if(str =='1' || str =='2'|| str =='4'){
		$("#qualificationTr").show();
	}else{
		$("#qualificationTr").hide();
	}
	 
 }


</script>
</body>
</html>