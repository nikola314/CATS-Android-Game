package com.kn160642.cats.screens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.Entities.UserComponent;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.game.BoxAdapter;
import com.kn160642.cats.game.BoxUpdateThread;
import com.kn160642.cats.game.ComponentAdapter;
import com.kn160642.cats.game.GameView;
import com.kn160642.cats.game.MusicPlayer;
import com.kn160642.cats.helpers.Globals;
import com.kn160642.cats.helpers.TypesHelper;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private RecyclerView wheelsRecyclerView;
    private ComponentAdapter wheelsAdapter;
    private RecyclerView.LayoutManager wheelsLayoutManager;

    private RecyclerView chassisRecyclerView;
    private ComponentAdapter chassisAdapter;
    private RecyclerView.LayoutManager chassisLayoutManager;

    private RecyclerView weaponsRecyclerView;
    private ComponentAdapter weaponsAdapter;
    private RecyclerView.LayoutManager weaponsLayoutManager;

    private RecyclerView boxesRecyclerView;
    private RecyclerView.Adapter boxesAdapter;
    private RecyclerView.LayoutManager boxesLayoutManager;

    private GameView gameView;
    private User activeUser;

    private ProgressBar progressEnergy;
    private ProgressBar progressHealth;
    private ProgressBar progressPower;

    BoxUpdateThread boxUpdateThread = null;

    private MusicPlayer musicPlayer;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        playMusicIfNeeded();
        initButtons(root);
        initRecyclerViews(root);
        initCanvas(root);
        initProgress(root);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        musicPlayer.resume();
