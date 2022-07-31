<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Cache" content="no-cache" />
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>

<form id="form3" method="post"  action="${ctx}/system/log/exportLogDataToExcelFromSearch"  style="display:none"   >
   <input type="hidden" id="startTimeHidden" name="startTime" />
   <input type="hidden" id="endTimeHidden"   name="endTime"  />
   <input type="hidden" id="userNameHidden"  name="userName"  />
</form>

 <div id="tb3" style="padding:5px;height:auto">
          <div>
	          <span class="toolbar-item dialog-tool-separator"></span>
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deleteLog0591()">删除</a>
	          <span class="toolbar-item dialog-tool-separator"></span>
	          
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-bin-closed" plain="true" id="delButton"   onclick="deleteAllLog()">清空</a>
	          <span class="toolbar-item dialog-tool-separator"></span>
	          
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"  title="全部导出excel"  onclick="exportDataToExcelFromSQL()">导出</a>
	          <span class="toolbar-item dialog-tool-separator"></span>
	                     
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	          	          
	          <span class="toolbar-item dialog-tool-separator"></span> &nbsp;&nbsp;&nbsp; 
	                           时间范围：<input type="text" id="startTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '开始时间'"  />
	           --<input type="text" id="endTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '结束时间'"/>
             &nbsp;&nbsp;&nbsp;  
                                    用户：<input type="text" id="userName" class="easyui-validatebox" data-options="width:100,prompt: '用户'"/>
            &nbsp;日志内容：<input type="text" id="log" class="easyui-validatebox" data-options="width:100,prompt:'日志内容'"/>
               <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-hamburg-search2" plain="true" onclick="query()">查询</a>
	          
          </div> 
  </div>
  <div id="dlgg" ></div>  
 
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

function exportDataToExcelFromSQL(){
	$("#startTimeHidden").val( $("#startTime").my97('getText') );
	$("#endTimeHidden").val( $("#endTime").my97('getText') );
	$("#userNameHidden").val( $("#userName").val() );
	$("#form3").submit();
}


function initData(){
	var startTimeVal = $("#startTime").my97('getText');
	var endTimeVal = $("#endTime").my97('getText'); 
	dgg=$('#dg3').datagrid({     
	method: "post",
    url: '${ctx}/system/log/logList/'+Math.random(), 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 30,
	pageList : [ 10, 20, 30, 40, 500 ],
	singleSelect:false,
	queryParams:{startTime:startTimeVal, endTime:endTimeVal ,userName:$("#userName").val(),log:$("#log").val() },
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'id',title:'id',align: 'center', hidden:'true'  },
	  	{field:'createTime',title:'时间',halign:'center' },
	  	{field:'username',title:'用户', halign:'center',sortable:true },
        {field:'ip',title:'IP',halign:'center', sortable:true,width:50},
        {field:'configName',title:'数据库配置', halign:'center',sortable:true,width:50},
        {field:'databaseName',title:'数据库',halign:'center', sortable:true,width:50},
        {field:'log',title:'日志内容',halign:'center',sortable:true,width:300 ,editor:'text',tooltip:true}
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    onLoadSuccess: function (data) {
    	if( data.status=='fail'){
    		 parent.$.messager.show({ title : "提示",msg: data.mess , position: "center" });
    	}
    	if(data.total == 0){
          var body = $(this).data().datagrid.dc.body2;
		  var width = body.width();
		  body.find('table tbody').append('<tr><td width = \''+width+'\' style=\' height: 25px; text-align: center;\' colspan=\'6\'>暂无数据！</td></tr>');
       }
    },
    autoEditing: true     //该属性启用双击行时自定开启该行的编辑状态
   }); 
  }
 
   //删除  
   function deleteLog0591(){
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  var data2=$('#dg3').datagrid('getData');
	  var totalLength = data2.total;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["id"] );
      }); 
       
	 $.easyui.messager.confirm("操作提示", "您确定要删除"+length+"行数据吗？", function (c) {
                if (c) {
                	
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/log/deleteLog",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }
 
   //清空日志
   function deleteAllLog(){
	 $.easyui.messager.confirm("操作提示", "您确定清空全部日志数据吗？", function (c) {
                if (c) {
                   $.ajax({
			        type:'GET',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/log/deleteAllLog",
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }
   
   function refresh(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
   }
    
</script>

</body>
</html>