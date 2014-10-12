angular.module('spcl').controller('phoneCtrl', ['$scope', '$interval', '$http', '$timeout', function($scope, $interval, $http, $timeout) {
    $scope.refreshUser = function() {
        $http.get('/users/' + $scope.userId)
            .success(function(data) {
                $scope.user = data;
            });
    };

    $scope.connect = function() {
        $scope.socket = io.connect('http://localhost:3000');
    };

    $scope.setStatus = function() {
        if(!$scope.statusId) return;

        var json = angular.toJson({
            userId: $scope.userId,
            statusId: $scope.statusId
        });
        $scope.socket.emit('setStatus', json);
    };

    $scope.userId = 1;
    $scope.user = {};

    $scope.connect();
    $scope.refreshUser();
}]);