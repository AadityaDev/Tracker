package com.skybee.tracker.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skybee.tracker.R;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.User;

import java.util.List;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserCardViewHolder> {

    private List<User> userList;

    public UserCardAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemView = layoutInflater.inflate(R.layout.employee_info_card, parent, false);
        return new UserCardViewHolder(parent.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(final UserCardViewHolder holder, int position) {
        final User user = userList.get(position);
        if (user != null) {
            if (user.getUserName() != null) {
                holder.employeeName.setText(user.getUserName());
            }
            if (user.getUserLatitude() != 0 && user.getUserLongitude() != 0) {

            }
            if (user.getUserImage() != null) {
//                holder.employeeImage.setImageResource();
            }
            if(user.getUserMobileNumber()!=null){
                holder.employeeMobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri number = Uri.parse("tel:"+user.getUserMobileNumber());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        view.getContext().startActivity(callIntent);
                    }
                });
            }
            holder.employeeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putLong(Constants.UserStore.USER_ID,user.getId());
                    Toast.makeText(holder.context,"UserId"+user.getId(),Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getId();
    }

    public static class UserCardViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private TextView employeeName;
        private TextView employeeLocation;
        private TextView employeeStatus;
        private ImageView employeeMobile;
        private CardView employeeCard;

        public UserCardViewHolder(Context context,View itemView) {
            super(itemView);
            this.context=context;
            employeeCard=(CardView)itemView.findViewById(R.id.employee_card);
            employeeName = (TextView) itemView.findViewById(R.id.employee_name);
            employeeMobile=(ImageView)itemView.findViewById(R.id.mobile_image);
//            employeeLocation = (TextView) itemView.findViewById(R.id.employee_location);
//            employeeStatus=(TextView)itemView.findViewById(R.id.e);
//            employeeImage = (ImageView) itemView.findViewById(R.id.employee_image);
        }
    }
}
