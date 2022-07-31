<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>导入文件</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	 <form id="ImportForm" runat="server"enctype="multipart/form-data" >
	   <div style="margin-top:15px;margin-bottom: 10px">
				 &nbsp;&nbsp;&nbsp;&nbsp;选择文件： 
				 <input type="file" name="fileToUpload" id="fileToUpload" multiple="multiple" onchange="fileSelected();" />
		  </div>
		 
          <div id="progressNumber" class="easyui-progressbar" style="width:340px;margin-left:10px;">     </div>
		  
		<div  style="width:350px;display: block"> 
		<fieldset class="fieldsetClass"> 
		    <legend>文件类型</legend> 
			<table>
		    
			<tr>
				<td width="170px" valign="baseline"> 
				    <input  id="fileType" name="fileType" type="radio" value="txt" onchange="fileTypeChange(this)" style="zoom:150%;margin-bottom:3px;"  checked="checked" /> 文本文件(*.txt)  
				    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="内容中需包含记录分隔符."></a>
				</td>
				<td width="170px" valign="baseline"> 
				   <input  id="fileType" name="fileType" type="radio" value="csv" onchange="fileTypeChange(this)" style="zoom:150%;margin-bottom:3px;" />csv文件(*.csv)
			    </td>
		    </tr>
		    
		    <tr>
		        <td  valign="baseline"> 
		           <input  id="fileType" name="fileType" type="radio" value="xls" onchange="fileTypeChange(this)" style="zoom:150%;margin-bottom:3px;" />Excel文件(*.xls) 
		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="excel文件默认将第一行识别为字段名. &#13; 仅支持office 2003版本xls格式."></a>
		        </td>
				
				<td   valign="baseline"> <input  id="fileType" name="fileType" type="radio" value="shp" onchange="fileTypeChange(this)" style="zoom:150%;margin-bottom:3px;" />二进制文件(*.bin) </td>
		    </tr>
			<tr>
				<td  valign="baseline" > 
				<input id="fileType" name="fileType" type="radio" value="sql" onchange="fileTypeChange(this)" style="zoom:150%;margin-bottom:3px;" /> SQL文件(*.sql) 
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="内容需为标准的insert语法格式. &#13; 每行都需以insert开头并包含表名."></a>
				</td>
				<td  valign="baseline" > <input id="fileType" name="fileType" type="radio" value="other" onchange="fileTypeChange(this)" style="zoom:150%;margin-bottom:3px;" />其他文件(*.*) </td>
		   
		    </tr>			
		 </table>
		 </fieldset>
		</div>
		 <div id="spaceTypeDiv" style="width:350px;display: block"> 
		 <fieldset class="fieldsetClass"> 
		    <legend>标题及分隔符</legend> 
			<table>
		    <tr>
		    
		        <td width="170px">
		          <input id="haveTitle" name="haveTitle" type="checkbox" style="zoom:130%;margin-bottom:3px;" />包含标题行
		        </td>
		        <td >  </td>
		    </tr>	    
		    <tr>    
		        <td width="170px">记录分隔符:
		         <select  id="spaceType" name="spaceType"  style="width:70px;" > 
		            <option value=",">逗号(,)</option> 
		            <option value=";">分号(;)</option> 
		            <option value=" ">空格 </option> 
		            <option value="	">定位键</option> 
		         </select>  
		        </td>
		         
			    <td width="180px">文本限定符:
				 <select  id="demarcate" name="demarcate"  style="width:80px;" > 
				   <option value='"'>双引号(") </option> 
				   <option value="'" >单引号(')</option> 
				   <option value="" >无</option> 
				 </select>  
			    </td>
		    </tr>		
		    
		    	 
		 </table>
		 </fieldset>  
		 </div> 
		 
		 <div id="importTypeDiv" style="width:350px;display: block">
		 <fieldset class="fieldsetClass"> 
		    <legend>导入模式</legend> 
			<table>
		    <tr>
				<td width="150px"> <input  id="importType"  name="importType" type="radio" value="insert" onchange="importTypeChange(this)" style="zoom:150%;margin-bottom:4px;" checked="checked"/>新增记录</td>
				<td width="150px"> <input  id="importType"  name="importType" type="radio" value="update" onchange="importTypeChange(this)" style="zoom:150%;margin-bottom:4px;" />更新记录</td>
		    </tr>
		 </table>
		 </fieldset>  
		</div>   
		 <div id="updateRuleDiv" style="width:350px;display: none">
		 <fieldset class="fieldsetClass"> 
		    <legend>更新规则</legend> 
			<table>
		    <tr>
				<td width="350px"> <input  id="updateRule" name="updateRule" type="radio" value="updateById" style="zoom:150%;margin-bottom:3px;" checked="checked" />按第一列主键匹配更新</td>
		    </tr>
		    <tr>
			  <td width="350px"> <input   id="updateRule" name="updateRule" type="radio"  value="updateColumn" style="zoom:150%;margin-bottom:3px;"  />
				更新指定行&nbsp;<input id="title" name="title" type="text"    style="width:60px" />, &nbsp;&nbsp;指定列名&nbsp;<input id="title" name="title" type="text"  style="width:60px" /> 
			  </td>
		    </tr>
		 </table>
		 </fieldset>      
	    </div> 
	</form>
</div>

<script type="text/javascript">
   
 
  function fileTypeChange(obj){
	  if($(obj).val()=='csv' ){
		 $("#spaceTypeDiv").show();		  
	  }
	  if($(obj).val()=='txt' ){
		 $("#spaceTypeDiv").show();	
	  }
	  if($(obj).val()=='xls' ){
		 $("#spaceTypeDiv").hide();	
	  }
	  if($(obj).val()=='shp' ){
		 $("#spaceTypeDiv").hide();	
	  }
	  if($(obj).val()=='sql' ){
		 $("#spaceTypeDiv").hide();	
	  }
	  if($(obj).val()=='other' ){
		 $("#spaceTypeDiv").hide();	
	  }
  }
  
  function importTypeChange(obj){
	  if($(obj).val()=='insert' ){
		 $("#updateRuleDiv").hide();		  
	  }
	   if($(obj).val()=='update' ){
		 $("#updateRuleDiv").show();		  
	  }
  }
   

</script>
</body>
</html>