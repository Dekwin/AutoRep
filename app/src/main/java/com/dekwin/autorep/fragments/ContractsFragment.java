package com.dekwin.autorep.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.ContractsAdapter;
import com.dekwin.autorep.adapters.SparesAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Contract;
import com.dekwin.autorep.entities.Organization;
import com.dekwin.autorep.entities.Repair;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Spare;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by dekst on 09.11.2015.
 */
public class ContractsFragment extends Fragment {
    private ListView tbl;

    ArrayList<Contract> contractsList;
    ContractsAdapter contractsListAdapter;

    View rootView;
    private boolean notShowMenu = false;
    private int workId = 0;
    private float workPrice=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contract_info, container, false);
        setHasOptionsMenu(true);
        tbl = (ListView) rootView.findViewById(R.id.contract_info_list);

        notShowMenu = getArguments().getBoolean("nomenu");
        workId = getArguments().getInt("workId");
        workPrice=getArguments().getFloat("workPrice");
        Log.e("in notshowmenu ", notShowMenu + " size " + getArguments().size() + " workid " + workId);

        showContracts(getActivity());
       // setSortHeader(getActivity());


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!notShowMenu) {
            inflater.inflate(R.menu.contracts, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }else
        {
            MenuItem mi = menu.add("Цена работы без деталей: "+workPrice);
            mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            mi.setEnabled(false);
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
        if (id == R.id.contracts_add) {
           // setAddSpare(getActivity()).show();
            setAddContract(getActivity()).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showContracts(final Context ctx) {
        contractsList = DatabaseHelper.selectContracts(null);
        contractsListAdapter = new ContractsAdapter(ctx, contractsList);
        tbl.setLongClickable(true);
        tbl.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                final Contract cursor = (Contract) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                final int countryCode =
                        cursor.getId();


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
          //      View dialogView = inflater.inflate(R.layout.spares_dialog, null);
            //    builder.setView(dialogView);


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
                                "Deleted. id: " + countryCode, Toast.LENGTH_SHORT).show();
                        contractsList.remove(cursor);
                        contractsListAdapter.notifyDataSetChanged();
                        DatabaseHelper.deleteContractsWorks(DatabaseHelper.CONTRACTS_WORKS_COLUMN_CONTRACT_ID + " = " + cursor.getId(), null);
                        DatabaseHelper.deleteContract(DatabaseHelper.CONTRACTS_COLUMN_ID + " = " + cursor.getId(), null);
                    }
                });
             //   ((EditText) dialogView.findViewById(R.id.editText_spares_name)).setText(cursor.getName());
              //  ((EditText) dialogView.findViewById(R.id.editText_spares_price)).setText(cursor.getPrice() + "");
                builder.show();
                return true;
            }
        });
        tbl.setAdapter(contractsListAdapter);
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



    public AlertDialog.Builder setAddContract(final Context ctx) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.contracts_add, null);
        builder.setView(dialogView);

        final Spinner responsibleSpinner = (Spinner) dialogView.findViewById(R.id.contracts_add_responsible);
        ArrayList<Responsible> responsibleList=DatabaseHelper.selectResponsible(null);
        final ArrayAdapter<Responsible> responsibleAdapter = new ArrayAdapter<Responsible>(getActivity(), android.R.layout.simple_spinner_item, responsibleList);
        responsibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsibleSpinner.setAdapter(responsibleAdapter);

        final Spinner organizationsSpinner = (Spinner) dialogView.findViewById(R.id.contracts_add_organization);
        ArrayList<Organization> organizationsList=DatabaseHelper.selectOrganizations(null);
        final ArrayAdapter<Organization> organizationsAdapter = new ArrayAdapter<Organization>(getActivity(), android.R.layout.simple_spinner_item, organizationsList);
        responsibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationsSpinner.setAdapter(organizationsAdapter);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub




              //  EditText price = (EditText) ((Dialog) dialog).findViewById(R.id.contracts_add_organization);

                ContentValues cvcontract = new ContentValues();
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID, ((Responsible) responsibleSpinner.getSelectedItem()).getId());
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID, ((Organization) organizationsSpinner.getSelectedItem()).getId());


                long lastContractId = DatabaseHelper.addContract(null, cvcontract);
                GregorianCalendar initialDate= new GregorianCalendar();
                GregorianCalendar finalDate = new GregorianCalendar();
                Contract contract = new Contract((int) lastContractId,(Responsible) responsibleSpinner.getSelectedItem(),
                        (Organization) organizationsSpinner.getSelectedItem(),initialDate,finalDate);



                if (worksFromFragment != null) {
                    ContentValues cv = new ContentValues();


                    for (Work w : worksFromFragment) {
                        cv.clear();

                        cv.put(DatabaseHelper.CONTRACTS_WORKS_COLUMN_CONTRACT_ID, lastContractId);
                        cv.put(DatabaseHelper.CONTRACTS_WORKS_COLUMN_WORK_ID, w.getId());
                        Log.e("works_spares: ", "CONTRACT_ID: " + lastContractId + " WORK_ID: " + w.getId());
                        DatabaseHelper.addContractsWorks(null, cv);
                      //  Log.e("works ", "id=" + w.getId() + " repairsid " + w.getRepairsId() + " name " + w.getName());
                    }
                } else Log.e("null point ", " null");

                //    EditText et2 =(EditText)getActivity().findViewById(R.id.editText2);

                contractsList.add(contract);
                Log.e("notified ", contract.getId()+"");

                contractsListAdapter.notifyDataSetChanged();


                fr = null;

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        Button selectWorks = (Button) dialogView.findViewById(R.id.contracts_add_select_works);

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


    /*
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
                contractsList = DatabaseHelper.selectSpares(sortType, workId);
                contractsListAdapter = new SparesAdapter(ctx, contractsList);
                tbl.setAdapter(contractsListAdapter);
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
                contractsList = DatabaseHelper.selectSpares(sortType, workId);
                contractsListAdapter = new SparesAdapter(ctx, contractsList);
                tbl.setAdapter(contractsListAdapter);
                return false;
            }
        });
    }
*/

    List<Work> worksFromFragment = null;

    public void getWorksIdFromDialog(List<Work> works) {
        worksFromFragment = works;

    }

}
