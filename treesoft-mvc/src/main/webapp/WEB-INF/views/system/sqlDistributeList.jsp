<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<title>SQL分发</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>

 <div id="tb3" style="padding:5px;height:auto">
                         <div>
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRowButton"  onclick="addConfigForm()"> 新增</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deleteConfig()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	        	           <!--  
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="editRowButton" onclick="editConfigForm()">修改</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       -->
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true"   onclick="copySqlDistribute()">复制</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-run" plain="true" id="editRowButton" onclick="startTask3()">运行</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                         
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" plain="true" id="editRowButton" onclick="stopTask()">停止</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh12()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" id="operatorHint" title="将SQL命令快速下发到数据库节点. &#13;提高数据库运维效率."></a>
                          
                         </div> 
                       
  </div>
  <div id="dlgg" ></div>  
  <div id="dlgg2" ></div> 
 <table id="dg3"></table>  
 <script type="text/javascript" src="${ctx}/static/js/sqlDistribute.js"> </script>  
</body>
</html>