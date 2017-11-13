package com.example.contactmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;



public class GroupListAdapter extends ArrayAdapter<Group> {

    public GroupListAdapter(@NonNull Context context, List<Group> groups) {
        super(context, R.layout.grouplistview_item, groups);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.grouplistview_item, parent, false);


        Group currentGroup = getItem(position);

        TextView name = (TextView) theView.findViewById(R.id.groupName);
        name.setText(currentGroup.getGroupName());


        return theView;
    }
}

