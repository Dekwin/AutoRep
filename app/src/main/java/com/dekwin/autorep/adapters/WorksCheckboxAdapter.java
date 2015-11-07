package com.dekwin.autorep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Spare;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;

/**
 * Created by dekst on 05.11.2015.
 */
public class WorksCheckboxAdapter extends ArrayAdapter<Work> {


        private ArrayList<Work> worksList;

        public WorksCheckboxAdapter(Context context, int textViewResourceId,
                               ArrayList<Work> worksList) {
            super(context, textViewResourceId, worksList);
            this.worksList = new ArrayList<>();
            this.worksList.addAll(worksList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.work_info_element, null);

                holder = new ViewHolder();

                holder.name = (CheckBox) convertView.findViewById(R.id.work_info_element_checkbox);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                       Work work = (Work) cb.getTag();
                        Toast.makeText(getContext().getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                      //  work.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Work work = worksList.get(position);
          //  holder.code.setText(" (" +  spare.getCode() + ")");
            holder.name.setText(work.getName());
          //  holder.name.setChecked(work.isSelected());
            holder.name.setTag(work);

            return convertView;

        }


}
