package dk.itu.pervasive.tablet.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.User;
import dk.itu.pervasive.tablet.R;

public class MainActivity extends Activity {
    RelativeLayout userStateFrame;
    TextView userStateText;

    private Boolean derp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userStateFrame = (RelativeLayout) findViewById(R.id.frameUserState);
        userStateText = (TextView) findViewById(R.id.txtUserState);

        User user = Common.getUserFromPreferences(this);

        if(user == null) {
            // navigate to SelectUserActivity
            Common.navigateToActivity(this, SelectUserActivity.class);
        }
    }

    private void setState(int state) {
        switch(state) {
            case Common.USER_STATE_AVAILABLE:
                userStateFrame.setBackgroundColor(Color.GREEN);
                userStateText.setText(R.string.state_available);
                break;
            case Common.USER_STATE_BUSY:
                userStateFrame.setBackgroundColor(Color.RED);
                userStateText.setText(R.string.state_busy);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
