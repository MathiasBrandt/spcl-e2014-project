package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class RoleChooserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
    }

    public void btnOfficeRoleOnClick(View v) {
        Intent i = new Intent(RoleChooserActivity.this, OfficeMainActivity.class);
        startActivity(i);
    }

    public void btnVisitorRoleOnClick(View v) {
        Intent i = new Intent(RoleChooserActivity.this, VisitorMainActivity.class);
        startActivity(i);
    }
}
