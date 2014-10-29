<?php
function listUsers() {
    $users = User::all();
    echo $users->toJson();
}

function getUser($id) {
    $user = User::with(array('status', 'groups'))->findOrFail($id);

    echo $user->toJson();
}

function login() {
    $app = \Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());
    $user = User::where('username', '=', $json['username'])->where('password', '=', $json['password'])->firstOrFail();

    getUser($user->id);
}

function updateUser($id) {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());
    
    $user = User::findOrFail($id);
    $user->update($json);
    echo $user->toJson();
}

function updateStatus($id, $password) {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());
    
    $user = User::with('status', 'groups')->findOrFail($id);

    // authenticate
    if($user->password !== $password)
        throw new Exception();

    $status = Status::findOrFail($json['status_id']);
    $user->status()->associate($status);
    $user->save();
    echo $user->toJson();

    //sendSyncMessage($user->id, true);
}

function createUser() {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());

    $user = new User($json);
    $user->save();

    echo $user->toJson();
}
