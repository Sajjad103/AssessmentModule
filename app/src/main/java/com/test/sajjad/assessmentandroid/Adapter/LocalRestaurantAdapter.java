package com.test.sajjad.assessmentandroid.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.sajjad.assessmentandroid.Common.Common;
import com.test.sajjad.assessmentandroid.Model.RestaurantInfo;
import com.test.sajjad.assessmentandroid.Model.WeatherForecastInfo;
import com.test.sajjad.assessmentandroid.R;

import java.util.ArrayList;

public class LocalRestaurantAdapter extends RecyclerView.Adapter<LocalRestaurantAdapter.MyViewHolder> {

    Context context;
    ArrayList<RestaurantInfo> restaurantInfo;

    public LocalRestaurantAdapter(Context context, ArrayList<RestaurantInfo> restaurantInfo) {
        this.context = context;
        this.restaurantInfo = restaurantInfo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_adapter_nearby_restaurants,parent,false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_restaurant.setText(restaurantInfo.get(position).getName());
        holder.rating.setText(restaurantInfo.get(position).getRating());
        holder.reviews.setText(restaurantInfo.get(position).getReviews());
        holder.ratingBar.setRating(Float.valueOf(restaurantInfo.get(position).getRating()));
        if(!restaurantInfo.get(position).getImage().contains("")){
            Picasso.get()
                    .load(new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=")
                            .append(restaurantInfo.get(position).getImage()).append("&key=").append(Common.API_KEY).toString())
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {

        return restaurantInfo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name_restaurant;
        ImageView imageView;
        TextView rating;
        TextView reviews;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            name_restaurant = (TextView) itemView.findViewById(R.id.name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            reviews = (TextView) itemView.findViewById(R.id.reviews);
            imageView = (ImageView)itemView.findViewById(R.id.weather_forecast_icon);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
        }
    }
}
