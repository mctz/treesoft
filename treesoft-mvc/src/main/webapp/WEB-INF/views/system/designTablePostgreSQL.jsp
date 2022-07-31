<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title> MySQL设计表 </title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>

 <div style="padding:0px;height:600px">
   <input type="hidden" id="databaseName" value="${databaseName}" />
   <input type="hidden" id="tableName"  value="${tableName}" />
   <input type="hidden" id="databaseConfigId"  value="${databaseConfigId}" />
   <div id="designTableTab" class="easyui-tabs"  data-options="fit: true, border: false" >
        
      <div  data-options="title: '字段', iconCls: 'icon-hamburg-product'">
        <div id="tb3">
          <div>                          
	       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-insert" plain="true" id="addRowButton"  onclick="addRow2()"> 新增字段</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
	       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-delete" plain="true" id="delRowButton"   onclick="deleteTableColumn()">删除字段</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
	       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true" id="editRowButton" onclick="editRow2()">修改字段</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
	       <!--                  
	       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-up" plain="true" id="moveUpButton" onclick="MoveUp()">上移</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
	       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-down" plain="true" id="moveDownButton" onclick="MoveDown()">下移</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
	       -->             
	       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
                         
           <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
                         
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="saveRowButton" onclick="saveRow()">保存</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="cancelChangeButton" onclick="cancelChange()">取消</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
          </div> 
         </div>
         <table id="dg3"></table>         
      </div>
      
      <div  data-options="title: '索引', iconCls: 'icon-hamburg-docs'"  >
         <div id="toolbarIndex3">
          <div> 
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-insert" plain="true" id="addRowButton"  onclick="addIndex()"> 新增索引</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-delete" plain="true" id="delRowButton"   onclick="deleteIndex()">删除索引</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refreshIndex()">刷新</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
                         
           <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>
	       <span class="toolbar-item dialog-tool-separator"></span>
                         
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="saveRowButton" onclick="saveIndex()">保存</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="cancelChangeButton" onclick="refreshIndex()">取消</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
	      </div>
         </div>
         <table id="dgForDesignIndex"></table> 		   
      </div>
        
      <div  data-options="title: '触发器', iconCls: 'icon-hamburg-project'"  >
           <div id="toolbarTrigger">
           <div> 
            <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-delete" plain="true" id="delRowButton"   onclick="deleteTrigger()">删除触发器</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        
           </div>
          </div>               
		  <table id="dgForTrigger"></table> 	
      </div>
      
      <div  data-options="title: '详情', iconCls: 'icon-hamburg-future'"  >
		  <table id="dgForMess"></table> 	
      </div>
    </div>
                       
 </div>
  
<script type="text/javascript" src="${ctx}/static/js/designTable.js"> </script> 
 
<script type="text/javascript">
  var tableName;
  var databaseName;
  var databaseConfigId;
  var dggDesign;
  $(function(){
    databaseName = $("#databaseName").val();
	tableName = $("#tableName").val();
	databaseConfigId = $("#databaseConfigId").val();
    
  });
  
  $('#designTableTab').tabs({
	    onSelect:function(title,index){
	      if(index == 1){
	        initIndexData();
	      }	 
	      if(index == 2){
	        initTriggerData();
	      }	 
	      if(index == 3){
	        initMessData();
	      }	 
	    }
  });    
  
  function initIndexData(){
    if(dggDesign){
       return;
    }
    var IndexTypes =[{"value":"BTREE","text":"BTREE"},{"value":"HASH","text":"HASH"}] ;
    var uniqueTypes =[{"value":"1","text":"Normal"},{"value":"0","text":"Unique"}] ;    
    dggDesign = $('#dgForDesignIndex').datagrid({     
	method: "POST",
    url: '${ctx}/designTable/selectTableIndexs', 
    queryParams:{databaseConfigId:databaseConfigId,databaseName:databaseName,tableName:tableName },
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
	singleSelect:true,
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
        {field:'indexName',title:'索引名称', width:100 ,editor:{type:'text', options: { required: true }}},
        {field:'columnName',title:'字段',width:100 ,editor:{ type:'text', options: { required: true }}}        
        
    ]],      
    extEditing:false,
    toolbar:'#toolbarIndex3' , 
    singleEditing: true
   }); 
  }

  function initTriggerData(){
     $("#dgForTrigger").datagrid({     
	    method: "post",
        url: '${ctx}/designTable/selectTableTriggers', 
        queryParams:{databaseConfigId:databaseConfigId,databaseName:databaseName,tableName:tableName },
        fit : true,
	    fitColumns : true,
	    border : false,
	    idField : 'treeSoftPrimaryKey',
	    striped:true,
	    pagination:true,
	    rownumbers:true,
	    pageNumber:1,
	    pageSize : 20,
	    pageList : [ 10, 20, 30, 40, 50 ],
	    singleSelect:false,
        columns:[[    
	    	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 		  
	    	{field:'trigger_name',title:'触发器名称', width:30},
	    	{field:'action_timing',title:'触发',width:50   } ,
         	{field:'event_manipulation',title:'操作',width:50 }         	      	
        ]], 
        checkOnSelect:true,
        selectOnCheck:true,
        extEditing:false,
        toolbar:'#toolbarTrigger', 
        autoEditing: true,     //该属性启用双击行时自定开启该行的编辑状态
        singleEditing: true
	}); 	
  }

