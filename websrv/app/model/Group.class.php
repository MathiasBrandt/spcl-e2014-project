<?php
use Illuminate\Database\Eloquent\Model as Model;

class Group extends Model {
    public $timestamps = false;
    
    public function users() {
        return $this->belongsToMany('User');
    }
}