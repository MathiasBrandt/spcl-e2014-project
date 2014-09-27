package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import dk.itu.group10.spclsmartphoneapp.common.Common;


public class LoginActivity extends Activity implements LoginAsExistingUserFragment.OnFragmentInteractionListener {
    private static final String TAG = "LoginActivity";

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: DEBUGGING REMOVE THIS
        Common.saveUserIdToPreferences(this, Common.DEFAULT_USER_ID);

        // check preferences for user id
        int userId = Common.getUserIdFromPreferences(this);

        // if a user id was found, navigate to main activity
        if(userId != Common.DEFAULT_USER_ID) {
            Common.navigateToMainActivity(this);
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
    }

    private void loginAsExistingUser() {
        // build UI
        // get users
        // show users in UI
        // on selection, navigate to MainActivity
    }

    private void getUsers() {
        IOnCompleteListener onCompleteListener = new IOnCompleteListener() {
            @Override
            public void onComplete(String jsonData) {
                // hide create user layout
                RelativeLayout createUserLayout = (RelativeLayout) findViewById(R.id.createUserLayoutContainer);
                createUserLayout.setVisibility(View.INVISIBLE);

                // show existing user layout
                RelativeLayout existingUserLayout = (RelativeLayout) findViewById(R.id.existingUserLayoutContainer);
                existingUserLayout.setVisibility(View.VISIBLE);

                LoginAsExistingUserFragment fragment = LoginAsExistingUserFragment.newInstance(jsonData);
                getFragmentManager().beginTransaction().add(R.id.existingUserLayoutContainer, fragment, "").commit();
            }
        };

        Common.getUsers(this, onCompleteListener);
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
        getUsers();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
