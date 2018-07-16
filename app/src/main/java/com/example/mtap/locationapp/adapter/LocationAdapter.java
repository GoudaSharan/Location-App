package com.example.mtap.locationapp.adapter;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mtap.locationapp.R;
import com.example.mtap.locationapp.model.Location;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private List<Location> locationsList;
    Cursor dataCursor;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView latitude,longitude;

        public MyViewHolder(View view) {
            super(view);
            latitude = (TextView) view.findViewById(R.id.TextView_Latitude);
            longitude = (TextView) view.findViewById(R.id.TextView_Longitude);

        }
    }

    public LocationAdapter(List<Location> locationsList,Cursor cursor) {
        this.locationsList = locationsList;
        this.dataCursor=cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Location movie = locationsList.get(position);
        holder.latitude.setText(movie.getLat());
        holder.longitude.setText(movie.getLng());

    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }
}