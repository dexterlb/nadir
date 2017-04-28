package org.qtrp.nadir.CustomListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.util.ArrayList;
public class RollAdapter extends ArrayAdapter<Roll> {
    public RollAdapter(Context context, ArrayList<Roll> rolls) {
        super(context, 0, rolls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Roll roll = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.roll_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvColour = (TextView) convertView.findViewById(R.id.tv_colour);
        // Populate the data into the template view using the data object
        tvName.setText(roll.getName());
        tvColour.setText(roll.getColour());
        // Return the completed view to render on screen
        return convertView;
    }
}