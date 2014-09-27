package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import dk.itu.group10.spclsmartphoneapp.common.Common;
import dk.itu.group10.spclsmartphoneapp.models.User;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = Common.getUserFromPreferences(MainActivity.this);
        TextView tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvTemp.setText(String.format("LOGGED IN AS: %s (%d), %s, %s", user.getName(), user.getId(), user.getEmail(), user.getPhone()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_switch_user:
                Common.removeUserInPreferences(this);
                Common.navigateToActivity(this, CreateUserActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
