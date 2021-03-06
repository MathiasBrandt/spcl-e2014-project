package dk.rzs.mymodule.service;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    Callback mCallback;
    Button mLoginButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (Callback)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "LoginFragment");
        mCallback.createToast("Test callback interface");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login,
                container, false);
        mLoginButton = (Button) view.findViewById(R.id.LoginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start sensor service
                Intent i = new Intent(getActivity(), SensorService.class);
                getActivity().startService(i);

                // Implementation to save username, password and other info for server (how much info do we need?)

                // Implementation to POST information to server

                // open fragment to submit message to display (perhaps should just close fragment and only run app in BG until service detects NFC?)
                mCallback.changeFragment(new MessageFragment());
            }
        });
        return view;
    }





}
