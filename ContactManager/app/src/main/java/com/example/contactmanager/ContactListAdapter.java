package com.example.contactmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;



public class ContactListAdapter extends ArrayAdapter<Contact> {

    public ContactListAdapter(@NonNull Context context, List<Contact> contacts) {
        super(context, R.layout.listview_item, contacts);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.listview_item, parent, false);


        Contact currentContact = getItem(position);

        TextView name = (TextView) theView.findViewById(R.id.contactName);
        name.setText(currentContact.getName());
        TextView phone = (TextView) theView.findViewById(R.id.phoneNumber);
        phone.setText(currentContact.getPhone());
        TextView address = (TextView) theView.findViewById(R.id.cAddress);
        address.setText(currentContact.getAddress());
        TextView email = (TextView) theView.findViewById(R.id.emailAddress);
        email.setText(currentContact.getEmail());

        return theView;
    }
}

