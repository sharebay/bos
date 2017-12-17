<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>添加宣传任务</title>
		<!-- 导入jquery核心类库 -->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
		<!-- 导入easyui类库 -->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/kindeditor/kindeditor-all.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/kindeditor/lang/zh-CN.js"></script>


		<script type="text/javascript">
			$(function(){
				$("body").css({visibility:"visible"});
				$("#back").click(function(){
					location.href = "promotion.html";
				});
			});
		</script>
	</head>
	<body class="easyui-layout" style="visibility:hidden;">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
                <script>
                    $(function() {
                       $("#save").click(function() {
                           var b = $("#promotionForm").form("validate");
                           if (b) {
                              window.editor.sync();
                               $("#promotionForm").form("submit",{
                                   success: function(data){
                                       if ("success" == data) {
                                           location.href = "${pageContext.request.contextPath}/pages/take_delivery/promotion.jsp";
									   }
                                   }
                               });
                           } else {
                               $.messager.alert('警告','表单有问题');
                           }
                       });
                    });
                </script>
				<a id="back" icon="icon-back" href="#" class="easyui-linkbutton" plain="true">返回列表</a>
			</div>
		</div>
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="promotionForm" method="post" action="${pageContext.request.contextPath}/promotion/save.action" enctype="multipart/form-data">
				<table class="table-edit" width="95%" align="center">
					<tr class="title">
						<td colspan="4">宣传任务</td>
					</tr>
					<tr>
						<td>宣传概要(标题):</td>
						<td colspan="3">
							<input type="text" name="title" id="title" class="easyui-validatebox" required="true" />
						</td>
					</tr>
					<tr>
						<td>活动范围:</td>
						<td>
							<input type="text" name="activeScope" id="activeScope" class="easyui-validatebox" />
						</td>
						<td>宣传图片:</td>
						<td>
							<input type="file" name="titleImgFile" id="titleImg" class="easyui-validatebox" required="true"/>
						</td>
					</tr>
					<tr>
						<td>发布时间: </td>
						<td>
							<input type="text" name="startDate" id="startDate" class="easyui-datebox" required="true" />
						</td>
						<td>失效时间: </td>
						<td>
							<input type="text" name="endDate" id="endDate" class="easyui-datebox" required="true" />
						</td>
					</tr>
					<tr>
						<td>宣传内容(活动描述信息):</td>
						<td colspan="3">
							<textarea id="editor" name="description" style="width:80%" rows="20"></textarea>
							<script>
                                KindEditor.ready(function (K) {
                                    window.editor = K.create("#editor", {
                                        uploadJson :"${pageContext.request.contextPath}/promotion/uploadImage.action",
                                        fileManagerJson : "${pageContext.request.contextPath}/promotion/manageImage.action",
                                        allowFileManager : true
                                    });
                                });
                            </script>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>
