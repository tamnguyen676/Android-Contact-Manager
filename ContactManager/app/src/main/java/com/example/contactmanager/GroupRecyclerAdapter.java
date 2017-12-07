package com.example.contactmanager;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for the Group RecyclerView
 */
public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.GroupViewHolder>{
    private int mNumItems;
    private ArrayList<Group> groupList;
    MainActivity mainActivity;


    public GroupRecyclerAdapter(int numItems, MainActivity mainActivity){
        mNumItems = numItems;
        this.mainActivity = mainActivity;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        public final TextView groupNameTextView;
        //public final ImageView contactImageView;

        public GroupViewHolder(View itemView) {
            super(itemView);

            groupNameTextView = (TextView) itemView.findViewById(R.id.groupName);
            //contactImageView = (ImageView) itemView.findViewById(R.id.imgListProfilePicture);
        }
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview2_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        GroupViewHolder viewHolder = new GroupViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, final int position) {
        Group group = groupList.get(position);
        String contactInfo = group.getGroupName();

        holder.groupNameTextView.setText(contactInfo);
        //holder.contactImageView.setImageURI(Uri.parse(contact.getImageUri()));

        holder.groupNameTextView.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                mainActivity.viewGroup(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (groupList != null) {
            return groupList.size();
        }
        return 0;
    }

    public void updateList(ArrayList<Group> list){
        groupList = list;
        notifyDataSetChanged();
    }
}
