package com.skybee.tracker.ui.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.AttendancePojo;
import com.skybee.tracker.model.RosterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

import java.util.List;

public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.RoasterViewHolder> {

    public List<RosterPojo> rosterList;
    private boolean isActionCard;
    private int isSiteCard;

    public RosterAdapter(List<RosterPojo> rosterList) {
        this.rosterList = rosterList;
    }

    public RosterAdapter(List<RosterPojo> rosterList, boolean isActionCard) {
        this.rosterList = rosterList;
        this.isActionCard = isActionCard;
    }

    public RosterAdapter(List<RosterPojo> rosterList, int isSiteCard) {
        this.rosterList = rosterList;
        this.isSiteCard = isSiteCard;
    }

    @Override
    public RoasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (isSiteCard == Constants.isSiteCard) {
            itemView = layoutInflater.inflate(R.layout.roster_cutomer_detail_card, parent, false);
        } else if (isActionCard == true) {
            itemView = layoutInflater.inflate(R.layout.roster_no_action_card, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.roster_accept_card, parent, false);
        }
        return new RoasterViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final RoasterViewHolder holder, final int position) {
        final RosterPojo roaster = rosterList.get(position);
        if (roaster != null) {
            if (roaster.getCUSTOMERNAME() != null) {
                holder.customerCompanyName.setText(roaster.getCUSTOMERNAME());
            }
            if (!TextUtils.isEmpty(roaster.getCustomerName()) && holder.customerName != null) {
                holder.customerName.setText(roaster.getCustomerName());
            }
            if (!TextUtils.isEmpty(roaster.getTaskName())) {
                holder.taskName.setText(roaster.getTaskName());
            }
            if (!TextUtils.isEmpty(roaster.getDate()) && !TextUtils.isEmpty(roaster.getDate_to())) {
                holder.workDate.setText("Date:" + roaster.getDate() + " - " + roaster.getDate_to());
            }
            if (!TextUtils.isEmpty(roaster.getTime_from()) && !TextUtils.isEmpty(roaster.getTime_to())) {
                holder.workTime.setText("Time:" + roaster.getTime_from() + " - " + roaster.getTime_to());
            }
            if (!TextUtils.isEmpty(roaster.getDay()) && !TextUtils.isEmpty(roaster.getDay_to())) {
                holder.workDay.setText("Day:" + roaster.getDay() + " - " + roaster.getDay_to());
            }
            if (holder.customerCall != null && !TextUtils.isEmpty(roaster.getMobile())) {
                holder.customerCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri number = Uri.parse("tel:" + roaster.getMobile());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        view.getContext().startActivity(callIntent);
                    }
                });
            }
            if (holder.acceptRoster != null) {
                holder.acceptRoster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.rosterAction(holder.context, API.ACCEPTED_ROSTER_ACTION, API.Roster.CONFIRMED,
                                new ProgressDialog(holder.context), new long[]{rosterList.get(position).getRoster_id()});
                    }
                });
            }
            if (holder.rejectRoster != null) {
                holder.rejectRoster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.rosterAction(holder.context, API.ACCEPTED_ROSTER_ACTION, API.Roster.REJECTED,
                                new ProgressDialog(holder.context), new long[]{rosterList.get(position).getRoster_id()});
                    }
                });
            }
            if(holder.markAttendance!=null){
                holder.markAttendance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AttendancePojo attendancePojo=new AttendancePojo();
                        if(roaster.getCustomer_site_id()!=0)
                            attendancePojo.setCustomer_site_id(roaster.getCustomer_site_id());
                        UserStore userStore=new UserStore(holder.context);
                        User user=new User();
                        user=userStore.getUserDetails();
                        ProgressDialog progressDialog=ProgressDialog.show(holder.context,"","Loading...",true);
                        Utility.saveAttendance(holder.context,attendancePojo,user,progressDialog);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return rosterList.size();
    }

    public static class RoasterViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView customerCompanyName;
        private TextView taskName;
        private TextView workDate;
        private TextView workTime;
        private TextView workDay;
        private TextView locationText;
        private TextView customerName;
        private ImageView customerCall;
        private TextView markAttendance;
        private LinearLayout acceptRoster;
        private LinearLayout rejectRoster;

        public RoasterViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            customerCompanyName = (TextView) itemView.findViewById(R.id.customer_company_name);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            workDate = (TextView) itemView.findViewById(R.id.work_date);
            workTime = (TextView) itemView.findViewById(R.id.work_time);
            workDay = (TextView) itemView.findViewById(R.id.work_day);
            locationText = (TextView) itemView.findViewById(R.id.location_text);
            customerName = (TextView) itemView.findViewById(R.id.customer_name);
            customerCall = (ImageView) itemView.findViewById(R.id.call_customer);
            acceptRoster = (LinearLayout) itemView.findViewById(R.id.accept_roster);
            rejectRoster = (LinearLayout) itemView.findViewById(R.id.reject_roster);
            markAttendance = (TextView) itemView.findViewById(R.id.mark_attendance);
        }
    }
}
