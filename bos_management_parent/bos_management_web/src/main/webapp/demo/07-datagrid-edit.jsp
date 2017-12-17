<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>easyui-datagrid-行编辑功能</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
<table id="grid"></table>
<script type="text/javascript">
    $(function(){
        $("#grid").datagrid({
            url:'${pageContext.request.contextPath}/data/courier.json',
            columns:[[
                {title:'编号',field:'id',checkbox:true},
                {title:'姓名',field:'name', editor : {
                    type : "validatebox",
                    options : {
                        required : true
                    }
                }},
                {title:'电话',field:'telephone',editor : {
                    type : "validatebox",
                    options : {
                        required : true
                    }
                }}
            ]],
            toolbar:[
                {text:'增加',iconCls:'icon-add',handler:function(){
                    $('#grid').datagrid('insertRow',{
                        index: 0,
                        row : {}
                    });
                    $('#grid').datagrid("beginEdit", 0);

                }},
                {text:'删除',handler:function(){
                    var rows = $("#grid").datagrid("getSelections");
                    if (rows.length > 0) {
                        $.each(rows, function (i, row) {
                            var index = $("#grid").datagrid("getRowIndex", row);
                            $("#grid").datagrid("deleteRow", index);
                        })
                    }
                }},
                {text:'修改',handler:function(){
                    var rows = $("#grid").datagrid("getSelections");
                    if (rows.length > 0) {
                        $.each(rows, function (i, row) {
                            var index = $("#grid").datagrid("getRowIndex", row);
                            $("#grid").datagrid("beginEdit", index);
                        })
                    }
                }},
                {text:'保存',handler:function(){
                    var rows = $("#grid").datagrid("getSelections");
                    if (rows.length > 0) {
                        $.each(rows, function (i, row) {
                            var index = $("#grid").datagrid("getRowIndex", row);
                            $("#grid").datagrid("endEdit", index);
                        })
                    }
                }}
            ],
            pagination:true,//开启分页查询条
            pageList:[10,30,50],//每页条数选项
            onAfterEdit:function(rowIndex, rowData, changes){
                alert(rowIndex+"--"+rowData.name+"--"+changes.name);
            }
        });
    });
</script>
</body>
</html>