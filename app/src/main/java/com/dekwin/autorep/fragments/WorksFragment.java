package com.dekwin.autorep.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.ExpandableListAdapter;
import com.dekwin.autorep.adapters.WorksAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Repair;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dekst on 07.11.2015.
 */
public class WorksFragment extends Fragment{








    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.work_info_list, container, false);
        //tbl = (ListView) findViewById(R.id.spares_list1);



        createGroupList();

        createCollection();

        expListView = (ExpandableListView) rootView.findViewById(R.id.work_info_list);
        final WorksAdapter wrkAdapter = new WorksAdapter(getActivity(), groupList, repairCollection);
        expListView.setAdapter(wrkAdapter);

        //setGroupIndicatorToRight();


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Log.e("!!!!", "6666");
                final Work selected = (Work) wrkAdapter.getChild(
                        groupPosition, childPosition);

                Bundle args = new Bundle();
                args.putBoolean("nomenu", true);
                args.putInt("workId", selected.getId());
                SparesFragment sparesFragment = new SparesFragment();
                sparesFragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, sparesFragment).addToBackStack(null)
                        .commit();
                Log.e("selected ",selected.getId()+"");

                return true;
            }
        });


        //fragmentIntListener.retArg("inner frag!!");




        return rootView;

    }
    List<Repair> groupList;
    List<String> childList;
    Map<String, List<Work>> repairCollection;
    ExpandableListView expListView;

    private void createGroupList() {
        groupList = new ArrayList<>();
        // groupList.add(new Repair(1,"HP"));
        //groupList.add(new Repair(2,"Dell"));
        // groupList.add(new Repair(1,"Lenovo"));
        groupList= DatabaseHelper.selectRepairs(null, null);
        // groupList.add("Sony");
        //groupList.add("HCL");
        //groupList.add("Samsung");
    }

    private void createCollection() {
        // preparing laptops collection(child)



        repairCollection = new LinkedHashMap<>();

        // DatabaseHelper.selectRepairs(null,null);

        //DatabaseHelper.selectWorks(DatabaseHelper.REPAIRS_COLUMN_ID+" = ",null);
        List<Work> wrk;
        for (Repair repair : groupList) {

            wrk=DatabaseHelper.selectWorks(DatabaseHelper.WORKS_COLUMN_REPAIRSID+" = "+repair.getId(),null);

            if (wrk!=null) {
                Log.e("wrk", " wrk:: " + wrk);
                for (Work w : wrk){
                    Log.e("work ",w.getName());
                }
            }
            repairCollection.put(repair.getName(), wrk);
        }


    }


}
