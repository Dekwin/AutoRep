package com.dekwin.autorep.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.ExpandableListAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Repair;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dekst on 04.11.2015.
 */
public class AddWorksFragment extends DialogFragment {

    public interface FragmentIntListener {
        void getWorksList(List<Work> works);
    }

    FragmentIntListener fragmentIntListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentIntListener = (FragmentIntListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.works_info, container, false);
        //tbl = (ListView) findViewById(R.id.spares_list1);

        if (getDialog() != null)
            getDialog().setTitle("Выбор работ");

        createGroupList();
        createCollection();

        expListView = (ExpandableListView) rootView.findViewById(R.id.laptop_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupList, repairCollection);
        expListView.setAdapter(expListAdapter);

        Button acceptButton = (Button) rootView.findViewById(R.id.works_info_accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //  getActivity().getSupportFragmentManager().beginTransaction().remove(WorksFragment.this).commit();

                //getActivity().getSupportFragmentManager().popBackStackImmediate();
                List<Work> worksId = new ArrayList<>();

                for (Map.Entry<String, List<Work>> entry : repairCollection.entrySet()) {
                    for (Work w : entry.getValue())
                        if (w.isSelected())
                            worksId.add(w);
                }

                // for (Work wrk: repairCollection)
                fragmentIntListener.getWorksList(worksId);
                // getDialog().dismiss();
                getDialog().hide();
                //getActivity().onBackPressed();
            }
        });
        return rootView;

    }

    List<Repair> groupList;
    List<String> childList;
    Map<String, List<Work>> repairCollection;
    ExpandableListView expListView;

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList = DatabaseHelper.selectRepairs(null, null);
    }

    private void createCollection() {
        // preparing laptops collection(child)
        repairCollection = new LinkedHashMap<>();

        // DatabaseHelper.selectRepairs(null,null);

        //DatabaseHelper.selectWorks(DatabaseHelper.REPAIRS_COLUMN_ID+" = ",null);
        List<Work> wrk;
        for (Repair repair : groupList) {

            wrk = DatabaseHelper.selectWorks(DatabaseHelper.WORKS_COLUMN_REPAIRSID + " = " + repair.getId(), null);

            if (wrk != null) {
                Log.e("wrk", " wrk:: " + wrk);
                for (Work w : wrk) {
                    Log.e("work ", w.getName());
                }
            }
            repairCollection.put(repair.getName(), wrk);
        }
    }
}
