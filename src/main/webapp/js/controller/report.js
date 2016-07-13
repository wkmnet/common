/**
 * Created by wkm on 16-6-10.
 */
reportApp.controller("ReportController",function($scope,$http,$timeout){

    $scope.alertConfig = {"show":false,"message":""};

    $scope.inputData = {"sql":""};

    $scope.excelResource = {"src":"","show":false};

    $scope.outputExcel = function (){
        var newDate = new Date();
        var myDate = newDate.getFullYear() + "" + newDate.getMonth() + "" + newDate.getDay();
        $http.get("/api/report/" + myDate + "?sql=" + $scope.inputData.sql).success(function(response){
            if(response.success){
                $scope.excelResource.src = response.data.url;
            }
            $scope.excelResource.show = response.success;
            $scope.alertMessage(response.message);
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    };

    $scope.alertMessage = function(message){
        $scope.alertConfig.message = message;
        $scope.alertConfig.show = true;
        $timeout(function(){
            $scope.alertConfig.show = false;
        },2000);
    };
});