<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据同步、状态监控预警配置</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
    <div>
	<form id="monitorConfigForm" action="${ctx }/monitorConfig/monitorConfigSave" method="post">
	  <input id="id" name="id" type="hidden" value="${monitorConfig.id }"   />
	  
      <div id="monitorConfigTabs" class="easyui-tabs" style="width:385px;height:285px;" data-options="fit:true, border:false, showOption:false"   >
       <div  data-options="title:'发送消息', iconCls: 'icon-hamburg-address'" style="padding:5px;"  >
		  <table class="formTable">
			 <tr>
				<td>&nbsp;消息标题：</td>
				<td>  <input id="title" name="title" type="text" value="${monitorConfig.title }" class="easyui-validatebox"  data-options="width:245,required:true,validType:'text'"  /> 	
		        </td>
			 </tr>	
			 <tr>
				<td>&nbsp;消息内容：</td>
				<td><textarea id="content" name="content" type="text" value="${monitorConfig.content }" style="width:238px;height:50px" data-options="required:true,validType:'text'"  >${monitorConfig.content}</textarea>  
		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="参数说明：&#13;告警时间：{time}&#13;告警类型：{type}&#13;告警级别：{level}&#13;告警详情：{message}"></a>
		        </td>
			 </tr>	
			 <tr>
				<td>&nbsp;状&nbsp;&nbsp;态：</td>
				<td>  <select id="state" name="state"  class="esayui-combobox"   style="width:245px;"  >
				   <option value="0" <c:if test="${monitorConfig.state=='0' }"> selected </c:if>  >启用</option>  
				   <option value="1" <c:if test="${monitorConfig.state=='1' }"> selected </c:if>  >停用</option> 
				</select>  
				</td>
			 </tr>	
			 <tr>
				<td> &nbsp; </td>
				<td> &nbsp; </td>
			 </tr>	
			  <tr>
				<td>&nbsp;预警阶段：</td>
				<td>  <input type="checkbox" id="phase" name="phase" value="0" <c:if test="${fn:contains(monitorConfig.phase,'0')}">checked="checked"</c:if> style="zoom:120%;">启动时</input> <br>
			          <input type="checkbox" id="phase" name="phase" value="1" <c:if test="${fn:contains(monitorConfig.phase,'1')}">checked="checked"</c:if> style="zoom:120%;">结束时 </input> <br>
			          <input type="checkbox" id="phase" name="phase" value="2" <c:if test="${fn:contains(monitorConfig.phase,'2')}">checked="checked"</c:if> style="zoom:120%;">成功时 </input> <br>
			          <input type="checkbox" id="phase" name="phase" value="3" <c:if test="${fn:contains(monitorConfig.phase,'3')}">checked="checked"</c:if> style="zoom:120%;">失败时 </input> <br>
			     </td>
			 </tr>	
		  </table>	  
       </div>

       <div data-options="title:'媒介配置', iconCls: 'icon-hamburg-settings'" style="padding:10px;" >
         <div style="height:85%" >
          <table>
            <tr>
				<td>预警方式：</td>
				<td> <select id="style" name="style"  class="esayui-combobox"   style="width:230px;"  >
				   <option value="0" <c:if test="${ monitorConfig.style==null || monitorConfig.style=='0' }"> selected </c:if>  >发送邮件</option>  
				   <option value="1" <c:if test="${monitorConfig.style=='1' }"> selected </c:if>  >运行脚本</option> 
				   <option value="2" <c:if test="${monitorConfig.style=='2' }"> selected </c:if>  >发送短信</option> 
				   <option value="3" <c:if test="${monitorConfig.style=='3' }"> selected </c:if>  >钉钉消息</option> 
				   <option value="4" <c:if test="${monitorConfig.style=='4' }"> selected </c:if>  >微信消息</option> 
				</select>  
				</td>	
			 </tr>    
			 
			<tbody id="emailConfig">       
            <tr name="emailConfig">
				<td>&nbsp;收 件 人：</td>
				<td ><input  id="receiveEmail" name="receiveEmail" type="text" value="${monitorConfig.receiveEmail }"   class="easyui-validatebox"  data-options="width:230,required:true,validType:'text'" /> 	
				</td>
		    </tr>
            <tr name="emailConfig">
				<td>邮箱账号：</td>
				<td><input id="sendEmail"  name="sendEmail" type="text" value="${monitorConfig.sendEmail }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'email'"/> </td>
		    </tr>
          
           <tr name="emailConfig">
				<td>&nbsp;&nbsp;密&nbsp;&nbsp;码：</td>
				<td>
				  <input id="sendPassword"  name="sendPassword" type="password" value="${monitorConfig.sendPassword }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="QQ、163邮箱请填写在邮件系统中申请的授权码"></a>
				</td>
		    </tr>
		    
		     <tr name="emailConfig">
				<td>SMTP地址：</td>
				<td><input id="smtpServer" name="smtpServer" type="text" value="${monitorConfig.smtpServer }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				</td>	
			 </tr>
		    
		     <tr>
				<td>SMTP端口：</td>
				<td><input id="smtpServerPort" name="smtpServerPort" type="text" value="${monitorConfig.smtpServerPort }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				</td>	
			 </tr>
          
             <tr>
				<td>&nbsp;开启SSL：</td>
				<td> <select id="smtpSsl" name="smtpSsl"  class="esayui-combobox"   style="width:230px;"  >
				   <option value="1" <c:if test="${monitorConfig.smtpSsl=='1' }"> selected </c:if>  >关闭</option>  
				   <option value="0" <c:if test="${monitorConfig.smtpSsl=='0' }"> selected </c:if>  >开启</option> 
				</select>  
				</td>	
			 </tr>
			 </tbody>
			 
			 <tbody  id="scriptMessage" > 
			 <tr >
				<td>执行脚本:</td>
				<td> <textarea id="script" name="script" type="text"   style="width:225px;height:100px" data-options="required:true,validType:'text'">${monitorConfig.script }</textarea>
			      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="支持多行命令 , &#13;支持运行批处理脚本命令  "></a>
			     </td>
			 </tr>
			 </tbody>
			 
			 <tbody id="shortMessage"> 
			 <tr>
				<td>&nbsp;厂 &nbsp; 商:</td>
				<td> <select id="producer" name="producer"  class="esayui-combobox"   style="width:230px;"  >
				   <option value="0" <c:if test="${monitorConfig.producer=='0' }"> selected </c:if>  >阿里</option>  
				   <option value="1" <c:if test="${monitorConfig.producer=='1' }"> selected </c:if>  >腾讯</option> 
				   <option value="2" <c:if test="${monitorConfig.producer=='2' }"> selected </c:if>  >电信</option> 
				   <option value="3" <c:if test="${monitorConfig.producer=='3' }"> selected </c:if>  >其他 </option> 
				</select>  
				 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="短信功能需按厂商定制化适配才能使用！"></a>
				</td>	
			 </tr>
			 <tr>
				<td>电话号码:</td>
				<td><input  id="telphone" name="telphone" type="text" value="${monitorConfig.telphone }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				</td>	
			 </tr>
			 <tr>
				<td>domain:</td>
				<td><input  id="domain"   name="domain" type="text" value="${monitorConfig.domain }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				</td>	
			 </tr>
			 <tr>
				<td>accessId:</td>
				<td><input  id="accessId" name="accessId" type="text" value="${monitorConfig.accessId }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				</td>	
			 </tr>
			 <tr>
				<td>accessKey:</td>
				<td><input  id="accessKey" name="accessKey" type="text" value="${monitorConfig.accessKey }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				</td>	
			 </tr>
			 </tbody> 
			 
			 <tbody id="dingding"> 
			  <tr>
				<td>hook地址:</td>
				<td><input  id="hookUrl"   name="hookUrl" type="text" value="${monitorConfig.hookUrl }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="钉钉机器人安全设置, &#13;请选择“自定义关键词”,&#13;并在消息内容中包含此关键词, &#13;例如：添加  “预警” 关键词"></a>
				</td>	
			 </tr>
			 
			  <tr>
				<td>接收人:</td>
				<td><input  id="at"   name="at" type="text" value="${monitorConfig.at }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="@全体人员时填写all, &#13;否则填写具体的手机号码,&#13;多人时用逗号分隔"></a>
				</td>	
			 </tr>
			 </tbody> 
			 
			 <tbody id="wechat"> 
			  <tr>
				<td>接收人:</td>
				<td><input  id="weixinAt"  name="weixinAt" type="text" value="${monitorConfig.weixinAt }"   class="easyui-validatebox"   data-options="width:230,required:true,validType:'text'"/>
				 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="@全体人员时填写all, &#13;否则填写具体的手机号码,&#13;多人时用逗号分隔"></a>
				</td>	
			 </tr>
			 <tr>
			   <td> </td> <td> 注：用户需先加入企业微信 </td>
			 </tr>
			 </tbody> 
			 
          </table>
           </div> 
           <div  align="center"   >
		    <a href="javascript:monitorConnTest()" class="l-btn l-btn-small" > <span class="l-btn-left l-btn-icon-left" ><span class="l-btn-text">测试一下</span><span class="l-btn-icon icon-ok">&nbsp;</span></span></a> 
		   </div>
       </div>
       
	 </div>  
	</form>
	</div>
