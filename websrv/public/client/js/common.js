angular.module('spcl', ['ngResource'])
    .service('commonService', ['$resource', '$q', function($resource, $q) {
        var self = this;

        this.messages = $resource('/users/:id/messages/:password');
        this.users    = $resource('/users/:id/:password', {}, {
            update: {
                method: 'PUT'
            }
        });
        this.groups   = $resource('/groups/:id');
        this.login    = $resource('/login');

        this.statuses = {
            AVAILABLE: 1,
            BUSY: 2
        };
        this.urgencies = {
            LOW: 1,
            HIGH: 2
        };

        this.stringToDate = function(str) {
            return new Date(str);
        };
    }]);