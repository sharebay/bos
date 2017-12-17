<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>角色添加</title>
    <!-- 导入jquery核心类库 -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
    <!-- 导入easyui类库 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/ext/portal.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/ext/jquery.portal.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/ext/jquery.cookie.js"></script>
    <script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
    <!-- 导入ztree类库 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/ztree/css/metroStyle/metroStyle.css"
          type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/admin/ztree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/admin/ztree/js/jquery.ztree.excheck.min.js"></script>

    <script type="text/javascript">
        $(function(){

            //加载权限
            $.post("${pageContext.request.contextPath}/permission/findAll.action",
                {},
                function (data) {
                    $.each(data, function (i, e) {
                        var s = "<input type=\"checkbox\" name=\"permissionIds\" value=\""+e.id+"\" />" + e.name;
                        $("#permissions").append(s);
                    })
                },
                "json");
            // 授权树初始化
            var setting = {
                data : {
                    key : {
                        title : "t"
                    },
                    simpleData : {
                        enable : true
                    }
                },
                check : {
                    enable : true
                }
            };

            $.ajax({
                url : '${pageContext.request.contextPath}/menu/findAllForTree.action',
                type : 'POST',
                dataType : 'json',
                success : function(data) {
                    $.fn.zTree.init($("#menuTree"), setting, data);
                },
                error : function(msg) {
                    alert('树加载异常!');
                }
            });

            // 点击保存
            $('#save').click(function(){

                if ($("#roleForm").form("validate")) {
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var nodes = tree.getCheckedNodes("true");
                    var s = "";
                    $.each(nodes, function (i, n) {
                        s += " " + n.id;
                    })
                    $("#menuIds").val(s);
                    $("#roleForm").form("submit", {
                        success : function (data) {
                            if ("success" == data) {
                                location.href = "${pageContext.request.contextPath}/pages/system/role.jsp";
                            }
                        }
                    })
                } else {
                    $.messager.alert("警告", "请完成表格！");
                }
            });
        });
    </script>
</head>

<body class="easyui-layout">
<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
    <div class="datagrid-toolbar">
        <a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
    </div>
</div>
<div region="center" style="overflow:auto;padding:5px;" border="false">
    <form id="roleForm" method="post" action="${pageContext.request.contextPath}/role/save.action">
        <table class="table-edit" width="80%" align="center">
            <tr class="title">
                <td colspan="2">角色信息</td>
            </tr>
            <tr>
                <td>名称</td>
                <td>
                    <input type="text" name="name" class="easyui-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
                <td>关键字</td>
                <td>
                    <input type="text" name="keyword" class="easyui-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
                <td>描述</td>
                <td>
                    <textarea name="description" rows="4" cols="60"></textarea>
                </td>
            </tr>
            <tr>
                <td>权限选择</td>
                <td id="permissions">
                </td>
            </tr>
            <tr>
                <td>菜单授权</td>
                <td>
                    <input id="menuIds" type="hidden" name="menuIds">
                    <ul id="menuTree" class="ztree"></ul>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>

</html>