package com.kn160642.cats.game;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.helpers.RenderHelper;
import com.kn160642.cats.screens.GameFragment;

import java.util.ArrayList;
import java.util.List;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.MyViewHolder> {
    private ArrayList<Component> mDataset;
    private GameFragment parent;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComponentAdapter(GameFragment gameFragment, ArrayList<Component> myDataset) {
        mDataset = myDataset;
        this.parent = gameFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ComponentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_box, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Component c = mDataset.get(position);
        TextView tv = holder.view.findViewById(R.id.label1);
        tv.setText(c.getName());

        tv=holder.view.findViewById(R.id.tvEnergy);
        tv.setText(Integer.toString(c.getEnergy()));

        tv=holder.view.findViewById(R.id.tvPower);
        tv.setText(Integer.toString(c.getPower()));

        tv=holder.view.findViewById(R.id.tvHealth);
        tv.setText(Integer.toString(c.getHealth()));

        ImageButton image = holder.view.findViewById(R.id.imageButton);
        int imageResourceId = RenderHelper.getImageResourceIdForComponent(c);
        if(imageResourceId!= -1){
            image.setImageResource(imageResourceId);
        }

        holder.view.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DRAW", "USAO ONCLICK");
                parent.selectComponent(c);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swapItems(ArrayList<Component> cm){
        this.mDataset = cm;
        parent.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ComponentAdapter.this.notifyDataSetChanged();
            }
        });

    }
}