package dk.itu.group10.spclsmartphoneapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import dk.itu.group10.spclsmartphoneapp.common.Common;
import dk.itu.group10.spclsmartphoneapp.models.User;


public class CreateUserActivity extends Activity {
    private static final String TAG = "CreateUserActivity";

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

        // TODO: DEBUGGING REMOVE THIS
        //Common.saveUserToPreferences(this, Common.DEFAULT_USER_ID);

        // check preferences for user
        User user = Common.getUserFromPreferences(this);

        // if a user id was found, navigate to main activity
        if(user != null) {
            Common.navigateToActivity(this, MainActivity.class, false);
            finish();
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
    }

    /***
     * Click handler for the "Create User" button.
     * @param v
     */
    public void btnCreateUserOnClick(View v) {
        // fetch variables from text fields
        String name = txtName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String email = txtEmail.getText().toString().trim().toLowerCase();

        Common.createUser(this, name, phone, email);
    }

    /***
     * Click handler for the "Login With Existing User" button.
     * @param v
     */
    public void btnLoginExistingUserOnClick(View v) {
        //TODO: false => true?
        Common.navigateToActivity(this, LoginExistingUserActivity.class, CreateUserActivity.class, false);
    }


}
