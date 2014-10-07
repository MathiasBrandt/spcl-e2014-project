package dk.itu.pervasive.tablet.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.itu.pervasive.common.Common;
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
