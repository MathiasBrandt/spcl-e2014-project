package dk.itu.pervasive.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dk.itu.pervasive.R;
import dk.itu.pervasive.abstractClasses.SingleFragmentActivity;
import dk.itu.pervasive.fragments.Login;
import dk.itu.pervasive.interfaces.FragmentCallback;

public class Main extends SingleFragmentActivity implements FragmentCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    // This Abstract method is called on initialization
    // Uses fragment factory to instantiate login fragment (because always need to open)
    // Delegates fragment transaction to abstract class.
    @Override
    protected Fragment createFragment() {
        return Login.newInstance("login", "fragment");
    }

    // callback method from fragments, call abstract method to change fragment
    // delegates fragment transactions to abstract class
    @Override
    public void callToExchangeFragment(Fragment fragment) {
        changeFragment(fragment);
    }

    // callback method from fragments, call abstract method to remove fragment
    // delegates fragment transactions to abstract class
    @Override
    public void callToRemoveFragment() {
        removeFragment();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void createToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

}
