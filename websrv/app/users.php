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

function updateUser($id, $password) {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());

    $user = User::with('status', 'groups')->findOrFail($id);

    // authenticate
    if($user->password !== $password)
        throw new Exception();

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

    $statusId = $json['status_id'];
    $statusMessage = $json['status_message'];

    $status = Status::findOrFail($statusId);
    $user->status()->associate($status);
    $user->status_message = $statusMessage;
    $user->save();

    $statusChange = new StatusChange();
    $statusChange->user()->associate($user);
    $statusChange->status()->associate($status);
    $statusChange->status_message = $statusMessage;
    $statusChange->save();

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
