package com.dekwin.autorep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Spare;

import java.util.ArrayList;

/**
 * Created by dekst on 07.11.2015.
 */
public class ResponsibleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Responsible> responsibleList;

    public ResponsibleAdapter(Context context, ArrayList<Responsible> list) {

        this.context = context;
    responsibleList = list;
}

    @Override
    public int getCount() {

        return responsibleList.size();
    }

    @Override
    public Object getItem(int position) {

        return responsibleList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Responsible responsible = responsibleList.get(position);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.responsible_info_element, null);

        }
        TextView name = (TextView) convertView.findViewById(R.id.responsible_info_element_name);
        name.setText(responsible.getName());
        TextView surname = (TextView) convertView.findViewById(R.id.responsible_info_element_surname);
        surname.setText(responsible.getSurname());


        return convertView;
    }

}
