<?php

function sendGoogleCloudMessage(array $data, array $recipientIds) {
    $apiKey = decodeJsonOrFail(file_get_contents('../app/gcm.json'))['apiKey'];
    $url = 'https://android.googleapis.com/gcm/send';
    $post = array(
        'registration_ids' => $recipientIds,
        'data'             => $data,
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