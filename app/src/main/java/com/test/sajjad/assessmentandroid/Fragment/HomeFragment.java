package com.test.sajjad.assessmentandroid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test.sajjad.assessmentandroid.R;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }
    Button showWeather,showLocalRestaurants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        showWeather = (Button)view.findViewById(R.id.weather_display_btn);
        showLocalRestaurants = (Button)view.findViewById(R.id.restaurant_show_btn);

        showWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new WeatherDisplayFragment()).
                        addToBackStack(null).commit();
            }
        });

        showLocalRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new LocalRestaurantsFragment()).
                        addToBackStack(null).commit();
            }
        });
    }
}
