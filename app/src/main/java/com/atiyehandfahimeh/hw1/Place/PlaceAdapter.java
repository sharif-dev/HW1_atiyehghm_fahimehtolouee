package com.atiyehandfahimeh.hw1.Place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atiyehandfahimeh.hw1.Models.Place;
import com.atiyehandfahimeh.hw1.R;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter {
    private static final int TYPE_1 = 1;
    private static final int TYPE_2 = 0;

    private Context context ;
    private ArrayList<Place> data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PlaceAdapter(Context context , ArrayList<Place> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<Place> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_1){
            View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            return new ItemListViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            return new ItemListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Place currentItem = data.get(position);
        String itemName = currentItem.getName();
        ItemListViewHolder holder1 = (ItemListViewHolder) holder;
        holder1.textView.setText(itemName);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0){
            return TYPE_2;
        }else {
            return TYPE_1;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemListViewHolder extends RecyclerView.ViewHolder {
        TextView textView ;
        public ItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
