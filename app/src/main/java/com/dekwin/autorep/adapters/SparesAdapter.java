package com.dekwin.autorep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Spare;

import java.util.ArrayList;

public class SparesAdapter extends BaseAdapter {

    Context context;
    ArrayList<Spare> spareList;

    public SparesAdapter(Context context, ArrayList<Spare> list) {

        this.context = context;
        spareList = list;
    }

    @Override
    public int getCount() {

        return spareList.size();
    }

    @Override
    public Object getItem(int position) {

        return spareList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Spare spare = spareList.get(position);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spare_info_element, null);

        }
        TextView spareId = (TextView) convertView.findViewById(R.id.spare_name);
        spareId.setText(spare.getName());
        TextView tvName = (TextView) convertView.findViewById(R.id.spare_price);
        tvName.setText(spare.getPrice() + "");


        return convertView;
    }

}