<?php

/**
 * recipients: the user and possibly every user in the user's groups
 */
function sendSyncMessage($userId, $sendToGroup = false) {
    $user = User::findOrFail($userId);
    $recipientIds = array($user->gcm_id);

    if($sendToGroup) {
        $groups = $user->groups()->with('users')->get();
        foreach ($groups as $group) {
            foreach ($group->users as $groupUser) {
                // add user's gcm registration id to recipients
                if ($groupUser->gcm_id)
                    array_push($recipientIds, $groupUser->gcm_id);
            }
        }
    }

    sendGoogleCloudMessage('refresh', array_unique($recipientIds));
}

function sendGoogleCloudMessage($collapseKey, array $recipientIds) {
    $apiKey = decodeJsonOrFail(file_get_contents('../app/gcm.json'))['apiKey'];
    $url = 'https://android.googleapis.com/gcm/send';
    $post = array(
        'registration_ids' => $recipientIds,
        'collapse_key'     => $collapseKey,
    );
    $headers = array(
        'Authorization: key=' . $apiKey,
        'Content-Type: application/json'
    );

    $options = array(
        'http' => array(
            'header'  => $headers,
            'method'  => 'POST',
            'content' => json_encode($post),
        ),
    );
    $context = stream_context_create($options);
    $result = file_get_contents($url, false, $context);
    //echo json_encode($post);
    //echo $result;
}