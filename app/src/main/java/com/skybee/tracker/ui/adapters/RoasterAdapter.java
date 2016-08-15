package com.skybee.tracker.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.model.RoasterPojo;

import java.util.List;

public class RoasterAdapter extends RecyclerView.Adapter<RoasterAdapter.RoasterViewHolder> {

    public List<RoasterPojo> roasterList;

    public RoasterAdapter(List<RoasterPojo> roasterList) {
        this.roasterList = roasterList;
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
        final RoasterPojo roaster = roasterList.get(position);
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
        }
    }

    @Override
    public int getItemCount() {
        return roasterList.size();
    }

    public static class RoasterViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView companyName;
        private TextView companyEmployeeName;
        private TextView customerCompanyName;
        private TextView customerCompanyLocation;

        public RoasterViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            companyEmployeeName = (TextView) itemView.findViewById(R.id.employee_name);
            customerCompanyName = (TextView) itemView.findViewById(R.id.customer_company_name);
            customerCompanyLocation = (TextView) itemView.findViewById(R.id.location_text);
        }
    }
}
