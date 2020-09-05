package com.kn160642.cats.screens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.helpers.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnPlay = (Button) root.findViewById(R.id.btn_play);
        Button btnAddUser = (Button) root.findViewById(R.id.btn_add_user);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_homeFragment_to_addUserFragment);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner s = root.findViewById(R.id.spinner);
                Globals.setActiveUser((String)s.getSelectedItem());
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_homeFragment_to_gameFragment);
            }
        });

        LiveData<List<User>> users = MyDatabase.getInstance(getContext()).userDao().getAllUsers();
        users.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Spinner spinner = root.findViewById(R.id.spinner);

                List<String> list = new ArrayList<>();
                for(User u: users){
                    list.add(u.getUsername());
                }

                ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(HomeFragment.this.getContext(),R.layout.support_simple_spinner_dropdown_item,list);
                spinner.setAdapter(spinnerAdapter);
            }
        });

        return root;
    }

}
