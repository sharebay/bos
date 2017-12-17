<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>管理取派员</title>
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
				//$("#addForm").form("clear");
				layer.open({
					  type: 1,
					  title: '新增快递员',
					  content: $('#add_div'), 
					  zIndex: 1,
					  area: ['600px', '300px'],
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
				
				var rows = $('#grid').datagrid('getSelections');
				if (rows.length == 1) {
					//数据回显
					$("#addForm").form("load", rows[0]);
					//alert(JSON.stringify(rows[0]));
					$("#standardComboBox").combobox('select',rows[0].standard.id);
					layer.open({
						  type: 1,
						  title: '修改快递员信息',
						  content: $('#add_div'), 
						  zIndex: 1,
						  area: ['600px', '300px'],
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
				} else if (rows.length < 1) {
					tip('您未选择要修改的数据')
				} else {
					tip('每次只能修改一条数据')
				}
				
			}
			
			function doDelete(){
				//这里的rows会保留刷新前的rows
				//导致报500异常
				var rows = $('#grid').datagrid('getSelections');
				if (rows.length >= 1) {
					layer.confirm('确定要删除吗？', {icon: 3, title:'提示'}, function(index){
						var ids = "";
						$.each(rows, function(index, row) {
							ids += row.id + " ";
						});
						$.post("${pageContext.request.contextPath}/courier/deleteAllById.action", 
								{"idArray" : ids, "deleteLogically" : true},
								function(data) {
									if ("success" == data) {
										$('#grid').datagrid('clearSelections')
										$("#grid").datagrid("load");
									}
								},"text");
						
						layer.close(index);
						});
				} else {
					tip('您未选择任何数据！')
				}
			}
			
			function doRestore(){
				//这里的rows会保留刷新前的rows
				//导致报500异常
				var rows = $('#grid').datagrid('getSelections');
				if (rows.length >= 1) {
					layer.confirm('确定要还原吗？', {icon: 3, title:'提示'}, function(index){
						var ids = "";
						$.each(rows, function(index, row) {
							if (row.deltag == "1") {
								ids += row.id + " ";
							}
						});
						$.post("${pageContext.request.contextPath}/courier/restoreAllById.action", 
								{"idArray" : ids},
								function(data) {
									if ("success" == data) {
										$("#grid").datagrid("load");
									}
								},"text");
						layer.close(index);
						});
				} else {
					tip('您未选择任何数据！')
				}
			}
			
			function doSearch() {
				layer.open({
					  type: 1,
					  title: '查找快递员',
					  content: $('#search_div'), 
					  zIndex: 1,
					  area: ['400px', '250px'],
					  btn: ['查找'],
					  btnAlign: 'c',
					  yes: function(index, layero){
						  //alert(JSON.stringify($("#searchForm").serializeObject()));
						  //调用datagrid的load方法进行查询
						  $('#grid').datagrid('load', $("#searchForm").serializeObject());
						  
						  layer.close(index); //如果设定了yes回调，需进行手工关闭
					  }
				});
			}
			//工具栏
			var toolbar = [ {
				id : 'button-add',	
				text : '增加',
				iconCls : 'icon-add',
				handler : doAdd
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : doEdit
			}, {
				id : 'button-delete',
				text : '作废',
				iconCls : 'icon-cancel',
				handler : doDelete
			},{
				id : 'button-restore',
				text : '还原',
				iconCls : 'icon-save',
				handler : doRestore
			},{
				id : 'button-search',
				text : '查找',
				iconCls : 'icon-search',
				handler : doSearch
			}];
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true,
			},{
				field : 'courierNum',
				title : '工号',
				width : 80,
				align : 'center'
			},{
				field : 'name',
				title : '姓名',
				width : 80,
				align : 'center'
			}, {
				field : 'telephone',
				title : '手机号',
				width : 120,
				align : 'center'
			}, {
				field : 'checkPwd',
				title : '查台密码',
				width : 120,
				align : 'center'
			}, {
				field : 'pda',
				title : 'PDA号',
				width : 120,
				align : 'center'
			}, {
				field : 'standard.name',
				title : '取派标准',
				width : 120,
				align : 'center',
				formatter : function(data,row, index){
					if(row.standard != null){
						return row.standard.name;
					}
					return "";
				}
			}, {
				field : 'type',
				title : '取派员类型',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
				title : '所属单位',
				width : 200,
				align : 'center'
			}, {
				field : 'deltag',
				title : '是否作废',
				width : 80,
				align : 'center',
				formatter : function(data,row, index){
					if(data=="0"){
						return "正常使用"
					}else{
						return "已作废";
					}
				}
			}, {
				field : 'vehicleType',
				title : '车型',
				width : 100,
				align : 'center'
			}, {
				field : 'vehicleNum',
				title : '车牌号',
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
				
				// 取派员信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/courier/queryPage.action",
					idField : 'id',
					columns : columns,
					onDblClickRow : doDblClickRow
				});

			});
		
			function doDblClickRow(){
				alert("双击表格数据...");
			}
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		
		
		<!-- 添加快递员 -->
		<div id="add_div" style="overflow:auto;padding:5px;display: none" border="false">
			<form id="addForm" method="post"
					action="${pageContext.request.contextPath}/courier/save.action">
					<input type="hidden" name="id" />
					<table class="table-edit" width="80%" align="center">
						<tr>
							<td colspan="4">收派员信息</td>
						</tr>
						<tr>
							<td>快递员工号</td>
							<td>
								<input type="text" name="courierNum" class="easyui-validatebox" required="true" />
							</td>
							<td>姓名</td>
							<td>
								<input type="text" name="name" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>手机</td>
							<td>
								<input type="text" name="telephone" class="easyui-validatebox" required="true" />
							</td>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>查台密码</td>
							<td>
								<input type="text" name="checkPwd" class="easyui-validatebox" required="true" />
							</td>
							<td>PDA号码</td>
							<td>
								<input type="text" name="pda" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>快递员类型</td>
							<td>
								<input type="text" name="type" class="easyui-validatebox" required="true" />
							</td>
							<td>取派标准</td>
							<td>
								<input id="standardComboBox" type="text" name="standard.id" 
										class="easyui-combobox" 
										data-options="required:true,valueField:'id',textField:'name',
											url:'${pageContext.request.contextPath}/standard/findAll.action'"/>
							</td>
						</tr>
						<tr>
							<td>车型</td>
							<td>
								<input type="text" name="vehicleType" class="easyui-validatebox" required="true" />
							</td>
							<td>车牌号</td>
							<td>
								<input type="text" name="vehicleNum" class="easyui-validatebox" required="true" />
							</td>
						</tr>
					</table>
				</form>
		</div>
		
		
		<!-- 查询快递员-->
		<div id="search_div" style="overflow:auto;padding:5px;display: none" border="false">
				<form id="searchForm">
					<table class="table-edit" width="80%" align="center">
						<tr>
							<td>工号</td>
							<td>
								<input type="text" name="courierNum" />
							</td>
						</tr>
						<tr>
							<td>收派标准</td>
							<td>
								<input type="text" name="standard.name" />
							</td>
						</tr>
						<tr>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" />
							</td>
						</tr>
						<tr>
							<td>类型</td>
							<td>
								<input type="text" name="type" />
							</td>
						</tr>
					</table>
				</form>
		</div>
	</body>

</html>