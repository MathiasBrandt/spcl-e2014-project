package dk.itu.pervasive.tablet.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.IOnCompleteListener;
import dk.itu.pervasive.common.User;
import dk.itu.pervasive.tablet.R;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    RelativeLayout userStateFrame;
    TextView userStateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userStateFrame = (RelativeLayout) findViewById(R.id.frameUserState);
        userStateText = (TextView) findViewById(R.id.txtUserState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        User user = Common.getUserFromPreferences(this);

        if(user == null) {
            // navigate to SelectUserActivity
            Common.navigateToActivity(this, SelectUserActivity.class);
        } else {
            setUserName(user.getName());
            setUserState(user.getStatusId());
        }

        populateGroupMembers();
    }

    private void populateGroupMembers() {
        Common.getUsers(this, new IOnCompleteListener() {
            @Override
            public void onComplete(String jsonData) {
                List<User> users = Common.deserializeUsers(jsonData);
                List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

                for(User u : users) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("NAME", u.getName());
                    map.put("STATUS", "" + u.getStatusId());
                    data.add(map);
                }

                String[] from = new String[] { "NAME", "STATUS" };
                int[] to = new int[] { R.id.derp_name, R.id.derp_status };

                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data, R.layout.group_grid_item, from, to);


                GridView grid = (GridView) findViewById(R.id.gridGroupMembers);
                grid.setNumColumns(2);
                grid.setAdapter(adapter);
            }
        });
    }

    private void setUserName(String userName) {
        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(String.format("%s is", userName));
    }

    private void setUserState(int state) {
        ActionBar actionBar = getActionBar();
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        RelativeLayout frameBottomUI = (RelativeLayout) findViewById(R.id.frameBottomUI);

        switch(state) {
            case Common.USER_STATE_AVAILABLE:
                ColorDrawable greenDrawable = new ColorDrawable(getResources().getColor(R.color.state_color_green));
                ColorDrawable darkGreenDrawable = new ColorDrawable(getResources().getColor(R.color.state_color_dark_green));

                userStateFrame.setBackground(darkGreenDrawable);
                userStateText.setText(R.string.state_available);

                actionBar.setBackgroundDrawable(greenDrawable);
                rootLayout.setBackground(greenDrawable);
                frameBottomUI.setBackground(darkGreenDrawable);

                break;
            case Common.USER_STATE_BUSY:
                ColorDrawable redDrawable = new ColorDrawable(getResources().getColor(R.color.state_color_red));
                ColorDrawable darkRedDrawable = new ColorDrawable(getResources().getColor(R.color.state_color_dark_red));

                userStateFrame.setBackground(darkRedDrawable);
                userStateText.setText(R.string.state_busy);

                actionBar.setBackgroundDrawable(redDrawable);
                rootLayout.setBackground(redDrawable);
                frameBottomUI.setBackground(darkRedDrawable);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_logout) {
            Common.removeUserInPreferences(this);
            Common.navigateToActivity(this, SelectUserActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
