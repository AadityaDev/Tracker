package com.skybee.tracker.ui.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.activities.MapActivity;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.AttendancePojo;
import com.skybee.tracker.model.RosterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.RoasterViewHolder> {

    private final String TAG=this.getClass().getSimpleName();
    private GPSTracker gpsTracker;
    public List<RosterPojo> rosterList;
    private boolean isActionCard;
    private boolean isAttendanceCard;
    private int isSiteCard;
    private UserStore userStore;

    public RosterAdapter(List<RosterPojo> rosterList) {
        this.rosterList = rosterList;
    }

    public RosterAdapter(List<RosterPojo> rosterList, boolean isActionCard) {
        this.rosterList = rosterList;
        this.isActionCard = isActionCard;
    }

    public RosterAdapter(List<RosterPojo> rosterList, boolean isActionCard, boolean isAttendanceCard) {
        this.rosterList = rosterList;
        this.isAttendanceCard = isAttendanceCard;
    }

    public RosterAdapter(Context context, List<RosterPojo> rosterList, int isSiteCard) {
        this.rosterList = rosterList;
        this.isSiteCard = isSiteCard;
        gpsTracker = new GPSTracker(context);
        userStore = new UserStore(context);
    }

    @Override
    public RoasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (isSiteCard == Constants.isSiteCard) {
            itemView = layoutInflater.inflate(R.layout.roster_cutomer_detail_card, parent, false);
        } else if (isActionCard == true) {
            itemView = layoutInflater.inflate(R.layout.roster_no_action_card, parent, false);
        } else if (isAttendanceCard == true) {
            itemView = layoutInflater.inflate(R.layout.attendance_list_item, parent, false);
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
            } else if (!TextUtils.isEmpty(roaster.getCompany_name())) {
                holder.customerCompanyName.setText(roaster.getCompany_name());
            }
//            if (holder.status != null) {
//                if (roaster.getLogin_status() == 0) {
////                    holder.status.setText("ABSENT");
//                } else if (roaster.getLogin_status() == 1) {
//                    holder.status.setText("Status : " + "PRESENT");
//                } else if (roaster.getLogin_status() == 2) {
//                    holder.status.setText("Status : " + "LOGOUT");
//                } else if (roaster.getLogin_status() == 3) {
//                    holder.status.setText("Status : " + "OFF DUTY");
//                }
//            }
//            if (!TextUtils.isEmpty(roaster.getCustomerName()) && holder.customerName != null) {
//                holder.customerName.setText(roaster.getCustomerName());
//            }
            if (holder.taskName != null && !TextUtils.isEmpty(roaster.getTaskName())) {
                holder.taskName.setText(roaster.getTaskName());
            }
            if (!TextUtils.isEmpty(roaster.getDate()) && !TextUtils.isEmpty(roaster.getDate_to())) {
                holder.workDate.setText("Date: " + Utility.customizedDate(roaster.getDate())
                            +" - " + Utility.customizedDate(roaster.getDate_to()));
            }
            if ((roaster.getDate() == null) && !(TextUtils.isEmpty(roaster.getCreated())) && (holder.workDate != null)) {
                    holder.workDate.setText(Utility.customizedDate(roaster.getCreated()));
            }
            if (!TextUtils.isEmpty(roaster.getTime_from()) && !TextUtils.isEmpty(roaster.getTime_to())) {

                holder.workTime.setText("Time: " + Utility.convertTo24Hour(roaster.getTime_from()) + " - " + Utility.convertTo24Hour(roaster.getTime_to()));
            }
            if (!TextUtils.isEmpty(roaster.getDay()) && !TextUtils.isEmpty(roaster.getDay_to())) {
                holder.workDay.setText("Day: " + roaster.getDay() + " - " + roaster.getDay_to());
            }
            if (!TextUtils.isEmpty(roaster.getTotal_hours())) {
                holder.workDay.setText("Work Hours: " + roaster.getTotal_hours());
            }
            if (holder.locationText != null && !TextUtils.isEmpty(roaster.getAddress())) {
                holder.locationText.setText(roaster.getAddress());
            } else if (holder.locationText != null) {
                holder.locationText.setText("Lat: " + roaster.getLatitude() + " Long: " + roaster.getLongitude());
            }
            if (!TextUtils.isEmpty(roaster.getCOMPANY()) && holder.customerNameText != null) {
                holder.customerNameText.setText(roaster.getCOMPANY());
            }
//            if (holder.customerCall != null && !TextUtils.isEmpty(roaster.getMobile())) {
//                holder.customerCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Uri number = Uri.parse("tel:" + roaster.getMobile());
//                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//                        view.getContext().startActivity(callIntent);
//                    }
//                });
//            }
            if (holder.acceptRoster != null) {
                holder.acceptRoster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.rosterAction(holder.context, API.ACCEPTED_ROSTER_ACTION, API.Roster.CONFIRMED,
                                new ProgressDialog(holder.context), rosterList.get(position).getRoster_id(), true);
                    }
                });
            }
            if (holder.rejectRoster != null) {
                holder.rejectRoster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.rosterAction(holder.context, API.ACCEPTED_ROSTER_ACTION, API.Roster.REJECTED,
                                new ProgressDialog(holder.context), rosterList.get(position).getRoster_id(), false);
                    }
                });
            }
            if (holder.markOffDuty != null) {
                if (roaster.isOff_btn_status() == false && roaster.isMark_btn_status()) {
                    holder.markOffDuty.setCardBackgroundColor(holder.context.getResources().getColor(R.color.material_red));
                    holder.markOffDuty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserStore userStore = new UserStore(holder.context);
                            AttendancePojo attendancePojo = new AttendancePojo();
                            attendancePojo.setRoster_id(roaster.getRoster_id());
                            attendancePojo.setCustomer_site_id(roaster.getCustomer_site_id());
                            if (userStore.getUserDetails() != null) {
                                attendancePojo.setLattitude(userStore.getUserDetails().getUserLatitude());
                                attendancePojo.setLongitude(userStore.getUserDetails().getUserLongitude());
                            }
                            attendancePojo.setLoginStatus(Constants.LOGIN_STATUS.OFF_DUTY);
                            if (!TextUtils.isEmpty(roaster.getCOMPANY())) {
                                attendancePojo.setCompany_name(roaster.getCOMPANY());
                            }
                            if (!TextUtils.isEmpty(Utility.getIMEINumber(holder.context))) {
                                attendancePojo.setImei_in(Utility.getIMEINumber(holder.context));
                            }
                            if (!TextUtils.isEmpty(Utility.getIPAddress(holder.context))) {
                                attendancePojo.setIp_in(Utility.getIPAddress(holder.context));
                            }
                            ProgressDialog progressDialog = ProgressDialog.show(holder.context, "", "Loading...", true);
                            Utility.saveOffDuty(holder.context, attendancePojo, progressDialog);
                        }
                    });
                } else if (roaster.isOff_btn_status()) {
                    holder.markOffDuty.setCardBackgroundColor(holder.context.getResources().getColor(R.color.answer_grey));
                    holder.markOffDuty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(holder.context, "Your are already clocked out.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    holder.markOffDuty.setCardBackgroundColor(holder.context.getResources().getColor(R.color.answer_grey));
                    holder.markOffDuty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(holder.context, "Please clock in to start your duty.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            if (holder.cardView != null) {
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Fragment fragment = new Map();
                        Bundle bundle = new Bundle();
                        bundle.putString("company", roaster.getCOMPANY());
                        bundle.putDouble("Lat", roaster.getLatitude());
                        bundle.putDouble("Long", roaster.getLongitude());
                        Intent intent = new Intent(holder.context, MapActivity.class);
                        intent.putExtras(bundle);
                        holder.context.startActivity(intent);
//                        fragment.setArguments(bundle);
//                        FragmentManager fragmentManager;
//                        FragmentTransaction fragmentTransaction;
//                        fragmentManager = ((FragmentActivity) holder.context).getSupportFragmentManager();
//                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frame_view, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
                    }
                });
            }
            if (holder.clockInTime != null && !TextUtils.isEmpty(roaster.getTimeOne())) {
                    holder.clockInTime.setText(Utility.customizedDate(roaster.getTimeOne()));
            }
            if (holder.clockOutTime != null) {
                    holder.clockOutTime.setText("Clock Out Time:" + Utility.customizedDate(roaster.getTimeTwo()));
            }
            if (holder.totalTime != null && ((!TextUtils.isEmpty(roaster.getTimeOne())) && (!TextUtils.isEmpty(roaster.getTimeTwo())))) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date1 = format.parse(roaster.getTimeOne().replace("-", "/"));
                    Date date2 = format.parse(roaster.getTimeTwo().replace("-", "/"));
                    long diff = date2.getTime() - date1.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    holder.totalTime.setText("Worked Hours: " + diffHours + " hours " + diffMinutes + " minutes");
                } catch (Exception e) {

                }
