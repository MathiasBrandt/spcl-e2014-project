package dk.itu.pervasive.interfaces;

import android.app.Fragment;

/**
 * Created by rnoe on 04/10/14.
 */
public interface FragmentCallback {

    public void callToExchangeFragment(Fragment fragment);

    public void callToRemoveFragment();

    public void closeActivity();
}
