/**
 * Created by wkm on 16-6-9.
 */

modifyMenuApp.controller("modifyMenuController",function($scope,$http,$timeout){
    //init form
    $scope.itemForm = {"id":"","parentId":"","menuName":"","menuLink":""};
    //close message
    $scope.alertConfig = {"show":false,"message":""};
    //load root
    $scope.loadRootMenus = function () {
        $http.get('/api/menu/root').success(function(response){
            $scope.roots = response;
        });
    };
    //load all menu
    $scope.loadMenus = function () {
        $http.get('/api/menu/all').success(function(response){
            $scope.menus = response;
        });
    };

    //edit menu
    $scope.editMenuItem = function(itemId){
        $http.get('/api/menu/' + itemId).success(function(response){
            var result = response;
            if(result.success){
                $scope.itemForm = result.data;
            }
            $scope.alertMessage(result.message);
        });
    };

    $scope.cancelEditMenuItem = function(){
        $scope.itemForm = {"id":"","parentId":"","menuName":"","menuLink":""};
    };

    //add root menu
    $scope.addRootMenu = function(){
        $scope.itemForm = {"id":"","parentId":"root","menuName":"","menuLink":"rootLink"};
    };

    //add child menu
    $scope.addChildMenu = function(parentId){
        $scope.itemForm = {"id":"","parentId":"","menuName":"","menuLink":""};
        $scope.itemForm.parentId = parentId;
    };

    //save menu
    $scope.saveMenuItem = function(){
        if($scope.itemForm.id) {
            $http.put("/api/menu/" + $scope.itemForm.id,$scope.itemForm).success(function(response){
                if(response.success){
                    $scope.loadAllMenus();
                }
                $scope.alertMessage(response.message);
            }).error(function(data, status, headers, config){
                $scope.alertMessage(data + "" + status + "" + headers + "" + config);
            });
        } else {
            $http.post("/api/menu",$scope.itemForm).success(function(response){
                if(response.success){
                    $scope.loadAllMenus();
                }
                $scope.alertMessage(response.message);
            }).error(function(data, status, headers, config){
                $scope.alertMessage(data + "" + status + "" + headers + "" + config);
            });
        }
    };

    //delete menu with id
    $scope.deleteMenuItem = function(itemId){
        $http.delete("/api/menu/" + itemId).success(function(response){
            var result = response;
            if(result.success){
                $scope.loadAllMenus();
            }
            $scope.alertMessage(result.message);
        });
    };

    //load all menus
    $scope.loadAllMenus = function(){
        $scope.loadRootMenus();
        $scope.loadMenus();
    };

    $scope.loadAllMenus();

    $scope.alertMessage = function(message){
        $scope.alertConfig.message = message;
        $scope.alertConfig.show = true;
        $timeout(function(){
            $scope.alertConfig.show = false;
        },2000);
    };
});