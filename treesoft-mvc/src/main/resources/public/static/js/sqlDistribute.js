 
var dgg;
var sqlDistribute;
var sqlDistributeLog;

$(function(){  
	
    initData();
});

function initData(){
	dgg=$('#dg3').datagrid({     
	method: "get",
    url: baseUrl+'/system/sqlDistribute/sqlDistributeList?v=' + Math.random(),
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
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'id',title:'操作',align:'center' ,halign:'center' ,width:80,sortable:true,formatter: function(value,row,index){
		         return '<a  class="l-btn l-btn-small" href="javascript:viewLog(\'' +row["id"]+  '\')" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text" style="line-height: 18px;">日志</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a> <a class="l-btn l-btn-small" href="javascript:editConfigFormOne(\'' +row["id"]+  '\')" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text" style="line-height: 18px;">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a> ';
	
	  	}},
	  	{field:'name',title:'任务名称',width:100 },
	  	{field:'databaseNumber',title:'数据库数量',sortable:true,width:100 },
        {field:'state',title:'是否启用',sortable:true,formatter: function(value,row,index){
				if (row.state =='0'){
					return '<font color="green">启用</font>';
				}else if (row.state =='1'){
					return '<font color="red">停用</font>';
				} else {
					return '';
				}
		}},
		{field:'runTime',title:'最后运行时间',sortable:true,width:100 },
        {field:'status',title:'最后运行状态',sortable:true,formatter: function(value,row,index){
				if (row.status =='0'){
					return '<font color="red">停止</font>';
				}else if (row.status =='1'){
					return '<font color="green">运行中</font>';
				}else if (row.status =='2'){
					return '<font color="green">成功</font>';
				}else if (row.status =='3'){
					return '<font color="red">异常</font>';
				} else {
					return '';
				}
		}}
	  	
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false,
    
    onDblClickRow: function (rowIndex, rowData) {  
    	var id =rowData.id  ;
	    sqlDistribute = $("#dlgg").dialog({   
	    title: '查看',    
	    width: 580,    
	    height: 400,    
	    href: baseUrl+'/system/sqlDistribute/editSqlDistributeForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[
	    	{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
					sqlDistribute.panel('close');
				}
		 }]
	  });
    }
   }); 
  }
     

   //打开 新增 编辑 对话框
   function addConfigForm(){
	    sqlDistribute = $("#dlgg").dialog({   
	    title: ' 新增',    
	    width: 580,    
	    height: 400,    
	    href: baseUrl+'/system/sqlDistribute/addSqlDistributeForm',
	    maximizable:true,
	    modal:true,
	    buttons:[ {
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
				$("#mainform").submit(); 
			}
		   },{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
					sqlDistribute.panel('close');
				}
		 }]
	  });
  }
   
 function editConfigForm(){
	
	var checkedItems = $('#dg3').datagrid('getChecked');
	if(checkedItems.length == 0 ){
	      parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		  return;
	 }
	    
	 if(checkedItems.length >1 ){
	      parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		  return;
	 }
	 
	 var id = checkedItems[0]['id']  ;
	 
	 sqlDistribute= $("#dlgg").dialog({   
	    title: '编辑',    
	    width: 580,    
	    height: 400,    
	    href: baseUrl+'/system/sqlDistribute/editSqlDistributeForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
				$("#mainform").submit(); 
			}
		   },{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
					sqlDistribute.panel('close');
				}
		 }]
	});
 }
 
 function editConfigFormOne( id ){
	 sqlDistribute= $("#dlgg").dialog({   
	    title: '编辑',    
	    width: 580,    
	    height: 400,    
	    href: baseUrl+'/system/sqlDistribute/editSqlDistributeForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
				$("#mainform").submit(); 
			}
		   },{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
					sqlDistribute.panel('close');
				}
		 }]
	});
 }
 
   //删除  
   function deleteConfig(){
	 
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  var data2=$('#dg3').datagrid('getData');
	  var totalLength = data2.total;
	  
	  // alert('总数据量:'+data.total)
	  
	   if(totalLength - length <= 0 ){
		 parent.$.messager.show({ title : "提示",msg: "必须保留一行配置信息！", position: "bottomRight" });
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
                    url: baseUrl+"/system/sqlDistribute/sqlDistributeDelete",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: "删除成功！", position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
            });
   }
    
   // 启动  任务  
   function startTask3(){
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  var data2=$('#dg3').datagrid('getData');
	  var totalLength = data2.total;
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["id"] );
     }); 
     parent.$.messager.show({ title : "提示",msg: "任务已提交！", position: "bottomRight" });
      $.ajax({
		 type:'POST',
		 contentType:'application/json;charset=utf-8',
         url: baseUrl+"/system/sqlDistribute/startSqlDistribute",
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
   
   // 停止 定时任务任务  
   function stopTask(){
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  var data2=$('#dg3').datagrid('getData');
	  var totalLength = data2.total;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["id"] );
     }); 
              	
      $.ajax({
		 type:'POST',
		 contentType:'application/json;charset=utf-8',
         url: baseUrl+"/system/sqlDistribute/stopSqlDistribute",
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
   
   function refresh12(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
   }
   
   
   //打开 日志查看   对话框
   function viewLog( taskId ){
	    sqlDistributeLog = $("#dlgg2").dialog({   
	    title: '日志查看',    
	    width: 880,    
	    height: 480,    
	    href: baseUrl+'/system/sqlDistribute/sqlDistributeLogForm/'+taskId ,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
					sqlDistributeLog.panel('close');
				}
		 }]
	  });
  }
   
    //复制
   function copySqlDistribute(){
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
                	
      $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:baseUrl+"/system/sqlDistribute/copySqlDistribute",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $('#dg3').datagrid('reload');
            	    	   $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg:data.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
      });
            
   }
   
     