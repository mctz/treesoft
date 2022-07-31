<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>欢迎登录，请填写数据库连接信息 </title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx }/system/config/configUpdate" method="post">
	    <input id="status" name="status" type="hidden" value="0"  />
		<table class="formTable">
		     <tr>
				<td align="center" colspan="2">
				<span class="icon-standard-star">&nbsp;&nbsp;&nbsp;&nbsp;</span>
				欢迎登录，请填写数据库连接信息&nbsp;
				<span class="icon-standard-star">&nbsp;&nbsp;&nbsp;&nbsp;</span>
				</td>				 
		      </tr>
			 <tr>
				<td align="center" colspan="2">----------------------------------------------------------------</td>				 
		      </tr>
		      
		      <tr>
				<td align="right">配置名称：</td>
				<td><input id="name"  name="name" type="text"   class="easyui-validatebox"   data-options="width: 150,required:'required'"/>
				</td>
		      </tr>
			  <tr>
				<td align="right">数据库类型： </td>
				<td>
				<select id="databaseType" name="databaseType" class="esayui-combobox" style="width:150px" >
				   <option value="MySQL" <c:if test="${config.databaseType=='MySQL'}"> selected </c:if> >MySQL</option> 
				   <option value="MySQL8.0" <c:if test="${config.databaseType=='MySQL8.0'}"> selected </c:if> >MySQL8.0</option> 
				   <option value="MariaDB" <c:if test="${config.databaseType=='MariaDB'}"> selected </c:if> >MariaDB</option> 
				   <option value="SQL Server" <c:if test="${config.databaseType == 'SQL Server'}">selected</c:if>  >SQL Server</option>
				   <option value="MongoDB" <c:if test="${config.databaseType == 'MongoDB' }">selected</c:if>  >MongoDB</option>
				   <option value="Oracle" <c:if test="${config.databaseType == 'Oracle'}">selected</c:if>  >Oracle</option>
				   <option value="ES" <c:if test="${config.databaseType == 'ES' }">selected</c:if>  >ElasticSearch</option>
				   <option value="PostgreSQL" <c:if test="${config.databaseType == 'PostgreSQL' }">selected</c:if>  >PostgreSQL</option>
				   <option value="Redshift" <c:if test="${config.databaseType == 'Redshift' }">selected</c:if>  >Redshift</option>
				   <option value="Hive2" <c:if test="${config.databaseType == 'Hive2' }">selected</c:if>  >Hive2</option>
				   <option value="HANA2" <c:if test="${config.databaseType == 'HANA2' }">selected</c:if>  >HANA2</option>
				   <option value="Impala" <c:if test="${config.databaseType == 'Impala' }">selected</c:if>  >Impala</option>				   
				   <option value="Informix" <c:if test="${config.databaseType=='Informix' }"> selected </c:if> >Informix</option> 
				   
				   <option value="DB2" <c:if test="${config.databaseType=='DB2' }"> selected </c:if> >DB2</option> 
				   <option value="DM7" <c:if test="${config.databaseType=='DM7' }"> selected </c:if> >达梦DM</option> 
				   <option value="Kingbase" <c:if test="${config.databaseType=='Kingbase' }"> selected </c:if> >Kingbase</option> 
				   <option value="Sybase" <c:if test="${config.databaseType=='Sybase' }"> selected </c:if> >Sybase</option>
				   <option value="ShenTong" <c:if test="${config.databaseType=='ShenTong' }"> selected </c:if> >ShenTong</option> 
				   <option value="TiDB" <c:if test="${config.databaseType=='TiDB' }"> selected </c:if> >TiDB</option> 				   
				   <option value="Cache" <c:if test="${config.databaseType=='Cache' }"> selected </c:if> >Cache</option> 
				   <option value="ClickHouse" <c:if test="${config.databaseType=='ClickHouse' }"> selected </c:if> >ClickHouse</option> 
				   
				   <option value="Redis" <c:if test="${config.databaseType=='Redis' }"> selected </c:if> >Redis </option> 
				   <!--  
				   <option value="Memcache" <c:if test="${config.databaseType=='Memcache' }"> selected </c:if> >Memcache </option> 
				    -->
				</select>
				</td>
			</tr>
			 
			 <tr>
				<td align="right">默认数据库：</td>
				<td><input id="databaseNameConfig"  name="databaseNameConfig" type="text"   class="easyui-validatebox"   data-options="width: 150,required:'required'"/>
				  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="Oracle可填写SID：orcl, &#13;HANA可填写SYSTEM , &#13;ClickHouse可填写default, &#13;Impala可填写default, &#13;Informix填写格式： 数据库名@实例名, &#13;Informix默认实例名为demoServer, &#13;ElasticSearch填写es , &#13;达梦DM请填写schema模式名称 , &#13;Redis请直接填写Redis"></a>
				</td>
			 </tr>
			 
			 <tr>
				<td align="right">数据库IP地址：</td>
				<td><input id="ip" name="ip" type="text"  class="easyui-validatebox" data-options="width: 150,required:'required',validType:'length[3,80]'">
			      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="直接填写IP或域名地址， &#13;集群地址请用分号隔开"></a>
			    </td>
			 </tr>
			 <tr>
				<td align="right">数据库端口：</td>
				<td><input  id="port" name="port" type="text" value="${config.port }" class="easyui-numberbox"   data-options="width: 150,required:'required' "    />   
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title=" MySql 默认端口为:3306 &#13;MariaDB默认端口为:3306 &#13;Oracle默认端口为:1521 &#13;PostgreSQL默认端口:5432 &#13;SQL Server默认端口为:1433 &#13;Hive默认端口:10000 &#13; MongoDB默认端口:27017 &#13;SAP HANA默认端口:39013 &#13;Cache默认端口:1972 &#13;DB2默认端口:50000 &#13;ClickHouse默认端口:8123 &#13;Informix默认端口:9092 &#13;ElasticSearch默认端口:9200 &#13;达梦DM默认端口:5236 &#13;Redis默认端口:6379"></a>
				</td>
			 </tr>
			
			 <tr>
				<td align="right">数据库用户名：</td>
				<td><input id="userName"  name="userName" type="text" value="${config.userName }" class="easyui-validatebox"  data-options="width:150" /></td>
			 </tr>
			
			<tr>
				<td align="right">数据库密码：</td>
				<td><input id="password" name="password" type="password" value="${config.password }" class="easyui-validatebox"  data-options="width:150"  /></td>
			</tr>
			 
			 <tr>
				<td align="right">是否默认：</td>
				<td>
				<select id="isDefault" name="isDefault"  class="esayui-combobox"  style="width:150px" >
				   <option value="0" <c:if test="${config.isDefault =='0' }"> selected</c:if> >否</option>  
				   <option value="1" <c:if test="${config.isDefault =='1' }"> selected</c:if> >是</option>
				</select>
			</tr>
			 
			 <tr>
				<td align="right">仅显示默认库：</td>
				<td>
				<select id="isDefaultView" name="isDefaultView"  class="esayui-combobox"  style="width:150px" >
				   <option value="1" <c:if test="${config.isDefaultView =='1' }"> selected</c:if> >是</option>
				   <option value="0" <c:if test="${config.isDefaultView =='0' }"> selected</c:if> >否</option>  
				</select>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="MySQL、SQL Server、PostgreSQL实例中可能有多个库, &#13; 仅显示默认库时将只展示默认的库，加快加载速度。"></a>
			</tr>
			
			<tr>
				<td align="right">只读限制：</td>
				<td>
				<select id="isRead" name="isRead"  class="esayui-combobox"  style="width:150px" >
				   <option value="0" <c:if test="${config.isRead =='0' }"> selected</c:if> >否</option>  
				   <option value="1" <c:if test="${config.isRead =='1' }"> selected</c:if> >是</option>
				</select>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="只读限制后，将只允许执行查询、导出操作。"></a>
			</tr>	 
			
			 <tr>
				<td align="right">备注信息：</td>
				<td><input id="memo" name="memo" type="text" value="${config.memo }" class="easyui-validatebox" data-options="width: 150"/></td>
			 </tr>
			 
			 <tr>
				<td> </td>
				<td> <span id="mess2">  </span> </td>
			</tr>
			 
		</table>
		
		 
	</form>
