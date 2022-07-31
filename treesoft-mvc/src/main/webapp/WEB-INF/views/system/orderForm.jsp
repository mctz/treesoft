<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>SQL工单申请</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<style type="text/css"> 
  
</style> 
</head>
<body>
<div>
    <div >
	<form id="mainform" action="${ctx }/order/orderUpdate" enctype="multipart/form-data" method="post">
	    <input id="id" name="id" type="hidden" value="${order.id }"  />
	    <input id="configName" name="configName" type="hidden" value="${order.configName }" />
		<table class="formTable">
			<tr>
				<td>数据库配置：</td>
				<td>
				<select id="configId" name="configId" class="esayui-combobox"  style="width:150px;" onchange="changeConfig(this);"   >
				   <option value="" > </option>
		           <c:forEach var="config" items="${ configList }"   >
		                 <option value="${config.id}" title="${config.ip}:${config.port}:${config.databaseName}" <c:if test="${ order.configId == config.id}"> selected </c:if>  >${config.name}</option>
		           </c:forEach>
				</select>
				</td>
				
				<td>&nbsp;&nbsp;&nbsp;&nbsp;数据库名：</td>
				<td><input id="databaseName"  name="databaseName" type="text" value="${order.databaseName }"   class="easyui-validatebox"   data-options="width: 150,required:'required'"/>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="Oracle请填写服务名"></a>
				</td>				
		    </tr>
			 
			 <tr>
			  <td>SQL语句：</td>
			  <td colspan="3">
			     <textarea id="doSql" name="doSql" type="text"  data-options="required:'required' "  >${order.doSql }</textarea>  	
			     <font color="red">注：可输入多行SQL命令，命令请以分号;结尾。</font>  	    
			  </td>
			 </tr>
			 
			 <tr>
			  <td>备注说明：</td>
			  <td colspan="3"><textarea id="comments" name="comments" type="text" value="${order.comments }" style="width:450px;height:30px" data-options="width: 350,required:'required' "  <c:if test="${order.status==1||order.status==2||order.status==4}"> readonly="true"  </c:if>  >${order.comments}</textarea>  </td>
			 </tr>
			 
			 <tr>
			  <td>附件提交： </td>
			  <td colspan="3">  
			       <c:if test="${order.status==null||order.status==0||order.status==3}"> 
			         <input type="file" name="attachments" id="attachments"  accept=".txt,.sql"  multiple="multiple" onchange="fileSelected();" />  <br>
			       </c:if> 
			       
			       <div id="attachDiv" style="width:450px;">
			       <c:forEach var="entity" items="${order.attachList}" >
			          <span style="cursor:pointer" >
			           <a href="#" target="_blank"  type="text/html" style="text-decoration:none" onclick="downloadFile('${entity.id}')" >
			               <img src="${ctx}/static/images/old-versions.png" /> ${entity.fileName}
			           </a>
			           <c:if test="${order.status==null||order.status==0||order.status==3}">
			              <img src="${ctx}/static/images/delete.png" id="${entity.id}" onclick="deleteAttach(this)" />&nbsp;&nbsp;&nbsp;
			              <input id='attachmentIds' name='attachmentIds' type='hidden' value='${entity.id}' />
			           </c:if> 
			         </span>
			       	</c:forEach>
			       </div>
			       
			   </td>
			 </tr>
			 
			<c:if test="${order.status==1||order.status==2||order.status==3||order.status==4}">  
			 <tr>
			  <td>审核意见：</td>
			  <td colspan="3"> ${order.remark }  </td>
			 </tr>
			</c:if>  
			<c:if test="${order.status==4}">   
			  <tr>
			    <td>执行信息：</td>
			    <td colspan="3"><textarea id="runMessage" name="runMessage" type="text" value="${order.runMessage }" style="width:450px;height:50px" data-options="width: 350 " readonly="true" >${order.runMessage}</textarea>  </td>
			   </tr>
			</c:if> 
			<tr>
				<td> &nbsp;</td>
				<td> </td>
				<td> </td>
				<td> </td>
			</tr>
			<tr align="center">
				<td colspan="4"> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				  
				  <c:if test="${order.status==null||order.status==0||order.status==3}">  
				    <a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveOrder()">提交</a> &nbsp;&nbsp;&nbsp;
				  </c:if> 
				  <c:if test="${order.status==1}">  
				    <a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-run'" onclick="runOrder()">执行</a> &nbsp;&nbsp;&nbsp;
				  </c:if> 
				  <a id="saveBtn" href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeOrderPanel()" >取消</a> &nbsp;&nbsp;&nbsp;
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
       autofocus: true
  });  
  doSql.setSize('450px','140px');
  
  var orderStatus = "<c:out default="0" value="${order.status}"/>";
  if(orderStatus==1 ||orderStatus==2 ||orderStatus==4 ){
	  doSql.setOption("readOnly", true)
  }
  
  //提交表单
  $('#mainform').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
    	   
		//if( doSql.getValue()==""){
		//	   parent.$.messager.show({ title : "提示",msg: "请输入SQL语句！" , position: "bottomRight" });
		//	  $("#doSql").focus();
		//	  isValid= false; 
		// }  		 
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	var obj = eval('(' + data + ')');
    	$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
    	setTimeout(function () {
           order.panel('close');
           $("#dgOrder").datagrid('reload');
            
        }, 2000);
    	// successTip(data,dg,d);
    }    
   });   

   //执行工单
   function runOrder(){
	   $.easyui.loading({ msg: "执行中，请稍等！" });
	   var id = $('#id').val();
       $.ajax({
		   type:'GET',
		   contentType:'application/json;charset=utf-8',
           url:"${ctx}/order/orderRun/"+id ,
           datatype: "json", 
          //成功返回之后调用的函数             
           success:function(data){
               var status = data.status ;
               $.easyui.loaded();
               if(status == 'success' ){
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	   setTimeout(function () {
                     order.panel('close');
                     $("#dgOrder").datagrid('reload');
                   }, 2000);
            	}else{
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	   $("#dgOrder").datagrid('reload');
            	}
           }  
      });
  }

  function saveOrder( ){	
	 $("#mainform").submit(); 
  }
 
  function closeOrderPanel( ){	
	 order.panel('close');
  }

  function changeConfig(obj){
	 $("#configName").val(obj.options[obj.options.selectedIndex].text );
	 var title = obj.options[obj.options.selectedIndex].title ;
	 var titleArray =  title.split(":");
	 $("#databaseName").val( titleArray[2] );
	 $("#databaseName").focus();
  }

  function fileSelected() {
	 // var file = document.getElementById('attachments').files[0];
     // var fileName = file.name;
      $.easyui.loading({ msg: "附件上传中..." });
	  uploadFile()
  }
  
  function uploadFile() {
          var fd = new FormData();
          fd.append("fileToUpload", document.getElementById('attachments').files[0]);
          var xhr = new XMLHttpRequest();
         // xhr.upload.addEventListener("progress", uploadProgress, false);
          xhr.addEventListener("load", uploadComplete, false);
          xhr.addEventListener("error", uploadFailed, false);
          xhr.addEventListener("abort", uploadCanceled, false);
          xhr.open("POST", "${ctx}/system/fileUpload/fileUploadCommon");
          xhr.send(fd);
  }
  
   function uploadComplete(evt) {
         /* 服务器返回数据*/
        var message = evt.target.responseText;
        var data = testJson = eval("(" + message + ")");
        //alert( message );
        var file = document.getElementById('attachments').files[0];
        var fileName = file.name;
        var str = " <span style='cursor:pointer'>" 
        +"<a href='#' target='_blank'  type='text/html' style='text-decoration:none' onclick='downloadFile(\'" + data.id + "\')' >"
        +" <img src='${ctx}/static/images/old-versions.png' />   "+ fileName
        +"</a>"
		+" <img src='${ctx}/static/images/delete.png' id='"+ data.id +"' onclick='deleteAttach(this)' />&nbsp;&nbsp;&nbsp;"
		+" <input id='attachmentIds' name='attachmentIds' type='hidden' value='" + data.id + "' />"
		+" </span>";
		
        $.easyui.loaded();        
        $("#attachDiv").append(str );
        $.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
        
      }
   
   function uploadFailed(evt) {
    	$.messager.show({ title : "提示",msg: "上传出错.", position: "bottomRight" });
   }
  
   function uploadCanceled(evt) {
    	$.messager.show({ title : "提示",msg: "上传已由用户或浏览器取消删除连接.", position: "bottomRight" });
   }
   
   function deleteAttach(obj){
	    $.easyui.messager.confirm("操作提示", "您确定删除附件吗？", function (c) {
           if (c) {
        	  // alert( obj.id );
        	    $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/fileUpload/fileDelete",
                    data: JSON.stringify({'id':obj.id}),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       data = eval("(" + data + ")");
                       var status = data.status ;
            	       if(status == 'success' ){
            	            parent.$.messager.show({ title : "提示",msg:data.mess , position: "bottomRight" });
            	             $(obj).parent().remove();
            	           // $(this).remove();
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg:data.mess, position: "bottomRight" });
            	       }
            	     }  
              });
           }
        });
   }
   
   function downloadFile( id ){
	   var url = "${ctx}/system/fileUpload/fileDownload/"+id ;
       window.open( url );
   }
      
</script>
</body>
</html>