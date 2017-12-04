package com.example.contactmanager;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.ArrayList;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder>{
    private int mNumItems;
    private ArrayList<Contact> contactList;
    MainActivity mainActivity;
    ViewGroupActivity viewGroupActivity;
    int switcher;
    public ContactRecyclerAdapter(int numItems, MainActivity mainActivity){
        mNumItems = numItems;
        this.mainActivity = mainActivity;
        switcher = 0;
    }
    public ContactRecyclerAdapter(int numItems, ViewGroupActivity viewGroupActivity){
        mNumItems = numItems;
        this.viewGroupActivity = viewGroupActivity;
        switcher = 1;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        public final TextView contactNameTextView;
        public final ImageView contactImageView;

        public ContactViewHolder(View itemView) {
            super(itemView);

            contactNameTextView = (TextView) itemView.findViewById(R.id.contactName);
            contactImageView = (ImageView) itemView.findViewById(R.id.imgListProfilePicture);
        }
    }

    @Override
    public ContactRecyclerAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ContactViewHolder viewHolder = new ContactViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        Contact contact = contactList.get(position);
        String contactInfo = contact.getName();

        holder.contactNameTextView.setText(contactInfo);
        holder.contactImageView.setImageURI(Uri.parse(contact.getImageUri()));

        holder.contactNameTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(switcher == 1)
                    viewGroupActivity.viewContact(position);
                else
                    mainActivity.viewContact(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (contactList != null) {
            return contactList.size();
        }
        return 0;
    }

    public void updateList(ArrayList<Contact> list){
        contactList = list;
        notifyDataSetChanged();
    }


}
