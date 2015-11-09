package com.dekwin.autorep.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.ExpandableListAdapter;
import com.dekwin.autorep.adapters.ResponsibleAdapter;
import com.dekwin.autorep.adapters.SparesCheckBoxAdapter;
import com.dekwin.autorep.adapters.WorksAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Repair;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Spare;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dekst on 07.11.2015.
 */
public class WorksFragment extends Fragment {
    //ArrayList<Spare> list;
   // ArrayAdapter<String> listAdapter;
    private ListView listView;
   private WorksAdapter wrkAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.work_info_list, container, false);
        //tbl = (ListView) findViewById(R.id.spares_list1);


        createGroupList();

        createCollection();

        expListView = (ExpandableListView) rootView.findViewById(R.id.work_info_list);
        wrkAdapter = new WorksAdapter(getActivity(), groupList, repairCollection);
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
                args.putFloat("workPrice", selected.getPrice());

                SparesFragment sparesFragment = new SparesFragment();
                sparesFragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, sparesFragment).addToBackStack(null)
                        .commit();
                Log.e("selected ", selected.getId() + "");

                return true;
            }
        });


        //fragmentIntListener.retArg("inner frag!!");
        editWorks(getActivity());

        setHasOptionsMenu(true);

        return rootView;

    }

    List<Repair> groupList;
    List<String> childList;
    Map<Repair, List<Work>> repairCollection;
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
            repairCollection.put(repair, wrk);
        }


    }



    private void editWorks(final Context ctx){
        expListView.setLongClickable(true);
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    final Work cursor = (Work) parent.getItemAtPosition(position);

                    // parent.get

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.works_add, null);
                    builder.setView(dialogView);

                    EditText name = (EditText) dialogView.findViewById(R.id.works_add_name);
                    name.setText(cursor.getName());

                    final EditText price = (EditText) dialogView.findViewById(R.id.works_add_price);
                    price.setText(cursor.getPrice()+"");

                    final ArrayList<Spare> tmpList1 = DatabaseHelper.selectSpares(null);
                    final ArrayList<Spare> tmpList2 = DatabaseHelper.selectSpares(null, cursor.getId());

                    for (Spare spare : tmpList2) {
                        spare.isSelected(true);
                        if (spare.isSelected())
                            Log.e("inselected ", spare.getName());
                    }

                    int imax = tmpList1.size();
                    int jmax = tmpList2.size();
                    for (int i = 0; i < imax; i++) {
                        for (int j = 0; j < jmax; j++) {
                            if (tmpList1.get(i).equals(tmpList2.get(j))) {
                                tmpList1.get(i).isSelected(true);
                            }
                        }
                    }


                    // list = tmpList1;


                    ListView spares = (ListView) dialogView.findViewById(R.id.works_add_spares);
                    final SparesCheckBoxAdapter sparesCheckBoxAdapter = new SparesCheckBoxAdapter(getActivity(), tmpList1);

                    spares.setAdapter(sparesCheckBoxAdapter);

                    final ArrayList<Repair> repairs = DatabaseHelper.selectRepairs(null);

                    final ArrayAdapter<Repair> adapter = new ArrayAdapter<Repair>(getActivity(), android.R.layout.simple_spinner_item, repairs);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    final Spinner spinner = (Spinner) dialogView.findViewById(R.id.works_add_spinner);
                    spinner.setAdapter(adapter);

                    final Repair groupRepair = (Repair) wrkAdapter.getGroup(groupPosition);
                    //   List<Repair> reps= DatabaseHelper.selectRepairs(DatabaseHelper.REPAIRS_COLUMN_ID + " = " + cursor.getRepairsId(), null);
                    //  Log.e("repairs id ",cursor.getRepairsId()+"");
                    //   final Repair rep = reps.get(0);
                    for (int i = 0; i < repairs.size(); i++)
                        if (groupRepair.getId() == repairs.get(i).getId()) {
                            spinner.setSelection(i);
                            break;
                        }


                    builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            // TODO Auto-generated method stub
                            EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.works_add_name);

                            //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);
                            ContentValues cv = new ContentValues();
                            cv.put(DatabaseHelper.WORKS_COLUMN_NAME, name.getText().toString());
                            cv.put(DatabaseHelper.WORKS_COLUMN_PRICE, price.getText().toString());
                            //add work!!!!

                            cursor.setName(name.getText().toString());
                            cursor.setPrice(Float.parseFloat(price.getText().toString()));
                            DatabaseHelper.updateWork(cv, DatabaseHelper.WORKS_COLUMN_ID + " = ?", new String[]{cursor.getId() + ""});

                            DatabaseHelper.deleteWorksSpares(DatabaseHelper.WORKS_SPARES_WORK_ID + " = " + cursor.getId(), null);


                            for (Spare spare : tmpList1) {
                                if (spare.isSelected()) {
                                    cv.clear();
                                    cv.put(DatabaseHelper.WORKS_SPARES_WORK_ID, cursor.getId());
                                    cv.put(DatabaseHelper.WORKS_SPARES_SPARE_ID, spare.getId());
                                    DatabaseHelper.addWorksSpares(null, cv);
                                }
                            }

                            //namellection.get(cursor.getName());
                            //    listAdapter.notifyDataSetChanged();


                            Repair selectedRepair = (Repair) spinner.getSelectedItem();
                            if (selectedRepair != null&&groupRepair.getId()!=selectedRepair.getId()) {
                                cv.clear();
                                cv.put(DatabaseHelper.WORKS_COLUMN_REPAIRSID, selectedRepair.getId());


                                repairCollection.get(groupRepair).remove(cursor);
                                for (Repair rep : repairCollection.keySet()) {
                                    if (rep.getId() == selectedRepair.getId()) {
                                        repairCollection.get(rep).add(cursor);
                                        cursor.setRepairsId(rep.getId());
                                        break;
                                    }
                                }

                                /*if ( repairCollection.get(selectedRepair)==null)
                                  Log.e("selectedRepair)==null"," null");else
                                repairCollection.get(selectedRepair).add(cursor);
                                */


                                //workList.remove(0);
                                // repairCollection.put(selectedRepair.getName(),cursor);
                                int res = DatabaseHelper.updateWork(cv, DatabaseHelper.WORKS_COLUMN_ID + " = ?", new String[]{cursor.getId() + ""});
                                Log.e("selected item= ", "updated res: " + res);
                            } else Log.e("selected item= ", "null");

                            wrkAdapter.notifyDataSetChanged();
                            // adapter.notifyDataSetChanged();




                            /*
                            DatabaseHelper.updateResponsible(cv, DatabaseHelper.WORKS_COLUMN_ID + " = ?",
                                    new String[]{currentId + ""});

                            Toast.makeText(ctx,
                                    "Updated. id: " + currentId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                                    */
                        }
                    });
                    builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });


                    builder.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            //retval = 0;
                            Toast.makeText(ctx, "Deleted. id: " + cursor.getId()
                                    + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();

                            // list.remove(cursor);
                            repairCollection.get(groupRepair).remove(cursor);

                            wrkAdapter.notifyDataSetChanged();
                            DatabaseHelper.deleteWorksSpares(DatabaseHelper.WORKS_SPARES_WORK_ID + " = " + cursor.getId(), null);
                            DatabaseHelper.deleteWork(DatabaseHelper.WORKS_COLUMN_ID+" = "+cursor.getId(),null);

                        }
                    });

                    builder.show();




                    return true;
                }

                return false;
            }
        });


        /*
        expListView.setLongClickable(true);
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                final Work cursor = (Work) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                final int currentId =
                        cursor.getId();


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.works_add, null);
                builder.setView(dialogView);
                list = DatabaseHelper.selectSpares(null);
                ListView spares = (ListView) dialogView.findViewById(R.id.works_add_spares);
                SparesCheckBoxAdapter sparesCheckBoxAdapter = new SparesCheckBoxAdapter(ctx, list);
                spares.setAdapter(sparesCheckBoxAdapter);

                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.works_add_name);

                        //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);
                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.WORKS_COLUMN_NAME, name.getText().toString());

                        cursor.setName(name.getText().toString());

                        listAdapter.notifyDataSetChanged();
                        DatabaseHelper.updateResponsible(cv, DatabaseHelper.WORKS_COLUMN_ID + " = ?",
                                new String[]{currentId + ""});

                        Toast.makeText(ctx,
                                "Updated. id: " + currentId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

                builder.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //retval = 0;
                        Toast.makeText(ctx,
                                "Deleted. id: " + currentId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                        list.remove(cursor);
                        listAdapter.notifyDataSetChanged();
                        DatabaseHelper.deleteResponsible(DatabaseHelper.RESPONSIBLE_COLUMN_ID + "=" + currentId, null);
                    }
                });

                ((EditText) dialogView.findViewById(R.id.responsible_add_name)).setText(cursor.getName());
            //    ((EditText) dialogView.findViewById(R.id.responsible_add_surname)).setText(cursor.getSurname());
                builder.show();
                return true;
            }
        });

        */
    }

    private AlertDialog.Builder setAddWork(final Context ctx) {


        // parent.get

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.works_add, null);
        builder.setView(dialogView);

        EditText name = (EditText) dialogView.findViewById(R.id.works_add_name);

        final EditText price = (EditText) dialogView.findViewById(R.id.works_add_price);

        final ArrayList<Spare> tmpList1 = DatabaseHelper.selectSpares(null);


        // list = tmpList1;


        ListView spares = (ListView) dialogView.findViewById(R.id.works_add_spares);
        final SparesCheckBoxAdapter sparesCheckBoxAdapter = new SparesCheckBoxAdapter(getActivity(), tmpList1);

        spares.setAdapter(sparesCheckBoxAdapter);

        final ArrayList<Repair> repairs = DatabaseHelper.selectRepairs(null);

        final ArrayAdapter<Repair> adapter = new ArrayAdapter<Repair>(getActivity(), android.R.layout.simple_spinner_item, repairs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.works_add_spinner);
        spinner.setAdapter(adapter);


        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                // TODO Auto-generated method stub
                EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.works_add_name);
                EditText price = (EditText) ((Dialog) dialog).findViewById(R.id.works_add_price);

                //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.WORKS_COLUMN_NAME, name.getText().toString());
                cv.put(DatabaseHelper.WORKS_COLUMN_PRICE, price.getText().toString());
                cv.put(DatabaseHelper.WORKS_COLUMN_REPAIRSID, ((Repair) spinner.getSelectedItem()).getId());


              long addedWorkId= DatabaseHelper.addWork(null,cv);
                Work work = new Work((int)addedWorkId,name.getText().toString(),Float.parseFloat(price.getText().toString()),((Repair) spinner.getSelectedItem()).getId());


                for (Spare spare : tmpList1) {
                    if (spare.isSelected()) {
                        cv.clear();
                        cv.put(DatabaseHelper.WORKS_SPARES_WORK_ID, addedWorkId);
                        cv.put(DatabaseHelper.WORKS_SPARES_SPARE_ID, spare.getId());
                        DatabaseHelper.addWorksSpares(null, cv);
                    }
                }

              Repair selectedRepair=  (Repair)spinner.getSelectedItem();
                for (Repair rep : repairCollection.keySet()) {
                    if (rep.getId() == selectedRepair.getId()) {
                        repairCollection.get(rep).add(work);
                        work.setRepairsId(rep.getId());
                        break;
                    }
                }


                wrkAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

     return builder;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


            inflater.inflate(R.menu.works, menu);
            super.onCreateOptionsMenu(menu, inflater);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.works_add) {
           setAddWork(getActivity()).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
