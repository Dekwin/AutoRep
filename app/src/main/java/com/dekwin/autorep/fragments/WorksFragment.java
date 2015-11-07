package com.dekwin.autorep.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dekwin.autorep.R;

/**
 * Created by dekst on 07.11.2015.
 */
public class WorksFragment extends Fragment{

    View rootView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.spare_info, container, false);
        setHasOptionsMenu(true);

        return rootView;
    }
}
