//控制层
app.controller('orderController' ,function($scope,$controller,orderService){

    $controller('baseController',{$scope:$scope});//继承

    // //读取列表数据绑定到表单中
    // $scope.findAll=function(){
    //     seckillService.findAll().success(
    //         function(response){
    //             $scope.list=response;
    //         }
    //     );
    // }
    //
    // //分页
    // $scope.findPage=function(page,rows){
    //     seckillService.findPage(page,rows).success(
    //         function(response){
    //             $scope.list=response.rows;
    //             $scope.paginationConf.totalItems=response.total;//更新总记录数
    //         }
    //     );
    // }
    //
    // //查询实体
    // $scope.findOne=function(id){
    //     seckillService.findOne(id).success(
    //         function(response){
    //             $scope.entity= response;
    //         }
    //     );
    // }
    //
    // //保存
    // $scope.save=function(){
    //     var serviceObject;//服务层对象
    //     if($scope.entity.id!=null){//如果有ID
    //         serviceObject=seckillService.update( $scope.entity ); //修改
    //     }else{
    //         serviceObject=seckillService.add( $scope.entity  );//增加
    //     }
    //     serviceObject.success(
    //         function(response){
    //             if(response.flag){
    //                 //重新查询
    //                 $scope.reloadList();//重新加载
    //             }else{
    //                 alert(response.message);
    //             }
    //         }
    //     );
    // }
    //
    //
    // //批量删除
    // $scope.dele=function(){
    //     //获取选中的复选框
    //     goodsService.dele( $scope.selectIds ).success(
    //         function(response){
    //             if(response.flag){
    //                 $scope.reloadList();//刷新列表
    //                 $scope.selectIds = [];
    //             }
    //         }
    //     );
    // }

    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(page,rows){
        orderService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    // 显示状态"未付款","已付款","未发货","已发货","交易成功","交易关闭",""
    $scope.status = ["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];
    //支付类型，1、在线支付，2、货到付款
    $scope.paystatus=["","在线支付","货到付款"]
    // 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
    $scope.orginstatus=["","app端","pc端","M端","微信端","手机qq端"]


    // 审核的方法:
    $scope.updateStatus = function(status){
        goodsService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }
});
