package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import dk.itu.group10.spclsmartphoneapp.common.Common;
import dk.itu.group10.spclsmartphoneapp.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginAsExistingUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginAsExistingUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LoginAsExistingUserFragment extends Fragment {
    private static final String USERS_KEY = "Users";

    private String jsonUsers;
    private List<User> userList;
    private ListView lstUsers;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jsonUsers a json string representation of the users to show.
     * @return A new instance of fragment LoginAsExistingUserFragment.
     */
    public static LoginAsExistingUserFragment newInstance(String jsonUsers) {
        LoginAsExistingUserFragment fragment = new LoginAsExistingUserFragment();
        Bundle args = new Bundle();
        args.putString(USERS_KEY, jsonUsers);
        fragment.setArguments(args);
        return fragment;
    }
    public LoginAsExistingUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userList = Common.deserializeUsers(jsonUsers);
        User[] userArray = new User[userList.size()];
        userList.toArray(userArray);
        lstUsers = (ListView) getView().findViewById(R.id.lstUsers);

        ListAdapter adapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_list_item_1, userArray);
        lstUsers.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jsonUsers = getArguments().getString(USERS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_as_existing_user, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
