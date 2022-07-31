<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>注册页面</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
		<table class="formTable">
			 <tr>
				<td>机器码：</td>
				<td><input  id="personNumber" name="personNumber"  value="${personNumber }" type="text"  class="easyui-validatebox" data-options="width: 250,required:'required'" readonly  /></td>
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
				  <textarea id="token" name="token" type="text"  style="width:245px;height:50px" data-options="width: 245,required:'required' "  >${token}</textarea> 
				</td>
			</tr>
		</table>
		 <c:if test="${mess=='registered'}">
		    <span style="color:#5dc82e;font-size:14px;">&nbsp;&nbsp;&nbsp;已注册</span>
		 </c:if>
		  <c:if test="${mess=='unregistered'||mess==''}">
		    <span style="color:#c00;font-size:14px;">&nbsp;&nbsp;&nbsp;未注册</span>
		 </c:if>
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
            	            parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	            setTimeout(function () {
                            $("#dlgg").panel('close');
                            }, 2000);
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	       }
            	     }  
       });
   }
 
</script>
</body>
</html>