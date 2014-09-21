<?php
use Illuminate\Database\Eloquent\Model as Model;

class Urgency extends Model {
    public $timestamps = false;
    protected $guarded = array('id');

    public function messages() {
        return $this->hasMany('Message');
    }
}