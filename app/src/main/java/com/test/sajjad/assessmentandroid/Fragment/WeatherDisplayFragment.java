package com.test.sajjad.assessmentandroid.Fragment;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.pavlospt.CircleView;
import com.squareup.picasso.Picasso;
import com.test.sajjad.assessmentandroid.Adapter.WeatherForecastAdapter;
import com.test.sajjad.assessmentandroid.Common.Common;
import com.test.sajjad.assessmentandroid.Model.WeatherForecastInfo;
import com.test.sajjad.assessmentandroid.Model.WeatherInfo;
import com.test.sajjad.assessmentandroid.MySingleton;
import com.test.sajjad.assessmentandroid.R;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class WeatherDisplayFragment extends Fragment {

    static WeatherDisplayFragment instance;

    private String jsonResponse;
    ImageView weather_iv;
    TextView location_tv,date_time_tv,humidity_tv,wind_tv;
    CircleView circleView;
    ArrayList<WeatherForecastInfo> weatherForecastInfoList;
    RecyclerView recyclerView;
    WeatherForecastAdapter weatherForecastAdapter;
    private String urlJson = "http://api.openweathermap.org/data/2.5/weather?lat=" +
            Common.current_location.getLatitude() +
            "&lon=" +
            Common.current_location.getLongitude() +
            "&appid=" +
            Common.API_ID;

    private String urlForecast = "http://api.openweathermap.org/data/2.5/forecast?lat=" +
            Common.current_location.getLatitude() +
            "&lon=" +
            Common.current_location.getLongitude() +
            "&appid=" +
            Common.API_ID;


    public static WeatherDisplayFragment getInstance(){
        if (instance == null)
            instance = new WeatherDisplayFragment();
        return instance;
    }

    public WeatherDisplayFragment() {
        weatherForecastInfoList = new ArrayList<WeatherForecastInfo>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_weather_display,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        location_tv = (TextView)view.findViewById(R.id.city_country);
        circleView = (CircleView) view.findViewById(R.id.weather_result);
        date_time_tv = (TextView)view.findViewById(R.id.current_date);
        humidity_tv = (TextView)view.findViewById(R.id.humidity_result);
        wind_tv = (TextView)view.findViewById(R.id.wind_result);

        weather_iv = (ImageView)view.findViewById(R.id.weather_icon);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.weather_daily_list);
        recyclerView.setLayoutManager(layoutManager);

        weatherForecastAdapter = new WeatherForecastAdapter(getContext(),weatherForecastInfoList);
        recyclerView.setAdapter(weatherForecastAdapter);


        makeJsonWeatherRequest();
        makeJsonWeatherForecastRequest();
        super.onViewCreated(view, savedInstanceState);
    }

    private void makeJsonWeatherRequest() {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJson, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    WeatherInfo weatherInfo = new WeatherInfo();
                    JSONArray array = response.getJSONArray("weather");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject weatherJson = array.getJSONObject(i);
                        weatherInfo.setDescription(weatherJson.getString("description"));
                        weatherInfo.setIcon(weatherJson.getString("icon"));
                    }
                    weatherInfo.setCity(response.getString("name"));
                    weatherInfo.setTimeAndDate(Common.convertUnixToDate(response.getInt("dt")).toString());

                    JSONObject sysJson = response.getJSONObject("sys");
                    weatherInfo.setCountry(sysJson.getString("country"));

                    JSONObject mainJson = response.getJSONObject("main");
                    double temp =  mainJson.getDouble("temp") -273.15;
                    weatherInfo.setTemperature(String.valueOf(Math.round(temp * 100D) / 100D));
                    weatherInfo.setHumidity(mainJson.getString("humidity"));

                    JSONObject windJson = response.getJSONObject("wind");
                    weatherInfo.setWindSpeed(windJson.getString("speed"));
                    setWeatherInfo(weatherInfo);
                } catch (Exception e) {
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

    void setWeatherInfo(WeatherInfo weatherInfo)
    {
        Picasso.get()
                .load(new StringBuilder("https://openweathermap.org/img/w/")
                        .append(weatherInfo.getIcon()).append(".png").toString())
                .into(weather_iv);
        location_tv.setText(weatherInfo.getCity()+","+weatherInfo.getCountry());
        circleView.setTitleText(weatherInfo.getTemperature()+"°C");
        circleView.setSubtitleText(weatherInfo.getDescription());
        date_time_tv.setText(weatherInfo.getTimeAndDate());
        humidity_tv.setText(weatherInfo.getHumidity()+"%");
        wind_tv.setText(weatherInfo.getWindSpeed()+"km/h");
    }
    private void makeJsonWeatherForecastRequest() {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlForecast, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    int[] everyday = new int[]{0,0,0,0,0,0,0};

                    JSONArray array = response.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        WeatherForecastInfo weatherForecastInfo = new WeatherForecastInfo();

                        JSONObject listJson = array.getJSONObject(i);
                        weatherForecastInfo.setTimeAndDate(Common.convertUnixToDay(listJson.getInt("dt")).toString());
                        JSONArray array1 = listJson.getJSONArray("weather");
                        for (int x = 0; x < array1.length(); x++) {
                            JSONObject weatherJson = array1.getJSONObject(x);
                            weatherForecastInfo.setIcon(weatherJson.getString("icon"));
                        }

                        JSONObject mainJson = listJson.getJSONObject("main");
                        double temp =  mainJson.getDouble("temp") -273.15;
                        weatherForecastInfo.setTemperature(String.valueOf(Math.round(temp * 100D) / 100D));


                        if(weatherForecastInfo.getTimeAndDate().equals("Mon") && everyday[0] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[0] = 1;
                        }
                        else if(weatherForecastInfo.getTimeAndDate().equals("Tue") && everyday[1] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[1] = 1;
                        }
                        else if(weatherForecastInfo.getTimeAndDate().equals("Wed") && everyday[2] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[2] = 1;
                        }
                        else if(weatherForecastInfo.getTimeAndDate().equals("Thu") && everyday[3] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[3] = 1;
                        }
                        else if(weatherForecastInfo.getTimeAndDate().equals("Fri") && everyday[4] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[4] = 1;
                        }
                        else if(weatherForecastInfo.getTimeAndDate().equals("Sat") && everyday[5] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[5] = 1;
                        }
                        else if(weatherForecastInfo.getTimeAndDate().equals("Sun") && everyday[6] < 1){
                            weatherForecastInfoList.add(weatherForecastInfo);
                            everyday[6] = 1;
                        }
                    }
                    weatherForecastAdapter.notifyDataSetChanged();
                } catch (Exception e) {
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
