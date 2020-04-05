package com.atiyehandfahimeh.hw1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter {
    private static final int TYPE_1 = 207;
    private static final int TYPE_2 = 208;

    private Context context ;
    private ArrayList<ListItem> data = new ArrayList<>();
    public ListAdapter(Context context , ArrayList<ListItem> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<ListItem> data) {
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
        if (position % 2 == 0){
            ItemListViewHolder holder1 = (ItemListViewHolder) holder;
            holder1.textView.setText(data.get(position).getName());
        }else {
            ItemListViewHolder holder1 = (ItemListViewHolder) holder;
            holder1.textView.setText(data.get(position).getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0){
            return TYPE_1;
        }else {
            return TYPE_2;
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
        }
    }
}
