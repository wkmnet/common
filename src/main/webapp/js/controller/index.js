/**
 * Created by wkm on 16-6-9.
 */

indexApp.controller("loadMenuController",function($scope,$http){
    $http.get('/api/menu/all').success(function(response){
        $scope.menus = response;
    });
});
