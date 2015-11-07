package com.dekwin.autorep.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.ResponsibleAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Responsible;


import java.util.ArrayList;

/**
 * Created by dekst on 07.11.2015.
 */
public class ResponsibleFragment extends Fragment {
    ArrayList<Responsible> responsibleList;
    ResponsibleAdapter responsibleListAdapter;
    private ListView responsibleListView;
    View rootView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.responsible_info, container, false);
        responsibleListView = (ListView)rootView.findViewById(R.id.responsible_info_list);
        setHasOptionsMenu(true);
        showResponsible(getActivity());
        setSortHeader(getActivity());


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.responsible, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.responsible_add) {
            setAddResponsible(getActivity()).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void showResponsible(final Context ctx){
        responsibleList = DatabaseHelper.selectResponsible(null);
        responsibleListAdapter = new ResponsibleAdapter(  ctx, responsibleList);
        responsibleListView.setLongClickable(true);
        responsibleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                final Responsible cursor = (Responsible) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                final int responsibleId =
                        cursor.getId();


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.responsible_dialog, null);
                builder.setView(dialogView);

                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.responsible_dialog_name);
                        EditText surname = (EditText) ((Dialog) dialog).findViewById(R.id.responsible_dialog_surname);
                        //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);
                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.RESPONSIBLE_COLUMN_NAME, name.getText().toString());
                        cv.put(DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME, surname.getText().toString());
                        cursor.setName(name.getText().toString());
                        cursor.setSurname(surname.getText().toString());
                        responsibleListAdapter.notifyDataSetChanged();
                        DatabaseHelper.updateResponsible(cv, DatabaseHelper.RESPONSIBLE_COLUMN_ID + " = ?",
                                new String[]{responsibleId + ""});
                        Toast.makeText(ctx,
                                "Updated. id: " + responsibleId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
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
                                "Deleted. id: " + responsibleId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                        responsibleList.remove(cursor);
                        responsibleListAdapter.notifyDataSetChanged();
                        DatabaseHelper.deleteResponsible(DatabaseHelper.RESPONSIBLE_COLUMN_ID + "=" + responsibleId, null);
                    }
                });
                ((EditText) dialogView.findViewById(R.id.responsible_dialog_name)).setText(cursor.getName());
                ((EditText) dialogView.findViewById(R.id.responsible_dialog_surname)).setText(cursor.getSurname());
                builder.show();
                return true;
            }
        });
        responsibleListView.setAdapter(responsibleListAdapter);
    }




    public void setSortHeader(final Context ctx){
        TextView headerId= (TextView)rootView.findViewById(R.id.responsible_info_header_name);
        headerId.setOnTouchListener(new View.OnTouchListener() {
            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.RESPONSIBLE_COLUMN_NAME + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.RESPONSIBLE_COLUMN_NAME + " DESC";
                    asc = false;
                }
                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                responsibleList = DatabaseHelper.selectResponsible(sortType);
                responsibleListAdapter = new ResponsibleAdapter(ctx, responsibleList);
                responsibleListView.setAdapter(responsibleListAdapter);
                return false;
            }
        });

        TextView headerName= (TextView)rootView.findViewById(R.id.responsible_info_header_surname);
        headerName.setOnTouchListener(new View.OnTouchListener() {

            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME + " DESC";
                    asc = false;
                }


                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                responsibleList = DatabaseHelper.selectResponsible(sortType);
                responsibleListAdapter = new ResponsibleAdapter(ctx, responsibleList);
                responsibleListView.setAdapter(responsibleListAdapter);
                return false;
            }
        });
    }






    public AlertDialog.Builder setAddResponsible(final Context ctx){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.responsible_add, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.responsible_add_name);
                EditText surname = (EditText) ((Dialog) dialog).findViewById(R.id.responsible_add_surname);

                ContentValues cvresponsible = new ContentValues();
                cvresponsible.put(DatabaseHelper.RESPONSIBLE_COLUMN_NAME, name.getText().toString());
                cvresponsible.put(DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME, surname.getText().toString());
                long lastResponsinleId = DatabaseHelper.addResponsible(null, cvresponsible);
                Responsible responsible = new Responsible((int) lastResponsinleId, name.getText().toString(), surname.getText().toString());


                //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);

                responsibleList.add(responsible);


                responsibleListAdapter.notifyDataSetChanged();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });




        return builder;

    }


}
