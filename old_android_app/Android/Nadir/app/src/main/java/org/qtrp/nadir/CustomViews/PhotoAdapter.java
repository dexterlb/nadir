package org.qtrp.nadir.CustomViews;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qtrp.nadir.Database.Photo;
import org.qtrp.nadir.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<Photo> photoList;
    private Context mContext;
    private int selectedPos = RecyclerView.NO_POSITION;

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
            select(getLayoutPosition());
        }
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(int position);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public boolean canSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    private boolean canSelect;

    private OnItemSelectedListener onItemSelectedListener;

    public void select(int position) {
        if (!canSelect) {
            return;
        }

        notifyItemChanged(selectedPos);
        selectedPos = position;
        notifyItemChanged(selectedPos);
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(position);
        }
    }

    public void deselect() {
        int oldPos = selectedPos;
        notifyItemChanged(selectedPos);
        selectedPos = RecyclerView.NO_POSITION;
        notifyItemChanged(selectedPos);
        notifyItemChanged(oldPos);
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

        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Photo photo = photoList.get(position);

        Long timestamp = photo.getTimestamp();
        String description = photo.getDescription();

        Date d = new Date(timestamp * 1000);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        String pretty_date = df.format(d);

        holder.location.setText(photo.getFriendlyLocation());

        holder.description.setText(description);
        holder.number.setText(String.valueOf(photoList.size() - position));
        holder.timestamp.setText(pretty_date);

        holder.itemView.setLongClickable(true);
        holder.itemView.setOnClickListener(holder);
        holder.itemView.setSelected(selectedPos == position);
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