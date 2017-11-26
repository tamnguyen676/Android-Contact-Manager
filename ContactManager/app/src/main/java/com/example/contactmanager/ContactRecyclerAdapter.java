package com.example.contactmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder>{
    private int mNumItems;
    private ListItemClickListener onClickListener;
    private ArrayList<Contact> contactList;

    public ContactRecyclerAdapter(int numItems, ListItemClickListener listener){
        mNumItems = numItems;
        onClickListener = listener;
    }

    public interface ListItemClickListener{
        void onListItemClick(int positionClicked);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView contactNameTextView;

        public ContactViewHolder(View itemView) {
            super(itemView);

            contactNameTextView = (TextView) itemView.findViewById(R.id.contactName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int positionClicked = getAdapterPosition();
            onClickListener.onListItemClick(positionClicked);
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
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        String contactInfo = contact.getName();

        holder.contactNameTextView.setText(contactInfo);
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