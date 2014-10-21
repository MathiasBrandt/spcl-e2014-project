angular.module('spcl').controller('tabletCtrl', ['$scope', '$http', '$location', '$timeout', function($scope, $http, $location, $timeout) {
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
            console.log('messageAdded');
            $scope.refreshMessages();

            // show flash message and hide it after 5 seconds
            if(angular.fromJson(data).user_id == $scope.user.id) {
                $scope.flashShown = true;
                $timeout(function() {
                    $scope.flashShown = false;
                }, 5000);
            }
        });
    };

    $scope.exceptOwner = function(user) {
        return user.id != $scope.user.id;
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
    $scope.urgencies = {
        LOW: 1,
        MEDIUM: 2,
        HIGH: 3
    };
    $scope.flashShown = false;

    $scope.connect();
    $scope.refreshUser();
    $scope.refreshMessages();
}]);