angular.module('spcl').controller('phoneCtrl', ['$scope', '$location', '$timeout', 'commonService', function($scope, $location, $timeout, commonService) {
    $scope.refreshMessages = function() {
        commonService.messages.query({id: $scope.userId}, function(data) {
            $scope.messages = data;
        });
    };

    $scope.refreshUser = function() {
        commonService.users.get({id: $scope.userId}, function(data) {
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
            console.log('messageAdded');
            $scope.refreshMessages();
        });

        $scope.socket.on('phoneUpdated', function() {
            window.location.reload();
        });

        $scope.refreshUser();
        $scope.refreshMessages();
    };

    $scope.setStatus = function(statusId) {
        if(!statusId) return;

        var json = angular.toJson({
            user_id: $scope.userId,
            status_id: statusId
        });

        $scope.socket.emit('setStatus', json);
    };

    $scope.sendMessage = function() {
        if(!$scope.user)
            return;

        $scope.message.from_user_id = $scope.userId;

        var json = angular.toJson($scope.message);
        $scope.socket.emit('addMessage', json);

        $scope.flashShown = true;
        $timeout(function() {
            $scope.flashShown = false;
        }, 2000);
    };

    $scope.userId = $location.search().user;
    $scope.message = {
        urgency_id: commonService.urgencies.LOW
    };
    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;
    $scope.commonService = commonService;

    $scope.connect();
}]);