//                holder.totalTime.setText("Total Time: "+DateUtils.formatElapsedTime(Date.parse(roaster.getTimeTwo())-Date.parse(roaster.getTimeTwo())));
            }
            if (holder.markAttendance != null) {
                if (roaster.isMark_btn_status()) {
                    holder.markAttendance.setCardBackgroundColor(holder.context.getResources().getColor(R.color.answer_grey));
                    holder.markAttendance.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(holder.context, "Your are already clocked in.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    holder.markAttendance.setCardBackgroundColor(holder.context.getResources().getColor(R.color.material_green));
                    holder.markAttendance.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AttendancePojo attendancePojo = new AttendancePojo();
                            attendancePojo.setRoster_id(roaster.getRoster_id());
                            if (roaster.getCustomer_site_id() != 0)
                                attendancePojo.setCustomer_site_id(roaster.getCustomer_site_id());
                            if (userStore != null && gpsTracker != null) {
                                User user = new User();
                                user = userStore.getUserDetails();
                                if (gpsTracker.getLatitude() != 0 || gpsTracker.getLongitude() != 0) {
                                    userStore.saveLatitude(gpsTracker.getLatitude());
                                    userStore.saveLongitude(gpsTracker.getLongitude());
                                    attendancePojo.setLattitude(gpsTracker.getLatitude());
                                    attendancePojo.setLongitude(gpsTracker.getLongitude());
                                    attendancePojo.setLoginStatus(Constants.LOGIN_STATUS.PRESENT);
                                    Log.d("Location Save", "Lat: " + gpsTracker.getLatitude() + "Longi: " + gpsTracker.getLongitude());
                                }
                                if (!TextUtils.isEmpty(roaster.getCOMPANY())) {
                                    attendancePojo.setCompany_name(roaster.getCOMPANY());
                                }
                                if (!TextUtils.isEmpty(Utility.getIMEINumber(holder.context))) {
                                    Log.d("RosterAdapter", Utility.getIMEINumber(holder.context));
                                    attendancePojo.setImei_in(Utility.getIMEINumber(holder.context));
                                }
                                if (!TextUtils.isEmpty(Utility.getIPAddress(holder.context))) {
                                    Log.d("RosterAdapter", Utility.getIPAddress(holder.context));
                                    attendancePojo.setIp_in(Utility.getIPAddress(holder.context));
                                }
                                attendancePojo.setRoster_id(roaster.getRoster_id());
                                userStore.saveCompanyLatitude(roaster.getLatitude());
                                userStore.saveCompanyLongitude(roaster.getLongitude());
                                userStore.saveCompanyRadius(roaster.getRadius());
                                userStore.saveCompanyId(roaster.getCustomer_site_id());
                                userStore.saveRosterId(roaster.getRoster_id());
                                if (!TextUtils.isEmpty(roaster.getCUSTOMERNAME())) {
                                    userStore.saveUserCompany(roaster.getCUSTOMERNAME());
                                } else if (!TextUtils.isEmpty(roaster.getCOMPANY())) {
                                    userStore.saveUserCompany(roaster.getCUSTOMERNAME());
                                }
                                ProgressDialog progressDialog = ProgressDialog.show(holder.context, "", "Loading...", true);
                                String ds="Lat: "+attendancePojo.getLattitude()+" Long: "+attendancePojo.getLongitude()+
                                        " Company Name: " +attendancePojo.getCompany_name()+" Status: "+attendancePojo.getLoginStatus()+
                                        " Imei: "+attendancePojo.getImei_in()+" IP: "+attendancePojo.getIp_in()+
                                        " Roster id: "+attendancePojo.getRoster_id();
                                Snackbar snackbar=Snackbar.make(holder.markAttendance,ds,Snackbar.LENGTH_INDEFINITE);
                                snackbar.show();
                                Utility.saveAttendance(holder.context, attendancePojo, user, progressDialog, roaster, RosterAdapter.this);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return rosterList.size();
    }

    public void openFragment(@NonNull Context context, @NonNull final Fragment fragment) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static class RoasterViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView customerCompanyName;
        private TextView workDate;
        private TextView workTime;
        private TextView workDay;
        private TextView locationText;
        private TextView customerNameText;
        private TextView clockInTime;
        private TextView clockOutTime;
        private TextView totalTime;
        private TextView taskName;
        //        private TextView customerName;
        private TextView status;
        //        private ImageView customerCall;
        private CardView markAttendance;
        private CardView acceptRoster;
        private CardView rejectRoster;
        private CardView cardView;
        private CardView markOffDuty;

        public RoasterViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            customerCompanyName = (TextView) itemView.findViewById(R.id.customer_company_name);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            workDate = (TextView) itemView.findViewById(R.id.work_date);
            workTime = (TextView) itemView.findViewById(R.id.work_time);
            workDay = (TextView) itemView.findViewById(R.id.work_day);
            locationText = (TextView) itemView.findViewById(R.id.location_text);
            clockInTime = (TextView) itemView.findViewById(R.id.clock_in_date);
            clockOutTime = (TextView) itemView.findViewById(R.id.clock_out_date);
            totalTime = (TextView) itemView.findViewById(R.id.total_time);
//            customerName = (TextView) itemView.findViewById(R.id.customer_name);
            customerNameText = (TextView) itemView.findViewById(R.id.customer_name);
//            status = (TextView) itemView.findViewById(R.id.status);
//            customerCall = (ImageView) itemView.findViewById(R.id.call_customer);
            acceptRoster = (CardView) itemView.findViewById(R.id.accept_roster);
            rejectRoster = (CardView) itemView.findViewById(R.id.reject_roster);
            markAttendance = (CardView) itemView.findViewById(R.id.mark_attendance);
            cardView = (CardView) itemView.findViewById(R.id.card);
            markOffDuty = (CardView) itemView.findViewById(R.id.mark_attendance_off);
        }
    }
}