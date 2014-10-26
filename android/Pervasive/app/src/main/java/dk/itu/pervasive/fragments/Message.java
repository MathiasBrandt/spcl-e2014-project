package dk.itu.pervasive.fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import dk.itu.pervasive.R;
import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Message#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Message extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button submitButton;
    Button cancelButton;
    EditText messageInput;
    RadioButton lowUrgencyRadio;
    RadioButton highUrgencyRadio;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static Message newInstance(String param1, String param2) {
        Message fragment = new Message();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Message() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        submitButton = (Button) view.findViewById(R.id.submit_message_button);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        messageInput = (EditText) view.findViewById(R.id.message_field);
        lowUrgencyRadio = (RadioButton) view.findViewById(R.id.low_urgency_radio);
        highUrgencyRadio = (RadioButton) view.findViewById(R.id.high_urgency_radio);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = Common.getUserFromPreferences(getActivity());

                if(user == null)
                    return;

                int urgency = highUrgencyRadio.isChecked() ? Common.URGENCY_HIGH : Common.URGENCY_LOW;
                int toUserId = 1; // get user id from tag instead

                Common.addMessage(user.getId(), toUserId, null, urgency, messageInput.getText().toString().trim());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close activity
                getActivity().finish();
            }
        });

        return view;
    }


}
