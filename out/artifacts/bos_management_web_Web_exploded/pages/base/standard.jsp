<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>取派标准</title>
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
			
			function tip(msg) {
				layer.msg(msg, {
			        time: 20000, //20s后自动关闭
			        btnAlign: 'c',
			        btn: ['朕知道了']
			      });
			}
		
			$(function(){
				layui.use('layer', function(){
					  layer = layui.layer;
					});  
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派标准信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/standard/queryPage.action",
					idField : 'id',
					columns : columns
				});
			});	
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					//$("#standardWindow").window("open");
					//$("#addForm").form("clear");
					layer.open({
						  type: 1,
						  title: '新增标准',
						  content: $('#add_div'), 
						  area: ['400px', '300px'],
						  zIndex: 1,
						  btn: ['保存'],
						  btnAlign: 'c',
						  yes: function(index, layero){
						    var b = $("#addForm").form("validate");
						    if (b) {
						    	$("#addForm").submit();
						    } else {
						    	tip('检查一下你填的数据哪里出错了')
						    }
						  }
					});
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					var rows = $("#grid").datagrid("getSelections");
					if (rows.length == 1) {
						$("#addForm").form("load",rows[0]);
						layer.open({
							  type: 1,
							  title: '修改标准',
							  content: $('#add_div'), 
							  area: ['400px', '300px'],
							  btn: ['保存'],
							  btnAlign: 'c',
							  yes: function(index, layero){
							    var b = $("#addForm").form("validate");
							    if (b) {
							    	$("#addForm").submit();
							    } else {
							    	tip('检查一下你填的数据哪里出错了')
							    }
							  }
						});
					} else if (rows.length < 1) {
						tip('您未选择要修改的数据')
					} else {
						tip('每次只能修改一条数据')
					}
				}
			},{
				id : 'button-delete',
				text : '作废',
				iconCls : 'icon-cancel',
				handler : function(){
					
					var rows = $("#grid").datagrid("getSelections");
					
					if (rows.length >= 1) {
						layer.confirm('确定要删除吗？', {icon: 3, title:'提示'}, function(index){
							var ids = "";
							$.each(rows, function(index, row) {
								ids += row.id + " ";
							});
							$.post("${pageContext.request.contextPath}/standard/deleteStandards.action", 
									{idArray : ids},
									function(data) {
										if ("success" == data) {
											$("#grid").datagrid("reload");
										}
									},"text");
							
							layer.close(index);
						});
					} else {
						tip('皇上，请选择要删除的数据。')
					}
					
				}
			},{
				id : 'button-restore',
				text : '还原',
				iconCls : 'icon-save',
				handler : function(){
					alert('还原');
				}
			}];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true
			},{
				field : 'name',
				title : '标准名称',
				width : 120,
				align : 'center'
			}, {
				field : 'minWeight',
				title : '最小重量',
				width : 120,
				align : 'center'
			}, {
				field : 'maxWeight',
				title : '最大重量',
				width : 120,
				align : 'center'
			}, {
				field : 'minLength',
				title : '最小长度',
				width : 120,
				align : 'center'
			}, {
				field : 'maxLength',
				title : '最大长度',
				width : 120,
				align : 'center'
			}, {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operatingTime',
				title : '操作时间',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
				title : '操作单位',
				width : 120,
				align : 'center'
			} ] ];
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>

		<div id="add_div" style="display: none" >
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="addForm" method="post" action="${pageContext.request.contextPath}/standard/addStandard.action">
					<table class="table-edit" width="80%" align="center">
						<tr>
							<td colspan="2">收派标准信息
								<!--提供隐藏域 装载id -->
								<input type="hidden" name="id" />
							</td>
						</tr>
						<tr>
							<td>收派标准名称</td>
							<td>
								<input type="text" name="name" 
									class="easyui-validatebox" data-options="required:true" />
							</td>
						</tr>
						<tr>
							<td>最小重量</td>
							<td>
								<input type="text" name="minWeight" 
										class="easyui-numberbox" required="true" />
							</td>
						</tr>
						<tr>
							<td>最大重量</td>
							<td>
								<input type="text" name="maxWeight" class="easyui-numberbox" required="true" />
							</td>
						</tr>
						<tr>
							<td>最小长度</td>
							<td>
								<input type="text" name="minLength" class="easyui-numberbox" required="true" />
							</td>
						</tr>
						<tr>
							<td>最大长度</td>
							<td>
								<input type="text" name="maxLength" class="easyui-numberbox" required="true" />
							</td>
						</tr>
					</table>
				</form>
			</div>
	</div>
		
	</body>
	
	

</html>