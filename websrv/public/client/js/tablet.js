angular.module('spcl').controller('tabletCtrl', ['$scope', '$http', '$location', '$timeout', 'commonService', function($scope, $http, $location, $timeout, commonService) {
    $scope.refreshMessages = function() {
        commonService.messages.query({id: $scope.userId}, function(data) {
            $scope.messages = data;
        });
    };

    $scope.refreshUser = function() {
        commonService.users.get({id: $scope.userId},
            function(data) {
                $scope.user = data;

                var groups = [];
                angular.forEach(data.groups, function(group) {
                    groups.push(commonService.groups.get({id: group.id}));
                });

                $scope.groups = groups;
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
                $scope.refreshMessages();

                // show flash message and hide it after 5 seconds
                $scope.flashShown = true;
                $timeout(function() {
                    $scope.flashShown = false;
                }, 5000);
            }
        });

        $scope.socket.on('tabletUpdated', function() {
            window.location.reload();
        });

        $scope.refreshUser();
        $scope.refreshMessages();
    };

    $scope.exceptOwner = function(user) {
        return user.id != $scope.user.id;
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
        $scope.formShown = false;
        $scope.message = {
            urgency_id: commonService.urgencies.LOW
        };
    };

    $scope.userId = $location.search().user;
    $scope.user = {};
    $scope.messages = [];
    $scope.groups = [];
    $scope.flashShown = false;
    $scope.formShown = false;
    $scope.message = {
        urgency_id: commonService.urgencies.LOW
    };
    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;
    $scope.commonService = commonService;

    $scope.connect();
}]);