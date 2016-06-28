/**
 * Created by wkm on 16-6-10.
 */
toolApp.controller("toolController",function($scope,$http,$timeout){

    $scope.alertConfig = {"show":false,"message":""};

    $scope.inputData = {"content":"测试"};

    $scope.imageResource = {"src":"","index":0,"alt":"空"};

    $scope.showImageMessage = function (){
        $http.post("/api/qrcode",$scope.inputData).success(function(response){
            var result = response;
            if(result.success){
                $scope.imageResource.src = result.src;
                $scope.imageResource.alt = result.alt;
            }
            $scope.alertMessage(result.message);
        });
    };
    $scope.showImageMessage();

    $scope.alertMessage = function(message){
        $scope.alertConfig.message = message;
        $scope.alertConfig.show = true;
        $timeout(function(){
            $scope.alertConfig.show = false;
        },2000);
    };
});