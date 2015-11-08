package com.dekwin.autorep.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Repair;
import com.dekwin.autorep.entities.Work;

import java.util.List;
import java.util.Map;

public class WorksAdapter extends BaseExpandableListAdapter {

    private Activity context;

    protected Map<String, List<Work>> laptopCollections;
    private List<Repair> laptops;


    public WorksAdapter(Activity context, List<Repair> laptops,
                        Map<String, List<Work>> laptopCollections) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;

        if (laptops == null)
            Log.e("Err11 ", "laptops=null");
        if (laptopCollections == null)
            Log.e("Err11 ", "laptopscollection=null");

        Log.e("Err11 ", "not null");
    }

    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition).getName()).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             final boolean isLastChild, View convertView, ViewGroup parent) {
        final Work laptop = (Work) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.work_info_list_element, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.work_info_list_element_name);

        item.setText(laptop.getName());


        //laptop.isSelected(item.isChecked());
        // CheckBox cb =(CheckBox)convertView.findViewById(R.id.work_info_element_checkbox);
        item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                //  country.setSelected(cb.isChecked());
            }
        });


        //ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        /*
        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        laptopCollections.get(laptops.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
*/


        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition).getName()).size();
    }

    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public int getGroupCount() {
        return laptops.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Repair laptopName = (Repair) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.repair_group_element,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.repair_group_element_name);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName.getName());





















        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}