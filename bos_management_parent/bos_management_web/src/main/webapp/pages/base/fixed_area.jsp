<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>管理定区/调度排班</title>
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
    <script src="${pageContext.request.contextPath}/admin/plugins/layui/layui.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/plugins/layui/css/layui.css" media="all">
    <script type="text/javascript">
        function tip(msg, t) {
            msg = msg || '提示消息！';
            t = t || 20000;
            layer.msg(msg, {
                time: t,
                btnAlign: 'c',
                btn: ['朕知道了']
            });
        }
        function doAdd(){
//				$('#addWindow').window("open");
            layer.open({
                type: 1,
                title: '新增',
                content: $('#add_div'),
                zIndex: 199,
                area: ['400px', '300px'],
                btn: ['保存'],
                btnAlign: 'c',
                yes: function(index, layero){
                    var b = $("#addForm").form("validate");
                    if (b) {
                        $("#addForm").form('submit', {
                            success:function(data){
                                if (data == 'success') {
                                    $('#grid').datagrid('reload');
                                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                                    tip("保存成功！", 2000);
                                }
                            }
                        });
                    } else {
                        tip('检查一下你填的数据哪里出错了')
                    }
                }
            });
        }

        function doEdit(){
            alert("修改...");
        }

        function doDelete(){
            alert("删除...");
        }

        function doSearch(){
            layer.open({
                type: 1,
                title: '新增',
                content: $('#search_div'),
                zIndex: 199,
                area: ['400px', '240px'],
                btn: ['查询'],
                btnAlign: 'c',
                yes: function(index, layero){
                    //调用datagrid的load方法进行查询
                    $('#grid').datagrid('load', $("#searchForm").serializeObject());
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            });
        }

        function doAssociationsToSubarea() {
            var rows = $("#grid").datagrid('getSelections');
            if (rows.length < 1) {
                tip("请选择关联定区", 2000);
            } else {
                var fixedAreaId = rows[0].id;
                //清空左右下拉框
                $("#noAssociationSelect").empty();
                $("#associationSelect").empty();
                //ajax请求后台，返回未关联用户和关联了fixedAreaId的用户
                $.get("${pageContext.request.contextPath}/fixedarea/subarea.action",
                    {"id" : fixedAreaId, "unassociated" : true},
                    function(data) {
                        var left = data.left;
                        var right = data.right;
                        $(left).each(function (index, subarea) {
                            //ajax查询json，将得到的未关联用户放入这里
                            $("#noAssociationSelect").append($("<option/>").val(subarea.id).text(subarea.keyWords));
                        });
                        $(right).each(function (index, subarea) {
                            //ajax查询json，将得到的关联用户放入这里
                            $("#associationSelect").append(
                                $("<option/>")
                                    .val(subarea.id)
                                    .text(subarea.keyWords)
                            );
                        });

                    },"json");


                $('#customerWindow').window('open');
            }
        }

        function doAssociations(){

            var rows = $("#grid").datagrid('getSelections');
            if (rows.length < 1) {
                tip("请选择关联客户", 2000);
            } else {
                var fixedAreaId = rows[0].id;
                //清空左右下拉框
                $("#noAssociationSelect").empty();
                $("#associationSelect").empty();
                //ajax请求后台，返回未关联用户和关联了fixedAreaId的用户
                $.get("${pageContext.request.contextPath}/fixedarea/customers.action",
                    {"id" : fixedAreaId, "unassociated" : true},
                    function(data) {
                        var left = data.left;
                        var right = data.right;
                        $(left).each(function (index, customer) {
                            //ajax查询json，将得到的未关联用户放入这里
                            $("#noAssociationSelect").append($("<option/>").val(customer.id).text(customer.username));
                        });
                        $(right).each(function (index, customer) {
                            //ajax查询json，将得到的关联用户放入这里
                            $("#associationSelect").append(
                                    $("<option/>")
                                        .val(customer.id)
                                        .text(customer.username)
                                );
                        });

                    },"json");


                $('#customerWindow').window('open');
            }
        }

        //工具栏
        var toolbar = [ {
            id : 'button-search',
            text : '查询',
            iconCls : 'icon-search',
            handler : doSearch
        }, {
            id : 'button-add',
            text : '增加',
            iconCls : 'icon-add',
            handler : doAdd
        }, {
            id : 'button-edit',
            text : '修改',
            iconCls : 'icon-edit',
            handler : doEdit
        },{
            id : 'button-delete',
            text : '删除',
            iconCls : 'icon-cancel',
            handler : doDelete
        },{
            id : 'button-association',
            text : '关联客户',
            iconCls : 'icon-sum',
            handler : doAssociations
        },{
            id : 'button-association-courier',
            text : '关联快递员',
            iconCls : 'icon-sum',
            handler : function(){
                // 判断是否已经选中了一个定区，弹出关联快递员窗口
                var rows = $("#grid").datagrid('getSelections');
                if(rows.length==1){
                    // 只选择了一个定区
                    // 弹出定区关联快递员 窗口
                    $("#courierWindow").window('open');
                }else{
                    // 没有选中定区，或者选择 了多个定区
                    $.messager.alert("警告","关联快递员,只能（必须）选择一个定区","warning");
                }
            }
        },{
            id : 'button-association2',
            text : '关联分区',
            iconCls : 'icon-sum',
            handler : doAssociationsToSubarea
        }];
        // 定义列
        var columns = [ [ {
            field : 'id',
            title : '编号',
            width : 80,
            align : 'center',
            checkbox:true
        },{
            field : 'fixedAreaName',
            title : '定区名称',
            width : 120,
            align : 'center'
        }, {
            field : 'fixedAreaLeader',
            title : '负责人',
            width : 120,
            align : 'center'
        }, {
            field : 'telephone',
            title : '联系电话',
            width : 120,
            align : 'center'
        }, {
            field : 'company',
            title : '所属公司',
            width : 120,
            align : 'center'
        } ] ];

        $(function(){
            //定义jQuery表单转换插件
            $.fn.serializeObject = function()
            {
                var o = {};
                var a = this.serializeArray();
                $.each(a, function() {
                    if (o[this.name]) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                });
                return o;
            };
            //加载layui
            layui.use(['layer'], function(){
                layer = layui.layer;
            });

            // 先将body隐藏，再显示，不会出现页面刷新效果
            $("body").css({visibility:"visible"});

            // 定区数据表格
            $('#grid').datagrid( {
                iconCls : 'icon-forward',
                fit : true,
                border : true,
                rownumbers : true,
                striped : true,
                pageList: [30,50,100],
                pagination : true,
                toolbar : toolbar,
                url : "${pageContext.request.contextPath}/fixedarea/queryPage.action",
                idField : 'id',
                columns : columns,
                onDblClickRow : doDblClickRow,
                singleSelect : true
            });

            $("#btn").click(function(){
                alert("执行查询...");
            });

        });

        function doDblClickRow(){
            alert("双击表格数据...");
            $('#association_subarea').datagrid( {
                fit : true,
                border : true,
                rownumbers : true,
                striped : true,
                url : "${pageContext.request.contextPath}/data/association_subarea.json",
                columns : [ [{
                    field : 'id',
                    title : '分拣编号',
                    width : 120,
                    align : 'center'
                },{
                    field : 'province',
                    title : '省',
                    width : 120,
                    align : 'center',
                    formatter : function(data,row ,index){
                        if(row.area!=null){
                            return row.area.province;
                        }
                        return "";
                    }
                }   , {
                    field : 'city',
                    title : '市',
                    width : 120,
                    align : 'center',
                    formatter : function(data,row ,index){
                        if(row.area!=null){
                            return row.area.city;
                        }
                        return "";
                    }
                }, {
                    field : 'district',
                    title : '区',
                    width : 120,
                    align : 'center',
                    formatter : function(data,row ,index){
                        if(row.area!=null){
                            return row.area.district;
                        }
                        return "";
                    }
                }, {
                    field : 'addresskey',
                    title : '关键字',
                    width : 120,
                    align : 'center'
                }, {
                    field : 'startnum',
                    title : '起始号',
                    width : 100,
                    align : 'center'
                }, {
                    field : 'endnum',
                    title : '终止号',
                    width : 100,
                    align : 'center'
                } , {
                    field : 'single',
                    title : '单双号',
                    width : 100,
                    align : 'center'
                } , {
                    field : 'position',
                    title : '位置',
                    width : 200,
                    align : 'center'
                } ] ]
            });
            $('#association_customer').datagrid( {
                fit : true,
                border : true,
                rownumbers : true,
                striped : true,
                url : "${pageContext.request.contextPath}/data/association_customer.json",
                columns : [[{
                    field : 'id',
                    title : '客户编号',
                    width : 120,
                    align : 'center'
                },{
                    field : 'name',
                    title : '客户名称',
                    width : 120,
                    align : 'center'
                }, {
                    field : 'company',
                    title : '所属单位',
                    width : 120,
                    align : 'center'
                }]]
            });

        }
    </script>
</head>

<body class="easyui-layout" style="visibility:hidden;">
<div region="center" border="false">
    <table id="grid"></table>
</div>
<div region="south" border="false" style="height:150px">
    <div id="tabs" fit="true" class="easyui-tabs">
        <div title="关联分区" id="subArea" style="width:100%;height:100%;overflow:hidden">
            <table id="association_subarea"></table>
        </div>
        <div title="关联客户" id="customers" style="width:100%;height:100%;overflow:hidden">
            <table id="association_customer"></table>
        </div>
    </div>
</div>

<!-- 添加 修改定区 -->

<div id="add_div" style="overflow:auto;padding:5px;" border="false">
    <form id="addForm" method="post" action="${pageContext.request.contextPath}/fixedarea/save.action">
        <table class="table-edit" width="80%" align="center">
            <tr>
                <td>定区编码</td>
                <td><input type="text" name="id" class="easyui-validatebox"
                           required="true" /></td>
            </tr>
            <tr>
                <td>定区名称</td>
                <td><input type="text" name="fixedAreaName"
                           class="easyui-validatebox" required="true" /></td>
            </tr>
            <tr>
                <td>负责人</td>
                <td><input type="text" name="fixedAreaLeader"
                           class="easyui-validatebox" required="true" /></td>
            </tr>
            <tr>
                <td>联系电话</td>
                <td><input type="text" name="telephone"
                           class="easyui-validatebox" required="true" /></td>
            </tr>
            <tr>
                <td>所属公司</td>
                <td><input type="text" name="company"
                           class="easyui-validatebox" required="true" /></td>
            </tr>
        </table>
    </form>
</div>

<!-- 查询定区 -->
<div id="search_div" style="overflow:auto;padding:5px;" border="false">
    <form id="searchForm">
        <table class="table-edit" width="80%" align="center">
            <tr>
                <td>定区编码</td>
                <td>
                    <input type="text" name="id" class="easyui-validatebox" />
                </td>
            </tr>
            <tr>
                <td>所属单位</td>
                <td>
                    <input type="text" name="courier.company" class="easyui-validatebox" />
                </td>
            </tr>
            <tr>
                <td>分区</td>
                <td>
                    <input type="text" name="subareaName" class="easyui-validatebox" />
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 关联客户窗口 -->
<div modal="true" class="easyui-window" title="关联客户窗口" id="customerWindow" collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 400px;height: 300px;">
    <div style="overflow:auto;padding:5px;" border="false">
        <form id="customerForm" action="${pageContext.request.contextPath}/fixedarea/associateCustomersWithFixedArea.action" method="post">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="3">关联客户</td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="id" id="customerFixedAreaId" />
                        <select id="noAssociationSelect" multiple="multiple" size="10"></select>
                    </td>
                    <td>
                        <input type="button" value=">>" id="toRight">
                        <br/>
                        <input type="button" value="<<" id="toLeft">
                        <script>
                            $(function () {
                                $("#toRight").click(function() {
                                    $("#associationSelect").append(
                                        $("#noAssociationSelect option:selected")
                                    );
                                });
                                $("#toLeft").click(function () {
                                    $("#noAssociationSelect").append(
                                        $("#associationSelect option:selected")
                                    );
                                });
                            });
                        </script>
                    </td>
                    <td>
                        <select id="associationSelect" name="customerIds" multiple="multiple" size="10"></select>
                    </td>
                </tr>
                <tr>
                    <td colspan="3"><a id="associationBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联客户</a> </td>
                    <script>
                        $("#associationBtn").click(
                            function() {
                                $("#associationSelect option").attr("selected", "selected");
                                $("#customerFixedAreaId").val($("#grid").datagrid('getSelections')[0].id)
                                $("#customerForm").form('submit', {
                                    success:function(data){
                                        if (data == 'success') {
                                            $('#grid').datagrid('reload');
                                            tip("保存成功！", 2000);
                                        }
                                        $('#customerWindow').window('close');
                                    }
                                });
                            }
                        );
                    </script>
                </tr>
            </table>
        </form>
    </div>
</div>

<!-- 关联快递员窗口 -->
<div class="easyui-window" title="关联快递员窗口" id="courierWindow" modal="true"
     collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 700px;height: 300px;">
    <div style="overflow:auto;padding:5px;" border="false">
        <form id="courierForm"
              action="${pageContext.request.contextPath}/fixedarea/associateCourierWithFixedArea.action" method="post">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="2">关联快递员</td>
                </tr>
                <tr>
                    <td>选择快递员</td>
                    <td>
                        <!-- 存放定区编号 -->
                        <input type="hidden" name="id" id="courierFixedAreaId" />
                        <input data-options="ditable:false, url:'${pageContext.request.contextPath}/courier/findAll.action',valueField:'id',textField:'name'"
                               type="text" name="courierId" class="easyui-combobox" required="true" />
                    </td>
                </tr>
                <tr>
                    <td>选择收派时间</td>
                    <td>
                        <select required="true" class="easyui-combobox" name="takeTimeId">
                            <option>请选择</option>
                            <option value="1">北京早班</option>
                            <option value="2">北京晚班</option>
                        </select>
                        <!-- <input type="text" name="takeTimeId" class="easyui-combobox" required="true" /> -->
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <a id="associationCourierBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联快递员</a>
                        <script>
                            $("#associationCourierBtn").click(
                                function() {
                                    $("#associationSelect option").attr("selected", "selected");
                                    $("#courierFixedAreaId").val($("#grid").datagrid('getSelections')[0].id)
                                    $("#courierForm").form('submit', {
                                        success:function(data){
                                            if (data == 'success') {
                                                $('#grid').datagrid('reload');
                                                tip("保存成功！", 2000);
                                            }
                                            $('#courierWindow').window('close');
                                        }
                                    });
                                }
                            );
                        </script>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>

</html>