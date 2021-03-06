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
            if(angular.fromJson(data).user_id == $scope.user.id) {
                console.log('messageAdded');
                $scope.refreshMessages();

                if (typeof Android != 'undefined')
                    Android.messageReceived();
            }
        });

        $scope.socket.on('phoneUpdated', function() {
            window.location.reload();
        });

        $scope.refreshUser();
        $scope.refreshMessages();

        if (typeof Android != 'undefined')
            Android.startMainService();
    };

    $scope.setStatus = function(statusId) {
        if(!statusId) return;

        var json = angular.toJson({
            user_id: $scope.user.id,
            status_id: statusId,
            status_message: $scope.user.status_message,
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

        if (typeof Android != 'undefined')
            Android.stopMainService();
    };

    $scope.initializeMessageForm = function(toUserId) {
        commonService.users.get({id: toUserId}, function(receiver) {
            $scope.message = {
                to_user_id: receiver.id,
                from_user_id: $scope.user.id,
                message: '',
                urgency_id: commonService.urgencies.LOW
            };

            $scope.messageReceiver = receiver;
            $scope.showMessageForm = true;
        });
    };

    $scope.sendMessage = function() {
        var json = angular.toJson($scope.message);
        $scope.socket.emit('addMessage', json);
        $scope.showMessageForm = false;
    };

    $scope.cancelMessage = function() {
        $scope.showMessageForm = false;
    };

    $scope.setStatusMessage = function() {
        if(!$scope.user.status_message)
            return;

        $scope.setStatus($scope.user.status.id);
    };

    $scope.openChangePasswordForm = function() {
        $scope.newPassword = null;
        $scope.showChangePasswordForm = true;
    };

    $scope.updatePassword = function() {
        if(!$scope.newPassword)
            return;

        var json = angular.toJson({
            password: Sha256.hash($scope.newPassword)
        });

        commonService.users.update({id: $scope.user.id, password: Sha256.hash($scope.password)}, json, function() {
            $scope.newPassword = null;
            $scope.showChangePasswordForm = false;
        });
    };

    $scope.statuses = commonService.statuses;
    $scope.urgencies = commonService.urgencies;
    $scope.commonService = commonService;
    $scope.authenticated = false;
    $scope.user = {};
    $scope.username = null;
    $scope.password = null;
    $scope.showMessageForm = false;
    $scope.newPassword = null;
    $scope.showChangePasswordForm = false;

    // global function to be called from android
    window.nfcReceived = function(tagId) {
        $scope.initializeMessageForm(tagId);
    };
    window.phoneTurned = function() {
        $scope.setStatus(commonService.statuses.BUSY);
    };
    window.phoneShaken = function() {
        $scope.setStatus(commonService.statuses.AVAILABLE);
    };
}]);