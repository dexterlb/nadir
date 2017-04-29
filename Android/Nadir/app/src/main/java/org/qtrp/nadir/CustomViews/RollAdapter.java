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
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.util.Collection;
import java.util.List;

public class RollAdapter extends RecyclerView.Adapter<RollAdapter.MyViewHolder> {

    private List<Roll> rollList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, colour;
        public int rollId;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            colour = (TextView) view.findViewById(R.id.tv_colour);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RollActivity.class);
            intent.putExtra("roll_id", rollId);
            view.getContext().startActivity(intent);
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
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return rollList.size();
    }

    public Roll getItem(int position) {
        return rollList.get(position);
    }

    public void clear() {
        rollList.clear();
    }

    public void addAll(Collection<? extends Roll> rolls) {
        rollList.addAll(rolls);
    }
}