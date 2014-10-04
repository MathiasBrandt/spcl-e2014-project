package dk.itu.pervasive.fragments;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.itu.pervasive.R;
import dk.itu.pervasive.interfaces.FragmentCallback;
import dk.itu.pervasive.services.MainService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Login extends Fragment {
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        return view;
    }

    // Call back to activity to change fragment.
    private void changeFragment(Fragment fragment){
        fragmentCallback.callToExchangeFragment(fragment);
    }

    private void setupUiFields(View view) {
        loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if login information exists
                // connect to server...

                // If login exists --> start service --> close fragment
                if (true) {
                    Intent serveviceIntent = new Intent(getActivity(), MainService.class);
                    getActivity().startService(serveviceIntent);

                    // closes activity --> service keeps running
                    fragmentCallback.closeActivity();
                }

                // If login doesn't exist --> show text input fields
                if (false) {


                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
