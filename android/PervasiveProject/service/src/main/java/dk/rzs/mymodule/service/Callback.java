package dk.rzs.mymodule.service;

import android.app.Fragment;

/**
 * Created by rnoe on 24/09/14.
 */

public interface Callback {
    void changeFragment(Fragment fragment);
    void createToast(String message);
}
