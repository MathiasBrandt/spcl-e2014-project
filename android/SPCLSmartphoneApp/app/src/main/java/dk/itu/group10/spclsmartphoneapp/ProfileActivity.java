package dk.itu.group10.spclsmartphoneapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import dk.itu.group10.spclsmartphoneapp.common.Common;
import dk.itu.group10.spclsmartphoneapp.models.User;


public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        User user = Common.getUserFromPreferences(this);

        TextView tvNameValue = (TextView) findViewById(R.id.tvNameValue);
        TextView tvEmailValue = (TextView) findViewById(R.id.tvEmailValue);
        TextView tvPhoneValue = (TextView) findViewById(R.id.tvPhoneValue);

        tvNameValue.setText(user.getName());
        tvEmailValue.setText(user.getEmail());
        tvPhoneValue.setText(user.getPhone());
    }

    public void btnSwitchUserOnClick(View v) {
        Common.navigateToActivity(this, LoginExistingUserActivity.class, ProfileActivity.class, true);
    }

    public void btnLogoutOnClick(View v) {
        Common.removeUserInPreferences(this);
        Common.navigateToActivity(this, CreateUserActivity.class, false);
        finish();
    }
}
