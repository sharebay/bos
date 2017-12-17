<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>区域设置</title>
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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ocupload-1.1.2.js"></script>
		<script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/admin/plugins/layui/layui.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/admin/plugins/layui/css/layui.css" media="all">
		<script type="text/javascript">
			function tip(msg, t) {
				msg = msg || '提示消息！';
				t = t || 20000;
				layer.msg(msg, {
			        time: t, //20s后自动关闭
			        btnAlign: 'c',
			        btn: ['朕知道了']
			      });
			}
			function doAdd(){
				layer.open({
					  type: 1,
					  title: '新增区域',
					  content: $('#add_div'), 
					  zIndex: 1,
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
			
			function doSearch() {
				layer.open({
					  type: 1,
					  title: '查找区域',
					  content: $('#search_div'), 
					  zIndex: 1,
					  area: ['300px', '200px'],
					  btn: ['查找'],
					  btnAlign: 'c',
					  yes: function(index, layero){
						  //调用datagrid的load方法进行查询
						  $('#grid').datagrid('load', $("#searchForm").serializeObject());
						  
						  layer.close(index); //如果设定了yes回调，需进行手工关闭
					  }
				});
			}
			
			function doView(){
				alert("修改...");
			}
			
			function doDelete(){
				alert("删除...");
			}
			
			//工具栏
			var toolbar = [ {
				id : 'button-edit',	
				text : '修改',
				iconCls : 'icon-edit',
				handler : doView
			}, {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : doAdd
			}, {
				id : 'button-delete',
				text : '删除',
				iconCls : 'icon-cancel',
				handler : doDelete
			}, {
				id : 'button-import',
				text : '导入',
				iconCls : 'icon-redo'
			}, {
				id : 'button-search',
				text : '查询',
				iconCls : 'icon-search',
				handler : doSearch
			}];
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true,
			},{
				field : 'province',
				title : '省',
				width : 120,
				align : 'center'
			}, {
				field : 'city',
				title : '市',
				width : 120,
				align : 'center'
			}, {
				field : 'district',
				title : '区',
				width : 120,
				align : 'center'
			}, {
				field : 'postcode',
				title : '邮编',
				width : 120,
				align : 'center'
			}, {
				field : 'shortcode',
				title : '简码',
				width : 120,
				align : 'center'
			}, {
				field : 'citycode',
				title : '城市编码',
				width : 200,
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
				
				// 区域管理数据表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/area/queryPage.action",
					idField : 'id',
					columns : columns,
					onDblClickRow : doDblClickRow
				});
				
				//文件上传
				$('#button-import').upload({  
		            action: '${pageContext.request.contextPath}/area/upload.action',//相当于form中的action属性 
		            name: 'areaFile', //后台接收文件的属性名
		            onComplete: function (data) {//接收后台返回的处理结果
		            	if ("success" == data) {
		            		$('#grid').datagrid("load")
		            		tip("导入成功！", 2000);
		            	}
		            }  
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


			<div id="add_div" style="overflow:auto;padding:5px;" border="false">
				<form id="addForm" action="${pageContext.request.contextPath }/area/save" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr>
							<td>省</td>
							<td>
								<input type="text" name="province" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>市</td>
							<td>
								<input type="text" name="city" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>区</td>
							<td>
								<input type="text" name="district" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>邮编</td>
							<td>
								<input type="text" name="postcode" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>简码</td>
							<td>
								<input type="text" name="shortcode" class="easyui-validatebox" required="true" />
							</td> 
						</tr>
						<tr>
							<td>城市编码</td>
							<td>
								<input type="text" name="citycode" class="easyui-validatebox" required="true" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		
		<!-- 查询区域-->
			<div id="search_div" style="overflow:auto;padding:5px;" border="false">
				<form id="searchForm">
					<table class="table-edit" width="80%" align="center">
						<tr>
							<td>省份</td>
							<td>
								<input type="text" name="province" />
							</td>
						</tr>
						<tr>
							<td>城市</td>
							<td>
								<input type="text" name="city" />
							</td>
						</tr>
						<tr>
							<td>区（县）</td>
							<td>
								<input type="text" name="district" />
							</td>
						</tr>
					</table>
				</form>
			</div>
	</body>

</html>