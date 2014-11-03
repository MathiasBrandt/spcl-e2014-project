<?php
use Illuminate\Database\Eloquent\Model as Model;

class StatusChange extends Model {
    protected $guarded = array('id');
    
    public function user() {
        return $this->belongsTo('User', 'user_id');
    }
    
    public function status() {
        return $this->belongsTo('Status', 'status_id');
    }
}