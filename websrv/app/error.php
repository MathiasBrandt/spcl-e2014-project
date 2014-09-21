<?php
function showNotFound() {
    $app = Slim\Slim::getInstance();
    throw new Exception('Route not found');
}

function showError(Exception $e) {
    $app = Slim\Slim::getInstance();
    
    if(is_a($e, 'Illuminate\Database\Eloquent\ModelNotFoundException'))
        $app->halt(404, json_encode(array('message' => 'Not found')));
    
    $app->halt(500, json_encode(array('message' => $e->getMessage())));
}