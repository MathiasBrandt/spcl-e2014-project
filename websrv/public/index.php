<?php
require 'require.php';

// load db settings
$settings = decodeJsonOrFail(file_get_contents('../app/db.json'));

// set up eloquent
use Illuminate\Database\Capsule\Manager as Capsule;
$capsule = new Capsule();
$capsule->addConnection($settings);
$capsule->setAsGlobal();
$capsule->bootEloquent();

// set up slim
$app = new Slim\Slim(array(
    'debug' => true
));
$app->response->headers->set('Content-Type', 'application/json');

$app->get('/users', 'listUsers');
$app->get('/users/:id', 'getUser');
//$app->put('/users/:id', 'updateUser');
$app->put('/users/:id/status/:password', 'updateStatus');
$app->post('/users', 'createUser');
$app->post('/login', 'login');

$app->get('/groups', 'listGroups');
$app->get('/groups/:id', 'getGroup');
$app->put('/groups/:id', 'updateGroup');

$app->get('/users/:id/messages', 'listMessagesForUser');
$app->get('/users/:id/messages/:password', 'listMessagesForUser');
$app->post('/users/:id/messages', 'sendMessageToUser');
//$app->get('/groups/:id/messages', 'listMessagesForGroup');
//$app->post('/groups/:id/messages', 'sendMessageToGroup');

$app->get('/statuses', 'listStatuses');
$app->get('/urgencies', 'listUrgencies');

$app->notFound('showNotFound');
$app->error('showError');
$app->run();
