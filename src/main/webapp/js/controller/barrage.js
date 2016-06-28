/**
 * Created by wkm on 16-6-11.
 */
barrageFileApp.controller('BarrageFileController', ['$scope', '$http', '$timeout', 'Upload', function ($scope, $http, $timeout, Upload) {
    $scope.$watch('files', function () {
        $scope.upload($scope.files);
    });
    $scope.$watch('file', function () {
        if ($scope.file != null) {
            $scope.files = [$scope.file];
        }
    });
    $scope.log = '';
    $scope.movieId = 1;
    $scope.platformId = 1;

    $scope.upload = function (files) {
        if (files && files.length) {
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                if (!file.$error) {
                    Upload.upload({
                        url: '/api/upload',
                        data: {
                            movieId: $scope.movieId,
                            platformId: $scope.platformId,
                            barrageType: $scope.barrageType,
                            file: file
                        }
                    }).then(function (resp) {
                        $timeout(function() {
                            $scope.log = 'file: ' +
                                resp.config.data.file.name +
                                ', Response: ' + JSON.stringify(resp.data) +
                                '\n' + $scope.log;
                        });
                    }, null, function (evt) {
                        var progressPercentage = parseInt(100.0 *
                            evt.loaded / evt.total);
                        $scope.log = 'progress: ' + progressPercentage +
                            '% ' + evt.config.data.file.name + '\n' +
                            $scope.log;
                    });
                }
            }
        }
    };
    $scope.loadBarrageType = function () {
        $http.get('/api/upload/1').success(function (response) {
            if(response.success){
                $scope.barrageTypes = response.data;
                $scope.barrageType = $scope.barrageTypes[0].code;
            }
        });
    };
    $scope.loadBarrageType();
}]);