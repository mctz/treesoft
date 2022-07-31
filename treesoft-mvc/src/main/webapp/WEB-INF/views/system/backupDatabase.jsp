<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

</head>
<body>

 <div id="tb3" style="padding:5px;height:auto">
                         <div>
                          
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-database-save" plain="true" id="addRowButton"  onclick="backupDB()"> 库备份</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	       		            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-database-edit" plain="true" id="addRowButton"  onclick="restoreDB()"> 库还原</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	       		           
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-database-go" plain="true" id="addRowButton"  onclick="OpenFileUpload()">文件提交</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	       		           
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-zip" plain="true" id="rarButton"   onclick="zipFile()">压缩文件</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-database-delete" plain="true" id="delButton"   onclick="deleteBackupFile()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
                           <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                      
                         </div> 
  </div>
  
   <div id="win" class="easyui-window" title="文件上传" style="width:450px;height:250px;"  data-options="iconCls:'icon-save',modal:true, closed:true,maximizable:false,minimizable:false,collapsible:false " > 
        <form id="ImportForm" runat="server"enctype="multipart/form-data" >
         <div style="width:250px;height:20px;margin-top:20px;margin-left:50px;align:center" >
                 <input type="file" name="fileToUpload" id="fileToUpload" multiple="multiple" onchange="fileSelected();" />
           
         </div>
          <div id="fileName" style="padding: 10px">  </div>
          <div id="fileSize" style="padding: 10px">  </div>
        
        <div id="progressNumber" class="easyui-progressbar" style="width: 400px;margin-left:10px;">     </div>
        
        <div style="padding:5px;text-align:center;">  
            <a href="#" class="easyui-linkbutton" icon="icon-ok"   onclick="uploadFile()" >上传</a>  
            <a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="closeWindow()">关闭</a>  
        </div>  
        </form>
</div>
 <input type="hidden" id="tableName" value="${tableName}" >
 <input type="hidden" id="databaseName" value="${databaseName}" >
 <input type="hidden" id="databaseConfigId" value="${databaseConfigId}" >
 <table id="dg3"></table> 
 
<script type="text/javascript">
var dgg;
var tableName;
var databaseName;
var databaseConfigId;

$(function(){  
	databaseName = $("#databaseName").val();
	tableName = $("#tableName").val();
	databaseConfigId = $("#databaseConfigId").val();
    initData();
});

