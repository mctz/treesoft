<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>注册页面</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

<style type="text/css">
  .login_top{width: 100%;height: 150px;background: #0060A9 url("../images/mosaic-pattern.png") repeat; display: block;float:left;}
  .login_title{
	width: 500px;height: 50px;display:block;margin: auto;
	text-align: center;
	color:#fff;
	margin-top: 100px;line-height: 50px;font-size: 24px;
	
	}
   .login_main{
	background: url('../images/login_bg.png');
	width:423px;height:366px;display: block;margin:auto;
	}
  
</style>

</head>
<body>
<div>

        <div class="login_top">
			<div class="login_title" >
				<span style="margin-left: 10px; margin-top: 10px;color: #fff"> <img src="${ctx}/static/images/logo.png" height="32px" />TreeSoft数据库管理系统 &nbsp; &nbsp;使用注册 </span>
			</div>
		</div>

      <div class="login_main">
		<table class="formTable">
		    <tr>
				<td> &nbsp; </td>
				<td> &nbsp; </td>
			</tr>
			<tr>
				<td> &nbsp; </td>
				<td> &nbsp; </td>
			</tr>
			 <tr>
				<td>机器码：</td>
				<td><input  id="personNumber" name="personNumber"  value="${personNumber }" type="text"  class="easyui-validatebox" data-options="width: 250,required:'required'" readonly="readonly"  /></td>
			</tr>
			<!--  
			 <tr>
				<td>用户名：</td>
				<td><input id="company" name="company" value="${company }" type="text"  class="easyui-validatebox" data-options="width: 250,required:'required'"  /></td>
			</tr>
			-->
			<tr>
				<td>注册码：</td>
				<td>
				  <textarea id="token" name="token"  style="width:245px;height:50px" data-options="width: 245,required:'required' "  >${token}</textarea> 
				</td>
			</tr>
			 
			<tr>				 
				<td colspan="2"><span style="color:red;" id="message"> </span> &nbsp; </td>
			</tr>
			
			<tr>				 
				<td colspan="2" align="center"> <input type="button" value=" 注 册 " onclick="registerUpdate()" /> </td>
			</tr>
		</table>
	 </div>  
	 
	 
</div>

<script type="text/javascript">
 
function registerUpdate(){  
	  var  personNumber = $('#personNumber').val();
      var  company = "treesoft";
      var  token = $('#token').val();
	  $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/registerUpdate" ,
                    data: JSON.stringify( { 'personNumber':personNumber  ,'company':company ,'token':token  } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	           // parent.$.messager.show({ title : "提示",msg: data.mess , position: "center" });
            	            document.getElementById("message").innerHTML= data.mess ;
            	            setTimeout(function () {
                              window.history.back();
                            }, 2500);
            	       }else{
            	    	   // parent.$.messager.show({ title : "提示",msg: data.mess , position: "center" });
            	    	    document.getElementById("message").innerHTML= data.mess ;
            	       }
            	     }  
       });
   }
 
</script>
</body>
</html>