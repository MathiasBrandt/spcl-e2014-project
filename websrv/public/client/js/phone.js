angular.module('spcl').controller('phoneCtrl', ['$scope', '$location', '$timeout', 'commonService', function($scope, $location, $timeout, commonService) {
    $scope.refreshMessages = function() {
        commonService.messages.query({id: $scope.user.id, password: Sha256.hash($scope.password)}, function(data) {
            $scope.messages = data;
        });
    };

    $scope.refreshUser = function() {
        commonService.users.get({id: $scope.user.id}, function(data) {
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
            user_id: $scope.user.id,
            status_id: statusId,
            'spcl-password': Sha256.hash($scope.password)
        });

        $scope.socket.emit('setStatus', json);
    };

    $scope.authenticate = function() {
        commonService.login.save({}, {username: $scope.username, password: Sha256.hash($scope.password)}, function(data) {
            $scope.user = data;
            $scope.authenticated = true;
            $scope.connect();
        }, function() {
            $scope.user = {};
            $scope.password = null;
            $scope.authenticated = false;
            alert('Incorrect login, please try again');
        });
    };

    $scope.logout = function() {
        $scope.user = {};
        $scope.username = null;
        $scope.password = null;
        $scope.authenticated = false;
        $scope.socket.disconnect();
    };

    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;
    $scope.commonService = commonService;
    $scope.authenticated = false;
    $scope.user = {};
    $scope.username = null;
    $scope.password = null;
}]);