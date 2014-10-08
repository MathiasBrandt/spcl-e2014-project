angular.module('spcl').controller('tabletCtrl', ['$scope', '$interval', '$http', function($scope, $interval, $http) {
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

    $scope.userId = 1;
    $scope.user = {};
    $scope.messages = [];

    $scope.refreshUser();
    $interval(function() {
        $scope.refreshMessages();
    }, 2000);
}]);