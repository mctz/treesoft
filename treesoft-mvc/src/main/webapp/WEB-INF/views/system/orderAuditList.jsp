<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<title>工单审核</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<%@ include file="/WEB-INF/views/include/codemirror.jsp"%>

<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/theme/eclipse.css">
<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/lib/codemirror.css" />  
<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/addon/hint/show-hint.css" /> 
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/codemirror/lib/codemirror.js"> </script>   
<script type="text/javascript" src="${ctx}/static/codemirror/mode/sql/sql.js"> </script>  
<script type="text/javascript" src="${ctx}/static/codemirror/addon/hint/show-hint.js"> </script> 
<script type="text/javascript" src="${ctx}/static/codemirror/addon/hint/sql-hint.js"> </script>  

</head>
<body>
 

 <div id="tb3" style="padding:5px;height:auto">
          <div>
              <!--  
              <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-library" plain="true" id="addRowButton"  onclick="orderAuditForm()"> 查看 </a>
	       	  <span class="toolbar-item dialog-tool-separator"></span>
	       	  -->
              <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true" id="addRowButton"  onclick="auditOrderPass()"> 审核通过 </a>
	       	  <span class="toolbar-item dialog-tool-separator"></span>
	      
	          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	          <span class="toolbar-item dialog-tool-separator"></span> &nbsp;&nbsp;&nbsp; 
	                          时间范围：<input type="text" id="startTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '开始时间'"  />
	           --<input type="text" id="endTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '结束时间'"/>
             &nbsp;&nbsp;&nbsp;  
                                    提交人：<input type="text" id="userName" class="easyui-validatebox" data-options="width:100,prompt: '提交人'"/>
            &nbsp;SQL内容：<input type="text" id="doSqlSearch" class="easyui-validatebox" data-options="width:100,prompt:'SQL内容'"/>
               <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-hamburg-search2" plain="true" onclick="query()">查询</a>
	          
          </div> 
  </div>
  <div id="dlgg" ></div>  
 
 <table id="dg6"></table> 
<script type="text/javascript">
var dgg;
var config;
$(function(){  
     initData();
});

function query(){
	 initData();
}

function initData(){
	var startTimeVal = $("#startTime").my97('getText');
	var endTimeVal = $("#endTime").my97('getText'); 
	dgg=$('#dg6').datagrid({     
	method: "post",
    url: '${ctx}/order/orderAuditListData/'+Math.random(), 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 30,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
	queryParams:{startTime:startTimeVal, endTime:endTimeVal ,userName:$("#userName").val(),doSql:$("#doSqlSearch").val() },
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'id',title:'操作',align:'center',halign:'center',width:30,sortable:true,formatter: function(value,row,index){
			return '<a  class="l-btn l-btn-small" href="javascript:auditOrderFormOne(\'' +row["id"]+  '\')" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text" style="line-height: 18px;">审核 </span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a> ';
		}},
	  	{field:'orderNumber',title:'工单编号',halign:'center',width:40 },
	  	{field:'createTime',title:'提交时间',halign:'center',width:55},
	  	{field:'createUserName',title:'提交人', halign:'center',sortable:true },         
        {field:'configName',title:'数据库配置', halign:'center',sortable:true,width:50},
        {field:'databaseName',title:'数据库名称',halign:'center', sortable:true,width:50},
        {field:'status',title:'工单状态',halign:'center',sortable:true,formatter: function(value,row,index){
				if (row.status =='0'){
					return '待审核';
				}else if (row.status =='1'){
					return '<font color="green">审核通过</font>';
				}else if (row.status =='2'){
					return '<font color="red">审核不通过</font>';
				}else if (row.status =='3'){
					return '<font color="red">驳回</font>';
				}else if (row.status =='4'){
					return '<font color="green">已执行</font>';
				}else if (row.status =='5'){
					return '已放弃';				 
				} else {
					return '';
				}
		}}, 		
	  	{field:'auditUserName',title:'审核人', halign:'center',sortable:true },
        {field:'auditTime',title:'审核时间',halign:'center',width:50},
        {field:'runUserName',title:'执行人', halign:'center',sortable:true },
        {field:'runTime',title:'执行时间',halign:'center',width:50}
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
 

 //打开 查看 对话框
 function orderAuditForm(){
	var checkedItems = $('#dg6').datagrid('getChecked');
	if(checkedItems.length == 0 ){
	      parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		  return;
	 }
	    
	 if(checkedItems.length >1 ){
	      parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		  return;
	 }
	 
	 var id = checkedItems[0]['id']  ;
	 order= $("#dlgg").dialog({   
	    title: '编辑',    
	    width: 580,    
	    height: 400,    
	    href:'${ctx}/order/orderAuditForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'保存xx',
			iconCls:'icon-ok',
			handler:function(){
			     $("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					order.panel('close');
				}
		}]
	});
  }

   //批量审核通过
   function auditOrderPass(){
	  var checkedItems = $('#dg6').datagrid('getChecked');
	  var length = checkedItems.length;
	  var data2=$('#dg6').datagrid('getData');
	  var totalLength = data2.total;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["id"] );
     }); 
       
      $.ajax({
		   type:'POST',
		   contentType:'application/json;charset=utf-8',
           url:"${ctx}/order/auditOrderPass",
           data: JSON.stringify( { 'ids':ids } ),
           datatype: "json", 
          //成功返回之后调用的函数             
           success:function(data){
               var status = data.status ;
               if(status == 'success' ){
            	   $('#dg6').datagrid('reload');
            	   $("#dg6").datagrid('clearSelections').datagrid('clearChecked');
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	}else{
            	   parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	}
           }  
      });
   }
   
   
 //打开 编辑 对话框
 function auditOrderFormOne( id ){
	 order= $("#dlgg").dialog({   
	    title: '审核',    
	    width: 580,    
	    height: 400,    
	    href:'${ctx}/order/orderAuditForm/'+id,
	    maximizable:true,
	    modal:true
	});
  }
 
   function refresh(){
	    $('#dg6').datagrid('reload');
	    $("#dg6").datagrid('clearSelections').datagrid('clearChecked');
   }
    
</script>

</body>
</html>