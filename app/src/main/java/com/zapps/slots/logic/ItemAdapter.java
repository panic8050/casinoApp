package com.zapps.slots.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zapps.slots.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Integer> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.image_view);
        }
    }

    public ItemAdapter(List<Integer> myDataset) {
        mDataset = new ArrayList<>(myDataset);
        Collections.shuffle(mDataset);
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int dataIndex = mDataset.get(position % mDataset.size());
        holder.mImageView.setImageResource(dataIndex);
        holder.mImageView.setTag(dataIndex);


    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}