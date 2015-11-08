package com.dekwin.autorep.fragments;

/**
 * Created by igor on 07.11.15.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.dekwin.autorep.adapters.OrganizationsAdapter;
import com.dekwin.autorep.adapters.ResponsibleAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Organization;
import com.dekwin.autorep.entities.Responsible;

import java.util.ArrayList;

/**
 * Created by dekst on 07.11.2015.
 */
public class OrganizationsFragment extends Fragment {
    ArrayList<Organization> organizationsList;
    OrganizationsAdapter organizationsListAdapter;
    private ListView organizationsListView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.organization_info, container, false);
        organizationsListView = (ListView) rootView.findViewById(R.id.organization_info_list);
        setHasOptionsMenu(true);
        showOrganizations(getActivity());
        setSortHeader(getActivity());


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.organizations, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.organizations_add) {
            setAddOrganization(getActivity()).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showOrganizations(final Context ctx) {
        organizationsList = DatabaseHelper.selectOrganizations(null);
        organizationsListAdapter = new OrganizationsAdapter(ctx, organizationsList);
        organizationsListView.setLongClickable(true);
        organizationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                final Organization cursor = (Organization) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                final int organizationId =
                        cursor.getId();


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.organizations_dialog, null);
                builder.setView(dialogView);

                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.organizations_dialog_name);
                        EditText account = (EditText) ((Dialog) dialog).findViewById(R.id.organizations_dialog_account);
                        EditText phone = (EditText) ((Dialog) dialog).findViewById(R.id.organizations_dialog_phone);
                        //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);
                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.ORGANIZATIONS_COLUMN_NAME, name.getText().toString());
                        cv.put(DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT, account.getText().toString());
                        cv.put(DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE, phone.getText().toString());

                        cursor.setName(name.getText().toString());
                        cursor.setAccount(account.getText().toString());
                        cursor.setPhone(phone.getText().toString());

                        organizationsListAdapter.notifyDataSetChanged();
                        DatabaseHelper.updateOrganization(cv, DatabaseHelper.ORGANIZATIONS_COLUMN_ID + " = ?",
                                new String[]{organizationId + ""});
                        Toast.makeText(ctx,
                                "Updated. id: " + organizationId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
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
                                "Deleted. id: " + organizationId + ", name: " + cursor.getName(), Toast.LENGTH_SHORT).show();
                        organizationsList.remove(cursor);
                        organizationsListAdapter.notifyDataSetChanged();
                        DatabaseHelper.deleteOrganization(DatabaseHelper.ORGANIZATIONS_COLUMN_ID + "=" + organizationId, null);
                    }
                });

                ((EditText) dialogView.findViewById(R.id.organizations_dialog_name)).setText(cursor.getName());
                ((EditText) dialogView.findViewById(R.id.organizations_dialog_account)).setText(cursor.getAccount());
                ((EditText) dialogView.findViewById(R.id.organizations_dialog_phone)).setText(cursor.getPhone());

                builder.show();
                return true;
            }
        });
        organizationsListView.setAdapter(organizationsListAdapter);
    }


    public void setSortHeader(final Context ctx) {
        TextView headerName = (TextView) rootView.findViewById(R.id.organization_info_header_name);
        headerName.setOnTouchListener(new View.OnTouchListener() {
            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.ORGANIZATIONS_COLUMN_NAME + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.ORGANIZATIONS_COLUMN_NAME + " DESC";
                    asc = false;
                }
                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                organizationsList = DatabaseHelper.selectOrganizations(sortType);
                organizationsListAdapter = new OrganizationsAdapter(ctx, organizationsList);
                organizationsListView.setAdapter(organizationsListAdapter);
                return false;
            }
        });

        TextView headerAccount = (TextView) rootView.findViewById(R.id.organization_info_header_account);
        headerAccount.setOnTouchListener(new View.OnTouchListener() {

            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT + " DESC";
                    asc = false;
                }


                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                organizationsList = DatabaseHelper.selectOrganizations(sortType);
                organizationsListAdapter = new OrganizationsAdapter(ctx, organizationsList);
                organizationsListView.setAdapter(organizationsListAdapter);
                return false;
            }
        });

        TextView headerPhone = (TextView) rootView.findViewById(R.id.organization_info_header_phone);
        headerPhone.setOnTouchListener(new View.OnTouchListener() {

            private boolean asc = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sortType;
                if (!asc) {
                    sortType = DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE + " ASC";
                    asc = true;
                } else {
                    sortType = DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE + " DESC";
                    asc = false;
                }


                //final SparesAdapter contactListAdapter = new SparesAdapter(  ctx, DatabaseHelper.selectSpares(sortType));
                organizationsList = DatabaseHelper.selectOrganizations(sortType);
                organizationsListAdapter = new OrganizationsAdapter(ctx, organizationsList);
                organizationsListView.setAdapter(organizationsListAdapter);
                return false;
            }
        });

    }


    public AlertDialog.Builder setAddOrganization(final Context ctx) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.organizations_dialog, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText name = (EditText) ((Dialog) dialog).findViewById(R.id.organizations_dialog_name);
                EditText account = (EditText) ((Dialog) dialog).findViewById(R.id.organizations_dialog_account);
                EditText phone = (EditText) ((Dialog) dialog).findViewById(R.id.organizations_dialog_phone);

                ContentValues cvorganization = new ContentValues();
                cvorganization.put(DatabaseHelper.ORGANIZATIONS_COLUMN_NAME, name.getText().toString());
                cvorganization.put(DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT, account.getText().toString());
                cvorganization.put(DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE, phone.getText().toString());
                long lastOrganizationId = DatabaseHelper.addOrganization(null, cvorganization);
                Organization organization = new Organization((int) lastOrganizationId, name.getText().toString(), account.getText().toString(), phone.getText().toString());


                //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);

                organizationsList.add(organization);


                organizationsListAdapter.notifyDataSetChanged();


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


}