function initMessData(){
	
	$('#dgForMess').datagrid({     
	    method: "post",
        url: '${ctx}/system/permission/i/viewTableMess/'+tableName+'/'+databaseName+'/'+databaseConfigId, 
        fit : true,
	    fitColumns : true,
	    border : false,
	    idField : 'treeSoftPrimaryKey',
	    striped:true,
	    pagination:true,
	    rownumbers:true,
	    pageNumber:1,
	    pageSize : 20,
	    pageList : [ 10, 20, 30, 40, 50 ],
	    singleSelect:false,
        columns:[[    
	    	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 		  
	    	{field:'propName',title:'名称', width:30   },		 
         	{field:'propValue',title:'信息',width:50   }        	
        ]], 
        checkOnSelect:true,
        selectOnCheck:true,
        extEditing:false,
       
        autoEditing: true,     //该属性启用双击行时自定开启该行的编辑状态
        singleEditing: true
	}); 	
  }
  
   //删除  
   function deleteIndex(){
	  var indexName;
	  var checkedItems = $('#dgForDesignIndex').datagrid('getChecked');
	  var length = checkedItems.length;
	   
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	  indexName = checkedItems[0]['Key_name'] ;
	 
	  $.easyui.messager.confirm("操作提示", "您确定要删除"+length+"行数据吗？", function (c) {
                if (c) {                           	
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/designTable/indexDelete",
                    data:JSON.stringify({"databaseConfigId":databaseConfigId,"databaseName":databaseName,"tableName":tableName,"indexName":checkedItems[0]['indexName'] }),
                    datatype: "json", 
                    cache:false,
                   //成功返回之后调用的函数             
                    success:function(data){
                       var obj = eval('(' + data + ')');
                       var status = obj.status ;
            	       if(status == 'success' ){
            	    	   $("#dgForDesignIndex").datagrid('reload');
            	    	   $("#dgForDesignIndex").datagrid('clearSelections').datagrid('clearChecked');
            	           parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	       }else{
            	    	   parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight",icon:"warning" });
            	    	   $("#dgForDesignIndex").datagrid('reload');
            	       }
            	     }  
                   });
                }
            });
   }
 
   function refreshIndex(){
	    $("#dgForDesignIndex").datagrid('reload');
	    $("#dgForDesignIndex").datagrid('clearSelections').datagrid('clearChecked');
   }
 
  function addIndex(){
	var data=$("#dgForDesignIndex").datagrid('getData');
	var length =  data.rows.length;
	$("#dgForDesignIndex").datagrid('insertRow',{
	   index: length,	// 索引从0开始
	   row: { }
    });
	 $("#dgForDesignIndex").datagrid("beginEdit", length );
  }
 
  function saveIndex(){
  
     var rows = $("#dgForDesignIndex").datagrid('getRows');
     for ( var i = 0; i < rows.length; i++) {
          $("#dgForDesignIndex").datagrid('endEdit', i);
     }
     var inserted = $("#dgForDesignIndex").datagrid('getChanges', 'inserted');
     if ( !inserted.length ) {
    	   return;
     }
     var checkedItem = JSON.stringify(inserted);
     
     $.ajax({
			 type:"POST",
		     contentType:'application/json;charset=utf-8',
             url:"${ctx}/designTable/indexSave",
             data:JSON.stringify({"databaseConfigId":databaseConfigId,"databaseName":databaseName,"tableName":tableName,"checkedItems":checkedItem }),
             datatype: "json", 
             cache:false,
             //成功返回之后调用的函数             
             success:function(data){
                   var obj = eval('(' + data + ')');
                   var status = obj.status ;
            	   if(status == 'success' ){
            	      $("#dgForDesignIndex").datagrid('reload');
            	      $("#dgForDesignIndex").datagrid('clearSelections').datagrid('clearChecked');
            	      parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	   }else{
            	       parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight",icon:"warning" });
            	   }
            }  
    });
  }
 
 
 //删除 触发器
  function deleteTrigger(){
	  var checkedItems = $("#dgForTrigger").datagrid("getChecked");
	  var length = checkedItems.length;
	   
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	   
	  $.easyui.messager.confirm("操作提示", "您确定要删除"+length+"行数据吗？", function (c) {
                if (c) {                           	
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/designTable/triggerDelete",
                    data:JSON.stringify({"databaseConfigId":databaseConfigId,"databaseName":databaseName,"tableName":tableName,"triggerName":checkedItems[0]['trigger_name'] }),
                    datatype: "json", 
                    cache:false,
                   //成功返回之后调用的函数             
                    success:function(data){
                       var obj = eval('(' + data + ')');
                       var status = obj.status ;
            	       if(status == 'success' ){
            	    	   $("#dgForTrigger").datagrid('reload');
            	    	   $("#dgForTrigger").datagrid('clearSelections').datagrid('clearChecked');
            	           parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	       }else{
            	    	   parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight",icon:"warning" });
            	    	   $("#dgForTrigger").datagrid('reload');
            	       }
            	     }  
                   });
                }
            });
   }
 
 
</script>
</body>
</html>