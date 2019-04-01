// 定义控制器:
app.controller("vitalityController",function($scope,$controller,vitalityService){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});

    // 查询所有的品牌列表的方法:
    $scope.findAll = function(){
        // 向后台发送请求:
        vitalityService.findAll().success(function(response){
            $scope.huo = response.huoList;
            $scope.bu = response.buList;
            $scope.qw=response.huoCount;
            $scope.er=response.buCount;

        });
    }


});