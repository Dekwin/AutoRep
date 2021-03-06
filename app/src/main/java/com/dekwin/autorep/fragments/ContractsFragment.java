package com.dekwin.autorep.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dekwin.autorep.R;
import com.dekwin.autorep.adapters.ContractsAdapter;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Contract;
import com.dekwin.autorep.entities.Organization;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Work;
import com.dekwin.autorep.tools.DataConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

        showContracts(getActivity(), null);
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

        switch (id) {
            case R.id.contracts_add:
                setAddContract(getActivity()).show();
                break;

            case R.id.contracts_current_period:
                showContracts(getActivity(), DatabaseHelper.CONTRACTS_COLUMN_INITIAL_DATE +
                        " <= date('now') AND date('now') <= " + DatabaseHelper.CONTRACTS_COLUMN_FINAL_DATE);
                break;

            case R.id.contracts_all:

                showContracts(getActivity(), null);
                break;

            case R.id.contracts_filter:
                // showContracts(getActivity(),null);
                setFilter();
                break;

            default:

                break;
        }
/*
        if (id == R.id.contracts_add) {
           // setAddSpare(getActivity()).show();
            setAddContract(getActivity()).show();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


    public void showContracts(final Context ctx, String where) {
        contractsList = DatabaseHelper.selectContracts(where);
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
                                "Deleted. id: " + countryCode, Toast.LENGTH_SHORT).show();
                        contractsList.remove(cursor);
                        contractsListAdapter.notifyDataSetChanged();
                        DatabaseHelper.deleteContractsWorks(DatabaseHelper.CONTRACTS_WORKS_COLUMN_CONTRACT_ID + " = " + cursor.getId(), null);
                        DatabaseHelper.deleteContract(DatabaseHelper.CONTRACTS_COLUMN_ID + " = " + cursor.getId(), null);

                    }
                });

                builder.setNegativeButton("В файл", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (cursor != null) {
                            DataConverter.writeToHTML(cursor);
                            Toast.makeText(ctx,
                                    "Файл записан по пути: " + Environment.getExternalStorageDirectory().toString()
                                            + "/AutoReport_" + cursor.getId() + ".html", Toast.LENGTH_SHORT).show();
                        }

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

        DialogFragment fragment;
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


    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    public AlertDialog.Builder setAddContract(final Context ctx) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.contracts_add, null);
        builder.setView(dialogView);


        final Spinner responsibleSpinner = (Spinner) dialogView.findViewById(R.id.contracts_add_responsible);
        ArrayList<Responsible> responsibleList=DatabaseHelper.selectResponsible(null);
        final ArrayAdapter<Responsible> responsibleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, responsibleList);
        responsibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsibleSpinner.setAdapter(responsibleAdapter);

        final Spinner organizationsSpinner = (Spinner) dialogView.findViewById(R.id.contracts_add_organization);
        ArrayList<Organization> organizationsList=DatabaseHelper.selectOrganizations(null);
        final ArrayAdapter<Organization> organizationsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, organizationsList);
        responsibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationsSpinner.setAdapter(organizationsAdapter);


        fromDateEtxt = (EditText) dialogView.findViewById(R.id.contract_info_element_initial_date);
        toDateEtxt = (EditText) dialogView.findViewById(R.id.contract_info_element_final_date);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        setDateTimeField();





        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

              //  EditText price = (EditText) ((Dialog) dialog).findViewById(R.id.contracts_add_organization);

                ContentValues cvcontract = new ContentValues();
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID, ((Responsible) responsibleSpinner.getSelectedItem()).getId());
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID, ((Organization) organizationsSpinner.getSelectedItem()).getId());
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_INITIAL_DATE, fromDateEtxt.getText().toString());
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_FINAL_DATE, toDateEtxt.getText().toString());

                long lastContractId = DatabaseHelper.addContract(null, cvcontract);

                Log.e("calendar", "day " + toDatePickerDialog.getDatePicker().getDayOfMonth());

                GregorianCalendar initialDate= new GregorianCalendar();
                initialDate.set(fromDatePickerDialog.getDatePicker().getYear(),
                        fromDatePickerDialog.getDatePicker().getMonth(),
                        fromDatePickerDialog.getDatePicker().getDayOfMonth());

                GregorianCalendar finalDate = new GregorianCalendar();
                finalDate.set(toDatePickerDialog.getDatePicker().getYear(),
                        toDatePickerDialog.getDatePicker().getMonth(),
                        toDatePickerDialog.getDatePicker().getDayOfMonth());


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

                cvcontract.clear();
                float contractPrice = DatabaseHelper.getFullPriceOfContract(contract.getId() + "");
                cvcontract.put(DatabaseHelper.CONTRACTS_COLUMN_PRICE,
                        contractPrice);

                DatabaseHelper.updateContract(cvcontract, DatabaseHelper.CONTRACTS_COLUMN_ID + " = ?",
                        new String[]{contract.getId() + ""});
                contract.setOriginalPrice(contractPrice);
                // Contract co= DatabaseHelper.getFullPriceOfContract(contract.getId() + "");
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

            }

        });


        return builder;

    }


    private DialogFragment fr = null;




    List<Work> worksFromFragment = null;

    public void getWorksIdFromDialog(List<Work> works) {
        worksFromFragment = works;

    }


    private void setDateTimeField() {

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        toDateEtxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();

        fromDateEtxt.setText(dateFormatter.format(newCalendar.getTime()));
        toDateEtxt.setText(dateFormatter.format(newCalendar.getTime()));

        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                toDatePickerDialog.getDatePicker().setMinDate(newDate.getTime().getTime() - 86400000);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        fromDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTime().getTime());

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                fromDatePickerDialog.getDatePicker().setMaxDate(newDate.getTime().getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTime().getTime());

    }


    private void setFilter() {

        // Get the cursor, positioned to the corresponding row in the result set


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.contracts_filter, null);
        builder.setView(dialogView);

        final EditText contractNumber = (EditText) dialogView.findViewById(R.id.contract_filter_contract_number);
        final EditText surname = (EditText) dialogView.findViewById(R.id.contract_filter_surname);
        final EditText name = (EditText) dialogView.findViewById(R.id.contract_filter_name);
        final EditText organizationName = (EditText) dialogView.findViewById(R.id.contract_filter_organization_name);
        final EditText organizationAccount = (EditText) dialogView.findViewById(R.id.contract_filter_organization_account);
        final EditText organizationPhone = (EditText) dialogView.findViewById(R.id.contract_filter_organization_phone);
        final EditText minPrice = (EditText) dialogView.findViewById(R.id.contract_filter_minprice);
        final EditText maxPrice = (EditText) dialogView.findViewById(R.id.contract_filter_maxprice);
        final CheckBox match = (CheckBox) dialogView.findViewById(R.id.contract_filter_match_checkbox);

        String query = "";
        String machStr = "";

        /*
        if(!match.isChecked()){
            if (!contractNumber.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ID+" = "+contractNumber.getText().toString();
            }
            if (!surname.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID+" IN (SELECT "+DatabaseHelper.RESPONSIBLE_COLUMN_ID
                        +" FROM "+DatabaseHelper.RESPONSIBLE_TABLE_NAME+" WHERE "
                        +DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME+" LIKE '%"+surname.getText().toString()+"%') ";
            }
            if(!name.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID+" IN (SELECT "+DatabaseHelper.RESPONSIBLE_COLUMN_ID
                        +" FROM "+DatabaseHelper.RESPONSIBLE_TABLE_NAME+" WHERE "
                        +DatabaseHelper.RESPONSIBLE_COLUMN_NAME+" LIKE '%"+name.getText().toString()+"%') ";
            }
            if(!organizationName.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID+" IN (SELECT "+DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                        +" FROM "+DatabaseHelper.ORGANIZATIONS_TABLE_NAME+" WHERE "
                        +DatabaseHelper.ORGANIZATIONS_COLUMN_NAME+" LIKE '%"+organizationName.getText().toString()+"%') ";
            }
            if (!organizationAccount.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID+" IN (SELECT "+DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                        +" FROM "+DatabaseHelper.ORGANIZATIONS_TABLE_NAME+" WHERE "
                        +DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT+" LIKE '%"+organizationAccount.getText().toString()+"%') ";
            }
            if(!organizationPhone.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID+" IN (SELECT "+DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                        +" FROM "+DatabaseHelper.ORGANIZATIONS_TABLE_NAME+" WHERE "
                        +DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE+" LIKE '%"+organizationPhone.getText().toString()+"%') ";
            }
            if (!minPrice.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_PRICE+" >= "+minPrice.getText().toString();
            }
            if(!maxPrice.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_PRICE+" <= "+maxPrice.getText().toString();
            }
        }else {
            if (!contractNumber.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ID+" = "+contractNumber.getText().toString();
            }
            if (!surname.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID+" IN (SELECT "+DatabaseHelper.RESPONSIBLE_COLUMN_ID
                        +" FROM "+DatabaseHelper.RESPONSIBLE_TABLE_NAME+" WHERE "
                        +DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME+" = '"+surname.getText().toString()+"') ";
            }
            if(!name.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID+" IN (SELECT "+DatabaseHelper.RESPONSIBLE_COLUMN_ID
                        +" FROM "+DatabaseHelper.RESPONSIBLE_TABLE_NAME+" WHERE "
                        +DatabaseHelper.RESPONSIBLE_COLUMN_NAME+" = '"+name.getText().toString()+"') ";
            }
            if(!organizationName.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID+" IN (SELECT "+DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                        +" FROM "+DatabaseHelper.ORGANIZATIONS_TABLE_NAME+" WHERE "
                        +DatabaseHelper.ORGANIZATIONS_COLUMN_NAME+" = '"+organizationName.getText().toString()+"') ";
            }
            if (!organizationAccount.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID+" IN (SELECT "+DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                        +" FROM "+DatabaseHelper.ORGANIZATIONS_TABLE_NAME+" WHERE "
                        +DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT+" = '"+organizationAccount.getText().toString()+"') ";
            }
            if(!organizationPhone.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID+" IN (SELECT "+DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                        +" FROM "+DatabaseHelper.ORGANIZATIONS_TABLE_NAME+" WHERE "
                        +DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE+" = '"+organizationPhone.getText().toString()+"') ";
            }
            if (!minPrice.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_PRICE+" >= "+minPrice.getText().toString();
            }
            if(!maxPrice.getText().equals("")){
                query+=" AND "+DatabaseHelper.CONTRACTS_COLUMN_PRICE+" <= "+maxPrice.getText().toString();
            }
        }
*/


        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                String query = "";
                //   String machStr="";
                if (!match.isChecked()) {
                    if (!contractNumber.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ID + " = " + contractNumber.getText().toString();
                    }
                    if (!surname.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID + " IN (SELECT " + DatabaseHelper.RESPONSIBLE_COLUMN_ID
                                + " FROM " + DatabaseHelper.RESPONSIBLE_TABLE_NAME + " WHERE "
                                + DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME + " LIKE '%" + surname.getText().toString() + "%') ";
                    }
                    if (!name.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID + " IN (SELECT " + DatabaseHelper.RESPONSIBLE_COLUMN_ID
                                + " FROM " + DatabaseHelper.RESPONSIBLE_TABLE_NAME + " WHERE "
                                + DatabaseHelper.RESPONSIBLE_COLUMN_NAME + " LIKE '%" + name.getText().toString() + "%') ";
                    }
                    if (!organizationName.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID + " IN (SELECT " + DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                                + " FROM " + DatabaseHelper.ORGANIZATIONS_TABLE_NAME + " WHERE "
                                + DatabaseHelper.ORGANIZATIONS_COLUMN_NAME + " LIKE '%" + organizationName.getText().toString() + "%') ";
                    }
                    if (!organizationAccount.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID + " IN (SELECT " + DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                                + " FROM " + DatabaseHelper.ORGANIZATIONS_TABLE_NAME + " WHERE "
                                + DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT + " LIKE '%" + organizationAccount.getText().toString() + "%') ";
                    }
                    if (!organizationPhone.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID + " IN (SELECT " + DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                                + " FROM " + DatabaseHelper.ORGANIZATIONS_TABLE_NAME + " WHERE "
                                + DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE + " LIKE '%" + organizationPhone.getText().toString() + "%') ";
                    }
                    if (!minPrice.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_PRICE + " >= " + minPrice.getText().toString();
                    }
                    if (!maxPrice.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_PRICE + " <= " + maxPrice.getText().toString();
                    }
                } else {
                    if (!contractNumber.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ID + " = " + contractNumber.getText().toString();
                    }
                    if (!surname.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID + " IN (SELECT " + DatabaseHelper.RESPONSIBLE_COLUMN_ID
                                + " FROM " + DatabaseHelper.RESPONSIBLE_TABLE_NAME + " WHERE "
                                + DatabaseHelper.RESPONSIBLE_COLUMN_SURNAME + " = '" + surname.getText().toString() + "') ";
                    }
                    if (!name.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_RESPONSEID + " IN (SELECT " + DatabaseHelper.RESPONSIBLE_COLUMN_ID
                                + " FROM " + DatabaseHelper.RESPONSIBLE_TABLE_NAME + " WHERE "
                                + DatabaseHelper.RESPONSIBLE_COLUMN_NAME + " = '" + name.getText().toString() + "') ";
                    }
                    if (!organizationName.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID + " IN (SELECT " + DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                                + " FROM " + DatabaseHelper.ORGANIZATIONS_TABLE_NAME + " WHERE "
                                + DatabaseHelper.ORGANIZATIONS_COLUMN_NAME + " = '" + organizationName.getText().toString() + "') ";
                    }
                    if (!organizationAccount.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID + " IN (SELECT " + DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                                + " FROM " + DatabaseHelper.ORGANIZATIONS_TABLE_NAME + " WHERE "
                                + DatabaseHelper.ORGANIZATIONS_COLUMN_ACCOUNT + " = '" + organizationAccount.getText().toString() + "') ";
                    }
                    if (!organizationPhone.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_ORGANIZATIONID + " IN (SELECT " + DatabaseHelper.ORGANIZATIONS_COLUMN_ID
                                + " FROM " + DatabaseHelper.ORGANIZATIONS_TABLE_NAME + " WHERE "
                                + DatabaseHelper.ORGANIZATIONS_COLUMN_PHONE + " = '" + organizationPhone.getText().toString() + "') ";
                    }
                    if (!minPrice.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_PRICE + " >= " + minPrice.getText().toString();
                    }
                    if (!maxPrice.getText().toString().equals("")) {
                        query += " AND " + DatabaseHelper.CONTRACTS_COLUMN_PRICE + " <= " + maxPrice.getText().toString();
                    }
                }

                query = query.replaceFirst("AND", "");

                Log.e("Query!: ", query + "");
                showContracts(getActivity(), query);

            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });


        builder.show();


    }


}
