package com.kn160642.cats.game;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.helpers.RenderHelper;
import com.kn160642.cats.screens.GameFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.MyViewHolder> {
    private ArrayList<Box> mDataset;
    private GameFragment parent;
    private List<TextView> views = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BoxAdapter(GameFragment gameFragment, ArrayList<Box> myDataset) {
        mDataset = myDataset;
        this.parent = gameFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
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
        final Box c = mDataset.get(position);
        final TextView tv = holder.view.findViewById(R.id.label1);
        tv.setText(formatRemainingTime(calculateRemainingTime(c)));
        views.add(tv);

/*        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        sleep(300);
                        Log.i("BOXADAPTER", "USAO");
                        tv.setText(formatRemainingTime(calculateRemainingTime(c)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        ImageButton image = holder.view.findViewById(R.id.imageButton);
        image.setImageResource(R.drawable.chest);

        holder.view.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calculateRemainingTime(c)>0) return;
                    parent.openBox(c);
                    mDataset.remove(c);
            }
        });
    }

    private String formatRemainingTime(long millis){
        if(millis < 0 ) return "Open";
        return String.format("%1$tH:%1$tM:%1$tS", millis);
    }

    private long calculateRemainingTime(Box box){
        long timePassed = System.currentTimeMillis() - box.getTimestamp();
        return box.getTimeToOpen() - timePassed;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swapItems(ArrayList<Box> boxes){
        this.mDataset = boxes;
        parent.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BoxAdapter.this.notifyDataSetChanged();
            }
        });

    }
}