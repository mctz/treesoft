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
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>

<form id="form3" method="post"  action="${ctx}/system/permission/i/exportDataStatisticsListToExcel/${databaseName}/${databaseConfigId}"  style="display:none"   >
   <input type="hidden" id="tableNameHidden"  name="tableName"  >
</form>

 <div id="tb3" style="padding:5px;height:auto">
          <div>
	          <span class="toolbar-item dialog-tool-separator"></span>
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"  title="全部导出excel"  onclick="exportDataStatisticsListToExcel()">导出</a>
	          <span class="toolbar-item dialog-tool-separator"></span>
	                     
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	          	        
	          <span class="toolbar-item dialog-tool-separator"></span>  
             &nbsp;&nbsp;&nbsp;  
                                    表名称：<input type="text" id="tableName" class="easyui-validatebox" data-options="width:150,prompt: '表名称'"/>
               <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-hamburg-search2" plain="true" onclick="query()">查询</a>
	          
          </div> 
  </div>
 
 <input type="hidden" id="databaseName" value="${databaseName}" >
 <input type="hidden" id="databaseConfigId"  value="${databaseConfigId}">
 
 <table id="dg3"></table> 
<script type="text/javascript">
var dgg;
var config;
$(function(){  
     initData();
});

function query(){
	 initData();
}

function exportDataStatisticsListToExcel(){
	$("#tableNameHidden").val( $("#tableName").val() );
	$("#form3").submit();
}

function initData(){
	var databaseName = $("#databaseName").val() ;
	var databaseConfigId = $("#databaseConfigId").val() ; 
	dgg=$('#dg3').datagrid({     
	method: "get",
    url: '${ctx}/system/permission/i/dataStatisticsList/'+databaseName +'/'+ databaseConfigId , 
    fit : true,
	fitColumns : true,//宽度自适应
	border : false,
	idField : 'id',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 30,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
	queryParams:{ tableName:$("#tableName").val() },
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'TABLE_SCHEMA',title:'数据库名称',align:'center' },
	  	{field:'TABLE_NAME',title:'表名称',sortable:true },
	  	{field:'CREATE_TIME',title:'创建时间',sortable:true ,width:30, align:'center'},
	  	{field:'ENGINE',title:'存储引擎',sortable:true,width:50,align:'center' },
	  	{field:'TABLE_ROWS',title:'行数',sortable:true,align:'right' },
	  	 
	  	{field:'DATA_LENGTH',title:'数据大小',sortable:true ,align:'center',align:'right'},
	  	{field:'INDEX_LENGTH',title:'索引大小',sortable:true,align:'center',align:'right' },
	  	{field:'TOTAL_LENGTH',title:'全部大小',sortable:true ,align:'center',align:'right'},	  	
        {field:'TABLE_COLLATION',title:'字符集排序规则',sortable:true ,align:'center'}
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
     onLoadSuccess: function (data) {
    	if( data.status=='fail'){
    		 parent.$.messager.show({ title : "提示",msg: data.mess , position: "center" });
    	}
      },
    autoEditing: true     //该属性启用双击行时自定开启该行的编辑状态
   }); 
  }
 
 
  function refresh(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
  }
    
</script>

</body>
</html>