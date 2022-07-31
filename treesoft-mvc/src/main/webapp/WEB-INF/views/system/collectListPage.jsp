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
<title>数据API列表</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>

 <div id="tb3" style="padding:5px;height:auto">
                         <div>
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRowButton"  onclick="addPersonForm()"> 新增</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deletePerson()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       
                           <span class="toolbar-item dialog-tool-separator"></span> &nbsp;&nbsp;&nbsp; 
                                                                                       名称：<input type="text" id="nameSearch" class="easyui-validatebox" data-options="width:100,prompt: '名称'"/>
                                &nbsp;数据库名称：<input type="text" id="sourceDatabaseSearch" class="easyui-validatebox" data-options="width:100,prompt:'数据库名称'"/>
                                 <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-hamburg-search2" plain="true" onclick="collectQuery()">查询</a>
                         </div> 
                       
  </div>
  <div id="dlgg" ></div>  
 
 <table id="dgForCollect"></table> 
<script type="text/javascript">
var dgg;
var collect;
$(function(){  
    initData();
});

function collectQuery(){
	 initData();
}

function initData(){
	dgg=$('#dgForCollect').datagrid({     
	method: "post",
    url: '${ctx}/collect/collectListSearch?v=' + Math.random(), 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
	queryParams:{ name:$("#nameSearch").val(),sourceDatabase:$("#sourceDatabaseSearch").val() },
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'id',title:'操作',align:'center',halign:'center',width:120,sortable:true,formatter: function(value,row,index){
			  return '<a  class="l-btn l-btn-small" href="javascript:openCollectApi(\'' +row["alias"]+  '\',\''  +row["appKey"]+  '\' )" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text" style="line-height: 18px;" title="打开">打开</span><span class="l-btn-icon icon-table-key">&nbsp;</span></span></a> <a  class="l-btn l-btn-small" href="javascript:editPersonFormOne(\'' +row["id"]+  '\')" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text" style="line-height: 18px;">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>  ';
		}},
	  	{field:'name',title:'名称',halign:'center',width:100 },
	  	{field:'alias',title:'别名',halign:'center',width:100 },
	  	{field:'sourceConfigName',title:'数据库配置',halign:'center',sortable:true,width:100 },
	  	{field:'sourceDatabase',title:'数据库名称',halign:'center',sortable:true,width:100 },
	    {field:'requestNumber',title:'已查询次数',halign:'center',width:100 },
        {field:'status',title:'状态',halign:'center',width:100,sortable:true,formatter: function(value,row,index){
				if (row.status=='0'){
					return '启用';
				} else {
					return '<font color="red">停用</font>';
				}
		}}
	  	
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false,
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
    onDblClickRow: function (rowIndex, rowData) {  
    	var id =rowData.id  ;
	    collect = $("#dlgg").dialog({   
	    title: '查看',    
	    width: 580,    
	    height: 400,    
	    href:'${ctx}/collect/collectSelectById/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[
	    	 
	    	{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					collect.panel('close');
				}
		 }]
	  });
    }
   }); 
  }

 //打开 新增 编辑 对话框
   function addPersonForm(){
	    collect = $("#dlgg").dialog({   
	    title: ' 新增',    
	    width: 580,    
	    height: 400,    
	    href:'${ctx}/collect/collectForm',
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		addUpdateCollect();
				//$("#mainform").submit(); 
			}
		  },{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					collect.panel('close');
				}
		 }]
	  });
  }
  
 function editPersonFormOne(id){
	 collect= $("#dlgg").dialog({   
	    title: '修改',    
	    width: 580,    
	    height: 400,    
	    href:'${ctx}/collect/collectSelectById/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		addUpdateCollect();
				// $("#mainform").submit(); 
			}
		  },{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					collect.panel('close');
				}
		 }]
	});
 }
 
 function editPersonForm(){
	var checkedItems = $('#dgForCollect').datagrid('getChecked');
	if(checkedItems.length == 0 ){
	      parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		  return;
	 }
	    
	 if(checkedItems.length >1 ){
	      parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		  return;
	 }
	 
	 var id = checkedItems[0]['id']  ;
	 collect= $("#dlgg").dialog({   
	    title: '修改',    
	    width: 580,    
	    height: 400,    
	    href:'${ctx}/collect/collectSelectById/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		addUpdateCollect();
				// $("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					collect.panel('close');
				}
		}]
	});
 }
 
 
   //删除  
   function deletePerson(){
	 
	  var checkedItems = $('#dgForCollect').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  var data2=$('#dgForCollect').datagrid('getData');
	  var totalLength = data2.total;
	  
	  // alert('总数据量:'+data.total)
	  
	   if(totalLength - length <= 0 ){
		 parent.$.messager.show({ title : "提示",msg: "必须保留一行信息！", position: "bottomRight" });
		 return ;
	  }
	  
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
                    url:"${ctx}/collect/collectDelete",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var obj = eval('(' + data + ')');
                       var status = obj.status ;
            	       if(status == 'success' ){
            	    	   $('#dgForCollect').datagrid('reload');
            	    	   $("#dgForCollect").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }
 
   function refresh(){
	    $('#dgForCollect').datagrid('reload');
	    $("#dgForCollect").datagrid('clearSelections').datagrid('clearChecked');
   }
   
   function openCollectApi( alias ,appKey ){ 
	 var url = "${ctx}/dataApi/select/"+  alias + "?appKey="+appKey;
	 window.open( url);
   }
    
</script>
</body>
</html>