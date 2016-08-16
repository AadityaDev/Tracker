package com.skybee.tracker.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.skybee.tracker.R;
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
        itemView = layoutInflater.inflate(R.layout.roaster_card, parent, false);
        return new RoasterViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(RoasterViewHolder holder, int position) {
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
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(roaster.isSelected());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    roaster.setSelected(b);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rosterList.size();
    }

    public long[] getSelectedRoasterList(){
        long[] selected=new long[]{};
        int i=0;
        for (RosterPojo rosterPojo:rosterList) {
            if(rosterPojo.isSelected()){
                selected[i]=rosterPojo.getRoster_id();
                i=i+1;
            }
        }
        return selected;
    }

    public static class RoasterViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private Context context;
        private TextView companyName;
        private TextView companyEmployeeName;
        private TextView customerCompanyName;
        private TextView customerCompanyLocation;

        public RoasterViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            checkBox=(CheckBox)itemView.findViewById(R.id.checkBox);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            companyEmployeeName = (TextView) itemView.findViewById(R.id.employee_name);
            customerCompanyName = (TextView) itemView.findViewById(R.id.customer_company_name);
            customerCompanyLocation = (TextView) itemView.findViewById(R.id.location_text);
        }
    }
}
