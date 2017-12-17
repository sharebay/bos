<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>angularjs-集合遍历</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular.min.js"></script>
</head>
<body>
	<div ng-app="myApp" ng-controller="myctrl">
		<table>
			<thead>
				<tr>
					<th>编号</th>
					<th>姓名</th>
					<th>电话</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="customer in customers">
					<td>{{$index+1}}</td>
					<td>{{customer.name}}</td>
					<td>{{customer.telephone}}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<script type="text/javascript">
	var app = angular.module('myApp',[]);
	app.controller('myctrl',function($scope){
		$scope.name='传智博客';
		$scope.doDefault=function(){
			$scope.name='九纹龙';
		}
		$scope.customers=[
		                  {id:'1',name:'马容',telephone:'13838389438'},
		                  {id:'2',name:'宋吉吉',telephone:'13838389438'}
		                  ];
	});
	</script>
</body>
</html>