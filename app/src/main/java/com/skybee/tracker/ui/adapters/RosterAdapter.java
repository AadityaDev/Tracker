package com.skybee.tracker.ui.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.model.RosterPojo;

import java.util.List;

public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.RoasterViewHolder> {

    public List<RosterPojo> rosterList;

    public RosterAdapter(List<RosterPojo> rosterList) {
        this.rosterList = rosterList;
    }

    @Override
    public RoasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemView = layoutInflater.inflate(R.layout.roster_no_action_card, parent, false);
        return new RoasterViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final RoasterViewHolder holder, final int position) {
        final RosterPojo roaster = rosterList.get(position);
        if (roaster != null) {
            if (roaster.getCompany_name() != null) {
                holder.companyName.setText(roaster.getCompany_name());
            }
            if (roaster.getEmployeeName() != null) {
                holder.companyEmployeeName.setText(roaster.getEmployeeName());
            }
            if (roaster.getCUSTOMERNAME() != null) {
                holder.customerCompanyName.setText(roaster.getCUSTOMERNAME());
            }
            if (roaster.getEmployeeName() != null) {
                holder.customerCompanyLocation.setText(roaster.getEmployeeName());
            }
            holder.acceptRoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.rosterAction(holder.context, API.ACCEPTED_ROSTER_ACTION, API.Roster.CONFIRMED,
                            new ProgressDialog(holder.context), new long[]{rosterList.get(position).getRoster_id()});
                }
            });
            holder.rejectRoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.rosterAction(holder.context, API.ACCEPTED_ROSTER_ACTION, API.Roster.REJECTED,
                            new ProgressDialog(holder.context), new long[]{rosterList.get(position).getRoster_id()});
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rosterList.size();
    }

    public static class RoasterViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView companyName;
        private TextView companyEmployeeName;
        private TextView customerCompanyName;
        private TextView customerCompanyLocation;
        private LinearLayout acceptRoster;
        private LinearLayout rejectRoster;

        public RoasterViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            companyEmployeeName = (TextView) itemView.findViewById(R.id.employee_name);
            customerCompanyName = (TextView) itemView.findViewById(R.id.customer_company_name);
            customerCompanyLocation = (TextView) itemView.findViewById(R.id.location_text);
            acceptRoster = (LinearLayout) itemView.findViewById(R.id.accept_roster);
            rejectRoster = (LinearLayout) itemView.findViewById(R.id.reject_roster);
        }
    }
}
