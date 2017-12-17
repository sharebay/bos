<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>angularjs-事件绑定</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular.min.js"></script>
</head>
<body>
	<div ng-app="myApp" ng-controller="myctrl">
		姓名：<input type="text" name="name" ng-model="name"/>
			<input type="button" ng-click="doDefault();" value="设置默认值"/><br/>
		您输入的姓名是：{{name}}<br/>
		您输入的姓名是：<span ng-bind="name"></span>
	</div>
	<script type="text/javascript">
	var app = angular.module('myApp',[]);
	app.controller('myctrl',function($scope){
		$scope.name='传智博客';
		$scope.doDefault=function(){
			$scope.name='九纹龙';
		}
	});
	</script>
</body>
</html>