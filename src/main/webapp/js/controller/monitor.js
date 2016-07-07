/**
 * Created by wkm on 16-6-10.
 */
monitorApp.controller("MonitorController",function($scope,$http,$timeout){

    $scope.alertConfig = {"show":false,"message":""};

    $scope.autoRefresh = false;

    $scope.serverBean = {"serverName":"","serverDomain":"","checkInterface":""};

    $scope.loadMonitorServer = function () {
        $http.get("/api/monitor").success(function (response) {
            if(response.success){
                $scope.alertMessage(response.message);
                $scope.serverList = response.data;
            } else {
                $scope.alertMessage(response.message);
            }
            if($scope.autoRefresh){
                $timeout(function(){
                    $scope.loadMonitorServer();
                },10000);
            }
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    }
    $scope.loadMonitorServer();

    $scope.addMonitorServer = function () {
        $http.post("/api/monitor",$scope.serverBean).success(function (response) {
            if(response.success){
                $scope.alertMessage(response.message);
                $scope.loadMonitorServer();
                $scope.serverBean = {"serverName":"","serverDomain":"","checkInterface":""};
            } else {
                $scope.alertMessage(response.message);
            }
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    }

    $scope.deleteMonitorServer = function (id) {
        $http.delete("/api/monitor/" + id).success(function (response) {
            if(response.success){
                $scope.alertMessage(response.message);
                $scope.loadMonitorServer();
            } else {
                $scope.alertMessage(response.message);
            }
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    }

    $scope.alertMessage = function(message){
        $scope.alertConfig.message = message;
        $scope.alertConfig.show = true;
        $timeout(function(){
            $scope.alertConfig.show = false;
        },2000);
    };
});