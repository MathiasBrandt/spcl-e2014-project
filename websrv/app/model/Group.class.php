<?php
use Illuminate\Database\Eloquent\Model as Model;

class Group extends Model {
    public $timestamps = false;
    
    public function users() {
        return $this->belongsToMany('User');
    }

    public function unreadMessages() {
        return $this->messages()->where('is_sent', false);
    }

    public function messages() {
        return $this->hasMany('Message', 'to_group_id');
    }
}