package com.test.sajjad.assessmentandroid.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.sajjad.assessmentandroid.Model.WeatherForecastInfo;
import com.test.sajjad.assessmentandroid.R;

import java.util.ArrayList;

public class WeatherForecastAdapter  extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    ArrayList<WeatherForecastInfo> weatherForecastInfo;

    public WeatherForecastAdapter(Context context, ArrayList<WeatherForecastInfo> weatherForecastInfo) {
        this.context = context;
        this.weatherForecastInfo = weatherForecastInfo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_adapter_recyclerview,parent,false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Load icon

        Picasso.get()
                .load(new StringBuilder("https://openweathermap.org/img/w/")
                        .append(weatherForecastInfo.get(position).getIcon()).append(".png").toString())
                .into(holder.imageView);

        holder.dayText.setText(weatherForecastInfo.get(position).getTimeAndDate());
        holder.temperatureText.setText(weatherForecastInfo.get(position).getTemperature()+"°");
    }

    @Override
    public int getItemCount() {
        return weatherForecastInfo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dayText;
        ImageView imageView;
        TextView temperatureText;
        public MyViewHolder(View itemView) {
            super(itemView);
            dayText = (TextView) itemView.findViewById(R.id.day_of_week);
            temperatureText = (TextView) itemView.findViewById(R.id.weather_result);
            imageView = (ImageView)itemView.findViewById(R.id.weather_forecast_icon);
        }
    }
}
