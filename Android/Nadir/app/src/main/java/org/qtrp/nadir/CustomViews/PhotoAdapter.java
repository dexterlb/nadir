package org.qtrp.nadir.CustomViews;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.qtrp.nadir.Activity.ManageRollsActivity;
import org.qtrp.nadir.Activity.RollActivity;
import org.qtrp.nadir.Database.Photo;
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<Photo> photoList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView location, timestamp, description, number;
        public int photoId;

        public MyViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.tv_number);
            location = (TextView) view.findViewById(R.id.tv_location);
            description = (TextView) view.findViewById(R.id.tv_description);
            timestamp = (TextView) view.findViewById(R.id.tv_photo_timestamp);
        }

        @Override
        public void onClick(View view) {

        }
    }
    public long getIdAt(int position) {
        return this.photoList.get(position).getPhotoId();
    }

    public PhotoAdapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Photo photo = photoList.get(position);

        Double latitude = photo.getLatitude();
        Double longtitude = photo.getLongtitude();
        Long timestamp = photo.getTimestamp();
        String description = photo.getDescription();

        Date d = new Date(timestamp * 1000);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        String pretty_date = df.format(d);
        NumberFormat formatter = new DecimalFormat("#0.00");

        String location = "(" + formatter.format(latitude) + ", " + formatter.format(longtitude) + ")";

        holder.description.setText(description);
        holder.number.setText(String.valueOf(photoList.size() - position));
        holder.timestamp.setText(pretty_date);
        holder.location.setText(location);

        holder.itemView.setLongClickable(true);
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public Photo getItem(int position) {
        return photoList.get(position);
    }

    public void clear() {
        photoList.clear();
    }

    public void addAll(Collection<? extends Photo> photos) {
        photoList.addAll(photos);
    }

    public void addOne(Photo photo) {
        photoList.add(photo);
    }

}