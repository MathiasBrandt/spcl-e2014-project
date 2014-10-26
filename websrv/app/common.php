<?php

const SPCL_STATUS_AVAILABLE = 1;
const SPCL_STATUS_BUSY = 2;

const SPCL_URGENCY_LOW = 1;
const SPCL_URGENCY_HIGH = 2;

/**
 * Decodes a json string to an associative array or throws an exception.
 * @param string $json
 * @return array
 * @throws Exception
 */
function decodeJsonOrFail($json) {
    $decoded = json_decode($json, true);
    
    if(!$decoded)
        throw new Exception('Could not deserialize JSON: ' . $json);
    
    return $decoded;
}
