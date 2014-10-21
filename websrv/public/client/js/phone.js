angular.module('spcl').controller('phoneCtrl', ['$scope', '$http', function($scope, $http) {
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
            user_id: $scope.userId,
            status_id: $scope.statusId
        });

        $scope.socket.emit('setStatus', json);
    };

    $scope.sendMessage = function() {
        var json = angular.toJson($scope.message);
        $scope.socket.emit('addMessage', json);
    };

    $scope.userId = 1;
    $scope.user = {};
    $scope.message = {};

    $scope.connect();
    $scope.refreshUser();
}]);