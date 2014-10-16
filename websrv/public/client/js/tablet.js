angular.module('spcl').controller('tabletCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
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
                $scope.refreshGroups();
            });
    };

    $scope.refreshGroups = function() {
        $scope.groups = [];
        angular.forEach($scope.user.groups, function(group) {
            $http.get('/groups/' + group.id)
                .success(function(data) {
                    $scope.groups.push(data);
                });
        });
    };

    $scope.connect = function() {
        $scope.socket = io.connect('http://localhost:3000');

        $scope.socket.on('statusChanged', function(data) {
            console.log('statusChanged');
            $scope.refreshUser();
        });

        $scope.socket.on('messageAdded', function(data) {
            console.log('statusChanged');
            $scope.refreshMessages();
        });
    };

    $scope.userId = $location.search().user;
    $scope.user = {};
    $scope.messages = [];
    $scope.groups = [];
    $scope.statuses = {
        AVAILABLE: 1,
        BUSY: 2,
        VERY_BUSY: 3
    };

    $scope.connect();
    $scope.refreshUser();
    $scope.refreshMessages();
}]);