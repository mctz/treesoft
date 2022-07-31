<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>elasticSearch Query Form</title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
	<link href="${ctx}/static/css/s.css" type="text/css" rel="stylesheet"></link>
	<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/lib/codemirror.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/codemirror/addon/hint/show-hint.css" />
	<script type="text/javascript" src="${ctx}/static/codemirror/lib/codemirror.js"> </script>
	<script type="text/javascript" src="${ctx}/static/codemirror/mode/sql/sql.js"> </script>
	<style>
		.CodeMirror { border: 1px solid #cccccc;  height: 98%;  }
		.imageHead:hover{
			background: #fff;
			color: #fff;
		}
	</style>
</head>
<body>
<div id="eastLayout" class="easyui-layout" data-options="fit: true">
    <div id="operator1" class="panel-header panel-header-noborder" style="padding: 5px; height: auto">
			<div>							
				<select id="requestMethod" class="esayui-combobox"  style="width:80px;" >
				  <option value="GET" >GET </option>
				  <option value="PUT" >PUT </option>
				  <option value="POST" >POST </option>
				  <option value="DELETE" >DELETE </option>
			   </select>
				&nbsp;&nbsp;
				<input type="text" id="requestURL" class="easyui-validatebox" style="width:300px;border-top-width:0px;" />
				<input type="hidden" id="databaseConfigId"  value="${databaseConfigId}" />
				<input type="hidden" id="databaseName" value="${databaseName}" />
				<input type="hidden" id="RawJson" />
 
               <span class="toolbar-item dialog-tool-separator"></span>
			   			
			   	<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-run"  plain="true" onclick="esQuery()">运行 </a>
				<span class="toolbar-item dialog-tool-separator"></span>

				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" onclick="clearmess()">清空</a>
			   			       
			    <span class="toolbar-item dialog-tool-separator"></span>
			    <span id="CollapsibleViewHolder" style="font-size:8px;"  > <label for="CollapsibleView">
				 <input type="checkbox" id="CollapsibleView"   onclick="CollapsibleViewClicked()" checked="true" />
							  显示控制  
				   </label> </span>
			    <span id="CollapsibleViewDetail" style="font-size:8px;" >
			      <a href="javascript:void(0);" onclick="ExpandAllClicked()">展开</a>
			      <a href="javascript:void(0);" onclick="CollapseAllClicked()">叠起</a>
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


     <div class="easyui-layout" data-options="fit: true" style="width:100%">
       <div data-options="region:'west',split: true, border: false"  style="width:450px; height:100%">		
		  <div class="HeadersRow" style="margin-left:5px;margin-top:5px;margin-right:10px;width:100%; height:98%;">
			<textarea id="requestBody"  style="font-size:14px;font-family: '微软雅黑';width:97%;height:98%; " > </textarea>
		  </div>
	   </div>
 
       <div data-options="region:'center',split: true, border: false" style="width:48%; height:95%">	
	    	<div class="HeadersRow" style="margin-left:8px;margin-top:5px; width:98%;height:95%;background:#ececec">
		       <div id="Canvas" class="Canvas" ></div>
		    </div>
	   </div>  
     </div>
	
</div>

<script type="text/javascript">
    
    var requestBodyArea = CodeMirror.fromTextArea(document.getElementById('requestBody'), {
        mode: "text/x-mysql",
        theme: "eclipse", //主题
        lineNumbers: true,
        autofocus: false         
    });
    

    window.SINGLE_TAB = "  ";
    window.ImgCollapsed = "${ctx}/static/images/Collapsed.gif";
    window.ImgExpanded = "${ctx}/static/images/Expanded.gif";
    window.QuoteKeys = true;
    function $id(id) {
        return document.getElementById(id);
    }
    function IsArray(obj) {
        return obj && typeof obj === 'object' && typeof obj.length === 'number'
            && !(obj.propertyIsEnumerable('length'));
    }

    function Process(json ) {
        SetTab();
        window.IsCollapsible = $id("CollapsibleView").checked;
        var json = $id("RawJson").value;
        var html = "";
       
        if (json == " " || json == "") {           
            return;
        }

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
                html += FormatLiteral("new RegExp(" + obj + ")", "", comma, indent, isArray, "RegExp");
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
        var str = "<span class='" + style + "'>" + quote + literal + quote + comma   + "</span>";

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
        $id("CollapsibleViewDetail").style.visibility = $id("CollapsibleView").checked ? "visible" : "hidden";  Process();

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
        //缩进量
          window.TAB = MultiplyString( 2, window.SINGLE_TAB);
    }
    
    function EnsureIsPopulated(){
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

        var range = (!!document.body && !!document.body.createTextRange) ? document.body.createTextRange() : document.createRange();
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
        $("#requestBodyArea").val("");
        $id("Canvas").innerHTML = "";   
        requestBodyArea.setValue("");
    }

    //执行ES命令
    function esQuery() {
        var requestMethod = $("#requestMethod").val();
        var requestURL = $("#requestURL").val();
        var databaseConfigId = $("#databaseConfigId").val();
        var databaseName = $("#databaseName").val();
        var requestBody = requestBodyArea.getValue();
       
        $.ajax({
            type:'POST',
            contentType:'application/json;charset=utf-8',
            url:baseUrl+"/newQuery/queryElasticSearch" ,
            data: JSON.stringify( { 'databaseName':databaseName, 'databaseConfigId':databaseConfigId,'requestMethod':requestMethod ,'requestURL':requestURL,'requestBody': requestBody } ),

            datatype: "json",
            //成功返回之后调用的函数
            success:function(data){
                var status = data.status ;
                if(status == 'success' ){
                   // alert( data.status );
                  // alert( data.result );
                    $("#RawJson").val( data.result );
                     Process();
                    $.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
                }else{
                    $.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
                }
            }
        });
    }

</script>

</body>
</html>