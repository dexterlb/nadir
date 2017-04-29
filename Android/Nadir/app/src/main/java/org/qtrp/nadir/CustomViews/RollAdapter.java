package org.qtrp.nadir.CustomViews;

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
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.util.List;

public class RollAdapter extends RecyclerView.Adapter<RollAdapter.MyViewHolder> {

    private List<Roll> rollList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, colour;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            colour = (TextView) view.findViewById(R.id.tv_colour);
        }

    }
    public long getIdAt(int position) {
        return this.rollList.get(position).getId();
    }

    public RollAdapter(List<Roll> rollList) {
        this.rollList = rollList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roll_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Roll roll = rollList.get(position);
        holder.name.setText(roll.getName());
        String c = roll.getColour();
        if (c.equals("y")) {
            holder.colour.setText("Colour");
        } else {
            holder.colour.setText("Black'n'white");
        }

        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return rollList.size();
    }

    public Roll getItem(int position) {
        return rollList.get(position);
    }
}