package com.dekwin.autorep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Spare;

import java.util.ArrayList;

/**
 * Created by dekst on 08.11.2015.
 */
public class SparesCheckBoxAdapter extends BaseAdapter{
    Context context;
    ArrayList<Spare> list;

    public SparesCheckBoxAdapter (Context context, ArrayList<Spare> list) {

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
        final Spare spare = list.get(position);

/*
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.responsible_info_element, null);

        }
        */
        /*
        TextView name = (TextView) convertView.findViewById(R.id.responsible_info_element_name);
        name.setText(spare.getName());
        TextView surname = (TextView) convertView.findViewById(R.id.responsible_info_element_surname);
        surname.setText(spare.getSurname());
*/
        CheckBox ch=new CheckBox(context);
        ch.setText(spare.getName());
        ch.setChecked(spare.isSelected());

        ch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                Log.e("checkbox: ", cb.getText().toString() + " check? " + cb.isChecked());

               spare.isSelected(cb.isChecked());


                //  country.setSelected(cb.isChecked());
            }
        });


        return ch;
    }

}
