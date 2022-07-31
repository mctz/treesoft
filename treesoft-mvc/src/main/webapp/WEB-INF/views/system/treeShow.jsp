<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>json format</title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
	<link href="${ctx}/static/css/s.css" type="text/css" rel="stylesheet"></link>

</head>
<body>	

 <div id="tb2" class="easyui-panel" style="padding:5px;height:38px">
                  <div>
                         
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-insert" plain="true"    onclick="SelectAllClicked()"> 全选 </a>
	       		        <span class="toolbar-item dialog-tool-separator"></span>
						<span>	
						 
						 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true"  onclick="ExpandAllClicked()">展开</a>
	                     <span class="toolbar-item dialog-tool-separator"></span>
	                    
	                     <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-add" plain="true"  onclick="CollapseAllClicked()">叠起</a>
	                     <span class="toolbar-item dialog-tool-separator"></span>
	                          
						 
						<span class="toolbar-item dialog-tool-separator"></span>
					 
						<span id="CollapsibleViewDetail"> <a
							<a href="javascript:void(0);" onclick="CollapseLevel(3)">2级</a> 
							<a href="javascript:void(0);" onclick="CollapseLevel(4)">3级</a> 
							<a href="javascript:void(0);" onclick="CollapseLevel(5)">4级</a> 
							<a href="javascript:void(0);" onclick="CollapseLevel(6)">5级</a> 
							<a href="javascript:void(0);" onclick="CollapseLevel(7)">6级</a> 
							<a href="javascript:void(0);" onclick="CollapseLevel(8)">7级</a> 
							<a href="javascript:void(0);" onclick="CollapseLevel(9)">8级</a> 
						</span>
	 </div>
</div>		 
 	<div id="pagep" class="easyui-pagination" style="border-top:1px solid #ccc;height:20px;"   >  </div> 
 
 <div style="width: 100%; height:400px;"   >
     <div  class="easyui-panel HeadersRow"  data-options="fit:true,border:false"   >
         <div id="Canvas" class="Canvas"></div>
     </div>
 </div>
  
 	
  
		
 <input type="hidden" id="databaseConfigId" value="${databaseConfigId}" >
 <input type="hidden" id="databaseName"    value="${databaseName}" >
 <input type="hidden" id="tableName"       value="${tableName}">
 
<script type="text/javascript">
var databaseName;
var tableName;
var databaseConfigId;
window.SINGLE_TAB = "  ";
window.ImgCollapsed ="${ctx}/static/images/Collapsed.gif";
window.ImgExpanded = "${ctx}/static/images/Expanded.gif";
window.QuoteKeys = true;


$(function(){ 
	databaseConfigId = $("#databaseConfigId").val();
	databaseName = $("#databaseName").val();
	tableName = $("#tableName").val();		
	init();
});


// 
function init(){
	  $.ajax({
			    type:'POST',
		       	contentType:'application/json;charset=utf-8',
                url:"${ctx}/system/permission/i/treeShowData/" +tableName+'/'+databaseName +'/'+databaseConfigId ,
                //data: JSON.stringify( {'databaseName':databaseName, 'tableName':tableName, 'pageSize':'20','pageList':'20','pageNumber':'2' } ),
                data: JSON.stringify({'pageNo':1, 'pageSize':30}) ,
                datatype: "json", 
               //成功返回之后调用的函数             
                success:function(data){
                   var status = data.status ;
                  //  alert( data.rows  );
            	   if(status == 'success' ){
            		   $("#Canvas").html(  JSON.stringify( data.rows )  );
            	       parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       Process();
            	       
            	       $('#pagep').pagination({
                           total:data.total,    //总记录数,也就是数据库总条数
                           pageSize:30 ,     //每页显示条数,就是每页显示多少条
                           onSelectPage: function(pageNumber, pageSize){
                               init2( pageNumber, pageSize );
                           }
                         });
            	   }else{
            	      parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	  }
            }  
       });
   }
	 
function init2( pageNumber, pageSize  ){
	//alert('sss222');
	  $.ajax({
			    type:'POST',
		       	contentType:'application/json;charset=utf-8',
               // url:"${ctx}/system/permission/i/treeShowData/" +tableName+'/'+databaseName +'/'+databaseConfigId +'?pageNo='+pageNumber +'&pageSize='+pageSize,
                url:"${ctx}/system/permission/i/treeShowData/" +tableName+'/'+databaseName +'/'+databaseConfigId ,
                data: JSON.stringify({'pageNo':pageNumber, 'pageSize':pageSize}) ,
                datatype: "json", 
               //成功返回之后调用的函数             
                success:function(data){
                   var status = data.status ;
                  //  alert( data.rows  );
            	   if(status == 'success' ){
            		   $("#Canvas").html(  JSON.stringify( data.rows )  );
            	       //parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       Process();
            	      
            	   }else{
            	      parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	  }
            }  
       });
   }

