
var timeTicket
var lastData = 11;
var axisData;
var databaseConfigId;
var databaseName ;


$(function(){ 
     databaseConfigId = $("#databaseConfigId").val();
	 databaseName = $("#databaseName").val();
     clearInterval(timeTicket);
     timeTicket = setInterval(function (){
        mainAddData();
     }, 10000);
                    	
	  queryInfoItem();
     	  
});
     
function refresh(){
	  queryInfoItem();
}

function monitorItem(){
	 parent.window.mainpage.mainTabs.addModule( '详细状态参数' ,baseUrl+'/system/permission/i/monitorItem/'+databaseName +'/'+ databaseConfigId ,  'icon-berlin-statistics');
}
    
 //查询状态参数
 function queryInfoItem(){
	// databaseConfigId  = $('#databaseConfigId').val();
	 //alert( databaseConfigId  );
	  $.ajax({
		type:'post',
		url:baseUrl+"/system/permission/i/queryDatabaseStatus/"+ databaseName +'/'+databaseConfigId,
		success: function(data){
			var status = data.status ;
			if(status == 'success' ){
				 $("#max_used_connections").html(data.Max_used_connections );
		         $("#Uptime").html( parseInt( data.Uptime /60/60/24 ) );
		         $("#Threads_connected").html( data.Threads_connected  );
		         $("#Threads_running").html(data.Threads_running  );
		         $("#Open_tables").html( data.Open_tables  ) ;
			}else{
				 parent.$.messager.show({ title : "提示",msg: data.mess , position: "Center" });
			}
		   
		}
      });
 }
 
  var myChart;
  var myChart2 ;
  var myChart3 ;
  var myChart4 ;
  var myChart5 ;
  
   // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            myChart = ec.init( document.getElementById('main1') );   
            var option = {
             title : {
               text: 'sent or received kbps',
               subtext: ''
             },
             tooltip : {
               trigger: 'axis'
              },
              legend: {
                 data:['Bytes_received','Bytes_sent']
              },
            toolbox: {
               show : true,
               feature : {
                  mark : {show: false},
                  dataView : {show: true, readOnly: true},
                  magicType : {show: true, type: ['line', 'bar']},
                  restore : {show: true},
                  saveAsImage : {show: true}
                }
             }, 
            calculable : true,
            xAxis : [
             {
               type : 'category',
               data :[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
             }
            ],
            yAxis : [
            {
                type : 'value',
                data: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
             }
            ],
           series : [
           {
               name:'Bytes_received',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
            },
           {
               name:'Bytes_sent',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
            }
           ]
        };
     
        // 为echarts对象加载数据   
            myChart.setOption(option);   
        }  
   );  
   
   
  var begin_old=0;
  var commit_old=0;
  var delete_old=0;
  var insert_old=0;
  var rollback_old=0;
  var select_old=0;
  var update_old=0;
  
  var Bytes_received_old=0;
  var Bytes_sent_old=0;
   
  function mainAddData(){
	var datetime = new Date();
     axisData = datetime.getHours()+":" + datetime.getMinutes() +":" + datetime.getSeconds();
	$.ajax({
	    type:"post",	  
		url:baseUrl+"/system/permission/i/queryDatabaseStatus/"+ databaseName +'/'+databaseConfigId ,
		dataType : "json",
		success:function(data) {
		  // myChart.hideLoading();
		 
		  
		  if(Bytes_received_old == 0){
			  Bytes_received_old = data.Bytes_received ;
		  }
		  if(Bytes_sent_old == 0){
			  Bytes_sent_old = data.Bytes_sent ;
		  }
		  
		  // 动态数据接口 addData
          myChart.addData([
            [
              0,        // 系列索引
           Math.abs(  parseInt( (data.Bytes_received - Bytes_received_old) /1000 ) ), // 新增数据
              false,     // 新增数据是否从队列头部插入
              false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            ],
            [
              1,        // 系列索引
            Math.abs( parseInt((data.Bytes_sent - Bytes_sent_old) /1000 )), // 新增数据
              false,    // 新增数据是否从队列头部插入
              false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
              axisData  // 坐标轴标签, For example 18:10:12
            ]
         ]);
		  
		  
		  if(begin_old == 0){
			  begin_old = data.Com_begin;
		  }
		  if(commit_old == 0){
			  commit_old = data.Com_commit;
		  }
		  if(delete_old == 0){
			  delete_old = data.Com_delete;
		  }
		  if(insert_old == 0){
			  insert_old = data.Com_insert;
		  }
		  if(rollback_old == 0){
			  rollback_old = data.Com_rollback;
		  }
		  if(select_old == 0){
			  select_old = data.Com_select;
		  }
		  if(update_old == 0){
			  update_old = data.Com_update;
		  }
		  
		  myChart2.addData([
            [
              0,        // 系列索引
            Math.abs( (data.Com_begin - begin_old)) /10 , // 新增数据 begin
              false,    // 新增数据是否从队列头部插入
              false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            ],
            [
              1,        
              Math.abs( ( data.Com_commit - commit_old )) /10, // 新增数据 commit
              false,     
              false   
            ],
            [
              2,        
              Math.abs( (data.Com_delete - delete_old)) / 10 , // 新增数据delete
              false,     
              false    
            ],
            [
              3,        
              Math.abs((data.Com_insert - insert_old)) / 10  , // 新增数据insert
              false,     
              false    
            ],
            [
              4,        
               Math.abs(( data.Com_rollback - rollback_old)) / 10, // 新增数据rollback
              false,     
              false    
            ],
            [
              5,        
               Math.abs(( data.Com_select - select_old )) / 10 ,  // 新增数据select
              false,     
              false     
            ],
            [
              6,        
               Math.abs( (data.Com_update - update_old)) / 10, // 新增数据update
              false,     
              false,
              axisData  // 坐标轴标签, For example 18:10:12
            ] 
            
          ]);
		  
		 // alert( data.Com_select +" " + select_old );
		  
		  begin_old  = data.Com_begin;
		  commit_old = data.Com_commit;
		  delete_old = data.Com_delete;
	      insert_old = data.Com_insert;
	      rollback_old=data.Com_rollback;
		  select_old = data.Com_select;
		  update_old = data.Com_update ;
		  
		  Bytes_received_old = data.Bytes_received ;
		  Bytes_sent_old = data.Bytes_sent ; ;
		  
	      myChart3.addData([
               [
                0,        // 系列索引
               data.Threads_connected, // 新增数据
                false,    // 新增数据是否从队列头部插入
                false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
                axisData  // 坐标轴标签, For example 18:10:12
               ]
           ]);
	      
	     //更新数据
          var option4 = myChart4.getOption();
          option4.series[0].data = [{value:data.diskUsage, name:'已使用'},{value:100-data.diskUsage, name:'未使用'}];   
          myChart4.setOption(option4);    
	      
          //更新数据
          var option5 = myChart5.getOption();
          option5.series[0].data = [{value:data.memUsage, name:'已使用'}];   
          myChart5.setOption(option5);    
	      
          
		  $("#Questions").html( data.Questions );
		  $("#Connections").html(data.Connections  );
		  $("#Threads_connected").html( data.Threads_connected  );
		  $("#Threads_running").html(data.Threads_running  );
		  $("#Open_tables").html( data.Open_tables  ) ;
		   
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
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
      function (ec) {  
       // 基于准备好的dom，初始化echarts图表  
       myChart2 = ec.init(document.getElementById('main2') );   
          
        var option = {  
             title : {  
             text: 'QPS(事务数/秒)',  
             subtext: ''  
      },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['begin','commit','delete','insert','rollback','select','update']  
      },  
      toolbox: {  
        show : true,  
        feature : {  
            mark : {show: false},  
            dataView : {show: true, readOnly: false},  
            magicType: {show: true, type: ['line', 'bar']},  
            restore : {show: true},  
            saveAsImage : {show: true}  
        }  
      },  
      calculable : true,  
      xAxis : [  
        {  
        	type : 'category',  
            data : [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
      ],  
      yAxis : [  
        {  
            type : 'value',  
            data: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
      ],  
      
      
      series : [  
        {  
               name:'begin',  
               type:'line',  
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'commit',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'delete',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'insert',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'rollback',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'select',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'update',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }
        
       ]  
     };  
          // 为echarts对象加载数据   
           myChart2.setOption(option);   
        }  
   ); 
    
   // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            myChart3 = ec.init(document.getElementById('main3') );   
          
            var option = {  
             title : {  
             text: 'Threads_connected',  
             subtext: ''  
           },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['Threads_connected']  
      },  
      toolbox: {  
        show:true,  
        feature : {  
            mark : {show: false},  
            dataView : {show: true, readOnly: false},  
            magicType: {show: true, type: ['line', 'bar']},  
            restore : {show: true},  
            saveAsImage : {show: true}  
        }  
      },  
      calculable:true,  
      xAxis : [  
        {  
        	type:'category',  
            data :[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
      ],  
      yAxis : [  
        {  
            type:'value',  
            data: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0] 
        }  
      ],  
      series : [  
        {  
            name:'Threads_connected',  
            type:'line',  
            data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
       ]  
     };  
            // 为echarts对象加载数据   
            myChart3.setOption(option);   
        }  
   ); 
    
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
                {value:1, name:'未使用'}
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
       