<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>运输配送管理</title>
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
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=bp09g732UD7Kh0UbG5GoaUyMGbMizI0A"></script>
		<script type="text/javascript">
			$(function() {
				// 设置3个全局变量
                var map = new BMap.Map("allmap");
                var start;
                var end;
                map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
                map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
                //三种驾车策略：最少时间，最短距离，避开高速
                var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];

                $("#result").click(function(){
                    map.clearOverlays();
                    //获取寻路策略
                    var i=$("#driving_way select").val();
                    //获取途径点
                    var viapoint = $("#viapoint").val();
                    var points = viapoint.split(/[,，_]/);
                    var waypoints;
                    if (points.length > 0) {
                        waypoints = {"waypoints" : points};
                    }

                    search(start,end,routePolicy[i],waypoints);
                    function search(start,end,route,waypoints){
                        var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true},policy: route});
                        driving.search(start,end,waypoints);
                    }
                });
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({
					visibility: "visible"
				});

				var transitToolbar = [{
					id: 'button-inoutstore',
					text: '出入库',
					iconCls: 'icon-add',
					handler: function() {
                        var row = $("#transitGrid").datagrid("getSelected");
                        if (row == null) {
                            $.messager.alert("警告", "未选择任何条目");
                        } else {
                            if (row.status == "出入库中转") {
                                $("#inoutstoreWindow").window('open');
                                $("#inOutStoreTransitInfoView").empty();
                                $("#inOutStoreTransitInfoView").append("运单号：" + row.wayBill.wayBillNum + "<br/>");
                                $("#inOutStoreTransitInfoView").append("发货地：" + row.wayBill.sendAddress + "<br/>");
                                $("#inOutStoreTransitInfoView").append("收货地：" + row.wayBill.recAddress + "<br/>");
                                $("#inOutStoreTransitInfoView").append("物流信息：" +row.transferInfo + "<br/>");
                                $("#inOutStoreId").val(row.id);
                            } else {
                                $.messager.alert("警告", "该条目状态为：【" + row.status + "】")
                            }

                        }
					}
				}, {
					id: 'button-delivery',
					text: '开始配送',
					iconCls: 'icon-print',
					handler: function() {

                        var row = $("#transitGrid").datagrid("getSelected");
                        if (row == null) {
                            $.messager.alert("警告", "未选择任何条目");
                        } else {
                            if (row.status == "到达网点") {
                                $("#deliveryWindow").window('open');
                                $("#deliveryTransitInfoView").empty();
                                $("#deliveryTransitInfoView").append("运单号：" + row.wayBill.wayBillNum + "<br/>");
                                $("#deliveryTransitInfoView").append("发货地：" + row.wayBill.sendAddress + "<br/>");
                                $("#deliveryTransitInfoView").append("收货地：" + row.wayBill.recAddress + "<br/>");
                                $("#deliveryTransitInfoView").append("物流信息：" +row.transferInfo + "<br/>");
                                $("#deliveryId").val(row.id);

                            } else {
                                $.messager.alert("警告", "该条目状态为：【" + row.status + "】")
                            }
                        }
					}
				}, {
					id: 'button-sign',
					text: '签收录入',
					iconCls: 'icon-save',
					handler: function() {
                        var row = $("#transitGrid").datagrid("getSelected");
                        if (row == null) {
                            $.messager.alert("警告", "未选择任何条目");
                        } else {
                            if (row.status == "开始配送") {
                                $("#signWindow").window('open');
                                $("#signTransitInfoView").empty();
                                $("#signTransitInfoView").append("运单号：" + row.wayBill.wayBillNum + "<br/>");
                                $("#signTransitInfoView").append("发货地：" + row.wayBill.sendAddress + "<br/>");
                                $("#signTransitInfoView").append("收货地：" + row.wayBill.recAddress + "<br/>");
                                $("#signTransitInfoView").append("物流信息：" +row.transferInfo + "<br/>");
                                $("#signId").val(row.id);

                            } else {
                                $.messager.alert("警告", "该条目状态为：【" + row.status + "】")
                            }
                        }
					}
				}, {
					id: 'button-transit',
					text: '运输路径',
					iconCls: 'icon-search',
					handler: function() {
					}
				}, {
					id: 'button-page',
					text: '实时配送路径',
					iconCls: 'icon-search',
					handler: function() {
					    var rows = $("#transitGrid").datagrid("getSelections");
					    if (rows.length < 1) {
					        $.messager.alert("警告", "未选择任何条目");
                        } else {

					        start = rows[0].wayBill.sendAddress;
					        end = rows[0].wayBill.recAddress;
                            map.clearOverlays();
                            var i=$("#driving_way select").val();
                            var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true},policy: routePolicy[i]});
                            driving.search(start,end);
                            $("#transitPathWindow").window('open');
                        }
					}
				}];

				var transitColumns = [
					[{
						field: 'id',
						title: '编号',
						width: 30
					}, {
						field: 'wayBillNum',
						title: '运单号',
						width: 100,
						formatter: function(data, row, index) {
							if(row.wayBill.wayBillNum != undefined) {
								return row.wayBill.wayBillNum;
							}
						}
					}, {
						field: 'sendName',
						title: '寄件人姓名',
						width: 100,
						formatter: function(data, row, index) {
							return row.wayBill.sendName;
						}

					}, {
						field: 'sendAddress',
						title: '寄件地址',
						width: 100,
						formatter: function(data, row, index) {
							return row.wayBill.sendAddress;
						}
					}, {
						field: 'recName',
						title: '收件人姓名',
						width: 100,
						formatter: function(data, row, index) {
							return row.wayBill.recName;
						}
					}, {
						field: 'recAddress',
						title: '收件地址',
						width: 100,
						formatter: function(data, row, index) {
							return row.wayBill.recAddress;
						}
					}, {
						field: 'goodsType',
						title: '托寄物',
						width: 100,
						formatter: function(data, row, index) {
							return row.wayBill.goodsType;
						}
					}, {
						field: 'status',
						title: '运输状态',
						width: 100
					}, {
						field: 'outletAddress',
						title: '网点地址',
						width: 100
					}, {
						field: 'transferInfo',
						title: '物流信息',
						width: 100
					}]
				];

				// 运输配送管理 表格定义
				$('#transitGrid').datagrid({
					iconCls: 'icon-forward',
				    url: '${pageContext.request.contextPath}/transit/queryPage.action',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [20, 50, 100],
					pagination: true,
					idField: 'id',
					singleSelect: true,
					toolbar: transitToolbar,
					columns: transitColumns
				});
				// 出入库保存按钮点击事件
				$("#inoutStoreSave").click(function(){
					if($("#inoutStoreForm").form('validate')){
						$("#inoutStoreForm").form('submit', {
                            success: function(data){
                                if ("success" == data) {
                                    $("#inoutstoreWindow").window('close');
                                    $("#transitGrid").datagrid("load");
                                }
                            }
                        });
                    }
				});

				// 开始配送保存按钮点击事件
				$("#deliverySave").click(function(){
					if($("#deliveryForm").form('validate')){
						$("#deliveryForm").form("submit", {
                            success: function(data){
                                if ("success" == data) {
                                    $("#deliveryWindow").window('close');
                                    $("#transitGrid").datagrid("load");
                                }
                            }
                        })
					}
				});

				// 签收录入按钮点击事件
				$("#signSave").click(function(){
					if($("#signForm").form('validate')){
						$("#signForm").form("submit", {
                            success: function(data){
                                if ("success" == data) {
                                    $("#signWindow").window('close');
                                    $("#transitGrid").datagrid("load");
                                }
                            }
                        })
					}
				});

				// 点击查询按钮，实现途经点的路线规划
				$("#result").click(function(){
				})
			});
		</script>
		<style type="text/css">
			body, html {width: 100%;height: 100%; margin:0;font-family:"微软雅黑";}
			#allmap{height:92%;width:100%;}
		</style>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">

		<div data-options="region:'center'">
			<table id="transitGrid"></table>
		</div>

		<div class="easyui-window" title="出入库" id="inoutstoreWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 600px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="inoutStoreSave" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="inoutStoreForm" method="post" action="${pageContext.request.contextPath}/inoutstore/save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">入库操作</td>
						</tr>
						<tr>
							<td>运单信息</td>
							<td>
								<input type="hidden" name="transitInfoId" id="inOutStoreId" />
								<span id="inOutStoreTransitInfoView"></span>
						</tr>
						<tr>
							<td>操作</td>
							<td>
								<select name="operation" class="easyui-combobox">
									<option value="入库">入库</option>
									<option value="出库">出库</option>
									<option value="到达网点">到达网点</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>仓库或网点地址</td>
							<td>
								<input type="text" name="address" size="40" />
							</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>
								<textarea rows="3" cols="40" name="description"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="easyui-window" title="开始配送" id="deliveryWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 600px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="deliverySave" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="deliveryForm" method="post" action="${pageContext.request.contextPath}/delivery/save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">开始配送</td>
						</tr>
						<tr>
							<td>运单信息</td>
							<td>
								<input type="hidden" name="transitInfoId" id="deliveryId" />
								<span id="deliveryTransitInfoView"></span>
						</tr>
						<tr>
							<td>快递员工号</td>
							<td>
								<input type="text" required="true" name="courierNum" />
							</td>
						</tr>
						<tr>
							<td>快递员姓名</td>
							<td>
								<input type="text" required="true" name="courierName" />
							</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>
								<textarea rows="3" cols="40" name="description"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="easyui-window" title="签收录入" id="signWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 600px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="signSave" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="signForm" method="post" action="${pageContext.request.contextPath}/sign/save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">签收录入</td>
						</tr>
						<tr>
							<td>运单信息</td>
							<td>
								<input type="hidden" name="transitInfoId" id="signId" />
								<span id="signTransitInfoView"></span>
						</tr>
						<tr>
							<td>签收人</td>
							<td>
								<input type="text" required="true" name="signName" />
							</td>
						</tr>
						<tr>
							<td>签收日期</td>
							<td>
								<input type="text" class="easyui-datebox" required="true" name="signTime" />
							</td>
						</tr>
						<tr>
							<td>签收状态</td>
							<td colspan="3">
								<select class="easyui-combobox" style="width:150px" name="signType">
									<option value="正常">正常</option>
									<option value="返单">返单</option>
									<option value="转发签收">转发签收</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>异常备注</td>
							<td>
								<textarea name="errorRemark" rows="4" cols="40"></textarea>
							</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>
								<textarea rows="3" cols="40" name="description"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="easyui-window" title="运输路径查看" id="transitPathWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:100px;width: 800px; height: 400px">
			<div id="allmap"></div>
			<div id="driving_way">
				请输入途经点（用,号分开）：<input type="text" id="viapoint" size="70" value="" />
				<input type="button" id="result" value="查询" />
			</div>
		</div>

		<div class="easyui-window" title="实时配送路径" id="deliveryInTimePathWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:100px;width: 800px; height:400px">
		</div>
	</body>

</html>