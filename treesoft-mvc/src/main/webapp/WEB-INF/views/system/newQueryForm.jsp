<%@ page language="java" contentType="text/html; charset=UTF-8"	 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title> new Query Form</title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
	<%@ include file="/WEB-INF/views/include/codemirror.jsp"%>
	<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctx}/static/plugins/easyui/jeasyui-extensions/jeasyui.extensions.datatime.js" type="text/javascript"></script>
	
	<link href="${ctx}/static/css/s.css" type="text/css" rel="stylesheet"></link>
	<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/lib/codemirror.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/addon/hint/show-hint.css" />
	<script type="text/javascript" src="${ctx}/static/codemirror/lib/codemirror.js"> </script>
	<script type="text/javascript" src="${ctx}/static/codemirror/mode/sql/sql.js"> </script>
	<script type="text/javascript" src="${ctx}/static/codemirror/addon/hint/show-hint.js"> </script>
	<script type="text/javascript" src="${ctx}/static/codemirror/addon/hint/sql-hint.js"> </script>
	 
	<style>
		.CodeMirror { border: 1px solid #cccccc;  height: 98%;  }
		.imageHead:hover{
			background: #fff;
			color: #fff;
		}
	</style>
</head>
<body>
<div id="eastLayout" class="easyui-layout" data-options="fit: true">
	<div data-options="region: 'north',split: true, border: false"	style="height: 280px">
		<div id="operator1" class="panel-header panel-header-noborder"  style="padding: 5px; height: auto">
			<div>
		  	    <a href="javascript:void(0)" id="newRunButton" class="easyui-linkbutton" iconCls="icon-run" plain="true" onclick="newRun()" title="运行选中SQL命令">运行(F8) </a>
				<span class="toolbar-item dialog-tool-separator"></span>
 
                <a href="javascript:void(0)" id="newStopRunButton" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-hamburg-stop',disabled:true"   onclick="newStopRun()" title="停止运行">停止</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	        	             
				<a href="javascript:void(0)" id="newClearSQLButton" class="easyui-linkbutton" iconCls="icon-standard-bin-closed" plain="true" onclick="newClearSQL()" title="清空编辑区SQL命令" >清空(F7) </a>
				<span class="toolbar-item dialog-tool-separator"></span>

				<a href="javascript:void(0)" id="newFormatSQLButton" class="easyui-linkbutton" iconCls="icon-hamburg-sitemap" plain="true" onclick="newFormatSQL()" title="SQL格式美化">美化</a>
				<span class="toolbar-item dialog-tool-separator"></span>

                <a href="javascript:void(0)" id="newSaveSearchButton" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="newSaveSearch()" title="SQL保存,可展开右侧工具栏查看" >保存</a>
	            <span class="toolbar-item dialog-tool-separator"></span>

				<span class="l-btn-left l-btn-icon-left" style="margin-top: 4px"><span class="l-btn-text">样式:</span><span class="l-btn-icon icon-hamburg-drawings">&nbsp;</span></span>
				<select id="codeThemeSelectorNew" onchange="selectThemeNew()" class="esayui-combobox"  style="margin-top:5.5px;" >
					<option selected>
						default
					</option>
					<option>
						3024-day
					</option>
					<option>
						3024-night
					</option>
					<option>
						abcdef
					</option>
					<option>
						ambiance
					</option>
					<option>
						base16-dark
					</option>
					<option>
						base16-light
					</option>
					<option>
						bespin
					</option>
					<option>
						blackboard
					</option>
					<option>
						cobalt
					</option>
					<option>
						dracula
					</option>
					<option>
						eclipse
					</option>
					<option>
						erlang-dark
					</option>
					<option>
						hopscotch
					</option>
					<option>
						isotope
					</option>
					<option>
						lesser-dark
					</option>
					<option>
						material
					</option>
					<option>
						mbo
					</option>
					<option>
						mdn-like
					</option>
					<option>
						midnight
					</option>
					<option>
						monokai
					</option>
					<option>
						neat
					</option>
					<option>
						paraiso-dark
					</option>
					<option>
						paraiso-light
					</option>
					<option>
						pastel-on-dark
					</option>
					<option>
						railscasts
					</option>
					<option>
						rubyblue
					</option>
					<option>
						seti
					</option>
					<option>
						solarized dark
					</option>
					<option>
						solarized light
					</option>
					<option>
						the-matrix
					</option>
					<option>
						ttcn
					</option>
					<option>
						twilight
					</option>
					<option>
						vibrant-ink
					</option>
					<option>
						xq-dark
					</option>
					<option>
						xq-light
					</option>
				</select>				
                <span class="toolbar-item dialog-tool-separator"></span>
				&nbsp;当前数据库：<span id="currentDbTitle" title="${databaseConfigName}">${databaseName}</span>
			</div>
		</div>

		<div id="newQuerySqlAreaDiv" style="width:100%;height:85%;" >
			<input type="hidden" id="databaseConfigId"  value="${databaseConfigId}" />
			<input type="hidden" id="databaseType" value="${databaseType}" />
			<input type="hidden" id="databaseName" value="${databaseName}" />
			<input type="hidden" id="exportLimit" value="${exportLimit}" />
			<input type="hidden" id="isRead" value="${isRead}" />
			<textarea  id="newQuerySqlArea" style="margin:10px; font-size:14px;font-family:'微软雅黑';width:97%;height:95%;" placeholder="请输入SQL"></textarea>
		</div>
	</div>

	<div id="searchHistoryPanel2"  data-options="region: 'center',split: true, collapsed: false,   border: false, title: '运行结果', iconCls: 'icon-standard-application-view-icons'">
		<div id="newQuerySearchTabs" class="easyui-tabs" data-options="fit:true, border:false,showOption:false, enableNewTabMenu:false, enableJumpTabMenu:false">
			<div id="newSearcHomePanel" data-options="title: '消息', iconCls: 'icon-hamburg-issue'">
				<textarea  id="newQueryExecuteMessage" style="margin:10px; font-size:14px;font-family:'微软雅黑';width:97%;height:95%; " readonly >  </textarea>
			</div>
		</div>
	</div>
    <div id="newDlgg" ></div>  
   <div id="executeSqlConfirmDialog" class="easyui-dialog" title="操作确认" style="width: 520px; height: 380px;" data-options="iconCls:'icon-database-error',modal:true" closed="true" resizable="true" buttons="#dlg-buttons" >
       <div style="display: block; padding-top:10px">
         &nbsp;&nbsp;&nbsp;确定执行以下SQL吗？
       </div>
       <div style="display: block">
           <table class="formTable">
             <tr>
	           <td><textarea id="newQueryDangerSQL"  style="width:480px;height:250px"  readonly="true" >   </textarea>  </td>
              </tr>
           </table>
       </div>
      <div id="dlg-buttons" style="display: block">
         <a  href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" selected="true" onclick="executeSQLOperator()"  >确定</a>        
         <a  href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-cancel" onclick="javascript:$('#executeSqlConfirmDialog').dialog('close');enableOperButton();" >取消</a>
     </div>     
  </div> 
</div>

 <form id="newQueryform3" method="post"  action="${ctx}/system/permission/i/exportDataToSQLFromSQL"  style="display:none"   >
   <input type="hidden"   name="databaseConfigId" value="${databaseConfigId}" />
   <input type="hidden"  name="databaseName" value="${databaseName}" />
   <input type="hidden" id="sql"    name="sql" />
   <input type="hidden" id="exportType" name="exportType" />
</form>
   
  <script type="text/javascript" src="${ctx}/static/js/newQuery.js"> </script> 
  <script type="text/javascript" src="${ctx}/static/js/sql-formatter.min.js"> </script>
</body>
</html>