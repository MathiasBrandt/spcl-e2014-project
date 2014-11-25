angular.module('spcl').controller('tagCtrl', ['$scope', '$http', '$location', '$timeout', 'commonService', function($scope, $http, $location, $timeout, commonService) {
    $scope.refreshUser = function() {
        commonService.users.get({id: $scope.userId},
            function(data) {
                $scope.user = data;
            });
    };

    $scope.connect = function() {
        $scope.socket = io.connect('http://' + window.location.hostname + ':3000');

        $scope.socket.on('statusChanged', function(data) {
            console.log('statusChanged');
            $scope.refreshUser();
        });

        $scope.socket.on('messageAdded', function(data) {
            if(angular.fromJson(data).user_id == $scope.user.id) {
                console.log('messageAdded');

                // show flash message and hide it after 5 seconds
                $scope.$apply(function() {
                    $scope.flashShown = true;
                    $timeout(function() {
                        $scope.flashShown = false;
                    }, 5000);
                });
            }
        });

        $scope.refreshUser();
    };

    $scope.sendMessage = function() {
        if(!$scope.user)
            return;

        $scope.message.to_user_id = $scope.userId;

        var json = angular.toJson($scope.message);
        $scope.socket.emit('addMessage', json);

        $scope.cancelMessage();
    };

    $scope.cancelMessage = function() {
        $scope.message = {
            urgency_id: commonService.urgencies.LOW
        };
    };

    $scope.userId = $location.search().user;
    $scope.user = {};
    $scope.flashShown = false;
    $scope.message = {
        urgency_id: commonService.urgencies.LOW
    };
    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;
    $scope.commonService = commonService;

    $scope.connect();
}]);