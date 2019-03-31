// 定义控制器:
app.controller("userController",function($scope,$controller,userService){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope})
    // 查询所有的品牌列表的方法:
    $scope.findAll = function(){
        // 向后台发送请求:
        userService.findAll().success(function(response){
            $scope.list = response;

        });
    }
    //分页查询
    $scope.findPage = function(page,rows){
        // 向后台发送请求获取数据:
        userService.findPage(page,rows).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){

        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
});