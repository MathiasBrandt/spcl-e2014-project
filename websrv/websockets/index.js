var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var client = require('http');

// handle an incoming message and notify other clients about it
function handleMessage(name, data) {
    console.log('received ' + name + ' with ' + data);

    var path = '/';
    var method = 'GET';
    var body = '';
    var eventName = '';

    switch (name) {
        case 'setStatus':
            path = '/users/' + data.user_id + '/status';
            method = 'PUT';
            body = JSON.stringify({
                status_id: data.status_id
            });
            eventName = 'statusChanged';
            break;
        case 'addMessage':
            path = '/users/' + data.to_user_id + '/messages';
            method = 'POST';
            body = JSON.stringify(data);
            eventName = 'messageAdded';
            break;
        default:
            return;
    }

    var responseData = '';
    var request = client.request({
        hostname: 'localhost',
        port: 8888,
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
            console.log('emitting ' + eventName);
            io.emit(eventName, '');
        });
    });

    request.write(body);
    request.end();
}

// initialize server
io.on('connection', function(socket) {
    socket.on('setStatus', function(data) {
        var data = JSON.parse(data);
        if(!data)
            return;

        handleMessage('setStatus', data);
    });

    socket.on('addMessage', function(data) {
        var data = JSON.parse(data);
        if(!data)
            return;

        handleMessage('addMessage', data);
    });
});

http.listen(3000, function() {
    console.log('listening on *:3000');
});