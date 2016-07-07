/**
 * Created by wkm on 16-6-10.
 */
ucloudApp.controller("UCloudController",function($scope,$http,$timeout){

    $scope.alertConfig = {"show":false,"message":""};

    $scope.cloudForm = {"urls":[],"file":true,"domainId":"","domain":""};

    $scope.cloudUrl = {"url":""};

    $scope.loadCloudDomain = function () {
        $http.get("/api/cloud").success(function (response) {
            if(response.RetCode == 0){
                $scope.domainSet = response.DomainSet;
                $scope.pageSize = $scope.domainSet.length % 5 == 0 ? $scope.domainSet.length / 5 : Math.floor($scope.domainSet.length / 5) + 1;
                $scope.currentIndex = 1;
                $scope.pages = [];
                $scope.pageItem = {"p":0,"index":0};
                for(var i =0;i < $scope.pageSize;i++){
                    $scope.pageItem.p = i + 1;
                    $scope.pageItem.index = i;
                    $scope.pages.push(JSON.parse(JSON.stringify($scope.pageItem)));
                }
                $scope.loadPage(1);
            }
        });
    }
    $scope.loadCloudDomain();

    $scope.loadPage = function (index) {
        if(index > $scope.pageSize){
            $scope.alertMessage("不存在的页码");return;
        }
        //$scope.domainShowSet = [];
        var c = (index - 1) * 5;
        var end = index * 5;
        if((c + 5) > $scope.domainSet.length){
            end = $scope.domainSet.length;
        }
        //$scope.alertMessage($scope.pageSize + "," + index + "," + c + "," + total);
        $scope.domainShowSet = $scope.domainSet.slice(c,end);
        $scope.currentIndex = index;
    }

    $scope.clearUCloudCache = function (){
        $http.post("/api/cloud",$scope.cloudForm).success(function(response){
            if(response.data){
                $scope.alertMessage(response.message + ";data:" + JSON.stringify(response.data));
            } else {
                $scope.alertMessage(response.message);
            }
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    };
    $scope.addUrl = function (){
        //$scope.cloudForm.urls.push(JSON.parse(JSON.stringify($scope.cloudUrl)));
        if(!$scope.cloudForm.domainId){
            $scope.alertMessage("清选择UCDNID");
            return;
        }
        $scope.addCloudCache();
    };

    $scope.switchCloud = function (domain){
        $scope.cloudForm.domain = domain;
        $scope.loadCloudUrl();
    }

    $scope.loadCloudUrl = function (){
        $http.get("/api/cloud/" + $scope.cloudForm.domainId).success(function(response){
            if(response.data){
                $scope.cloudForm.urls = response.data;
                $scope.alertMessage(response.message);
            } else {
                $scope.alertMessage(response.message);
            }
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    }

    $scope.addCloudCache = function (){
        $http.put("/api/cloud/" + $scope.cloudForm.domainId,$scope.cloudUrl).success(function(response){
            if(response.success){
                $scope.alertMessage(response.message);
                $scope.loadCloudUrl();
            } else {
                $scope.alertMessage(response.message);
            }
            $scope.cloudUrl.url = "";
        }).error(function(data,status) {
            $scope.alertMessage("data=" + data + ",status=" + status);
        });
    };

    $scope.deleteUrl = function (index){
        $scope.deleteCloudDomain(index);
    };

    $scope.deleteCloudDomain = function (index) {
        $http.delete("/api/cloud/" + $scope.cloudForm.urls[index].id).success(function (response) {
            if(response.success){
                $scope.alertMessage(JSON.stringify(response.message));
                $scope.loadCloudUrl();
            } else {
                $scope.alertMessage(response.message);
            }
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