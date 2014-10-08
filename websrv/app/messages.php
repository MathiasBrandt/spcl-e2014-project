<?php

/**
 * Lists all unread messages for the user and its groups, marking the messages read in the process.
 * @param $id The user id
 */
function listMessagesForUser($id) {
    $user = User::with('messages.sender', 'groups.messages.sender')->findOrFail($id);
    $userMessages = $user->unreadMessages()->with('sender')->get();
    foreach($userMessages as $message) {
        $message->is_sent = true;
        $message->save();
    }

    $groups = $user->groups;
    $groupMessages = array();
    foreach($groups as $group) {
        $messages = $group->unreadMessages()->with('sender', 'group')->get();
        foreach($messages as $message) {
            $message->is_sent = true;
            $message->save();
        }
        $groupMessages = array_merge($groupMessages, $messages->toArray());
    }

    echo json_encode(array_merge($userMessages->toArray(), $groupMessages));
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

    sendSyncMessage($user->id, false);
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