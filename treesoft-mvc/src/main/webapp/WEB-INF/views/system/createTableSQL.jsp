<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
		<table class="formTable">
			 <tr>
			  <td>建<br>表<br>语<br>句</td>
			  <td ><textarea id="doSql" name="doSql" type="text"   style="width:360px;height:220px"    >${createTableSQLItem }</textarea>   </td>
			 </tr>
		</table>
		 	 
</div>
 
</body>
</html>