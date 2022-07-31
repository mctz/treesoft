<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据库总览</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

<script type="text/javascript" src="${ctx}/static/js/echarts.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/pie.js"></script> 
<style>

.main {
    overflow:hidden;
    height:220px;
    padding:0px;
    margin:5px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}

</style>
</head>
<body>
<div style="float:center;width:98%;margin:0 auto">
    <div class ="mainChar" style="float:left;width:100%;"> 
      <div id="graphic1" style="height:auto;width:100%;"> 
            
            <div id="main0" class="main panel-body" style="float:left; width:48%;">
               <div  style="font-size:14px;margin:5px 10px 5px 10px;  float:left;">
               <span class='icon-hamburg-statistics'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>数据库总分布 </div>  
               <div id="main4"   style="float:right;height:200px;width:75%;"></div>
            </div> 
            
            <div id="main1" class="main panel-body" style="float:right;width:49%;">
               <div style="font-size:14px;margin:5px 10px 5px 10px"><span class='icon-hamburg-finished'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>数据库总览表</div>  
               <table id="dbList"></table> 
            </div>
      </div> 
    </div> 
    
    <div id="graphic2" style="width:100%;float:left;"> 
          <div id="main2" class="main panel-body" style="float:left;width:100%;">
               <div style="font-size:14px;margin:5px 10px 5px 10px;">
                   <a id="monitorConfigFormTwo" class="easyui-linkbutton" iconCls="icon-standard-bricks" plain="true" title="安全运维告警 配置" ></a>安全运维告警 
               </div>  
               <table id="monitorList"></table> 
          </div>
    </div> 
          
    <div id="graphic3" style="width:100%;float:left;"> 
           <div id="main3" class="main panel-body" style="float:left;width:100%;">
               <div style="font-size:14px;margin:5px 10px 5px 10px;">
                 <a id="countDBDataTotals" class="easyui-linkbutton" iconCls="icon-standard-application-view-columns" plain="true" title=""></a>数据量TOP5 
               </div>  
               <table id="dataTop5List"></table> 
          </div>
    </div>       
