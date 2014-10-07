package dk.itu.pervasive.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import dk.itu.pervasive.R;
import dk.itu.pervasive.interfaces.FragmentCallback;
import dk.itu.pervasive.common.User;
import dk.itu.pervasive.common.Common;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Login extends Fragment {
    private static final String TAG = "Login";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Reference to activity
    FragmentCallback fragmentCallback;

    // UI
    Button loginButton;
    Button createUser;
    Button logoutButton;
    EditText inputNameField;
    EditText inputEmailField;
    EditText inputPhoneField;




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Common.setContext(activity);
        fragmentCallback = (FragmentCallback)activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // ================================
        // MATHIAS login details goes here!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        setupUiFields(view);
        checkGooglePlayServices();
        return view;
    }

    // Call back to activity to change fragment.
    private void changeFragment(Fragment fragment){
        fragmentCallback.callToExchangeFragment(fragment);
    }

    private void setupUiFields(View view) {
        loginButton = (Button) view.findViewById(R.id.login_button);
        createUser = (Button) view.findViewById(R.id.create_user_button);
        logoutButton = (Button) view.findViewById(R.id.logout_button);
        inputNameField = (EditText) view.findViewById(R.id.set_name);
        inputEmailField = (EditText) view.findViewById(R.id.set_email);
        inputPhoneField = (EditText) view.findViewById(R.id.set_phone_number);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if login information exists
                // check preferences for user
                User user = Common.getUserFromPreferences(getActivity());

                // if a user id was found
                if (user != null) {
                    Common.startMainService(getActivity());

                    // closes activity --> service keeps running
                    fragmentCallback.closeActivity();
                } else {

                    // If login doesn't exist animate login button out
                    Animation loginButtonOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.login_button_out);
                    Animation logoutButtonOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.logout_button_out);
                    loginButton.startAnimation(loginButtonOutAnimation);
                    loginButton.setVisibility(View.INVISIBLE);
                    logoutButton.startAnimation(logoutButtonOutAnimation);
                    logoutButton.setVisibility(View.INVISIBLE);

                    // animate text input fields onto view.
                    Animation loginFieldsAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.login_fields_animation);
                    // set views to visible
                    inputNameField.setVisibility(View.VISIBLE);
                    inputEmailField.setVisibility(View.VISIBLE);
                    inputPhoneField.setVisibility(View.VISIBLE);
                    createUser.setVisibility(View.VISIBLE);

                    // start animtaion on views (defined in res/anim folder)
                    inputNameField.startAnimation(loginFieldsAnimation);
                    inputEmailField.startAnimation(loginFieldsAnimation);
                    inputPhoneField.startAnimation(loginFieldsAnimation);
                    createUser.startAnimation(loginFieldsAnimation);

                }
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fetch variables from text fields check if empty
                if (isEmpty(inputEmailField) || isEmpty(inputNameField) || isEmpty(inputPhoneField)) {
                    fragmentCallback.createToast("You must fill out all fields...");
                } else {
                    String name = inputNameField.getText().toString().trim();
                    String phone = inputEmailField.getText().toString().trim();
                    String email = inputPhoneField.getText().toString().trim().toLowerCase();

                    Common.createUser(getActivity(), name, phone, email);

                    // if user now exists close activity

                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.removeUserInPreferences(getActivity());
            }
        });
    }

    private void checkGooglePlayServices() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if(result != ConnectionResult.SUCCESS) {
            loginButton.setEnabled(false);
            GooglePlayServicesUtil.getErrorDialog(result, getActivity(), 0).show();
        }
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
