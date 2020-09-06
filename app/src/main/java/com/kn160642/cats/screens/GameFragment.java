package com.kn160642.cats.screens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.game.ComponentAdapter;
import com.kn160642.cats.helpers.Globals;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game, container, false);;

        recyclerView = (RecyclerView) root.findViewById(R.id.rvChassis);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);



        long userId = Long.parseLong(Globals.getActiveUser().split(":")[0]);
        Log.i("DATABASE", Long.toString(userId));
        // TODO: get data
        LiveData<List<Component>> components = MyDatabase.getInstance(getContext()).componentDao().getUserComponents(userId);
        components.observe(getViewLifecycleOwner(), new Observer<List<Component>>() {
            @Override
            public void onChanged(List<Component> components) {
                for(Component c: components){
                    Log.i("DATABASE", c.getName());
                }
            }
        });
        // TODO: set recycler data
        // specify an adapter (see also next example)
        mAdapter = new ComponentAdapter(new String[]{"abc","def"});
        recyclerView.setAdapter(mAdapter);
        return root;
    }

}
