var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var client = require('http');

io.on('connection', function(socket) {
    socket.on('setStatus', function(data) {
        if(!data) return;

        var data = JSON.parse(data);

        var responseData = '';

        var request = client.request({
            hostname: 'localhost',
            port: 8888,
            path: '/users/' + data.userId + '/status',
            method: 'PUT'
        }, function(response) {
            response.setEncoding('utf8');
            response.on('data', function(chunk) {
                responseData += chunk;
            });
            response.on('error', function() {
                console.errro('error in api request');
            });
            response.on('end', function() {
                console.log('emitting statusChanged with ' + responseData);
                io.emit('statusChanged', responseData);
            });
        });

        request.write(JSON.stringify({
            status_id: data.statusId
        }));
        request.end();
    });
});

http.listen(3000, function() {
    console.log('listening on *:3000');
});