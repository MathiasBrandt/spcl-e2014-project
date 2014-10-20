<?php
use Illuminate\Database\Eloquent\Model as Model;

class Message extends Model {
    protected $guarded = array('id', 'is_sent');
    
    public function user() {
        return $this->belongsTo('User', 'to_user_id');
    }
    
    public function group() {
        return $this->belongsTo('Group', 'to_group_id');
    }
    
    public function urgency() {
        return $this->belongsTo('Urgency');
    }

    public function sender() {
        return $this->belongsTo('User', 'from_user_id');
    }
}