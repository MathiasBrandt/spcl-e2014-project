<?php
use Illuminate\Database\Eloquent\Model as Model;

class User extends Model {
    public $timestamps = false;
    protected $guarded = array('id');
    protected $hidden = array('password');
    
    public function status() {
        return $this->belongsTo('Status');
    }
    
    public function groups() {
        return $this->belongsToMany('Group');
    }
    
    public function messages() {
        return $this->hasMany('Message', 'to_user_id');
    }
    
    public function unreadMessages() {
        return $this->messages()->where('is_sent', false);
    }

    public function sentMessages() {
        return $this->hasMany('Message', 'from_user_id');
    }
}