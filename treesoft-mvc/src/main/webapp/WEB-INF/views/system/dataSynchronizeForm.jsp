<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>dataSynchronize</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
    <div style="float:left;">
	<form id="mainform" action="${ctx }/system/permission/i/dataSynchronizeUpdate" method="post">
	    <input id="id" name="id" type="hidden" value="${dataSynchronize.id }"   />
	    <input id="status" name="status" type="hidden" value="${dataSynchronize.status }"   />
		<table class="formTable">
			
			<tr>
				<td>&nbsp;名称：&nbsp;</td>
				<td colspan="3" ><input id="name"  name="name" type="text" value="${dataSynchronize.name }"   class="easyui-validatebox"   data-options="width:415,required:'required'"/>	</td>
		    </tr>
		    
			<tr>
				<td>源数据库：</td>
				<td>
				<select id="souceConfigId" name="souceConfigId" class="esayui-combobox"  style="width:150px;"    >
				   <option value="" > </option>
		           <c:forEach var="config" items="${ configList }"   >
		                 <option value="${config.id}" title="${config.ip}:${config.port}"  <c:if test="${ dataSynchronize.souceConfigId == config.id}"> selected </c:if>  >${config.name}</option>
		           </c:forEach>
				</select>
				</td>
				
				<td>&nbsp;&nbsp;&nbsp;&nbsp;数据库名：</td>
				<td><input id="souceDataBase"  name="souceDataBase" type="text" value="${dataSynchronize.souceDataBase }"   class="easyui-validatebox"   data-options="width: 150,required:'required'"/>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="Oracle请填写实例名"></a>
				</td>				
		    </tr>
			 
			 <tr>
			  <td>查询SQL：</td>
			  <td colspan="3"><textarea id="doSql" name="doSql" type="text"   style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${dataSynchronize.doSql }</textarea>   </td>
			 </tr>
			 
			 <tr>
				<td>&nbsp; </td>
				<td> </td>
				<td> </td>
				<td> </td>
			</tr>
			 
			 <tr>
				<td>目 标 库： </td>
				<td> 
				 <select id="targetConfigId" name="targetConfigId" class="esayui-combobox"  style="width:150px;"   >
				       <option value="" > </option>
		               <c:forEach var="config" items="${ configList }"   >
		                 <option value="${config.id}"  title="${config.ip}:${config.port}" <c:if test="${dataSynchronize.targetConfigId == config.id}"> selected </c:if>  > ${config.name} </option>
		              </c:forEach>
				</select>
				 </td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;目标库名：</td>
				<td><input id="targetDataBase"  name="targetDataBase" type="text" value="${dataSynchronize.targetDataBase }" class="easyui-validatebox"  data-options="width: 150,required:'required'" /> </td>
			</tr>
			 
			 <tr>
				<td>目标表名： </td>
				<td> <input id="targetTable"  name="targetTable" type="text" value="${dataSynchronize.targetTable }" class="easyui-validatebox"  data-options="width: 150,required:'required'" />   </td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;执行动作： </td>
				<td>
				 <select id="operation" name="operation"  class="esayui-combobox"  style="width:150px;"  onchange="changeOperation()"  >
				   <option value="0" <c:if test="${dataSynchronize.operation=='0' }"> selected </c:if>  >新增</option>  
				   <option value="1" <c:if test="${dataSynchronize.operation=='1' }"> selected </c:if>  >更新</option> 
				   <option value="2" <c:if test="${dataSynchronize.operation=='2' }"> selected </c:if>  >覆盖</option>  
				  <!--  
				   <option value="3" <c:if test="${dataSynchronize.operation=='3' }"> selected </c:if>  >追加(暂未开发)</option> 
				  -->
				   <option value="4" <c:if test="${dataSynchronize.operation=='4' }"> selected </c:if>  >删除</option> 
				  
				   
				</select>
				</td>
			</tr>
			
			 <tr>
				<td> 调度计划： </td>
				<td> <input id="cron"  name="cron" type="text" value="${dataSynchronize.cron }" class="easyui-validatebox"  data-options="width: 150,required:'required'" />
				  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="调度计划使用Cron表达式,&#13;Cron表达式的格式：秒 分 时 日 月 周 年(可选)。&#13;Cron表达式范例：&#13;  每隔20秒执行一次：*/20 * * * * ?&#13;  每隔1分钟执行一次：0 */1 * * * ?&#13;  每天23点执行一次：0 0 23 * * ?&#13;  每天凌晨1点执行一次：0 0 1 * * ?&#13;  每月1号凌晨1点执行一次：0 0 1 1 * ?&#13;  每月最后一天23点执行一次：0 0 23 L * ?
				  "></a>
			    </td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;是否启用：</td>
				<td>
				 <select id="state" name="state"  class="esayui-combobox"   style="width:150px;"  >
				   <option value="0" <c:if test="${dataSynchronize.state=='0' }"> selected </c:if>  >启用</option> 
				   <option value="1" <c:if test="${dataSynchronize.state=='1' }"> selected </c:if>  >禁用</option>  
				</select>  </td>
			</tr>
			 
			 
			 <tr id ="qualificationTr" <c:if test="${dataSynchronize.operation =='0' || dataSynchronize.operation == null }"> style="display:none" </c:if>   >
				<td>约束条件:</td>
				<td colspan="3" ><input id="qualification"  name="qualification" type="text" value="${dataSynchronize.qualification }"   class="easyui-validatebox"   data-options="width:410"/><font color="red">*</font>		
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="约束字段条件,&#13;多个字段用逗号分隔。 &#13;示例：id,sn &#13;必填。"></a>
				</td>
		    </tr>
			 
			 <tr>
			  <td>备注说明：</td>
			  <td colspan="3"><textarea id="comments" name="comments" type="text" value="${dataSynchronize.comments }" style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${dataSynchronize.comments}</textarea>  </td>
			 </tr>
			 
			  <tr>
				<td> &nbsp;</td>
				<td> </td>
				<td> </td>
				<td> </td>
			</tr>
			 
			 <tr>
			  <td>功能说明：</td>
			  <td colspan="3"> 1、按调度计划定时进行数据交换，使用企业级任务调度框架。 <br>
                               2、数据来源通过查询SQL抽取结果集,mongoDB请直接输入shell查询命令。<br>
                               3、支持MySql，Oracle，PostgreSQL，SQL Server，mongoDB交互同步数据。 <br>
                               4、支持各种数据源之间同步数据,支持新增、删除、更新、覆盖、追加操作。 <br>
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
 
var dataSynchronizeLog;
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
           dataSynchronize.panel('close');
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