function $id(id) {
	return document.getElementById(id);
}
function IsArray(obj) {
	return obj && typeof obj === 'object' && typeof obj.length === 'number'
			&& !(obj.propertyIsEnumerable('length'));
}

function Process() {

	SetTab();
	window.IsCollapsible = true;
	 

	var json = $id("Canvas").innerHTML ;
	var html = "";
	try {
		if (json == " ") {
			json = "\"\"";
		}
		var obj = eval("[" + json + "]");
		html = ProcessObject(obj[0], 0, false, false, false);
		$id("Canvas").innerHTML = "<PRE class='CodeContainer'>" + html
				+ "</PRE>";
	} catch (e) {
		alert("JSON数据格式不正确:\n" + e.message);
		$id("Canvas").innerHTML = "";
	}
}

  window._dateObj = new Date();
  window._regexpObj = new RegExp();
  
  function ProcessObject(obj, indent, addComma, isArray, isPropertyContent) {
	var html = "";
	var comma = (addComma) ? "<span class='Comma'>,</span> " : "";
	var type = typeof obj;
	var clpsHtml = "";
	if (IsArray(obj)) {
		if (obj.length == 0) {
			html += GetRow(indent, "<span class='ArrayBrace'>[ ]</span>"
					+ comma, isPropertyContent);
		} else {
			clpsHtml = window.IsCollapsible ? "<span><img src=\""
					+ window.ImgExpanded
					+ "\" onClick=\"ExpImgClicked(this)\" /></span><span class='collapsible'>"
					: "";
			html += GetRow(indent, "<span class='ArrayBrace'>[</span>"
					+ clpsHtml, isPropertyContent);
			for ( var i = 0; i < obj.length; i++) {
				html += ProcessObject(obj[i], indent + 1, i < (obj.length - 1),
						true, false);
			}
			clpsHtml = window.IsCollapsible ? "</span>" : "";
			html += GetRow(indent, clpsHtml
					+ "<span class='ArrayBrace'>]</span>" + comma);
		}
	} else if (type == 'object') {
		if (obj == null) {
			html += FormatLiteral("null", "", comma, indent, isArray, "Null");
		} else if (obj.constructor == window._dateObj.constructor) {
			html += FormatLiteral("new Date(" + obj.getTime() + ") /*"
					+ obj.toLocaleString() + "*/", "", comma, indent, isArray,
					"Date");
		} else if (obj.constructor == window._regexpObj.constructor) {
			html += FormatLiteral("new RegExp(" + obj + ")", "", comma, indent,
					isArray, "RegExp");
		} else {
			var numProps = 0;
			for ( var prop in obj)
				numProps++;
			if (numProps == 0) {
				html += GetRow(indent, "<span class='ObjectBrace'>{ }</span>"
						+ comma, isPropertyContent);
			} else {
				clpsHtml = window.IsCollapsible ? "<span><img src=\""
						+ window.ImgExpanded
						+ "\" onClick=\"ExpImgClicked(this)\" /></span><span class='collapsible'>"
						: "";
				html += GetRow(indent, "<span class='ObjectBrace'>{</span>"
						+ clpsHtml, isPropertyContent);

				var j = 0;

				for ( var prop in obj) {

					var quote = window.QuoteKeys ? "\"" : "";

					html += GetRow(indent + 1, "<span class='PropertyName'>"
							+ quote
							+ prop
							+ quote
							+ "</span>: "
							+ ProcessObject(obj[prop], indent + 1,
									++j < numProps, false, true));

				}

				clpsHtml = window.IsCollapsible ? "</span>" : "";

				html += GetRow(indent, clpsHtml
						+ "<span class='ObjectBrace'>}</span>" + comma);

			}

		}

	} else if (type == 'number') {

		html += FormatLiteral(obj, "", comma, indent, isArray, "Number");

	} else if (type == 'boolean') {

		html += FormatLiteral(obj, "", comma, indent, isArray, "Boolean");

	} else if (type == 'function') {

		if (obj.constructor == window._regexpObj.constructor) {

			html += FormatLiteral("new RegExp(" + obj + ")", "", comma, indent,
					isArray, "RegExp");

		} else {

			obj = FormatFunction(indent, obj);

			html += FormatLiteral(obj, "", comma, indent, isArray, "Function");

		}

	} else if (type == 'undefined') {

		html += FormatLiteral("undefined", "", comma, indent, isArray, "Null");

	} else {

		html += FormatLiteral(obj.toString().split("\\").join("\\\\")
				.split('"').join('\\"'), "\"", comma, indent, isArray, "String");

	}

	return html;

}

