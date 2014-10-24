angular.module('spcl').controller('phoneCtrl', ['$scope', '$location', 'commonService', function($scope, $location, commonService) {
    $scope.refreshUser = function() {
        $scope.user = commonService.users.get({id: $scope.userId});
    };

    $scope.connect = function() {
        $scope.socket = io.connect('http://' + window.location.hostname + ':3000');

        $scope.socket.on('statusChanged', function(data) {
            console.log('statusChanged');
            $scope.refreshUser();
        });

        $scope.refreshUser();
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
        var json = angular.toJson($scope.message);
        $scope.socket.emit('addMessage', json);
    };

    $scope.userId = $location.search().user;
    $scope.user = {};
    $scope.message = {
        from_user_id: $scope.userId,
        message: '',
        urgency_id: commonService.urgencies.LOW
    };
    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;

    $scope.connect();
}]);