</div>

<script type="text/javascript">
  
$(function(){
   var str = $("#style").val();
   controllTr(str);
	
   $("#style").bind("change",function(){
	 var str = $("#style").val();
	 controllTr(str);
   });
});

function controllTr( str ){
	if(str =='0'){
		$('#emailConfig').each(function(){ $(this).find('input').validatebox({ novalidate:false  }); $(this).show(); });
		$('#scriptMessage').each( function(){$(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#shortMessage').each( function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#dingding').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#wechat').each(function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
	 } 
	 
	 if(str =='1'){
		$('#scriptMessage').each(function(){ $(this).find('input').validatebox({ novalidate:false }); $(this).show(); });
		$('#emailConfig').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#shortMessage').each( function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#dingding').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#wechat').each(function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
	 } 
	 
	 if(str =='2'){
		$('#shortMessage').each(function(){ $(this).find('input').validatebox({ novalidate:false }); $(this).show(); });
		$('#emailConfig').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#scriptMessage').each( function(){$(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#dingding').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#wechat').each(function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
	 } 
	 
	 if(str =='3'){
		$('#dingding').each(function(){ $(this).find('input').validatebox({ novalidate:false }); $(this).show(); });
		$('#emailConfig').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#shortMessage').each( function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#scriptMessage').each( function(){$(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#wechat').each(function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
	 } 
	 
	 if(str =='4'){
	    $('#wechat').each(function(){ $(this).find('input').validatebox({ novalidate:false }); $(this).show(); });
		$('#dingding').each(function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#emailConfig').each( function(){  $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#shortMessage').each( function(){ $(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
		$('#scriptMessage').each( function(){$(this).find('input').validatebox({ novalidate:true }); $(this).hide(); });
	 } 
}


//提交表单保存
$('#monitorConfigForm').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
    	if(!isValid){
    		$.messager.show({ title : "提示",msg:"请输入必填项" , position: "bottomRight" });
    	}
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	var obj = eval('(' + data + ')');
    	$.messager.show({ title : "提示",msg: obj.mess , position: "bottomRight" });
    	setTimeout(function () {
            monitorConfig.panel('close');
            
        }, 2000);
    }    
});   

// 测试
 function  monitorConnTest(){
	var styleValue = $("#style").val();
	
	var urlValue;
	var param ;
	if(styleValue == 0 ){
		urlValue = "${ctx}/monitorConfig/mailConfigTest";
		parm = { 'title':$("#title").val(),'content':$("#content").val(),'receiveEmail':$("#receiveEmail").val(),'sendEmail':$("#sendEmail").val(),'sendPassword':$("#sendPassword").val(),'smtpServer':$("#smtpServer").val(),'smtpServerPort':$("#smtpServerPort").val(),'smtpSsl':$("#smtpSsl").val()} ;
	}
	
	if(styleValue == 1 ){
		urlValue = "${ctx}/monitorConfig/shellConfigTest";
		parm = { 'title':$("#title").val(),'content':$("#content").val(),'script':$("#script").val() };
	}
	if(styleValue == 2 ){
		urlValue = "${ctx}/monitorConfig/smsConfigTest";
		parm = {'title':$("#title").val(),'content':$("#content").val(),'producer':$("#producer").val(),'telphone':$("#telphone").val(),'domain':$("#domain").val(),'accessId':$("#accessId").val(),'accessKey':$("#accessKey").val() };
	}
	if(styleValue == 3 ){
		urlValue = "${ctx}/monitorConfig/dingdingConfigTest";
		parm = {'title':$("#title").val(),'content':$("#content").val(),'hookUrl':$("#hookUrl").val(),'at':$("#at").val()};
	}
	
	if(styleValue == 4 ){
		urlValue = "${ctx}/monitorConfig/wechatConfigTest";
		parm = {'title':$("#title").val(),'content':$("#content").val(),'at':$("#weixinAt").val()};
	}
	
	//  parent.$.messager.show({ title : "提示",msg: "邮件测试中...！" , position: "bottomRight" });
	  $.easyui.loading({ msg: "测试中... " });
	  $.ajax({
			type:'POST',
		   	contentType:'application/json;charset=utf-8',
            url:urlValue,
            data: JSON.stringify( parm ),
            datatype: "json", 
            //成功返回之后调用的函数             
            success:function(data){
            	var status = data.status ;
            	$.easyui.loaded();
            	if(status == 'success' ){
            		 $.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	}else{
            		 $.messager.show({ title : "提示",msg: data.mess , position: "center" });
            	}
            }  
     });
 }

</script>
</body>
</html>