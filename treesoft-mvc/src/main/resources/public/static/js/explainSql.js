var columnsTemp=[],index=0;
function explainSQL(){var c=$.trim(editor.getValue()),b=editor.getSelection();""!=b&&(c=b);sqlArray=[];messTemp="";if(""==c||null==c)$.messager.show({title:"\u63d0\u793a",msg:"\u8bf7\u8f93\u5165SQL\u8bed\u53e5\uff01"});else if(""==databaseName||null==databaseName)$.messager.show({title:"\u63d0\u793a",msg:"\u8bf7\u70b9\u51fb\u5de6\u4fa7\u6307\u5b9a\u6570\u636e\u5e93\uff01"});else{disableOperButton();sqlArray=c.split(";");c=[];for(b=0;b<sqlArray.length;b++)""!=$.trim(sqlArray[b])&&c.push($.trim(sqlArray[b]));
$.easyui.loading({msg:"\u6267\u884c\u4e2d...",locale:"#sqltextareaDiv"});window.mainpage.searchTabs.closeAllTabs();executeSQLArrayForExplain(c,index)}}
var executeSQLArrayForExplain=function(c,b){if(b>=c.length){enableOperButton();$.messager.show({title:"\u63d0\u793a",msg:"\u6267\u884c\u5b8c\u6210\uff01",position:"bottomRight",timeout:2E3});$("#searchTabs").tabs("select",0);executeMessage.setValue(messTemp);var a=$("#searchTabs").tabs("tabs").length;$("#searchTabs").tabs("select",a-1);$.easyui.loaded("#sqltextareaDiv")}else{var d=c[b];executeAjax=$.ajax({type:"post",timeout:1E4,url:baseUrl+"/explain/queryExplain",data:{sql:d,databaseName:databaseName,
databaseConfigId:databaseConfigId},success:function(a){var f=a.status;if(null==a.status)messTemp=messTemp+a+" \n\n";else{if("fail"==f){$.messager.show({title:"\u63d0\u793a",msg:"\u6267\u884c\u5931\u8d25\uff01",position:"bottomRight",timeout:2E3,icon:"warning"});messTemp=messTemp+"\u3010\u8ba1\u5212\u3011 "+d+"\n \u4fe1\u606f\uff1a"+a.mess+" \n\n";$("#searchTabs").tabs("select",0);executeMessage.setValue(messTemp);a=$("#searchTabs").tabs("tabs").length;$("#searchTabs").tabs("select",a-1);$.easyui.loaded("#sqltextareaDiv");
enableOperButton();return}"success"==f?(messTemp=messTemp+"\u3010\u8ba1\u5212\u3011 "+d+"\n \u5f71\u54cd "+a.totalCount+" \u884c\uff0c \n \u6267\u884c\u65f6\u95f4\uff1a"+a.time+"\u6beb\u79d2\u3002\n\n",null!=a.rows&&showResultTabForExplain(a,d,b)):messTemp=messTemp+"\u3010\u8ba1\u5212\u3011 "+d+"\n \u4fe1\u606f\uff1a"+a.mess+" \n\n"}executeSQLArrayForExplain(c,b+1)},error:function(a,b,c){enableOperButton();$.easyui.loaded("#sqltextareaDiv");$.messager.show({title:"\u63d0\u793a",msg:"\u6267\u884c\u5931\u8d25\uff01",
position:"bottomRight",timeout:2E3,icon:"warning"})}})}};
function showResultTabForExplain(c,b,a){b="selectDg"+a;var d=' <div id="tb'+a+'" style="padding:5px;height:auto"> <div>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-insert" plain="true" id="addRowButton'+a+'"  onclick="addRow2('+a+')"> \u6dfb\u52a0 </a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-delete" plain="true" id="delButton'+a+'"   onclick="del( '+a+')">\u5220\u9664</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true" id="editRowButton'+
a+'" onclick="editRow2('+a+' )">\u4fee\u6539</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-link" plain="true" title="\u5168\u90e8\u5bfc\u51fajson"  id="exportDataToSQLButton'+a+'" onclick="exportDataToSQLFromSQL('+a+' )">\u5bfc\u51fa</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" title="\u5168\u90e8\u5bfc\u51faexcel"  id="exportDataToExcelButton'+
a+'" onclick="exportDataToExcelFromSQL('+a+' )">\u5bfc\u51fa</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="saveRowButton'+a+'"  onclick="saveRow('+a+' )"> \u4fdd\u5b58 </a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="cancelButton'+
a+'"  onclick="cancelChange('+a+')"> \u53d6\u6d88 </a>  <span class="toolbar-item dialog-tool-separator"></span>  </div>  </div> ';$("#searchTabs").tabs("add",{title:"\u7ed3\u679c"+(a+1),content:d+" <table id="+b+"></table>",closable:!0,tools:[{iconCls:"icon-berlin-calendar",handler:function(){}}]});d={url:baseUrl+"/explain/queryExplain",method:"POST",queryParams:{sql:sql,databaseName:databaseName,databaseConfigId:databaseConfigId},rownumbers:!0,fit:!0,fitColumns:!0,border:!1,striped:!0,pagination:!0,
pageNumber:1,pageSize:30,pageList:[20,30,40,50,100,200],singleSelect:!1,checkOnSelect:!0,toolbar:"#tb"+a,extEditing:!1,autoEditing:!0,singleEditing:!0,selectOnCheck:!0,onBeginEdit:function(a,b){obj=JSON.stringify(b)},onBeforeLoad:function(a){a=$(this).attr("firstLoad");return"false"==a||"undefined"==typeof a?($(this).attr("firstLoad","true"),!1):!0},onDblClickCell:function(a,b,c){c=$(this).datagrid("getColumnFields",!0).concat($(this).datagrid("getColumnFields"));for(var d=0;d<c.length;d++){var e=
$(this).datagrid("getColumnOption",c[d]);e.editor1=e.editor;c[d]!=b&&(e.editor=null)}$(this).datagrid("beginEdit",a);for(d=0;d<c.length;d++)e=$(this).datagrid("getColumnOption",c[d]),e.editor=e.editor1}};d.columns=eval(c.columns);d.idField=c.primaryKey;d.tableName=c.tableName;b=$("#"+b);0<a&&(d.onDblClickCell=null);b.datagrid(d);b.datagrid("loadData",c.rows);b.datagrid("getPager").pagination("refresh",{total:c.total});d=b.datagrid("getPager").pagination("options").displayMsg;b.datagrid("getPager").pagination("refresh",
{displayMsg:'<span class="pagination-btn-separator "></span> &nbsp; <span class="icon-hamburg-full-time">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> &nbsp;\u6267\u884c\u65f6\u95f4\uff1a'+c.time+"\u6beb\u79d2 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; "+d});$("#addRowButton"+a).linkbutton("disable");$("#copyRowButton"+a).linkbutton("disable");$("#delButton"+a).linkbutton("disable");$("#editRowButton"+a).linkbutton("disable");$("#saveRowButton"+a).linkbutton("disable");$("#cancelButton"+a).linkbutton("disable");
$("#exportDataToSQLButton"+a).linkbutton("disable");$("#exportDataToExcelButton"+a).linkbutton("disable")};