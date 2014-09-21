<?php
function listGroups() {
    $groups = Group::all();
    echo $groups->toJson();
}

function getGroup($id) {
    $group = Group::with('users')->findOrFail($id);
    echo $group->toJson();
}

function updateGroup($id) {
    $app = Slim\Slim::getInstance();
    $json = decodeJsonOrFail($app->request->getBody());
    
    $group = Group::findOrFail($id);
    $group->update($json);
    echo $group->toJson();
}