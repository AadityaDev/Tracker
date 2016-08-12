package com.skybee.tracker.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.skybee.tracker.Factory;
import com.skybee.tracker.R;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.adapters.UserCardAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminFeed.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminFeed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFeed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private ProgressDialog progressDialog;
    private String message;
    private Handler handler;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView employeeCards;
    private LinearLayoutManager linearLayoutManager;
    private List<User> employeeCardList;
    private UserCardAdapter employeeCardAdapter;
    private User user;

    public AdminFeed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFeed.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFeed newInstance(String param1, String param2) {
        AdminFeed fragment = new AdminFeed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserStore userStore = new UserStore(getContext());
        user = new User();
        user = userStore.getUserDetails();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        View view = inflater.inflate(R.layout.fragment_admin_feed, container, false);
        employeeCards = (RecyclerView) view.findViewById(R.id.employee_list);
        employeeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        employeeCards.setLayoutManager(linearLayoutManager);
        employeeCardList = new ArrayList<User>();
        employeeCardAdapter = new UserCardAdapter(employeeCardList);
        employeeCards.setAdapter(employeeCardAdapter);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
            }
        };
        getEmployeeList();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getEmployeeList() {
        ListenableFuture<JSONObject> getEmployees = Factory.getUserService().employeeList(API.EMPLOYEE_LIST, user);
        Futures.addCallback(getEmployees, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d(TAG, result.toString());
                try {
                    if (result.has(Constants.JsonConstants.ERROR)) {
                        if (result.has(Constants.JsonConstants.MESSAGE) && !result.isNull(Constants.JsonConstants.MESSAGE)) {
                            message = result.getString(Constants.JsonConstants.MESSAGE);
                            errorDialog = new ErrorDialog(getContext(), message);
                            errorDialog.show();
                        }
                    }
                    if (result.has(Constants.JsonConstants.DATA)) {
                        JSONArray resultEmployeeList = new JSONArray();
                        resultEmployeeList = result.getJSONArray(Constants.JsonConstants.DATA);
                        for (int i = 0; i < resultEmployeeList.length(); i++) {
                            JSONObject employeeJsonObject = resultEmployeeList.getJSONObject(i);
                            if (employeeJsonObject != null) {
                                User user = new User();
                                if (employeeJsonObject.has(Constants.JsonConstants.EMAIL))
                                    user.setUserEmail(employeeJsonObject.getString(Constants.JsonConstants.EMAIL));
                                if (employeeJsonObject.has(Constants.JsonConstants.API_TOKEN))
                                    user.setAuthToken(employeeJsonObject.getString(Constants.JsonConstants.API_TOKEN));
                                if (employeeJsonObject.has(Constants.JsonConstants.NAME))
                                    user.setUserName(employeeJsonObject.getString(Constants.JsonConstants.NAME));
                                if (employeeJsonObject.has(Constants.JsonConstants.EMPLOYEE_ID))
                                    user.setId(employeeJsonObject.getLong(Constants.JsonConstants.EMPLOYEE_ID));
                                if (employeeJsonObject.has(Constants.JsonConstants.MOBILE))
                                    user.setUserMobileNumber(employeeJsonObject.getString(Constants.JsonConstants.MOBILE));
                                    if (employeeJsonObject.has(Constants.JsonConstants.BRANCH_ID))
                                    user.setBranchId(employeeJsonObject.getLong(Constants.JsonConstants.BRANCH_ID));
                                employeeCardList.add(user);
                            }
                        }
                    }
                } catch (Exception e) {

                }
                employeeCardAdapter.notifyItemInserted(employeeCardList.size()-1);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error!");
                progressDialog.dismiss();
                if (t != null) {
                    if (t.getMessage() != null) {
                        message = t.getMessage();
                    } else {
                        message = Constants.ERROR_OCCURRED;
                    }
                } else {
                    message = Constants.ERROR_OCCURRED;
                }
                errorDialog = new ErrorDialog(getContext(), message);
                errorDialog.show();
            }
        });
    }
}
