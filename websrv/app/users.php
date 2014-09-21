<?php
function listUsers() {
    $users = User::all();
    echo $users->toJson();
}

function getUser($id) {
    $user = User::with(array('status', 'groups'))->findOrFail($id);
    echo $user->toJson();
}

function updateUser($id) {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());
    
    $user = User::findOrFail($id);
    $user->update($json);
    echo $user->toJson();
}

function updateStatus($id) {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());
    
    $user = User::findOrFail($id);
    $status = Status::findOrFail($json['status_id']);
    $user->status()->associate($status);
    $user->save();
    echo $user->toJson();
}