<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="keywords" content="jquery,ui,easy,easyui,web">
		<meta name="description" content="easyui help you build your web page easily!">
		<title>运单管理</title>
		<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="../../js/easyui/ext/portal.css">
		<link rel="stylesheet" type="text/css" href="../../css/default.css">

		<script type="text/javascript" src="../../js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="../../js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../../js/easyui/ext/jquery.portal.js"></script>
		<script type="text/javascript" src="../../js/easyui/ext/jquery.cookie.js"></script>
		<script src="../../js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
		<script type="text/javascript">
			function doSearch() {
			    //获取查询数据
                var searchJson = $("#searchForm").serializeObject();
                //查询
				$('#tt').datagrid('load', searchJson);
			}

			$(function () {
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

                //开启运送按钮事件
                $("#start").click(function () {
                   var rows = $("#tt").datagrid("getSelections");
                   if (rows.length < 1) {
                       $.messager.alert("警告", "未选择任何订单");
                   } else {
                       var ids = "";
                       $.each(rows, function(index, row) {
                           ids += row.id + " ";
                       });
                       var url = "${pageContext.request.contextPath}/transit/beginTransit.action";
                       $.post(url, {"idArray" : ids}, function (data) {
                           if ("success" == data) {
                               $("#tt").datagrid("load");
                           }
                       });
                   }
                });
            })
		</script>
	</head>

	<body>
		<div id="tb">
			<a id="save" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true">修改</a>
			<a id="cut" icon="icon-cut" href="#" class="easyui-linkbutton" plain="true">作废</a>
			<a id="help1" icon="icon-help" href="#" class="easyui-linkbutton" plain="true">任务提示</a>
			<a id="help2" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印查询结果</a>
			<a id="help3" icon="icon-cancel" href="#" class="easyui-linkbutton" plain="true">取消</a>
			<a id="help4" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
			<a id="help5" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印标签</a>
			<a id="help6" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印工作单</a>
			<a id="start" icon="icon-ok" href="#" class="easyui-linkbutton" plain="true">开始运送</a>

			<br />
            <form id="searchForm">
				运单号：<input name="wayBillNum" style="line-height:26px;border:1px solid #ccc">
				发货地：<input name="sendAddress" style="line-height:26px;border:1px solid #ccc" >
				收货地：<input name="recAddress" style="line-height:26px;border:1px solid #ccc" >
				
				<select class="easyui-combobox" style="width:150px" name="sendProNum">
					<option value="">请选择快递产品类型</option>
					<option value="速运当日">速运当日</option>
					<option value="速运次日">速运次日</option>
					<option value="速运隔日">速运隔日</option>
				</select>
				
				<select class="easyui-combobox" style="width:150px" name="signStatus">
					<option value="0">请选择运单状态</option>
					<option value="1">待发货</option>
					<option value="2">派送中</option>
					<option value="3">已签收</option>
					<option value="4">异常</option>
				</select>
                <a href="javascript:void" class="easyui-linkbutton"
                   plain="true" onclick="doSearch()">查询</a>
            </form>
		</div>
		<table id="tt" class="easyui-datagrid" fit="true" toolbar="#tb" rownumbers="true" pagination="true"
			url="${pageContext.request.contextPath}/waybill/queryPage.action">
			<thead>
				<tr>
					<th field="id" checkbox="true"></th>
					<th field="wayBillNum" width="80">运单编号</th>
					<th field="sendName" width="80">寄件人</th>
					<th field="sendMobile" width="80">寄件人电话</th>
					<th field="sendCompany" width="80">寄件人公司</th>
					<th field="sendAddress" width="120">寄件人详细地址</th>
					<th field="recName" width="80">收件人</th>
					<th field="recMobile" width="80">收件人电话</th>
					<th field="recCompany" width="80">收件人公司</th>
					<th field="recAddress" width="120">收件人详细地址</th>
					<th field="sendProNum" width="80">产品类型</th>
					<th field="payTypeNum" width="80">支付类型</th>
					<th field="weight" width="80"> 重量</th>
					<th field="num" width="80"> 原件数</th>
					<th field="feeitemnum" width="80">实际件数</th>
					<th field="actlweit" width="80">实际重量</th>
					<th field="vol" width="80">体积</th>
					<th field="floadreqr" width="80">配载要求</th>
					<th field="wayBillType" width="80">运单类型</th>
				</tr>
			</thead>
		</table>
	</body>

</html>