</div>

<script type="text/javascript">

var connSuccess = false;
 
function configUpdate3(){  
	  var  name = $('#name').val();
      var  databaseType = $('#databaseType option:selected').val();
      var  databaseNameConfig = $('#databaseNameConfig').val();
      var  ip = $('#ip').val();
      var  port = $('#port').val();
      var  userName = $('#userName').val();
      var  password = $('#password').val();
      var  isDefault = $('#isDefault').val();
      var  isDefaultView = $('#isDefaultView').val();
 
      var  memo = $('#memo').val();
      var  isRead = $('#isRead').val();
      var  exportLimit = $('#exportLimit').val();
      var  sort = $('#sort').val();
      var  status = $('#status').val();
      var  isValid = $("#mainform").form('validate');
      
      if( !isValid ){
    	   return isValid;	// 返回false终止表单提交
      }
      
	  $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/config/configUpdate" ,
                    data: JSON.stringify( {'name':name ,'databaseType':databaseType ,'databaseName':databaseNameConfig ,
                    	'ip':ip ,'port':port ,'userName':userName ,'password':password,'isDefault':isDefault ,
                    	'isDefaultView': isDefaultView , 'memo': memo, 'exportLimit': exportLimit,'isRead': isRead ,'sort': sort ,'status': status } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	            parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	            setTimeout(function () {
                               firstTimeLoginConfigDialog.panel('close');                             
                             }, 1000);
                           $('#isFirstTimeLogin').val('false');
            	           parent.init3();
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight", timeout:2000,icon:"warning" });
            	       }
            	     }  
       });
 }
 

//测试连接
 function  testConn(){ 
      if($("#databaseNameConfig").val() == ''){
         return;
      }
      if($("#ip").val() == ''){
         return;
      }
      if($("#port").val() == ''){
         return;
      }
	  $("#mess2").html("<span class='tree-loading'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>连接测试中..." );
	  $.ajax({
			type:'POST',
		   	contentType:'application/json;charset=utf-8',
            url:"${ctx}/system/permission/i/testConn",
            data: JSON.stringify({ 'databaseType':$("#databaseType").val(),'databaseName':$("#databaseNameConfig").val(),'userName':$("#userName").val(),'password':$("#password").val(),'port':$("#port").val(),'ip':$("#ip").val() } ),
            datatype: "json", 
            //成功返回之后调用的函数             
            success:function(data){
            	var status = data.status ;
            	if(status == 'success' ){
            		
            		$("#mess2").html(data.mess );
            		connSuccess = true;
            		//设置连接状态为有效 1
            		$('#status').val("1");
            	}else{
            		connSuccess= false;
            		$("#mess2").html( data.mess );
            	}
            }  
     });
 }

</script>
</body>
</html>