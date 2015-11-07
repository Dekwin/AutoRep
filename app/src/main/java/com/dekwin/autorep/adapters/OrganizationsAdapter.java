package com.dekwin.autorep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dekwin.autorep.R;
import com.dekwin.autorep.entities.Organization;
import com.dekwin.autorep.entities.Responsible;

import java.util.ArrayList;

/**
 * Created by igor on 07.11.15.
 */
public class OrganizationsAdapter extends BaseAdapter {
    Context context;
    ArrayList<Organization> organizationsList;

    public OrganizationsAdapter(Context context, ArrayList<Organization> list) {

        this.context = context;
        organizationsList = list;
    }

    @Override
    public int getCount() {

        return organizationsList.size();
    }

    @Override
    public Object getItem(int position) {

        return organizationsList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Organization organization = organizationsList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.organizations_info_element, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.organizations_info_element_name);
        name.setText(organization.getName());
        TextView account = (TextView) convertView.findViewById(R.id.organizations_info_element_account);
        account.setText(organization.getAccount());
        TextView phone = (TextView) convertView.findViewById(R.id.organizations_info_element_phone);
        phone.setText(organization.getPhone());

        return convertView;
    }

}
