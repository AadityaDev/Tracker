package com.skybee.tracker.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.TimeCard;
import com.skybee.tracker.ui.adapters.TimeCardAdapter;
import com.skybee.tracker.ui.customview.ItemClickSupport;
import com.skybee.tracker.ui.customview.StepperIndicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Home extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StepperIndicator stepperIndicator;
    private TextView checkInText;
    private TextView lunchText;
    private TextView checkOutText;
    private RecyclerView timeCards;
    private List<TimeCard> timeCardList;
    private LinearLayoutManager linearLayoutManager;
    private TimeCardAdapter timeCardAdapter;
    private int progress = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        stepperIndicator = (StepperIndicator) view.findViewById(R.id.stepper_indicator);
        checkInText = (TextView) view.findViewById(R.id.check_in_heading);
        checkInText.setText(Constants.HeadingText.CHECK_IN);
        lunchText = (TextView) view.findViewById(R.id.lunch_heading);
        lunchText.setText(Constants.HeadingText.CHECK_LUNCH);
        checkOutText = (TextView) view.findViewById(R.id.check_out_heading);
        checkOutText.setText(Constants.HeadingText.CHECK_OUT);
        timeCards = (RecyclerView) view.findViewById(R.id.time_list);
        timeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeCards.setLayoutManager(linearLayoutManager);
        timeCardList = new ArrayList<>();
        timeCardAdapter = new TimeCardAdapter(timeCardList);
        timeCards.setAdapter(timeCardAdapter);
        addTimeCard();
        ItemClickSupport.addTo(timeCards).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (timeCardList.get(position).getEvent() == null || timeCardList.get(position).getEvent().equals(Constants.HeadingText.EMPTY_CARD)) {
                    if (timeCardList.size() <= Constants.NUMBER_OF_CARDS) {
                        Utility.setEventType(timeCardList.get(position), timeCardList.size());
                        timeCardList.get(position).setTime(new Date());
                        timeCardAdapter.notifyDataSetChanged();
                        stepperIndicator.setCurrentStep(progress);
                        progress++;
                        if (timeCardList.size() < Constants.NUMBER_OF_CARDS) {
                            addTimeCard();
                        }
                    }
                }
            }
        });
        return view;
    }

    public void addTimeCard() {
        TimeCard timeCard = new TimeCard();
        timeCard.setTime(new Date());
        timeCardList.add(timeCard);
    }

}
