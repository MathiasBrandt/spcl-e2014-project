package dk.itu.pervasive.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dk.itu.pervasive.R;
import dk.itu.pervasive.abstractClasses.SingleFragmentActivity;
import dk.itu.pervasive.fragments.Message;
import dk.itu.pervasive.interfaces.FragmentCallback;

public class MessageActivity extends SingleFragmentActivity implements FragmentCallback {

    @Override
    protected Fragment createFragment() {
       return Message.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message, menu);
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

    @Override
    public void callToExchangeFragment(Fragment fragment) {

    }

    @Override
    public void callToRemoveFragment() {

    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void createToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Intent accessNFCIntent() {
        return getIntent();
    }
}
