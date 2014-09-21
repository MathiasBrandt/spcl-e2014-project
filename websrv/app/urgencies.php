<?php
function listUrgencies() {
    echo Urgency::all()->toJson();
}
