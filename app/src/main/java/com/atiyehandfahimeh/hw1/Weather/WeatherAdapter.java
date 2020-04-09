package com.atiyehandfahimeh.hw1.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atiyehandfahimeh.hw1.Models.DayWeather;
import com.atiyehandfahimeh.hw1.R;

import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private ArrayList<DayWeather> daydata;
    private Context context;

    public static class WeatherViewHolder extends RecyclerView.ViewHolder{
        public ImageView wethericon;
        public TextView title;
        public TextView date;
        public TextView maxtempc;
        public TextView avgtempc;
        public TextView mintempc;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            wethericon = itemView.findViewById(R.id.weathericon);
            title = itemView.findViewById(R.id.weathertitle);
            date = itemView.findViewById(R.id.weatherdate);
            maxtempc = itemView.findViewById(R.id.maxtemp);
            mintempc = itemView.findViewById(R.id.mintemp);
            avgtempc = itemView.findViewById(R.id.avgtemp);
        }
    }

    public WeatherAdapter(Context context1, ArrayList<DayWeather> dayList){
        daydata = dayList;
        context = context1;
    }


    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View weatherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wether_item, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(weatherView);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeather currentday = daydata.get(position);

        holder.title.setText(currentday.getWeather());
        holder.date.setText(currentday.getDate());
        int resid = context.getResources().getIdentifier("a"+currentday.getPhotoCode() , "drawable", context.getPackageName());
        holder.wethericon.setImageResource(resid);
        holder.maxtempc.setText(currentday.getMaxTemp().toString());
        holder.mintempc.setText(currentday.getMinTemp().toString());
        holder.avgtempc.setText(currentday.getAvgTemp().toString());
    }

    @Override
    public int getItemCount() {
        return daydata.size();
    }

}
