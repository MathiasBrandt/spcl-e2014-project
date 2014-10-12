angular.module('spcl').controller('tabletCtrl', ['$scope', '$interval', '$http', '$timeout', function($scope, $interval, $http, $timeout) {
    $scope.refreshMessages = function() {
        $http.get('/users/' + $scope.userId + '/messages')
            .success(function(data) {
                //$scope.messages = $scope.messages.concat(data);
                $scope.messages = data;
            });
    };

    $scope.refreshUser = function() {
        $http.get('/users/' + $scope.userId)
            .success(function(data) {
                $scope.user = data;
            });
    };

    $scope.connect = function() {
        $scope.socket = io.connect('http://localhost:3000');
        $scope.socket.on('statusChanged', function(data) {
            console.log('statusChanged: ' + data);

            $scope.$apply(function() {
                $scope.user = angular.fromJson(data);
            });
        });
    };

    $scope.userId = 1;
    $scope.user = {};
    $scope.messages = [];

    $scope.connect();
    $scope.refreshUser();
    /*$interval(function() {
        $scope.refreshMessages();
    }, 2000);*/
}]);