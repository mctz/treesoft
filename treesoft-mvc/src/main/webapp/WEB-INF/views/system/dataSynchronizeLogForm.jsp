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

</head>
<body>

 <div id="tb4" style="padding:5px;height:auto">
      <input id="dataSynchronizeId" name="dataSynchronizeId" type="hidden" value="${dataSynchronizeId }"   />
                         <div>
	       		          
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deleteSysLog()">删除</a>
	        	          
	                       <span class="toolbar-item dialog-tool-separator"></span>	                                     
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>	                      
	                       
                         </div> 
                       
  </div>
  <div id="dlgg" ></div>  
 
 <table id="dg4"></table> 
<script type="text/javascript">
var dgg4;
var dataSynchronize;
$(function(){  
	
    initData();
});

function initData(){
	var dataSynchronizeId = $("#dataSynchronizeId").val();
	dgg4=$('#dg4').datagrid({     
	method: "get",
    url: '${ctx}/system/permission/i/dataSynchronizeLogList/'+ dataSynchronizeId , 
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
	nowrap:false,   //允许换行
    fitColumns:true,//宽度自适应
    columns:[[    
	  	{field:'treesoft',checkbox:true}, 
	  	{field:'id',title:'id',align: 'center',width:120,hidden:'true'  },
	  	{field:'createdate',title:'运行时间',align: 'center',width:160 },
        {field:'status',title:'状态',sortable:true,width:50,formatter: function(value,row,index){
				if (row.status =='0'){
					return '失败';
				}else if (row.status =='1'){
					return '成功';
				} else {
					return '';
				}
		}},
		{field:'comments',title:'详情',sortable:true,width:350 },
	  	
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb4',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false

   }); 
  }

   //删除  
   function deleteSysLog(){
	 
	  var checkedItems = $('#dg4').datagrid('getChecked');
	  var length = checkedItems.length;
	  
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
                    url:"${ctx}/system/permission/i/deleteDataSynchronizeLog",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg4').datagrid('reload');
            	    	   $("#dg4").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "删除成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }
   
   function refresh(){
	    $('#dg4').datagrid('reload');
	    $("#dg4").datagrid('clearSelections').datagrid('clearChecked');
   }
    
</script>

</body>
</html>