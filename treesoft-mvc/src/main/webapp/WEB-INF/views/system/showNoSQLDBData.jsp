<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>

<body>
 <div id="tb" style="padding:5px;height:auto">
                         <div>
                           <span class="toolbar-item dialog-tool-separator"></span>
                           <input type="text" id="selectKey" class="easyui-validatebox" data-options="width:150,prompt: 'key'"/>
                           <input type="text" id="selectValue" class="easyui-validatebox" data-options="width:150,prompt: 'value'" style="display:none"/>
                           <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query()">查询</a>
                           <span class="toolbar-item dialog-tool-separator"></span>
                           
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRowButton"  onclick="addOrSaveDialog()"> 添加 </a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="del()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	        	           
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="editRowButton" onclick="editRow2()">修改</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                        &nbsp; &nbsp;<span id="databaseNameShow"> 127.0.0.1:6379 </span>
                         </div> 
                       
  </div>
  
 <input type="hidden" id="databaseConfigId" value="${databaseConfigId}" >
 <input type="hidden" id="NoSQLDbName"  value="${NoSQLDbName}">
<table id="dg2"></table> 

<div id="dlg2"></div>  
<div id="dlgg"   ></div>  
<div id="addRow" ></div> 

<script type="text/javascript"> 
var dg;
var d;
var primary_key;
var selectRowCount = 0;
var columnsTemp = new Array();
var databaseName;
var addDialog;
var tableName;
var NoSQLDbName;
var add;
var obj ;
var databaseConfigId;

var willChangeRow = new Array();

$(function(){ 
	  
	$("#databaseNameShow").html( $("#databaseSelect",window.parent.document).find("option:selected").text()  );
	databaseConfigId = $("#databaseConfigId").val();
	NoSQLDbName = $("#NoSQLDbName").val();
		
	query();
});


//查询方法  
function query() { 
	var url= "${ctx}/redis/showNoSQLDBValue/"+NoSQLDbName+"/"+databaseConfigId ;
	var selectKey =   $("#selectKey").val();
	var selectValue = $("#selectKey").val();
	if(!selectKey ){
		 url= url+"/nokey";
	}else{
		selectKey = encodeURIComponent( selectKey );
		url= url+"/"+ selectKey;
	}	
    if(!selectValue ){
		 url= url+"/novalue";
	}else{
		url= url+"/"+ selectValue;
	}	
	
   // alert( url );
	
	dg=$('#dg2').datagrid({    
	method: "post",
    url: url ,
    fit : true,
	fitColumns:true,
	border : false,
	striped:true,
	idField : 'key',
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 50,
	pageList : [ 10, 20, 30, 40, 50 ],
    columns:[[    
    	{field:'ck',checkbox:true}, 
        {field:'key',title:'key',sortable:true,width:80},    
        {field:'type',title:'type',sortable:true,width:20},
        {field:'value',title:'value',sortable:true,width:100}                
    ]],
    enableHeaderClickMenu: true,
    enableHeaderContextMenu: true,
    enableRowContextMenu: false,
    rowTooltip: true,
    toolbar:'#tb',
    onDblClickRow: function (rowIndex, rowData) {  
    	var key =rowData.key  ;
    	key = encodeURIComponent( key );
	    var type = rowData.type  ;
	    var url4 = '${ctx}/redis/editNotSqlData?noSQLDbName='+NoSQLDbName+'&databaseConfigId='+databaseConfigId+'&key='+key;
	    if( type =="Object" ){
	    	parent.$.messager.show({ title : "提示",msg: "序列化对象，不支持修改！", position: "bottomRight" });
		    return;
	    }
	    addDialog = $("#dlgg").dialog({   
	    title: '编辑', 
	    method: 'get',
	    width: 580,    
	    height: 360,    
	    href:url4,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-edit',
			handler:function(){
				$("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					addDialog.panel('close');
				}
		 }]
	  });
    }
	});
}  
   
   
 //删除行 
 //删除行时,先判断一下有没新增或编辑的数据行,如果有必须先提交才允许删.
 function del(){
	  var ids=[];
	  var data= $('#dg2').datagrid('getChecked');
	  for( var i=0,j=data.length;i<j;i++){
		ids.push(data[i].key );
	  }
	  
	 if(data.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	 }
	 $.easyui.messager.confirm("操作提醒", "您确定要删除"+data.length +"行数据？", function (c) {
                if (c) {
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/redis/deleteNoSQLKeys",
                    data: JSON.stringify( { 'databaseConfigId':databaseConfigId, 'noSQLDbName':NoSQLDbName, 'ids': ids  } ),
                    
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	    $('#dg2').datagrid('reload');
            	    	    $("#dg2").datagrid('clearSelections').datagrid('clearChecked');
            	    	    selectRowCount = 0;
            	            parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" ,timeout:2000,icon:"warning" });
            	       }
            	     }  
                   });
                }
            });
	  
   }
 
   function refresh(){
	    $("#dg2").datagrid('reload');
	    $("#dg2").datagrid('clearSelections').datagrid('clearChecked');
   }


    //打开 新增 编辑 对话框
   function addOrSaveDialog(){
	    addDialog = $("#dlgg").dialog({   
	    title: ' 新增',    
	    width: 580,    
	    height: 360,    
	    href:'${ctx}/redis/addOrEditNotSql/'+NoSQLDbName+'/'+databaseConfigId,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-edit',
			handler:function(){
				$("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					addDialog.panel('close');
				}
		 }]
	  });
  }
   
    //编辑 一行数据
   function editRow2(){
	   var checkedItems = $('#dg2').datagrid('getChecked');
	   if(checkedItems.length == 0 ){
	    	parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		    return;
	    }
	    
	    if(checkedItems.length >1 ){
	    	parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		    return;
	    }
	 
	    var key = checkedItems[0]['key']  ;
	    var type = checkedItems[0]['type']  ;
	 
	    if( type =="Object" ){
	    	parent.$.messager.show({ title : "提示",msg: "序列化对象，不支持修改！", position: "bottomRight" });
		    return;
	    }
	  
	    key = encodeURIComponent( key );
	    addDialog = $("#dlgg").dialog({   
	    method:'get',
	    title: '编辑',    
	    width: 580,    
	    height: 360,    
	    href:'${ctx}/redis/editNotSqlData?noSQLDbName='+NoSQLDbName+'&databaseConfigId='+databaseConfigId+'&key='+key,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'确认',
			iconCls:'icon-edit',
			handler:function(){
				$("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					addDialog.panel('close');
				}
		 }]
	  });
   }
 
   //取消 修改
   function  cancelChange(){
	 refresh();
   }
  
</script>
</body>
</html>