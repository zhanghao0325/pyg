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

    $scope.derive = function(){

        // 向后台发送请求:
        userService.derive($scope.selectIds).success(function(response){
            // 判断是否成功:
            if(response.flag){
                // 成功
                alert(response.message);

            }else{
                // 失败
                alert(response.message);
            }
        });
    }
});