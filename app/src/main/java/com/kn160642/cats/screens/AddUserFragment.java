package com.kn160642.cats.screens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.db.handlers.UserHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {


    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_add_user, container, false);

        Button btnAddUser = (Button) root.findViewById(R.id.btn_add);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t = root.findViewById(R.id.edit_username);
                final String username = t.getText().toString();
                if(username.isEmpty()) return;
                UserHandler.addUser(getContext(),username);
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigateUp();
            }
        });

        return root;
    }

}
