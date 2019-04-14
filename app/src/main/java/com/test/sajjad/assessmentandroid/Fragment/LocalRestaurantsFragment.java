package com.test.sajjad.assessmentandroid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.test.sajjad.assessmentandroid.Adapter.LocalRestaurantAdapter;
import com.test.sajjad.assessmentandroid.Common.Common;
import com.test.sajjad.assessmentandroid.Model.RestaurantInfo;
import com.test.sajjad.assessmentandroid.MySingleton;
import com.test.sajjad.assessmentandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class LocalRestaurantsFragment extends Fragment {

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private int radius = 1500;

    ArrayList<RestaurantInfo> restaurantInfoList;
    RecyclerView recyclerView;
    LocalRestaurantAdapter restaurantAdapter;

    public LocalRestaurantsFragment() {
        // Required empty public constructor
        restaurantInfoList = new ArrayList<RestaurantInfo>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_restaurants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.restaurants_available_list);
        recyclerView.setLayoutManager(layoutManager);
        restaurantAdapter = new LocalRestaurantAdapter(getContext(),restaurantInfoList);
        recyclerView.setAdapter(restaurantAdapter);

        makeJsonRestaurantRequest();
    }

    private void makeJsonRestaurantRequest() {
        String FULL_PLACE_URL = Common.PLACES_API_BASE +TYPE_SEARCH + OUT_JSON+"location="+
                String.valueOf(Common.current_location.getLatitude()) + "," + String.valueOf(Common.current_location.getLongitude()+
                "&radius=" + String.valueOf(radius)+"&type=restaurant&key="+Common.API_KEY);


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                FULL_PLACE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {


                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {

                        String name_res,photo_name="",rating,total_reviews;
                        JSONObject resultJson = array.getJSONObject(i);

                        name_res = resultJson.getString("name");

//                        Log.d("TagPhoto","P =="+resultJson.getJSONArray("photos"));
                        if(resultJson.has("photo")){
                            JSONArray array1 = resultJson.getJSONArray("photos");
                            for (int x = 0; x < array1.length(); x++) {
                                JSONObject photoJson = array1.getJSONObject(x);
                                photo_name = photoJson.getString("photo_reference");
                            }
                        }
                        else {
                            photo_name = "";
                        }
                        rating = resultJson.getString("rating");
                        total_reviews = resultJson.getString("user_ratings_total");

                        restaurantInfoList.add(new RestaurantInfo(photo_name,name_res,rating,total_reviews));

                    }


                    Log.d("list","rest"+restaurantInfoList);
                    restaurantAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: Check the Connection" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Check the Connection" + error.getMessage());
                Toast.makeText(getContext(),
                        "Error: Check the Connection", Toast.LENGTH_SHORT).show();

            }
        });
        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);
    }
}
