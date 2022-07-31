<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Elasticsearch monitor</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%> 

<script type="text/javascript" src="${ctx}/static/js/echarts.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/line.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/bar.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/pie.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/gauge.js"></script>
<style>

.main {
    overflow:hidden;
    height:250px;
    padding:0px;
    margin:10px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}

.mainChar {
    overflow:hidden;
    padding :0px;
    margin:10px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}

.main3 {
    overflow:hidden;
    height:250px;
    width:48%;
    float:left;
    padding:0px;
    margin:10px;
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
 <div id="tb2" style="height:auto">
 <input type="hidden" id="databaseConfigId"  value="${databaseConfigId}" >
 <input type="hidden" id="databaseName"  value="${databaseName}" >
                         <div class="panel-header panel-header-noborder  " 	style="padding: 5px; height: auto">
                         
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-gear" plain="true"   onclick="monitorItem()">详细状态参数</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
                            <input type="checkbox" checked id="isAuto" >自动刷新  </input>
	                        <span class="toolbar-item dialog-tool-separator"></span>
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="刷新间隔10秒"></a>  
                            <span class="toolbar-item dialog-tool-separator" ></span>
                             
                             <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"  >【${databaseType}】${name} ${ip}:${port} </a>
                            <span class="toolbar-item dialog-tool-separator"></span>                                            
                         </div> 
  </div>


  <div>
    <div class ="mainChar" style="text-align:center"> 
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#00C0EF">
        <span style="font-size:20px;color:#fff;" id="cluster_status" title="cluster status" >0 </span>  <br>
        <span   style="color:#fff;" >status </span>
      </div>
      <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#00A65A">
          <span style="font-size:20px;color:#fff;" id="node_total">0 </span> <br>
           <span   style="color:#fff;"> node.total </span> 
     </div>
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#F39C12">
         <span style="font-size:20px;color:#fff;" id="node_data" title="node.data">0 </span> <br> 
         <span   style="color:#fff;">node.data</span> 
     </div>
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#DD4B39" >
         <span style="font-size:20px;color:#fff;" id="shards" title="shards">0 </span> <br>
          <span   style="color:#fff;">shards </span> 
     </div>
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#3C8DBC">
          <span style="font-size:20px;color:#fff;" id="active_shards_percent">0 </span> <br>
          <span   style="color:#fff;" >active_shards_percent</span> 
     </div>
  </div>

  <div>
       <div  id="graphic4" style="height:auto;width:auto;"> 
          <div id="main4" class="main" style="float:left;width:46%;"></div>
          <div id="main5" class="main" style="float:right;width:46%;"></div>
       </div> 
  </div>
 
<script type="text/javascript">

var timeTicket
var lastData = 11;
var axisData;
var databaseConfigId;
var databaseName ;
//var questions1=0;
//var questions_qps = 0;

$(function(){ 
     databaseConfigId = $("#databaseConfigId").val();
	 databaseName = $("#databaseName").val();
      mainAddData();
     
});
     
function refresh(){
	  
}

function monitorItem(){
	 parent.window.mainpage.mainTabs.addModule( '详细状态参数' ,'${ctx}/system/permission/i/monitorItem/'+databaseName +'/'+ databaseConfigId ,  'icon-berlin-statistics');
}
  
  var myChart4 ;
  var myChart5 ;
  
  function mainAddData(){
	var datetime = new Date();
     axisData = datetime.getHours()+":" + datetime.getMinutes() +":" + datetime.getSeconds();
	$.ajax({
	    type:"post",	  
		url:"${ctx}/system/permission/i/queryDatabaseStatus/"+ databaseName +'/'+databaseConfigId ,
		dataType : "json",
		success:function(data) {
		    // myChart.hideLoading();
			var status = data.status ;
			if(status == 'success' ){
				 $("#cluster_status").html( data.cluster_status + "<font color='"+ data.cluster_status +"'> ██ </font>" );
		         $("#node_total").html( data.node_total   );
		         $("#node_data").html( data.node_data );
		         $("#shards").html(data.shards );
		         $("#active_shards_percent").html( data.active_shards_percent ) ;
		         
		        //更新数据
		        var option4 = myChart4.getOption();
		        option4.series[0].data = [{value:data.diskUsage, name:'已使用'},{value:100-data.diskUsage, name:'未使用'}];   
		        myChart4.setOption(option4);    
			      
		        //更新数据
		        var option5 = myChart5.getOption();
		        option5.series[0].data = [{value:data.memUsage, name:'已使用'}];   
		        myChart5.setOption(option5);  
			}
		},
		error: function(errorMsg) {
		  //  alert("不好意思，图表请求数据失败啦！");
		}
	});
  }
    
  // 使用  
  require(  
         [  
            'echarts', 
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载  
        ], 
  function (ec) {  
    // 基于准备好的dom，初始化echarts图表  
    myChart4 = ec.init(document.getElementById('main4'));   
          
    var option4 = {
    title : {
        text: '磁盘占用率',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        data:['已使用','未使用']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: false},
            dataView : {show: true, readOnly: false},
             
            restore : {show: false},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    series : [
        {
            name:'磁盘占用率',
            type:'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {value:0, name:'已使用'},
                {value:0, name:'未使用'}
            ]
        }
    ]
  };
  
        // 为echarts对象加载数据   
            myChart4.setOption(option4);   
        }  
   );              
 
 
  // 使用  
  require(  
         [  
            'echarts', 
            'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载  
        ], 
  function (ec) {  
     // 基于准备好的dom，初始化echarts图表  
     myChart5 = ec.init(document.getElementById('main5'));   
    
     var  option5 = {
        title : {
        text: '内存占用率',
        x:'center'
        },
       tooltip : {
          formatter: "{a} <br/>{b} : {c}%"
       },
      toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            restore : {show: true},
            saveAsImage : {show: true}
         }
       },
       series : [
           {
              name:'内存占用率',
              type:'gauge',
              detail : {formatter:'{value}%'},
              data:[{value: 0, name: '占用率'}]
            }
          ]
      };
          // 为echarts对象加载数据   
          myChart5.setOption(option5);   
        }  
   );              
</script>
</body>
</html>