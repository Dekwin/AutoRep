package com.dekwin.autorep.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.SparesAdapter;
import com.dekwin.autorep.entities.Spare;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dekst on 04.11.2015.
 */
public class SparesFragment extends Fragment {
    private ListView tbl;

    ArrayList<Spare> sparesList;
    SparesAdapter sparesListAdapter;

    View rootView;
    private boolean notShowMenu = false;
    private int workId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.spare_info, container, false);
        setHasOptionsMenu(true);
        tbl = (ListView) rootView.findViewById(R.id.spares_list);

        notShowMenu = getArguments().getBoolean("nomenu");
        workId = getArguments().getInt("workId");
        Log.e("in notshowmenu ", notShowMenu + " size " + getArguments().size() + " workid " + workId);

        showSpares(getActivity());
        setSortHeader(getActivity());


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!notShowMenu) {
            inflater.inflate(R.menu.spares, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.spares_add) {
            setAddSpare(getActivity()).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showSpares(final Context ctx) {
        sparesList = DatabaseHelper.selectSpares(null, workId);
        sparesListAdapter = new SparesAdapter(ctx, sparesList);
        tbl.setLongClickable(true);
        tbl.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                final Spare cursor = (Spare) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                final int countryCode =
                        cursor.getId();


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.spares_dialog, null);
                builder.setView(dialogView);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.editText_spares_name);
                        EditText price = (EditText) ((Dialog) dialog).findViewById(R.id.editText_spares_price);
                        //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);
                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.SPARES_COLUMN_NAME, name.getText().toString());
                        cv.put(DatabaseHelper.SPARES_COLUMN_PRICE, price.getText().toString());
                        cursor.setName(name.getText().toString());
                        cursor.setPrice(Float.parseFloat(price.getText().toString()));
                        sparesListAdapter.notifyDataSetChanged();
                        DatabaseHelper.updateSpares(DatabaseHelper.SPARES_TABLE_NAME, cv, DatabaseHelper.SPARES_COLUMN_ID + " = ?",
                                new String[]{countryCode + ""});
                        Toast.makeText(ctx,
                                "Updated. id: " + countryCode + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //retval = 0;
                        Toast.makeText(ctx,
                                "Deleted. id: " + countryCode + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                        sparesList.remove(cursor);
                        sparesListAdapter.notifyDataSetChanged();
                        DatabaseHelper.deleteSpares(DatabaseHelper.SPARES_TABLE_NAME, DatabaseHelper.SPARES_COLUMN_ID + " = " + countryCode, null);
                        DatabaseHelper.deleteWorksSpares(DatabaseHelper.WORKS_SPARES_SPARE_ID + " = " + countryCode,null);
                    }
                });
                ((EditText) dialogView.findViewById(R.id.editText_spares_name)).setText(cursor.getName());
                ((EditText) dialogView.findViewById(R.id.editText_spares_price)).setText(cursor.getPrice() + "");
                builder.show();
                return true;
            }
        });
        tbl.setAdapter(sparesListAdapter);
    }

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static DialogFragment newInstance(int sectionNumber) {

        DialogFragment fragment = null;
        switch (sectionNumber) {
            case 1:
                fragment = new AddWorksFragment();
                break;

            default:
                fragment = new AddWorksFragment();
                break;
        }

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }


    public AlertDialog.Builder setAddSpare(final Context ctx) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.spares_add, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.spares_add_name);
                EditText price = (EditText) ((Dialog) dialog).findViewById(R.id.spares_add_price);

                ContentValues cvspare = new ContentValues();
                cvspare.put(DatabaseHelper.SPARES_COLUMN_NAME, name.getText().toString());
                cvspare.put(DatabaseHelper.SPARES_COLUMN_PRICE, price.getText().toString());
                long lastSpareId = DatabaseHelper.addSpares(null, cvspare);
                Spare spare = new Spare((int) lastSpareId, name.getText().toString(), Float.parseFloat(price.getText().toString()));

                if (worksFromFragment != null) {
                    ContentValues cv = new ContentValues();


                    for (Work w : worksFromFragment) {
                        cv.clear();

                        cv.put(DatabaseHelper.WORKS_SPARES_SPARE_ID, lastSpareId);
                        cv.put(DatabaseHelper.WORKS_SPARES_WORK_ID, w.getId());
                        Log.e("works_spares: ", "SPARE_ID: " + lastSpareId + " WORK_ID: " + w.getId());
                        DatabaseHelper.addWorksSpares(null, cv);
                        Log.e("works ", "id=" + w.getId() + " repairsid " + w.getRepairsId() + " name " + w.getName());
                    }
                } else Log.e("null point ", " null");

                //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);

                sparesList.add(spare);
                Log.e("notified ", spare.getName());

                sparesListAdapter.notifyDataSetChanged();


                fr = null;

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        Button selectWorks = (Button) dialogView.findViewById(R.id.spares_add_select_works);

        selectWorks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // FragmentManager fragmentManager = getFragmentManager();
//newInstance(1);
                if (fr == null) {
                    fr = newInstance(1);

                    fr.show(getFragmentManager().beginTransaction(), "DialogFragment");
                } else {
                    //  if (fr.getDialog()==null)
                    // fr.show(getFragmentManager().beginTransaction(), "DialogFragment");
                    if (fr.getDialog() == null)
                        fr.show(getFragmentManager().beginTransaction(), "DialogFragment");
                    else
                        fr.getDialog().show();
                }
//
                Log.e("trinda", " trinda!!!");
                //  fragmentManager.beginTransaction()
                //           .replace(R.id.container, newInstance(1)).addToBackStack(null)
                //           .commit();


            }

        });


        return builder;

    }

    private DialogFragment fr = null;


    public void setSortHeader(final Context ctx) {
        TextView headerId = (TextView) rootView.findViewById(R.id.spares_list_header_name);
        headerId.setOnTouchListener(new View.OnTouchListener() {
            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.SPARES_COLUMN_NAME + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.SPARES_COLUMN_NAME + " DESC";
                    asc = false;
                }
                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                sparesList = DatabaseHelper.selectSpares(sortType, workId);
                sparesListAdapter = new SparesAdapter(ctx, sparesList);
                tbl.setAdapter(sparesListAdapter);
                return false;
            }
        });

        TextView headerName = (TextView) rootView.findViewById(R.id.spares_list_header_price);
        headerName.setOnTouchListener(new View.OnTouchListener() {

            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.SPARES_COLUMN_PRICE + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.SPARES_COLUMN_PRICE + " DESC";
                    asc = false;
                }


                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                sparesList = DatabaseHelper.selectSpares(sortType, workId);
                sparesListAdapter = new SparesAdapter(ctx, sparesList);
                tbl.setAdapter(sparesListAdapter);
                return false;
            }
        });
    }

    List<Work> worksFromFragment = null;

    public void getWorksIdFromDialog(List<Work> works) {
        worksFromFragment = works;

    }

}