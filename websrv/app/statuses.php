<?php
function listStatuses() {
    echo Status::all()->toJson();
}
