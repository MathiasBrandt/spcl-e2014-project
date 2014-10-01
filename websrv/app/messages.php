<?php
function listMessagesForUser($id) {
    $user = User::findOrFail($id);
    $messages = $user->unreadMessages()->with('sender')->get();
    foreach($messages as $message) {
        $message->is_sent = true;
        $message->save();
    }
    echo $messages->toJson();
}

function listMessagesForGroup($id) {
    $group = Group::findOrFail($id);
    $messages = $group->unreadMessages()->with('sender')->get();
    foreach($messages as $message) {
        $message->is_sent = true;
        $message->save();
    }
    echo $messages->toJson();
}

function sendMessageToUser($id) {
    $app = Slim\Slim::getInstance();
    $user = User::findOrFail($id);
    $json = decodeJsonOrFail($app->request->getBody());
    
    $message = new Message($json);
    $message->user()->associate($user);
    $message->save();
    echo $message->toJson();
}

function sendMessageToGroup($id) {
    $app = Slim\Slim::getInstance();
    $group = Group::findOrFail($id);
    $json = decodeJsonOrFail($app->request->getBody());
    
    $message = new Message($json);
    $message->group()->associate($group);
    $message->save();
    echo $message->toJson();
}