function initData(){
	dgg=$('#dg3').datagrid({     
	method: "get",
    url: '${ctx}/system/permission/i/backupDatabaseData/'+Math.random(), 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'fileName',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
        {field:'fileName',title:'文件名',sortable:true,width:100},
        {field:'fileLength',title:'文件大小',sortable:true,width:100},
        {field:'fileModifiedDate',title:'创建时间',sortable:true,width:100,tooltip: true},
        {field : 'action',title : '下载',
			formatter : function(value, row, index) {
				//return '<a href="${ctx}/backup/'+row.fileName+'" target="_blank"   ><div class="icon-hamburg-down" style="width:16px;height:16px" title="下载"></div></a>';
				return '<a href="javascript:downloadFile3(\''+row.fileName+ '\')"   ><div class="icon-hamburg-down" style="width:16px;height:16px" title="下载"></div></a>';
			}
        }
	  	
    ]], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false
   }); 
  }


    function backupDB(){
      $.easyui.messager.confirm("操作提示", "您确定备份数据库"+databaseName+" 吗？", function (c) {
      if (c) {
         $.easyui.loading({ msg: "备份中，请稍等！" });
	     parent.$.messager.show({ title : "提示",msg: "备份中，请稍等！" , position: "bottomRight" });
	     $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/backupDatabaseExecute/"+ databaseConfigId ,
                    data: JSON.stringify( { 'databaseName':databaseName  } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
                       $.easyui.loaded();
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	            parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	           // window.setTimeout(function () { $('#dg3').datagrid('reload'); }, 1000);
            	           // $('#dg3').datagrid('reload')
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	       }
            	     }  
         });
	   }
       });
    }
    
    function restoreDB(){
       var checkedItems = $('#dg3').datagrid('getChecked');
	   var length = checkedItems.length;
	  
	   if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	   }
	 
	   if(checkedItems.length > 1 ){
		 parent.$.messager.show({ title : "提示",msg: "您只需选择一行备份数据！", position: "bottomRight" });
		 return ;
	   }
	   
	   var ids = [];
       $.each( checkedItems, function(index, item){
    	  ids.push( item["fileName"] );
      }); 
	  
     // databaseName = $("#databaseSelect",window.parent.document).val();
       // alert( databaseName );
      
      $.easyui.messager.confirm("操作提示", "您确定用备份文件，还原数据库"+databaseName+" 吗？", function (c) {
        if (c) {
        	  $.easyui.loading({ msg: "数据库还原中，请稍等！" });
              parent.$.messager.show({ title : "提示",msg: "数据库还原中，请稍等！" , position: "bottomRight" });
	          $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/restoreDB/"+databaseConfigId,
                    data: JSON.stringify( { 'databaseName':databaseName, 'ids':ids  } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
                       $.easyui.loaded();
                       // $('#dg3').datagrid('reload');
            	       if(status == 'success' ){
            	    	    parent.$('#pid').treegrid('reload');
            	            parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	       }
            	     }  
              });
         }
       });
    }
      
    function downloadFile3( fileName ) { 
    	var url = "${ctx}/system/permission/i/backupFileDownload/"+fileName ;
    	window.open( url );
    }
    
   //删除文件 
   function deleteBackupFile(){
	 
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["fileName"] );
     }); 
	 
       
	 $.easyui.messager.confirm("操作提示", "您确定要删除"+length+"个文件吗？", function (c) {
                if (c) {
                	
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/deleteBackupFile",
                    data: JSON.stringify( { 'databaseName':databaseName, 'tableName':tableName, 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "删除成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }
   
    //压缩文件 
   function zipFile(){
	 
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["fileName"] );
     }); 
	  parent.$.messager.show({ title : "提示",msg: "文件压缩中，请稍等！" , position: "bottomRight" });
      $.easyui.loading({ msg: "文件压缩中，请稍等！" });
            $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/zipFile",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
                       $.easyui.loaded();
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "压缩成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
               });
             
   }
 
   function refresh(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
   }
 
  
 function closeWindow(){
	 $('#win').window('close');  
 }
   
   function OpenFileUpload() {
	   $('#win').window('open');
	   document.getElementById('fileName').innerHTML = '' ;
       document.getElementById('fileSize').innerHTML = '';
       $('#progressNumber').progressbar('setValue', 0);
       var obj = document.getElementById('fileToUpload') ;  
       obj.outerHTML=obj.outerHTML; 
       
   }
    
   function fileSelected() {
           var file = document.getElementById('fileToUpload').files[0];
           var fileName = file.name;
           var file_typename = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
       
           if (file_typename == '.rar' || file_typename == '.zip' || file_typename == '.sql'   ) {  //这里限定上传文件文件类型
               if (file) {
                  var fileSize = 0;
                  if (file.size > 1024 * 1024){
                	  fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                  }else{
                	  fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
                  }
                  document.getElementById('fileName').innerHTML = '文件名: ' + file.name;
                  document.getElementById('fileSize').innerHTML = '大小: ' + fileSize;
                  $('#progressNumber').progressbar('setValue', 0);
              }
          }else {
              document.getElementById('fileName').innerHTML = "<span style='color:Red'>错误提示:上传文件应该是rar,zip,sql后缀,而不应该是" + file_typename + ",请重新选择文件</span>"
              document.getElementById('fileSize').innerHTML ="";
              document.getElementById('fileType').innerHTML ="";
              document.getElementById('fileToUpload').innerHTML ="";
          }
      }
  
      function uploadFile() {
          var fd = new FormData();
          fd.append("fileToUpload", document.getElementById('fileToUpload').files[0]);
          var xhr = new XMLHttpRequest();
          xhr.upload.addEventListener("progress", uploadProgress, false);
          xhr.addEventListener("load", uploadComplete, false);
          xhr.addEventListener("error", uploadFailed, false);
          xhr.addEventListener("abort", uploadCanceled, false);
          xhr.open("POST", "${ctx}/system/fileUpload/i/fileUpload");
          xhr.send(fd);
      }
  
      function uploadProgress(evt) {
          if (evt.lengthComputable) {
              var percentComplete = Math.round(evt.loaded * 100 / evt.total);
              $('#progressNumber').progressbar('setValue', percentComplete);
          }
          else {
              document.getElementById('progressNumber').innerHTML = '无法计算';
         }
      }
  
      function uploadComplete(evt) {
         /* 服务器返回数据*/
        var message = evt.target.responseText;
       // alert( message );
        var data = testJson = eval("(" + message + ")"); 
         $('#dg3').datagrid('reload');
         $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
        parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
      }
  
      function uploadFailed(evt) {
          alert("上传出错.");
      }
  
      function uploadCanceled(evt) {
          alert("上传已由用户或浏览器取消删除连接.");
      }
   
</script>

</body>
</html>