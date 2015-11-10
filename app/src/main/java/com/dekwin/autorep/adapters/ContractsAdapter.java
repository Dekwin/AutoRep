package com.dekwin.autorep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Contract;
import com.dekwin.autorep.entities.Spare;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dekst on 10.11.2015.
 */
public class ContractsAdapter extends BaseAdapter{

    Context context;
    ArrayList<Contract> list;

    public ContractsAdapter(Context context, ArrayList<Contract> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Contract contract = list.get(position);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contract_info_element, null);

        }
        TextView id = (TextView) convertView.findViewById(R.id.contract_info_element_contract_number);
        id.setText(contract.getId()+"");
        TextView name = (TextView) convertView.findViewById(R.id.contract_info_element_name);
        TextView surname = (TextView) convertView.findViewById(R.id.contract_info_element_surname);
        if (contract.getResponsible()!=null) {
            name.setText(contract.getResponsible().getName());
            surname.setText(contract.getResponsible().getSurname());
        }

        TextView orgName = (TextView) convertView.findViewById(R.id.contract_info_element_organization_name);
        TextView orgAccount = (TextView) convertView.findViewById(R.id.contract_info_element_organization_account);
        TextView orgPhone = (TextView) convertView.findViewById(R.id.contract_info_element_organization_phone);
        if (contract.getOrganization()!=null){
            orgName.setText(contract.getOrganization().getName());
            orgAccount.setText(contract.getOrganization().getAccount());
            orgPhone.setText(contract.getOrganization().getPhone());
        }

        TextView initialDate = (TextView) convertView.findViewById(R.id.contract_info_element_initial_date);
        TextView finalDate = (TextView) convertView.findViewById(R.id.contract_info_element_final_date);

        initialDate.setText(contract.getInitialDate().get(Calendar.DAY_OF_MONTH)+"."
                +contract.getInitialDate().get(Calendar.MONTH)+"."+contract.getInitialDate().get(Calendar.YEAR));


        finalDate.setText(contract.getFinalDate().get(Calendar.DAY_OF_MONTH) + "."
                + contract.getFinalDate().get(Calendar.MONTH) + "." + contract.getFinalDate().get(Calendar.YEAR));


        ListView workList = (ListView) convertView.findViewById(R.id.contract_info_element_worklist);

        ArrayList<Work> works=DatabaseHelper.selectWorksByContractId(null, Integer.toString(contract.getId()));

        for (Work w : works)
        Log.e("works","contract "+contract.getId()+" workname "+w.getName());

        WorkListAdapter workListAdapter=new WorkListAdapter(context, works);
        workList.setAdapter(workListAdapter);




        return convertView;
    }

}