function FormatLiteral(literal, quote, comma, indent, isArray, style) {

	if (typeof literal == 'string')

		literal = literal.split("<").join("&lt;").split(">").join("&gt;");

	var str = "<span class='" + style + "'>" + quote + literal + quote + comma
			+ "</span>";

	if (isArray)
		str = GetRow(indent, str);

	return str;

}

function FormatFunction(indent, obj) {

	var tabs = "";

	for ( var i = 0; i < indent; i++)
		tabs += window.TAB;

	var funcStrArray = obj.toString().split("\n");

	var str = "";

	for ( var i = 0; i < funcStrArray.length; i++) {

		str += ((i == 0) ? "" : tabs) + funcStrArray[i] + "\n";

	}

	return str;

}

function GetRow(indent, data, isPropertyContent) {

	var tabs = "";

	for ( var i = 0; i < indent && !isPropertyContent; i++)
		tabs += window.TAB;

	if (data != null && data.length > 0 && data.charAt(data.length - 1) != "\n")

		data = data + "\n";

	return tabs + data;

}

function CollapsibleViewClicked() {

	$id("CollapsibleViewDetail").style.visibility = $id("CollapsibleView").checked ? "visible"
			: "hidden";

	Process();

}

function QuoteKeysClicked() {
	window.QuoteKeys = $id("QuoteKeys").checked;
	Process();
}

function CollapseAllClicked() {

	EnsureIsPopulated();

	TraverseChildren($id("Canvas"), function(element) {

		if (element.className == 'collapsible') {

			MakeContentVisible(element, false);

		}

	}, 0);

}

function ExpandAllClicked() {

	EnsureIsPopulated();

	TraverseChildren($id("Canvas"), function(element) {

		if (element.className == 'collapsible') {

			MakeContentVisible(element, true);

		}

	}, 0);

}

function MakeContentVisible(element, visible) {

	var img = element.previousSibling.firstChild;

	if (!!img.tagName && img.tagName.toLowerCase() == "img") {

		element.style.display = visible ? 'inline' : 'none';

		element.previousSibling.firstChild.src = visible ? window.ImgExpanded
				: window.ImgCollapsed;

	}

}

function TraverseChildren(element, func, depth) {

	for ( var i = 0; i < element.childNodes.length; i++) {

		TraverseChildren(element.childNodes[i], func, depth + 1);

	}

	func(element, depth);

}

function ExpImgClicked(img) {

	var container = img.parentNode.nextSibling;

	if (!container)
		return;

	var disp = "none";

	var src = window.ImgCollapsed;

	if (container.style.display == "none") {

		disp = "inline";

		src = window.ImgExpanded;

	}

	container.style.display = disp;

	img.src = src;

}

function CollapseLevel(level) {

	EnsureIsPopulated();

	TraverseChildren($id("Canvas"), function(element, depth) {

		if (element.className == 'collapsible') {

			if (depth >= level) {

				MakeContentVisible(element, false);

			} else {

				MakeContentVisible(element, true);

			}

		}

	}, 0);

}

function TabSizeChanged() {
	Process();
}

function SetTab() {
	window.TAB = MultiplyString( 2 ,window.SINGLE_TAB);
	 
}

function EnsureIsPopulated() {

	if (!$id("Canvas").innerHTML && !!$id("RawJson").value)
		Process();

}

function MultiplyString(num, str) {

	var sb = [];

	for ( var i = 0; i < num; i++) {

		sb.push(str);

	}

	return sb.join("");

}

function SelectAllClicked() {
	if (!!document.selection && !!document.selection.empty) {
		document.selection.empty();

	} else if (window.getSelection) {

		var sel = window.getSelection();

		if (sel.removeAllRanges) {

			window.getSelection().removeAllRanges();

		}
	}

	var range =

	(!!document.body && !!document.body.createTextRange)

	? document.body.createTextRange()

	: document.createRange();

	if (!!range.selectNode)

		range.selectNode($id("Canvas"));

	else if (range.moveToElementText)

		range.moveToElementText($id("Canvas"));

	if (!!range.select)

		range.select($id("Canvas"));

	else

		window.getSelection().addRange(range);

}

function LinkToJson() {

	var val = $id("RawJson").value;

	val = escape(val.split('/n').join(' ').split('/r').join(' '));

	$id("InvisibleLinkUrl").value = val;

	$id("InvisibleLink").submit();

}

function clearmess() {
	$("#RawJson").val("");
}

function clearmess2() {
	$id("Canvas").innerHTML = "";
}

Process();
</script>

	</body>
</html>