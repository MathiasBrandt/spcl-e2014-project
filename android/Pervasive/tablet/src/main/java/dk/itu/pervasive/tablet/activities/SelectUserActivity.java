package dk.itu.pervasive.tablet.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.IOnCompleteListener;
import dk.itu.pervasive.common.User;
import dk.itu.pervasive.tablet.R;

public class SelectUserActivity extends Activity {
    public static final String TAG = "SelectUserActivity";
    ListView lstUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        getActionBar().hide();
        populateUserList();
    }

    private void populateUserList() {
        Common.getUsers(this, new IOnCompleteListener() {
            @Override
            public void onComplete(String jsonData) {
                List<User> users = Common.deserializeUsers(jsonData);
                ListAdapter adapter = new ArrayAdapter<User>(SelectUserActivity.this, android.R.layout.simple_list_item_1, users);

                lstUsers = (ListView) findViewById(R.id.lstUsers);
                lstUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = (User) parent.getItemAtPosition(position);
                        Common.saveUserToPreferences(SelectUserActivity.this, user);
                        SelectUserActivity.this.finish();
                    }
                });
                lstUsers.setAdapter(adapter);
            }
        });
    }
}