</div>
 <div id="dlgg" ></div>  
 
 <script type="text/javascript" >
 var dbList;
 var monitorList;
 var dataTop5List;
 var myChart4 ;
 var timeTicket5;
  
  $(function(){   
     
    dataBaseTypeCountData();
    dataBaseListData();
    monitorData();
    dataTop5();
    
    //20秒查询一次 运维告警信息
    clearInterval(timeTicket5);
    timeTicket5 = setInterval(function (){
       // monitorData();
        $('#monitorList').datagrid('reload');
       // $('#dbList').datagrid('reload');
    }, 20000);
    
   
  });
   
   //窗口宽度改变时，datagrig列表刷新，不然列表宽度没有自适应，仅显示一部分，不好看！
  $(window).resize(function(){
      refresh();
  }); 
   
   //刷新页面
   function refresh(){
      $('#dbList').datagrid("resize");
      $('#monitorList').datagrid("resize");
      $('#dataTop5List').datagrid("resize");    
   }
   
  //状态监控
  function  monitor(databaseConfigId, databaseName ){
	 parent.mainpage.mainTabs.addModule( '状态监控' ,baseUrl+'/system/permission/i/monitor/'+databaseName +'/'+databaseConfigId,'icon-hamburg-equalizer');
  } 
   
  //查询统计 数据库类型及数量,更新饼图
  function  dataBaseTypeCountData(){
    $.ajax({
		type:"POST",
		contentType:"application/json;charset=utf-8",
		url:baseUrl+"/system/config/dataBaseTypeCount",
		datatype: "json", 
		success: function(response){
		    var json = eval('(' + response + ')'); 
			var option4 = myChart4.getOption();
            var legendTitles = new Array();
            $.each( json.data, function(index, dataItem){
               legendTitles.push( dataItem.name );
            });
            option4.legend.data = legendTitles;
            option4.series[0].data = json.data ;
            myChart4.setOption(option4);  
		}
	});
  }
 
  function dataBaseListData(){
	dbList = $('#dbList').datagrid({     
	method: "post",
    url: '${ctx}/system/config/configListForMonitor/'+Math.random(), 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:false,
	rownumbers:false,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
        
        {field:'databaseName',title:'databaseName',hidden:true,width:0},
	  	{field:'name',title:'数据库名称',halign:'center',width:100 },
	  	{field:'databaseType',title:'数据库类型', halign:'center',width:100 },	   
        {field:'ip',title:'数据库IP', halign:'center',width:100},
        {field:'port',title:'数据库端口', halign:'center',width:70},        
        {field:'status',title:'当前状态',halign:'center',align:'center',width:50,sortable:true,formatter: function(value,row,index){
				if (row.status =='0'){
					return '<font color="red">离线</font>';
				}else if (row.status =='1'){
					return '<font >在线</font>';
				}else if (row.status =='2'){
					return '<font color="yellow">异常</font>';
				} else {
				    return '<font color="grey"> </font>';
				}
		}},
        {field:'id',title:'操作',halign:'center',width:60, formatter: function(value,row,index){
			return '<a  class="l-btn l-btn-small" href="javascript:monitor( \''+ row["id"] +'\',\''+ row["databaseName"] +'\' )" ><span><span class="l-btn-text" style="line-height: 18px;">查看详情 </span></a> ';
		}}
    ]],
    enableHeaderClickMenu: false,
    enableHeaderContextMenu: false,
    enableRowContextMenu: false,
    dataPlain: true
   }); 
  }
  
  function monitorData(){
	monitorList = $('#monitorList').datagrid({     
	method: "post",
    url: '${ctx}/monitorGlobal/selectDBAlarm', 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:false,
	rownumbers:false,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
        {field:'ip',title:'数据库IP', halign:'center',width:60},
	  	{field:'configName',title:'数据库名称',halign:'center',width:80 },
	  	{field:'createTime',title:'告警时间',halign:'center',width:80 },
	  	{field:'level',title:'告警等级',halign:'center',align:'center',width:50,sortable:true,formatter: function(value,row,index){
				if (row.level =='error'){
					return '<font color="red">严重</font>';
				}else if (row.level =='warning'){
					return '<font color="orange">警告</font>';
				} else {
					return '待定';
				}
		}},
	  	{field:'type',title:'告警类型', halign:'center',width:60 }, 
        {field:'message',title:'告警详情', halign:'center',width:180}
        
    ]] ,
    enableHeaderClickMenu: false,
    enableHeaderContextMenu: false,
    enableRowContextMenu: false,
    dataPlain: true,
    onLoadSuccess:function(data){
       if(data.total == 0){
          var body = $(this).data().datagrid.dc.body2;
		  var width = body.width();
		  body.find('table tbody').append('<tr><td width = \''+width+'\' style=\' height: 25px; text-align: center;\' colspan=\'6\'>暂无数据！</td></tr>');
       }
    }
   }); 
  }
  
  function dataTop5(){
	dataTop5List = $('#dataTop5List').datagrid({     
	method: "post",
    url: '${ctx}/monitorGlobal/selectTop5Amount', 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:false,
	rownumbers:false,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
            {field:'id',title:'id', hidden:'true' },	
	  		{field:'rank',title:'排名', halign:'center',width:100,align:'center' },	 
	  	    {field:'name',title:'数据库名称', halign:'center',width:100 },
            {field:'databaseType',title:'数据库类型', halign:'center',width:100,align:'center'},
            {field:'totals',title:'数据量', halign:'center',width:100,align:'center'}
    ]] ,
    enableHeaderClickMenu: false,
    enableHeaderContextMenu: false,
    enableRowContextMenu: false,
    dataPlain: true
   }); 
  }
          
  // 使用  
  require(  
         [  
            'echarts', 
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载  
        ], 
  function (ec) {  
    myChart4 = ec.init(document.getElementById('main4'));   
    var option4 = {
     title : {
        show: false        
     },
    // color:['#45C2E0', '#C1EBDD', '#FFC851','#5A5476','#1869A0','#FF9393'],
     color: ['#00C0EF','#00A65A', '#F39C12', '#DD4B39', '#3C8DBC','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570'],
     tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
     },
     legend: {
        orient : 'vertical',
        x : 'right',
        y : 'center',
        data:['MySQL']
     },
    series : [
        {
            name:'数据库总分布',
            type:'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            center:['35%'],
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: false,
                    fontSize: '30',
                    fontWeight: 'bold'
                }
            },            
            data:[
                {value:1, name:'MySQL'}
            ]
        }
    ]
  };
   myChart4.setOption(option4);   
  }  
 );              
 
   // 进行数据量的统计 的计算(异步执行)
   $("#countDBDataTotals").click(function (){
	  $.easyui.messager.confirm("操作提示", "您确定进行数据量的统计吗？", function (c) {
                if (c) {
                   var ids = [];
                   var checkedItems = $('#dataTop5List').datagrid('getChecked');
                   $.each( checkedItems, function(index, item){
    	              ids.push( item["id"] );
                   }); 
                
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/monitorGlobal/countDBDataTotals",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	            parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }
            	     }  
                   });
                }
       });
   });
 
   //打开 告警配置 对话框
   $("#monitorConfigForm").click(function (){
	    monitorConfig = $("#dlgg").dialog({   
	    title: '&nbsp;告警配置',    
	    width: 400,    
	    height: 370,    
	    href:baseUrl+'/monitorConfig/monitorConfigForm?id=fe03df91fc8e',
	    maximizable:true,
	    modal:true,
	    buttons:[ {
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
				$("#monitorConfigForm").submit(); 
			}
		   }, {
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					monitorConfig.panel('close');
				}
		 }]
	  });
   });
  
 </script> 
</body>
</html>