//        boxUpdateThread = new BoxUpdateThread(getContext(), (BoxAdapter)boxesAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        musicPlayer.pause();
        stopBoxUpdateThread();
    }

    private void stopBoxUpdateThread(){
        boolean retry = true;
        try {

            while(retry){
                boxUpdateThread.running = false;
                boxUpdateThread.join();
                retry = false;
            }
        } catch (InterruptedException e) {
            retry = true;
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        stopBoxUpdateThread();
    }

    private void initCanvas(View root) {
        SurfaceView sfvTrack = (SurfaceView)root.findViewById(R.id.gameView);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        gameView = root.findViewById(R.id.gameView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                User u = MyDatabase.getInstance(getContext()).userDao().getUserById(Globals.getActiveUserId());
                activeUser = u;
                drawUsersVehicle();
            }
        }).start();
    }

    private void initProgress(View root){
        progressEnergy = root.findViewById(R.id.progressEnergy);
        progressPower = root.findViewById(R.id.progressPower);
        progressHealth = root.findViewById(R.id.progressHealth);

        progressEnergy.setMax(Globals.maxProgress);
        progressPower.setMax(Globals.maxProgress);
        progressHealth.setMax(Globals.maxProgress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    calculateStrength();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initButtons(View root) {
        Button btnSettings = root.findViewById(R.id.btnSettings);
        Button btnStats = root.findViewById(R.id.btnStats);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.settings_layout, null);

                final Switch sMusicOn = view.findViewById(R.id.switchMusicOn);
                final Switch sBotPlays = view.findViewById(R.id.switchBotPlays);

                final SharedPreferences sharedpreferences = GameFragment.this.getActivity().getSharedPreferences(Globals.sharedPreferencesName,
                        Context.MODE_PRIVATE);

                sMusicOn.setChecked(sharedpreferences.getBoolean(Globals.musicOn,true));
                sBotPlays.setChecked(sharedpreferences.getBoolean(Globals.botPlays, true));

                sMusicOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(Globals.botPlays, sBotPlays.isChecked());
                        editor.putBoolean(Globals.musicOn, sMusicOn.isChecked());
                        editor.commit();

                        if(isChecked){
                            musicPlayer.start();
                        }
                        else{
                            musicPlayer.stop();
                        }
                    }
                });

                builder.setView(view)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(Globals.botPlays, sBotPlays.isChecked());
                                editor.putBoolean(Globals.musicOn, sMusicOn.isChecked());
                                editor.commit();
                            }
                        });

                builder.setTitle("Settings");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement
            }
        });

        Button btnPlay = root.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_gameFragment_to_battleFragment);
            }
        });
    }

    private void playMusicIfNeeded(){
        musicPlayer = new MusicPlayer(getActivity().getApplicationContext());
        final SharedPreferences sharedpreferences = GameFragment.this.getActivity().getSharedPreferences(Globals.sharedPreferencesName,
                Context.MODE_PRIVATE);
        final boolean musicOn = sharedpreferences.getBoolean(Globals.musicOn, true);
        if(musicOn){
            musicPlayer.start();
        }
    }

    private void initRecyclerViews(View root){
        chassisRecyclerView = (RecyclerView) root.findViewById(R.id.rvChassis);
        chassisRecyclerView.setHasFixedSize(true);
        chassisLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        chassisRecyclerView.setLayoutManager(chassisLayoutManager);

        wheelsRecyclerView = (RecyclerView) root.findViewById(R.id.rvWheels);
        wheelsRecyclerView.setHasFixedSize(true);
        wheelsLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        wheelsRecyclerView.setLayoutManager(wheelsLayoutManager);

        weaponsRecyclerView = (RecyclerView) root.findViewById(R.id.rvWeapons);
        weaponsRecyclerView.setHasFixedSize(true);
        weaponsLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        weaponsRecyclerView.setLayoutManager(weaponsLayoutManager);

        boxesRecyclerView = (RecyclerView) root.findViewById(R.id.rvBoxes);
        boxesRecyclerView.setHasFixedSize(true);
        boxesLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        boxesRecyclerView.setLayoutManager(boxesLayoutManager);

        long userId = Globals.getActiveUserId();

        LiveData<List<Component>> components = MyDatabase.getInstance(getContext()).componentDao().getUserComponents(userId);
        components.observe(getViewLifecycleOwner(), new Observer<List<Component>>() {
            @Override
            public void onChanged(List<Component> components) {
                ArrayList<Component> chassis = new ArrayList<>();
                ArrayList<Component> wheels = new ArrayList<>();
                ArrayList<Component> weapons = new ArrayList<>();
                for(Component c: components){
                    switch(c.getType()){
                        case TypesHelper.ComponentType.CHASSIS: chassis.add(c); break;
                        case TypesHelper.ComponentType.WEAPON: weapons.add(c); break;
                        case TypesHelper.ComponentType.WHEELS: wheels.add(c); break;
                    }
                }
                chassisAdapter = new ComponentAdapter(GameFragment.this, chassis);
                weaponsAdapter = new ComponentAdapter(GameFragment.this, weapons);
                wheelsAdapter = new ComponentAdapter(GameFragment.this, wheels);

                chassisRecyclerView.setAdapter(chassisAdapter);
                wheelsRecyclerView.setAdapter(wheelsAdapter);
                weaponsRecyclerView.setAdapter(weaponsAdapter);
            }
        });

        LiveData<List<Box>> boxes = MyDatabase.getInstance(getContext()).boxDao().getUnopenedBoxesForUser(Globals.getActiveUserId());
        boxes.observe(getViewLifecycleOwner(), new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                ArrayList<Box> boxList = new ArrayList<>();
                for(Box b:boxes){
                    boxList.add(b);
                }
                boxesAdapter = new BoxAdapter(GameFragment.this, boxList);
                boxesRecyclerView.setAdapter(boxesAdapter);
                boxUpdateThread = new BoxUpdateThread(getContext(), (BoxAdapter)boxesAdapter);
                boxUpdateThread.start();
            }
        });
    }

    private void drawUsersVehicle(){
        gameView.draw(activeUser);
    }

    public void selectComponent(Component c){
        switch(c.getType()){
            case TypesHelper.ComponentType.CHASSIS: activeUser.setChassisId(c.getComponentId()); break;
            case TypesHelper.ComponentType.WHEELS: activeUser.setWheelsId(c.getComponentId()); break;
            case TypesHelper.ComponentType.WEAPON: activeUser.setWeaponId(c.getComponentId()); break;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDatabase.getInstance(getContext()).userDao().updateUser(activeUser);
            }
        }).start();
        gameView.changeSelectedComponent(c);
        calculateStrength();
    }

    private void calculateStrength(){
        Component[] selected = gameView.getSelectedComponents();
        int energy = 0, power = 0, health = 0;
        for(Component c:selected){
            if(c== null) continue;
            energy+=c.getEnergy();
            power+=c.getPower();
            health+=c.getHealth();
        }
        final int finalEnergy = energy;
        final int finalHealth = health;
        final int finalPower = power;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressEnergy.setProgress(finalEnergy);
                progressHealth.setProgress(finalHealth);
                progressPower.setProgress(finalPower);
            }
        });
    }

    public void openBox(final Box box){
        new Thread(new Runnable() {
            @Override
            public void run() {
                box.setOpened(true);
                MyDatabase db = MyDatabase.getInstance(getContext());
                db.boxDao().updateBox(box);
                List<Component> components = db.componentDao().getAllComponentsDead();

                int count = components.size()-1;
                int randomIndex = (int)(Math.random()*count);
                Component randomComponent = components.get(randomIndex);
//                Log.i("BOX", randomComponent.getName());
                toastFromOtherThread("You got: "+randomComponent.getName());

                UserComponent uc = new UserComponent(Globals.getActiveUserId(),randomComponent.getComponentId());
                db.componentDao().insertUserComponent(uc);
                refreshDataSets();
            }
        }).start();

    }

    public void toastFromOtherThread(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refreshDataSets(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Component> chassis = new ArrayList<>();
                ArrayList<Component> wheels = new ArrayList<>();
                ArrayList<Component> weapons = new ArrayList<>();
                List<Component> components = MyDatabase.getInstance(getContext()).componentDao().getUserComponentsDead(Globals.getActiveUserId());
                for(Component c: components){
                    switch(c.getType()){
                        case TypesHelper.ComponentType.CHASSIS: chassis.add(c); break;
                        case TypesHelper.ComponentType.WEAPON: weapons.add(c); break;
                        case TypesHelper.ComponentType.WHEELS: wheels.add(c); break;
                    }
                }
                chassisAdapter.swapItems(chassis);
                wheelsAdapter.swapItems(wheels);
                weaponsAdapter.swapItems(weapons);
            }
        }).start();
    }

}
