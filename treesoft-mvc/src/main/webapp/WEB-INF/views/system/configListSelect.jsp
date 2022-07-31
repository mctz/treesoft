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

 <div id="tbConfigSelect" style="padding:5px;height:auto">
                         <div>
                              <input type="hidden" id="personId" value="${personId}"  >
                              <input type="hidden" id="datascope" value="${datascope}"  >
	                          <span class="toolbar-item dialog-tool-separator"></span> &nbsp;&nbsp;&nbsp; 
                                                                                 配置名称：<input type="text" id="nameSearch" class="easyui-validatebox" data-options="width:100,prompt: '配置名称'"/>
                              &nbsp;数据库IP：<input type="text" id="ipSearch" class="easyui-validatebox" data-options="width:100,prompt:'数据库IP'"/>
                              <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-hamburg-search2" plain="true" onclick="configQuery()">查询</a>
                       
                         </div> 
  </div>
  <div id="dlgg" ></div>  
 
 <table id="dg6"></table> 
<script type="text/javascript">
var dgg;
var config;
var datascope = $('#datascope').val();
var datascopeArray = datascope.split(",");

$(function(){  
    initDataConfig();
});

function configQuery(){
	 initDataConfig();
}

function initDataConfig(){
	dgg=$('#dg6').datagrid({     
	method: "post",
    url: '${ctx}/system/config/configListData/'+Math.random(), 
    fit : true,
	fitColumns :true,
	border :false,
	idField:'id',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize :20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
	queryParams:{ name:$("#nameSearch").val(),ip:$("#ipSearch").val() },
    columns:[[    
    	{field:'id',checkbox:true}, 
	  	{field:'name',title:'配置名称',halign:'center',width:140 },
	  	{field:'databaseType',title:'数据库类型',sortable:true,halign:'center',width:80 },
	  	{field:'databaseName',title:'默认数据库',sortable:true,halign:'center',width:100 },
        {field:'ip',title:'数据库IP',sortable:true,halign:'center',width:100},
        {field:'port',title:'数据库端口',sortable:true,halign:'center',width:80}	  	
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tbConfigSelect',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false,
    onLoadSuccess: function (data) {
		if( datascopeArray.length >0 ){
			//选中指定行
    		for(j = 0; j < datascopeArray.length; j++) {
    			$("#dg6").datagrid("selectRecord",datascopeArray[j]);
    		}
    	}
    }
   }); 
  }

  // 保存选中的配置项
  function savePersonConfigSelect(){
	  var checkedItems = $('#dg6').datagrid('getChecked');
	 
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	  var ids = [];
      $.each(checkedItems, function(index, item){
    	  ids.push(item["id"]);
    	  //alert(item["id"] );
      }); 
      
      $.ajax({
			type:'POST',
		    contentType:'application/json;charset=utf-8',
            url:"${ctx}/system/person/saveSelectDatascope",
            data: JSON.stringify( {'id':$('#personId').val() , 'ids':ids } ),
            datatype: "json", 
            //成功返回之后调用的函数             
            success:function(data){
            	var obj = eval('(' + data + ')');
                var status = obj.status ;
            	if(status == 'success' ){
            	      parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	      setTimeout(function () {
                           person.panel('close');
                      }, 1500);
            	}else{
            	      parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
            	 }
            }  
      });
	  
  }
    
</script>

</body>
</html>