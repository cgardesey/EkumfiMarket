package com.ekumfi.juice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ServiceListAdapter;
import com.ekumfi.juice.other.InitApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class ListFragment extends Fragment {

    public JSONObject jsonObject;
    RecyclerView recyclerView;
    TextView titleTextView;
    ServiceListAdapter listAdapter;
    String title = "";
    Context mContext;
    static ArrayList<String> courses = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        titleTextView = rootView.findViewById(R.id.title);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        title = getArguments().getString("title");
        titleTextView.setText(title);

        courses.clear();
        populateNames(title + " >> ");

        return rootView;
    }

    private void populateNames(String search) {
        String URL = null;
        try {
            URL = API_URL + "filtered-courses/" + search;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    jsonArray -> {
                        if (jsonArray != null) {
                            courses.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    courses.add(jsonArray.getString(i));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /*listAdapter = new ListAdapter((names, position, holder) -> {
                            String textViewText = names.get(position);
                            startActivity(new Intent(mContext, MyListActivity.class).putExtra("title", title + " >> " + textViewText));
                        }, getActivity(), courses, title);*/

                        recyclerView.setAdapter(listAdapter);
                    },
                    error -> myVolleyError(mContext, error)
            );
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            InitApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My error", e.toString());
        }
    }

}