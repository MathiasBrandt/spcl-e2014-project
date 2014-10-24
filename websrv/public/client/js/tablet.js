angular.module('spcl').controller('tabletCtrl', ['$scope', '$http', '$location', '$timeout', 'commonService', function($scope, $http, $location, $timeout, commonService) {
    $scope.refreshMessages = function() {
        $scope.messages = commonService.messages.query({id: $scope.userId});
    };

    $scope.refreshUser = function() {
        $scope.user = commonService.users.get({id: $scope.userId},
            function(data) {
                $scope.groups = [];
                angular.forEach(data.groups, function(group) {
                    $scope.groups.push(commonService.groups.get({id: group.id}));
                });
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

            // show flash message and hide it after 5 seconds
            if(angular.fromJson(data).user_id == $scope.user.id) {
                $scope.flashShown = true;
                $timeout(function() {
                    $scope.flashShown = false;
                }, 5000);
            }
        });

        $scope.refreshUser();
        $scope.refreshMessages();
    };

    $scope.exceptOwner = function(user) {
        return user.id != $scope.user.id;
    };

    $scope.userId = $location.search().user;
    $scope.user = {};
    $scope.messages = [];
    $scope.groups = [];
    $scope.flashShown = false;
    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;

    $scope.connect();
}]);