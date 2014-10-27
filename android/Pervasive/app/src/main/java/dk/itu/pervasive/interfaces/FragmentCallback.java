package dk.itu.pervasive.interfaces;

import android.app.Fragment;
import android.content.Intent;

/**
 * Created by rnoe on 04/10/14.
 */
public interface FragmentCallback {

    public void callToExchangeFragment(Fragment fragment);

    public void callToRemoveFragment();

    public void closeActivity();

    public void createToast(String message);

    public Intent accessNFCIntent();

}
