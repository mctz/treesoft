<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据API编辑</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/collect/collectSave" method="post">
	   <input id="id" name="id" type="hidden" value="${collect.id }"   />
		<table class="formTable">
			 <tr>
				<td align="right"> 名   称：</td>
				<td ><input id="name"  name="name" type="text" value="${collect.name}"   class="easyui-validatebox"   data-options="width:150,required:'required'"/>	</td>
		      	<td align="right"> 别  名：</td>
				<td colspan="3" ><input id="alias"  name="alias" type="text" value="${collect.alias}"   class="easyui-validatebox"   data-options="width:150,required:'required'"/>	
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="别名是数据API接口的唯一标识,请输入英文."></a>
				</td>
		    </tr>
		    
			<tr>
				<td>数据库配置：</td>
				<td>
				<select id="sourceConfigId" name="sourceConfigId" class="esayui-combobox"  style="width:150px;" onchange="changeConfig(this);" >
				   <option value="" > </option>
		           <c:forEach var="config" items="${ configList }"   >
		                 <option value="${config.id}" title="${config.ip}:${config.port}:${config.databaseName}"  <c:if test="${ collect.sourceConfigId == config.id}"> selected </c:if>  >${config.name}</option>
		           </c:forEach>
				</select>
				</td>
				
				<td>&nbsp;&nbsp;&nbsp;&nbsp;数据库名称：</td>
				<td><input id="sourceDatabase"  name="sourceDatabase" type="text" value="${collect.sourceDatabase }"   class="easyui-validatebox"   data-options="width:150,required:'required'"/>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="Oracle请填写实例名"></a>
				</td>				
		     </tr>
			 
			 <tr>
				<td align="right">状   态： </td>
				<td>
				<select id="status" name="status" class="esayui-combobox" style="width:150px;"   >
				   <option value="0" <c:if test="${collect.status=='0' }"> selected </c:if> >启用</option>  
				   <option value="1" <c:if test="${collect.status=='1' }"> selected </c:if> >停用</option> 
				</select>
				</td>
				
				<td align="right">模版格式： </td>
				<td> 
				  <select id="model" name="model" class="esayui-combobox" style="width:150px;"   >
				   <option value="1" <c:if test="${collect.model=='1' }"> selected </c:if> >模版1</option>  
				   <option value="2" <c:if test="${collect.model=='2' }"> selected </c:if> >模版2</option> 
				   <option value="3" <c:if test="${collect.model=='3' }"> selected </c:if> >模版3</option>
				   <option value="4" <c:if test="${collect.model=='4' }"> selected </c:if> >模版4</option>
				  </select>
				  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="每种模版返回的数据格式不同，可以打开查看"></a>
				 </td>
			  </tr>
			 
			  <tr>
				<td align="right"> app Key：</td>
				<td> <input id="appKey"  name="appKey" type="text" value="${collect.appKey}"  onClick="changeAppKey()" class="easyui-validatebox"  data-options="width:150,required:'required'"  />  
			     	<a href="javascript:changeAppKey()"  onClick="changeAppKey()"  class="easyui-linkbutton" iconCls="icon-standard-help"   title="通过appKey进行权限验证，点击生成或修改Key."></a>
				 </td>
				<td align="right"> &nbsp; </td>
				<td> &nbsp;  </td>
		      </tr>
			 
			  <tr>
			  <td align="right">SQL语句：</td>
			     <td colspan="3"><textarea id="doSql" name="doSql" type="text"   style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${collect.doSql}</textarea> 
			       <a href="#"   class="easyui-linkbutton" iconCls="icon-standard-help"   title="当提交查询参数时，在SQL中使用  #(变量名) 占位，&#13;例如：select * from t_user where uid ='#(变量名)' &#13;当提交uid参数时：select * from t_user where uid ='#(uid)'"></a>
			    </td>
			 </tr>			 
		     
			 <tr>
			  <td align="right">备注说明：</td>
			  <td colspan="3"><textarea id="comments" name="comments" type="text"   style="width:410px;height:50px" data-options="width: 350,required:'required' "  >${collect.comments}</textarea>  </td>
			 </tr>
		      
		     <tr>
				<td> &nbsp; </td>
				<td colspan="3"> &nbsp; </td>
		     </tr>
		     
		</table>
		 
	</form>
</div>

<script type="text/javascript">

var connSuccess = false;

function addUpdateCollect(){  
	  var  id = $('#id').val();
	  var  name = $('#name').val();
	  var  alias = $('#alias').val();
      var  status = $('#status option:selected').val();
      var  sourceConfigId = $('#sourceConfigId option:selected').val();
      var  sourceDatabase = $('#sourceDatabase').val();
      var  toplimit = $('#toplimit').val();
      var  model = $('#model').val();
      var  appKey = $('#appKey').val();
      var  doSql = $('#doSql').val();
      var  comments = $('#comments').val();
      
      // alert( permission );
      var isValid = $("#mainform").form('validate');
      if( !isValid ){
    	   return isValid;	// 返回false终止表单提交
      }
      
	  $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/collect/collectSave" ,
                    data: JSON.stringify( { 'id':id ,'name':name,'alias':alias, 'status':status ,'sourceConfigId':sourceConfigId ,'sourceDatabase':sourceDatabase,'appKey':appKey ,'doSql':doSql ,'model':model,'comments':comments } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                      var obj = eval('(' + data + ')');
                      var status = obj.status ;
            	       if(status == 'success' ){
            	            parent.$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
            	            setTimeout(function () {
                                 collect.panel('close');
                                 $('#dgForCollect').datagrid('reload');
                             }, 1500);
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
            	       }
            	     }  
       });
 }

 function changeConfig(obj){
	 var title = obj.options[obj.options.selectedIndex].title ;
	 var titleArray =  title.split(":");
	 $("#sourceDatabase").val( titleArray[2] );
	 $("#sourceDatabase").focus();
 }

 function changeAppKey(){
	 var str = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
         str = str + (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
         str = str + (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
 	$('#appKey').val( str );
 }
 
</script>
</body>
</html>