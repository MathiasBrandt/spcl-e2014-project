angular.module('spcl', ['ngResource'])
    .service('commonService', ['$resource', '$q', function($resource, $q) {
        var self = this;

        this.messages = $resource('/users/:id/messages');
        this.users    = $resource('/users/:id');
        this.groups   = $resource('/groups/:id');

        this.statuses = {
            AVAILABLE: 1,
            BUSY: 2,
            VERY_BUSY: 3
        };
        this.urgencies = {
            LOW: 1,
            MEDIUM: 2,
            HIGH: 3
        };
    }]);