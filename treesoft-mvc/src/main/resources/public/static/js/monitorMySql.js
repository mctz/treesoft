var timeTicket,lastData=11,axisData,databaseConfigId,databaseName;$(function(){databaseConfigId=$("#databaseConfigId").val();databaseName=$("#databaseName").val();clearInterval(timeTicket);timeTicket=setInterval(function(){mainAddData()},1E4);queryInfoItem();$("#isAuto").change(function(){$(this).is(":checked")?timeTicket=setInterval(function(){mainAddData()},1E4):clearInterval(timeTicket)});parent.$.messager.show({title:"\u63d0\u793a",msg:"\u6570\u636e\u5237\u65b0\u95f4\u969410\u79d2\uff01",position:"top"})});
function refresh(){queryInfoItem()}function monitorItem(){parent.window.mainpage.mainTabs.addModule("\u8be6\u7ec6\u72b6\u6001\u53c2\u6570",baseUrl+"/system/permission/i/monitorItem/"+databaseName+"/"+databaseConfigId,"icon-berlin-statistics")}
function queryInfoItem(){$.ajax({type:"post",url:baseUrl+"/system/permission/i/queryDatabaseStatus/"+databaseName+"/"+databaseConfigId,success:function(b){"success"==b.status?($("#max_used_connections").html(b.Max_used_connections),$("#Uptime").html(parseInt(b.Uptime/60/60/24)),$("#Threads_connected").html(b.Threads_connected),$("#Threads_running").html(b.Threads_running),$("#Open_tables").html(b.Open_tables)):parent.$.messager.show({title:"\u63d0\u793a",msg:b.mess,position:"Center"})}})}
var myChart,myChart2,myChart3,myChart4,myChart5;
require(["echarts","echarts/chart/line","echarts/chart/bar"],function(b){myChart=b.init(document.getElementById("main1"));myChart.setOption({title:{text:"sent or received kbps",subtext:""},tooltip:{trigger:"axis"},legend:{data:["Bytes_received","Bytes_sent"]},toolbox:{show:!0,feature:{mark:{show:!1},dataView:{show:!0,readOnly:!0},magicType:{show:!0,type:["line","bar"]},restore:{show:!0},saveAsImage:{show:!0}}},calculable:!0,xAxis:[{type:"category",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0]}],yAxis:[{type:"value",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}],series:[{name:"Bytes_received",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"Bytes_sent",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]})});var begin_old=0,commit_old=0,delete_old=0,insert_old=0,rollback_old=0,select_old=0,update_old=0,Bytes_received_old=0,Bytes_sent_old=0;
function mainAddData(){var b=new Date;axisData=b.getHours()+":"+b.getMinutes()+":"+b.getSeconds();$.ajax({type:"post",url:baseUrl+"/system/permission/i/queryDatabaseStatus/"+databaseName+"/"+databaseConfigId,dataType:"json",success:function(a){0==Bytes_received_old&&(Bytes_received_old=a.Bytes_received);0==Bytes_sent_old&&(Bytes_sent_old=a.Bytes_sent);myChart.addData([[0,Math.abs(parseInt((a.Bytes_received-Bytes_received_old)/1E3)),!1,!1],[1,Math.abs(parseInt((a.Bytes_sent-Bytes_sent_old)/1E3)),!1,
!1,axisData]]);0==begin_old&&(begin_old=a.Com_begin);0==commit_old&&(commit_old=a.Com_commit);0==delete_old&&(delete_old=a.Com_delete);0==insert_old&&(insert_old=a.Com_insert);0==rollback_old&&(rollback_old=a.Com_rollback);0==select_old&&(select_old=a.Com_select);0==update_old&&(update_old=a.Com_update);myChart2.addData([[0,Math.abs(a.Com_begin-begin_old)/10,!1,!1],[1,Math.abs(a.Com_commit-commit_old)/10,!1,!1],[2,Math.abs(a.Com_delete-delete_old)/10,!1,!1],[3,Math.abs(a.Com_insert-insert_old)/10,
!1,!1],[4,Math.abs(a.Com_rollback-rollback_old)/10,!1,!1],[5,Math.abs(a.Com_select-select_old)/10,!1,!1],[6,Math.abs(a.Com_update-update_old)/10,!1,!1,axisData]]);begin_old=a.Com_begin;commit_old=a.Com_commit;delete_old=a.Com_delete;insert_old=a.Com_insert;rollback_old=a.Com_rollback;select_old=a.Com_select;update_old=a.Com_update;Bytes_received_old=a.Bytes_received;Bytes_sent_old=a.Bytes_sent;myChart3.addData([[0,a.Threads_connected,!1,!1,axisData]]);var b=myChart4.getOption();b.series[0].data=[{value:a.diskUsage,
name:"\u5df2\u4f7f\u7528"},{value:100-a.diskUsage,name:"\u672a\u4f7f\u7528"}];myChart4.setOption(b);b=myChart5.getOption();b.series[0].data=[{value:a.memUsage,name:"\u5df2\u4f7f\u7528"}];myChart5.setOption(b);$("#Questions").html(a.Questions);$("#Connections").html(a.Connections);$("#Threads_connected").html(a.Threads_connected);$("#Threads_running").html(a.Threads_running);$("#Open_tables").html(a.Open_tables)},error:function(a){}})}
require(["echarts","echarts/chart/line","echarts/chart/bar"],function(b){myChart2=b.init(document.getElementById("main2"));myChart2.setOption({title:{text:"QPS(\u4e8b\u52a1\u6570/\u79d2)",subtext:""},tooltip:{trigger:"axis"},legend:{data:"begin commit delete insert rollback select update".split(" ")},toolbox:{show:!0,feature:{mark:{show:!1},dataView:{show:!0,readOnly:!1},magicType:{show:!0,type:["line","bar"]},restore:{show:!0},saveAsImage:{show:!0}}},calculable:!0,xAxis:[{type:"category",data:[0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}],yAxis:[{type:"value",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}],series:[{name:"begin",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"commit",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"delete",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"insert",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"rollback",type:"line",
data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"select",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{name:"update",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]})});
require(["echarts","echarts/chart/line","echarts/chart/bar"],function(b){myChart3=b.init(document.getElementById("main3"));myChart3.setOption({title:{text:"Threads_connected",subtext:""},tooltip:{trigger:"axis"},legend:{data:["Threads_connected"]},toolbox:{show:!0,feature:{mark:{show:!1},dataView:{show:!0,readOnly:!1},magicType:{show:!0,type:["line","bar"]},restore:{show:!0},saveAsImage:{show:!0}}},calculable:!0,xAxis:[{type:"category",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}],yAxis:[{type:"value",
data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}],series:[{name:"Threads_connected",type:"line",data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]})});
require(["echarts","echarts/chart/pie"],function(b){myChart4=b.init(document.getElementById("main4"));myChart4.setOption({title:{text:"\u78c1\u76d8\u5360\u7528\u7387",x:"center"},tooltip:{trigger:"item",formatter:"{a} <br/>{b} : {c} ({d}%)"},legend:{orient:"vertical",x:"left",data:["\u5df2\u4f7f\u7528","\u672a\u4f7f\u7528"]},toolbox:{show:!0,feature:{mark:{show:!1},dataView:{show:!0,readOnly:!1},restore:{show:!1},saveAsImage:{show:!0}}},calculable:!0,series:[{name:"\u78c1\u76d8\u5360\u7528\u7387",
type:"pie",radius:"55%",center:["50%","60%"],data:[{value:0,name:"\u5df2\u4f7f\u7528"},{value:1,name:"\u672a\u4f7f\u7528"}]}]})});
require(["echarts","echarts/chart/gauge"],function(b){myChart5=b.init(document.getElementById("main5"));myChart5.setOption({title:{text:"\u5185\u5b58\u5360\u7528\u7387",x:"center"},tooltip:{formatter:"{a} <br/>{b} : {c}%"},toolbox:{show:!0,feature:{mark:{show:!0},restore:{show:!0},saveAsImage:{show:!0}}},series:[{name:"\u5185\u5b58\u5360\u7528\u7387",type:"gauge",detail:{formatter:"{value}%"},data:[{value:0,name:"\u5360\u7528\u7387"}]}]})});