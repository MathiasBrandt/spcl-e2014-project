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
    
    $user = User::with('status', 'groups')->findOrFail($id);
    $status = Status::findOrFail($json['status_id']);
    $user->status()->associate($status);
    $user->save();
    echo $user->toJson();

    // prepare message for tablets
    // recipients: the user's own tablet and the tablet of every user in the user's groups
    $recipientIds = array($user->id);
    $groups = $user->groups()->with('users')->get();
    foreach($groups as $group) {
        foreach($group->users as $groupUser) {
            // add user id to recipients
            array_push($recipientIds, $groupUser->id);
        }
    }

    sendGoogleCloudMessage($user->toArray(), array_unique($recipientIds));
}

function createUser() {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());

    $user = new User($json);
    $user->save();

    echo $user->toJson();
}
