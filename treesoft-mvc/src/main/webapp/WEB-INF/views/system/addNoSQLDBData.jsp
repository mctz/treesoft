<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>addDialog</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx }/redis/saveNotSqlData/${NoSQLDbName}/${databaseConfigId}" method="post">
		<table class="formTable">
			<tr>
				<td>key： </td>
				<td>
				<input id="key"  name="key" type="text"   value="<c:out value="${notSqlEntity.key}"/>"    class="easyui-validatebox"   data-options="width: 450,required:'required'"/>
				</td>
			</tr>
			<tr>
				<td>过期时间： </td>
				<td>
				<input id="exTime"  name="exTime" type="text" value="${notSqlEntity.exTime }"  class="easyui-numberbox" min="-1"   max="2592000"  precision="0"   data-options="width:100" />&nbsp;秒&nbsp; (为空或 -1秒永不过期 )
				</td>
			</tr>	
		    <tr>
				<td>数据类型： </td>
				<td>
				<c:if test="${notSqlEntity.key != null }">
				  <input   name="type" type="text" value="${notSqlEntity.type }" style="width:445px"    readonly />
				</c:if>
				<c:if test="${notSqlEntity.key == null }">
				<select id="type" name="type"   class="esayui-combobox" style="width:450px;"   onchange="changeType()"  >
				   <option value="string" <c:if test="${config.type=='string' }"> selected </c:if> >string </option>  
				   <option value="set" <c:if test="${notSqlEntity.type == 'set' }">selected</c:if>  >set</option>
				   <option value="list" <c:if test="${notSqlEntity.type =='list' }"> selected </c:if> >list </option>  
				   <option value="zset" <c:if test="${notSqlEntity.type == 'zset' }">selected</c:if>  >zset</option>
				   <option value="hash" <c:if test="${notSqlEntity.type == 'hash' }">selected</c:if>  >hash</option>
				   <option value="HashSet" <c:if test="${notSqlEntity.type == 'HashSet' }">selected</c:if>  >HashSet</option>
				   <option value="ArrayList" <c:if test="${notSqlEntity.type == 'ArrayList' }">selected</c:if> >ArrayList</option>
				   
				</select>
				</c:if>
				</td>
			</tr>
			 <tr >
				<td> &nbsp; </td>
				<td> &nbsp; </td>
			</tr>		 
		    <tr >
				<td>value：</td>
				<td id="keyValueTd">
				 <textarea name="value" <c:if test="${notSqlEntity.type == 'string' || notSqlEntity.type == 'String' || notSqlEntity.key == null }">  style="display:block;font-size:12px;width:445px;height:170px;" </c:if><c:if test="${notSqlEntity.type != 'string' }">  style="display:none" </c:if>><c:out value="${notSqlEntity.value}"/></textarea>
				
				<table   <c:if test="${ notSqlEntity.type == 'list' || notSqlEntity.type == 'set'|| notSqlEntity.type == 'ArrayList' || notSqlEntity.type == 'HashSet'}"> style="display:block" </c:if> id="reslutTable1"> 
				 <c:forEach var="entity" items="${notSqlEntity.list}" >
				   <tr> <td>   <input type="text" name="valuek"  value="<c:out value="${entity}"/>"   style="width:400px"> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png" style="vertical-align:middle;" onclick="addrow( this )" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" style="vertical-align:middle;" onclick="delrow( this )"  />  </td>   </tr> 
				 </c:forEach>
				 </table>
				<table  <c:if test="${notSqlEntity.type == 'hash' || notSqlEntity.type == 'HashMap' || notSqlEntity.type == 'zset'  }"> style="display:block" </c:if>  id="reslutTable2"> 
				  <c:forEach var="entity" items="${notSqlEntity.listMap}" >
				   <tr> <td>   <input type="text" name="valuek"  value="<c:out value="${entity.valuek}"/>"    style="width:190px"> <input type="text" name="valuev" value="<c:out value="${entity.valuev}"/>"    style="width:200px"> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png"  onclick="addrow2( this )" style="vertical-align:middle;" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" onclick="delrow( this )" style="vertical-align:middle;" />  </td>   </tr> 
				  </c:forEach>
			    </table>
			  </td>
			</tr>			 
		</table>
		 
	</form>
</div>

<script type="text/javascript"> 
 
// alert( databaseConfigId );
// alert( NoSQLDbName );

//提交表单
$('#mainform').form({    
    onSubmit: function(){
	    var type = $("#type").val();
	    if(type == "string"){
	    	if( $(".value").val() == "" ){
	    		parent.$.messager.show({ title : "提示",msg: "请填写对应的value值！", position: "bottomRight" });
	    		return false;
	    	}
	    }
	    
	    if(type == "list"){
	    	 
	    }
	
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){  
    	var obj =  eval('(' + data + ')'); 
    	var status = obj.status;
        if(status == 'success' ){
            parent.$.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight" });
    	    setTimeout(function () {
                addDialog.panel('close');
                $("#dg2").datagrid('reload');
                parent.dg.treegrid('reload');
            }, 1000);
        }else{
             $.messager.show({ title : "提示",msg: obj.mess, position: "bottomRight",timeout:2000,icon:"warning" });
        }
    }    
});   

function addrow( obj ){
	var str='<tr><td><input type="text" name="valuek"  value="${entity}" style="width:400px"> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png" style="vertical-align:middle;" onclick="addrow( this )" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" style="vertical-align:middle;" onclick="delrow( this )"  />  </td>   </tr> ';
	 $(obj).parent().parent().parent().append( str );
}

function addrow2( obj ){
	var str='<tr><td><input type="text" name="valuek"  value="${entity}"  style="width:190px"> <input type="text" name="valuev"  value="${entity}" style="width:200px"> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png"  onclick="addrow2( this )" style="vertical-align:middle;" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" onclick="delrow( this )" style="vertical-align:middle;" /> </td> </tr> ';
	 $(obj).parent().parent().parent().append( str );
}

function delrow( obj ){
	$(obj).parent().remove();
}

function changeType(){
	var tem1='<textarea  name="value" style="display:block;font-size:12px;width:445px;height:170px;" >  </textarea> ';
	var tem2='<table><tr><td><input type="text" name="valuek"    style="width:400px"> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png" style="vertical-align:middle;" onclick="addrow( this )" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" style="vertical-align:middle;" onclick="delrow( this )"  />  </td>   </tr> </table>';
	var tem3='<table><tr><td><input type="text" name="valuek"  style="width:190px"> <input type="text" name="valuev"  value="${entity}" style="width:200px"> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-add.png"  onclick="addrow2( this )" style="vertical-align:middle;" /> <img src="${ctx }/static/plugins/easyui/icons/icon-standard/16x16/table-delete.png" onclick="delrow( this )" style="vertical-align:middle;" /> </td> </tr> </table>';
	
	var type = $("#type").val();
	
	if(type == "string"){
		$("#keyValueTd").html( tem1 );
	}
	
	if(type == "list"){
		$("#keyValueTd").html( tem2 );
	}
	if(type == "ArrayList"){
		$("#keyValueTd").html( tem2 );
	}
	if(type == "set"){
		$("#keyValueTd").html( tem2 );
	}
	if(type == "HashSet"){
		$("#keyValueTd").html( tem2 );
	}
	
	if(type == "zset"){
		$("#keyValueTd").html( tem3 );
	}
	
	if(type == "hash"){
		$("#keyValueTd").html( tem3 );
	}	
}

</script>
</body>
</html>