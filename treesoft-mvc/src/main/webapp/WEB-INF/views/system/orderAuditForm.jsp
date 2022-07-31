<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>SQL工单审核</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>

<body>
<div>
    <div>
	<form id="mainform" action="${ctx }/order/orderUpdate" method="post">
	    <input id="id" name="id" type="hidden" value="${order.id }"  />
		<table class="formTable">
			<tr>
				<td>数据库配置：</td>
				<td>
				<select id="configId" name="configId" class="esayui-combobox"  style="width:150px;" onchange="changeConfig(this);" disabled="true"  >
				   <option value="" > </option>
		           <c:forEach var="config" items="${ configList }"   >
		                 <option value="${config.id}" title="${config.ip}:${config.port}"  <c:if test="${ order.configId == config.id}"> selected </c:if>  >${config.name}</option>
		           </c:forEach>
				</select>
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;数据库名：</td>
				<td><input id="databaseName"  name="databaseName" type="text" value="${order.databaseName }"   class="easyui-validatebox"   data-options="width: 150" readonly="true" />	</td>				
		    </tr>
			 
			 <tr>
			  <td>SQL语句：</td>
			  <td colspan="3"><textarea id="doSql" name="doSql" type="text"  readonly="true" >${order.doSql }</textarea>   </td>
			 </tr>
			 
			 <tr>
			  <td>备注说明：</td>
			  <td colspan="3"><textarea id="comments" name="comments" type="text" value="${order.comments }" style="width:450px;height:50px" data-options="width:350" readonly="true" >${order.comments}</textarea>  </td>
			 </tr>
			 
			 <tr>
			  <td>SQL附件： </td>
			  <td colspan="3">  
			      <div id="attachDiv" style="width:450px;" >
			       <c:forEach var="entity" items="${order.attachList}" >
			          <span style="cursor:pointer" >
			           <a href="#" target="_blank"  type="text/html" style="text-decoration:none" onclick="downloadFile('${entity.id}')" >
			               <img src="${ctx}/static/images/old-versions.png" /> ${entity.fileName}
			           </a>
			         </span>
			       	</c:forEach>
			       </div>
			   </td>
			 </tr>
			 
			 <tr>
			  <td>审核意见：</td>
			  <td colspan="3"><textarea id="remark" name="remark" type="text" value="${order.remark}" style="width:450px;height:40px" data-options="width:350"   >${order.remark}</textarea>  </td>
			 </tr>
			  <tr>
				<td> &nbsp;</td>
				<td> </td>
				<td> </td>
				<td> </td>
			</tr>
			 
			<tr align="center">
				<td colspan="4"> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${order.status==0||order.status==1||order.status==2||order.status==3}">  
				  <a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="auditOrderPass()">通过</a> &nbsp;&nbsp;&nbsp;
				  <a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"  onclick="auditOrderNoPass()">不通过</a> &nbsp;&nbsp;&nbsp;
				  <a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'"  onclick="auditOrderRebut() ">驳回</a> &nbsp;&nbsp;&nbsp;
				</c:if>  
				  <a id="saveBtn" href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"  onclick="closeOrderPanel()"  >取消</a> &nbsp;&nbsp;&nbsp;
				</td>
			</tr>			  
			 
		</table>
	</form>
	</div>
</div>

<script type="text/javascript">
  var doSql = CodeMirror.fromTextArea(document.getElementById('doSql'), {
       mode: "text/x-mysql",
       theme: "eclipse", //主题 
       lineNumbers: true,
       lineWrapping: true, // 自动换行
       autofocus: false,
       readOnly :true
  });  
  doSql.setSize('450px','140px');

//提交表单
$('#mainform').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
    	   
		if($("#doSql").val()==""){
			   // alert("请输入约束条件！");
			   parent.$.messager.show({ title : "提示",msg: "请输入SQL语句！" , position: "bottomRight" });
			  $("#doSql").focus();
			  isValid= false; 
		 }    
    	
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	var obj = eval('(' + data + ')');
    	parent.$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
    	setTimeout(function () {
           order.panel('close');
           $("#dg6").datagrid('reload');
            
        }, 2000);
    	// successTip(data,dg,d);
    	
    }    
});  

   //审核通过
   function auditOrderPass(){
	   var ids = [];
	   ids.push($('#id').val() );
       $.ajax({
		   type:'POST',
		   contentType:'application/json;charset=utf-8',
           url:"${ctx}/order/auditOrderPass",
           data: JSON.stringify( { 'ids':ids,'remark':$('#remark').val() } ),
           datatype: "json", 
          //成功返回之后调用的函数             
           success:function(data){
               var status = data.status ;
               if(status == 'success' ){
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	   setTimeout(function () {
                     order.panel('close');
                     $("#dg6").datagrid('reload');
                   }, 2000);
            	}else{
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	}
           }  
      });
  }
   
   //审核不通过
   function auditOrderNoPass(){
	   var ids = [];
	   ids.push($('#id').val() );
       $.ajax({
		   type:'POST',
		   contentType:'application/json;charset=utf-8',
           url:"${ctx}/order/auditOrderNoPass",
           data: JSON.stringify( { 'ids':ids,'remark':$('#remark').val() } ),
           datatype: "json", 
          //成功返回之后调用的函数             
           success:function(data){
               var status = data.status ;
               if(status == 'success' ){
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	   setTimeout(function () {
                     order.panel('close');
                     $("#dg6").datagrid('reload');
                   }, 2000);
            	}else{
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	}
           }  
      });
  }
    
   //审核 驳回
   function auditOrderRebut(){
	   var ids = [];
	   ids.push($('#id').val() );
       $.ajax({
		   type:'POST',
		   contentType:'application/json;charset=utf-8',
           url:"${ctx}/order/auditOrderRebut",
           data: JSON.stringify( { 'ids':ids,'remark':$('#remark').val() } ),
           datatype: "json", 
          //成功返回之后调用的函数             
           success:function(data){
               var status = data.status ;
               if(status == 'success' ){
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	   setTimeout(function () {
                     order.panel('close');
                     $("#dg6").datagrid('reload');
                   }, 2000);
            	}else{
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	}
           }  
      });
  }
   
  function closeOrderPanel( ){	
	 order.panel('close');
  }
 
  function downloadFile( id ){
	   var url = "${ctx}/system/fileUpload/fileDownload/"+id ;
       window.open( url );
  }

</script>
</body>
</html>