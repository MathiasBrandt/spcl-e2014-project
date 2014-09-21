<?php
use Illuminate\Database\Eloquent\Model as Model;

class Status extends Model {
    public $timestamps = false;
    
    public function users() {
        return $this->hasMany('User');
    }
}