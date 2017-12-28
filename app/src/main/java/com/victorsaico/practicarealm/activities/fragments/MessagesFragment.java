package com.victorsaico.practicarealm.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victorsaico.practicarealm.R;


public class MessagesFragment extends Fragment {
    private BottomNavigationItemView bnve;
    private TextView txtfragment;
    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"WrongConstant", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        txtfragment = view.findViewById(R.id.txtfragment);
        bnve = view.findViewById(R.id.bnve);
        final Fragment homeFragment = new HomeFragment();
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        txtfragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, homeFragment ).commit();
            }
        });
        return view;
    }

}
