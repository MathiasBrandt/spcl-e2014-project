package dk.itu.pervasive.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.ndeftools.Record;
import org.ndeftools.wellknown.TextRecord;

import java.util.List;

import dk.itu.pervasive.R;
import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.User;
import dk.itu.pervasive.interfaces.FragmentCallback;

public class Message extends Fragment {

    private static final String TAG = "Message Fragment";
    private String tabletId;

    Button submitButton;
    Button cancelButton;
    EditText messageInput;
    RadioButton lowUrgencyRadio;
    RadioButton highUrgencyRadio;

    // Reference to call back to activity
    private FragmentCallback fragmentCallback;

    public static Message newInstance() {
        Message fragment = new Message();
        return fragment;
    }
    public Message() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallback = (FragmentCallback) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent NFCintent = fragmentCallback.accessNFCIntent();

            Parcelable[] messageFromTag = NFCintent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messageFromTag != null) {
                Log.d(TAG, "Found " + messageFromTag.length + " NDEF messages");

                // parse to records
                for (int i = 0; i < messageFromTag.length; i++) {
                    try {
                        List<Record> records = new org.ndeftools.Message((NdefMessage) messageFromTag[i]);

                        Log.d(TAG, "Found " + records.size() + " records in message " + i);

                        for (int k = 0; k < records.size(); k++) {
                            Log.d(TAG, " Record #" + k + " is of class " + records.get(k).getClass().getSimpleName());

                            Record record = records.get(k);

                            if (record instanceof TextRecord) {
                                TextRecord tRec = (TextRecord) record;
                                tabletId = tRec.getText().trim();
                                Log.i(TAG, "Text is " + tRec.getText());
                                fragmentCallback.createToast(tabletId);
                            }
                        }
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Some problem");
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
                int toUserId = Integer.parseInt(tabletId);
                Log.i(TAG, "toUserId= " + toUserId + "\n" + "user: " + user.getName());

                Common.addMessage(user.getId(), toUserId, null, urgency, messageInput.getText().toString().trim());
                fragmentCallback.createToast("Message was sent. See you later...");
                fragmentCallback.closeActivity();
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
