/**
 * Created by wkm on 16-6-10.
 */
ucloudApp.controller("UCloudController",function($scope,$http,$timeout){

    $scope.alertConfig = {"show":false,"message":""};

    $scope.cloudForm = {"urls":[],"file":true,"domainId":""};

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
        $scope.cloudForm.urls.push(JSON.parse(JSON.stringify($scope.cloudUrl)));
        $scope.cloudUrl.url = "";
    };

    $scope.deleteUrl = function (index){
        $scope.alertMessage("remove:" + index + ",url=" + $scope.cloudForm.urls[index].url);
        $scope.cloudForm.urls.splice(index,1);
    };

    $scope.alertMessage = function(message){
        $scope.alertConfig.message = message;
        $scope.alertConfig.show = true;
        $timeout(function(){
            $scope.alertConfig.show = false;
        },2000);
    };
});