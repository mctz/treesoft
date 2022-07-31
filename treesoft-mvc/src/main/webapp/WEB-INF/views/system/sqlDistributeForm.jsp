<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>SQL分发</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
 
<body>
<div>
    <div >
     
	<form id="mainform" action="${ctx }/system/sqlDistribute/sqlDistributeUpdate" method="post">
	    <input id="id" name="id" type="hidden" value="${task.id }"   />
	    <input id="status" name="status" type="hidden" value="${task.status }"   />
		<table class="formTable">
			
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;任务名称：</td>
				<td  ><input id="name"  name="name" type="text" value="${task.name }"   class="easyui-validatebox"   data-options="width:415,required:'required'"/>	</td>
		    </tr>
			 
			 <tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;是否启用：</td>
				<td>
				 <select id="state" name="state"  class="esayui-combobox"   style="width:150px;"  >
				   <option value="0" <c:if test="${task.state=='0' }"> selected </c:if>  >启用</option> 
				   <option value="1" <c:if test="${task.state=='1' }"> selected </c:if>  >停用</option>  
				</select>  </td>
			</tr>
			 <tr>
			  <td>&nbsp;&nbsp;&nbsp;&nbsp;SQL语句：</td>
			  <td>
			       <textarea id="doSql" name="doSql" type="text"   style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${task.doSql }</textarea> 
			      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="请填写数据库维护SQL ,只能输入一段SQL ,无需以分号结尾"></a>
			  </td>
			 </tr>
			 
			 <tr>
			  <td>&nbsp;&nbsp;&nbsp;&nbsp;备注说明：</td>
			  <td ><textarea id="comments" name="comments" type="text" value="${task.comments }" style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${task.comments}</textarea>  </td>
			 </tr>
			 
			  <tr>
				<td> &nbsp;</td>
				<td> </td>
			</tr>
			
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;数据库配置：</td>
				<td> 
				    
				   <table id="databasesList">
				     <c:forEach var="entity" items="${task.databaseConfigList}" >
				        <tr> 
				          <td>
				              
                               <select  name="databaseConfigId"   style="width:180px" >                                   
                                  <c:forEach var="config" items="${ configList }"   >
                                        <option value="${config.id}"  <c:if test="${entity.databaseConfigId == config.id}"> selected</c:if></option>  ${config.name} </option>
		                          </c:forEach>
                              </select>
				              <input type="text" name="databaseName" value="<c:out value="${entity.databaseName}"/>"    style="width:180px"> 
				              <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png"    onclick="addrow2( this )" style="vertical-align:middle;" title="新增行" /> 
				              <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" onclick="delrow( this )"  style="vertical-align:middle;" title="删除行" />  
				          </td>   
				        </tr>
				     </c:forEach>
				   </table>
				
				</td>
			</tr>
		</table>
		
	</form>
	</div>
</div>

<script type="text/javascript">
 
var selectTemp;
var databaseConfigList;

$(function(){  
    getAllConfigList();
    
});
var taskLog;
//提交表单
$('#mainform').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	var obj = eval('(' + data + ')');
    	parent.$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
    	setTimeout(function () {
           sqlDistribute.panel('close');
           $("#dg3").datagrid('reload');
            
        }, 2000);
    	// successTip(data,dg,d);
    }    
});   

function getAllConfigList(){
	$.ajax({
		  type:'GET',
		   contentType:'application/json;charset=utf-8',
           url: baseUrl+"/system/sqlDistribute/getAllConfigList",
           //成功返回之后调用的函数             
            success:function(data){
             var status = data.status ;
             if(status == 'success' ){
            	 databaseConfigList = data.rows;
            	 if( databaseConfigList.length == 0 ){
            		return;
            	 }
            	 
                 var optionStr ="";
	             for (i = 0; i < databaseConfigList.length; i++) { 
	                 optionStr = optionStr +"<option value=" + databaseConfigList[i].id + " > "+ databaseConfigList[i].name  +" </option>";
	             }
	            
	             var str2 = '<select  name="databaseConfigId" style="width:180px;" onchange="changeDb(this)" > ' + optionStr + '</select>';                                
                 str2 = str2 + ' <input type="text" name="databaseName" value="'+ databaseConfigList[0].databaseName +'" style="width:180px" >';
                 var str3='<tr><td> '+ str2 +' <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png"  onclick="addrow2( this )" style="vertical-align:middle;" title="新增行" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" onclick="delrow( this )" style="vertical-align:middle;" title="删除行"  /> </td> </tr> ';
            	 selectTemp = str3;
            	 if($("#id").val()=='' ){
            		$("#databasesList").append( str3 );   
            	 }
            	 
             }else{
            	  parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
               }
             }  
   });
}

function changeDb(obj ){
	for (i = 0; i < databaseConfigList.length; i++) { 
		if( obj.value == databaseConfigList[i].id){
			$(obj).next().val( databaseConfigList[i].databaseName );
		}
	}
	
	//alert( obj.value );
}

function addrow2( obj ){
	 $(obj).parent().parent().parent().append( selectTemp );
}

function delrow( obj ){
	//alert( obj );
	$(obj).parent().remove();
}

</script>
</body>
</html>