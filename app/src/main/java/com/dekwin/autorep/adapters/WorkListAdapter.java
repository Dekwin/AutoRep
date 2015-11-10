package com.dekwin.autorep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;

/**
 * Created by dekst on 10.11.2015.
 */
public class WorkListAdapter extends BaseAdapter{
    Context context;
    ArrayList<Work> list;

    public WorkListAdapter(Context context, ArrayList<Work> list) {

        this.context = context;
        this.list = list;
        Log.e("worklistadapter "," list size "+list.size());
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
        Work work = list.get(position);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.work_info_list_element, null);

        }
        TextView name = (TextView) convertView.findViewById(R.id.work_info_list_element_name);
        name.setText(work.getName());
        Log.e("worklist ","position "+position+" workname "+work.getName());
       // TextView surname = (TextView) convertView.findViewById(R.id.work_info_list_element);
       // surname.setText(work.getSurname());


        return convertView;
    }

}
