var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var client = require('http');

// handle an incoming message and notify other clients about it
function handleMessage(name, data) {
    console.log('received ' + name + ' with ' + JSON.stringify(data));

    var path = '/';
    var method = 'GET';
    var body = '';
    var eventName = '';
    var eventBody = '';

    switch (name) {
        case 'setStatus':
            path = '/users/' + data.user_id + '/status';
            method = 'PUT';
            body = JSON.stringify({
                status_id: data.status_id
            });
            eventName = 'statusChanged';
            eventBody = JSON.stringify({
                user_id: data.user_id
            });
            break;
        case 'addMessage':
            path = '/users/' + data.to_user_id + '/messages';
            method = 'POST';
            body = JSON.stringify(data);
            eventName = 'messageAdded';
            eventBody = JSON.stringify({
                user_id: data.to_user_id
            });
            break;
        default:
            return;
    }

    var responseData = '';
    var request = client.request({
        hostname: 'localhost',
        port: 80,
        path: path,
        method: method
    }, function(response) {
        response.setEncoding('utf8');
        response.on('data', function(chunk) {
            responseData += chunk;
        });
        response.on('error', function() {
            console.error('error in api request');
        });
        response.on('end', function() {
            console.log('emitting ' + eventName + ' with ' + eventBody);
            io.emit(eventName, eventBody);
        });
    });

    request.on('error', function() {
        console.error('error in api request');
    });
    request.write(body);
    request.end();
}

// initialize server
io.on('connection', function(socket) {
    socket.on('setStatus', function(data) {
        try {
            var data = JSON.parse(data);
            if (!data)
                return;

            handleMessage('setStatus', data);
        } catch (err) {
            console.error('error thrown by setStatus: ' + err.toString());
        }
    });

    socket.on('addMessage', function(data) {
        try {
            var data = JSON.parse(data);
            if (!data)
                return;

            handleMessage('addMessage', data);
        } catch (err) {
            console.error('error thrown by addMessage: ' + err.toString());
        }
    });
});

http.listen(3000, function() {
    console.log('listening on *:3000');
});