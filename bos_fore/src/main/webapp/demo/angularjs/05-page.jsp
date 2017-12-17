<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>angularjs-page</title>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/angular.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/angular-route.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.css" />
</head>
<body  ng-app="paginationApp" ng-controller="paginationCtrl">
<table class="table table-bordered table-hover">
    <tr>
        <th>序号</th>
        <th>商品编号</th>
        <th>名称</th>
        <th>价格</th>
    </tr>
    <tr ng-repeat="product in products">
        <td>{{$index+1}}</td>
        <td>{{product.id}}</td>
        <td>{{product.name}}</td>
        <td>{{product.price}}</td>
    </tr>
</table>
<div>
    <ul class="pagination pull-right">
        <li><a href ng-click="prev()">上一页</a></li>
        <li ng-repeat="page in pageList"
            ng-class="{active: isActivePage(page)}"><a
                ng-click="selectPage(page)">{{page}}</a></li>
        <li><a href ng-click="next()">下一页</a></li>
    </ul>
</div>
<script type="text/javascript">
    var myapp = angular.module("paginationApp", []);
    myapp.controller("paginationCtrl", [
        "$scope",
        "$http",
        function($scope, $http) {
            $scope.currentPage = 1; // 当前页
            $scope.pageSize = 2; // 当前页显示的记录数
            $scope.totalCount = 0; // 总记录数
            $scope.totalPages = 0; // 总页数
            // 上一页
            $scope.prev = function() {
                if ($scope.currentPage > 1) {
                    $scope.currentPage = $scope.currentPage - 1;
                    $scope.selectPage($scope.currentPage);
                }
            }
            // 下一页
            $scope.next = function() {
                if ($scope.currentPage < $scope.totalPages) {
                    $scope.currentPage = $scope.currentPage + 1;
                    $scope.selectPage($scope.currentPage);
                }
            }
            // 选择页码，当前页，传递当前页
            $scope.selectPage = function(page) {
                $http({
                    method : 'GET',
                    url : './06_' + page + '.json',//动态加载json的文件
                    params : {
                        page : page, // 传递当前页
                        pageSize : $scope.pageSize
                        // 传递当前页最多显示的记录数
                    }
                }).success(
                    function(data, status, headers, config) {
                        // 显示表格数据
                        $scope.products = data.products;
                        // 计算总页数
                        $scope.totalCount = data.totalCount;
                        // 计算总页数：Math.ceil返回大于参数x的最小整数,即对浮点数向上取整.
                        $scope.totalPages = Math
                            .ceil($scope.totalCount
                                / $scope.pageSize);

                        // 当前显示页，设为当前页
                        $scope.currentPage = page;

                        // 固定显示10页 (前5后4)
                        var begin;
                        var end;

                        begin = page - 5;
                        if (begin < 0) {
                            begin = 1;
                        }

                        end = begin + 9;
                        if (end > $scope.totalPages) {
                            end = $scope.totalPages;
                        }

                        begin = end - 9;
                        if (begin < 1) {
                            begin = 1;
                        }
                        // 定义数组
                        $scope.pageList = new Array();
                        for (var i = begin; i <= end; i++) {
                            $scope.pageList.push(i);
                        }
                    }).error(
                    function(data, status, headers, config) {
                        alert("分页出错，请联系管理员");
                    });
            }

            // 是否激活当前页
            $scope.isActivePage = function(page) {
                return page == $scope.currentPage; // 返回boolean类型，如果传递的页码和当前页相等返回true
            }
            // 激活当前页
            $scope.selectPage($scope.currentPage);
        } ])
</script>
</body>
</html>