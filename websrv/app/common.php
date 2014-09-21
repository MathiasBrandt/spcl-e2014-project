<?php
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
