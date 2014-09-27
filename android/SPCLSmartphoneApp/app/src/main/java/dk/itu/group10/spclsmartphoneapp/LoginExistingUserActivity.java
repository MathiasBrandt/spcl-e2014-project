package dk.itu.group10.spclsmartphoneapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import dk.itu.group10.spclsmartphoneapp.common.Common;
import dk.itu.group10.spclsmartphoneapp.models.User;


public class LoginExistingUserActivity extends Activity {
    private static final String TAG = "LoginExistingUserActivity";

    private ListView lstExistingUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_existing_user);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        lstExistingUsers = (ListView) findViewById(R.id.lstExistingUsers);
        lstExistingUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                Common.saveUserToPreferences(LoginExistingUserActivity.this, user);
                Common.navigateToActivity(LoginExistingUserActivity.this, MainActivity.class, false);
            }
        });

        // get existing users from server and add them to the list
        Common.getUsers(this, new IOnCompleteListener() {
            @Override
            public void onComplete(String jsonData) {
                List<User> existingUsers = Common.deserializeUsers(jsonData);
                User[] existingUsersArray = new User[existingUsers.size()];
                existingUsers.toArray(existingUsersArray);

                ListAdapter adapter = new ArrayAdapter<User>(LoginExistingUserActivity.this, android.R.layout.simple_list_item_1, existingUsersArray);
                lstExistingUsers.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Navigate to the correct parent when touching the home/up button
            case android.R.id.home:
                Class<?> parent = (Class<?>) getIntent().getExtras().get(Common.PARENT_KEY);
                Common.navigateToActivity(this, parent, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
