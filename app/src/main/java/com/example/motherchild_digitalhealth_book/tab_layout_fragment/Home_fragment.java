package com.example.motherchild_digitalhealth_book.tab_layout_fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.motherchild_digitalhealth_book.EducationalInfo_Homepage;
import com.example.motherchild_digitalhealth_book.HeathWorkers_Info;
import com.example.motherchild_digitalhealth_book.MothersHealthInfo;
import com.example.motherchild_digitalhealth_book.MothersInformation;
import com.example.motherchild_digitalhealth_book.R;

public class Home_fragment extends Fragment {
    String searchtxt;
    View homeView;
    CardView motherInfo_cv, motherHealth_cv, childInfo_cv, eduInfo_cv, healthWorker_cv,location_cv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        motherInfo_cv = homeView.findViewById(R.id.mother_info);
        motherHealth_cv = homeView.findViewById(R.id.motherHealth_info);
        childInfo_cv = homeView.findViewById(R.id.child_info);
        eduInfo_cv = homeView.findViewById(R.id.Educational_info);
        healthWorker_cv = homeView.findViewById(R.id.healthWorker_info);
        location_cv = homeView.findViewById(R.id.location);

        motherInfo_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), MothersInformation.class));
            }
        });

        motherHealth_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MothersHealthInfo.class));
            }
        });

        childInfo_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        eduInfo_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EducationalInfo_Homepage.class));
            }
        });

        healthWorker_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HeathWorkers_Info.class));
            }
        });

        location_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mapDialog = new AlertDialog.Builder(getActivity());
                mapDialog.setTitle("Enter the hospital's name");

                final EditText searchEditTxt = new EditText(getActivity());
                searchEditTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                mapDialog.setView(searchEditTxt);

                mapDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        searchtxt = searchEditTxt.getText().toString();
                        String location = "https://www.google.com/maps/search/?api=1&query="+searchtxt;
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
                        startActivity(mapIntent);
                    }
                });
                mapDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mapDialog.show();
            }
        });
        return homeView;
    }
}