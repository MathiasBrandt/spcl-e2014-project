package dk.itu.pervasive.abstractClasses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import dk.itu.pervasive.R;


/**
 * Created by rnoe on 24/09/14.
 */
public abstract class SingleFragmentActivity extends Activity {
    private FragmentManager fm = getFragmentManager();

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        fragmentTransaction(fragment);
    }

    protected void changeFragment(Fragment fragment) {
        fragmentTransaction(fragment);
    }

    protected void removeFragment() {
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        fm.beginTransaction().remove(fragment).commit();
    }

    private void fragmentTransaction(Fragment fragment) {
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        } else {
            // use replace method if a fragment already exists
            fm.beginTransaction()
                    // enable back button
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
