package com.skybee.tracker.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.TimeCard;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TimeCardAdapter extends RecyclerView.Adapter<TimeCardAdapter.TimeCardViewHolder> {

    private List<TimeCard> timeCardList;
    private Unbinder unbinder;

    public TimeCardAdapter(List<TimeCard> timeCardList) {
        this.timeCardList = timeCardList;
    }

    @Override
    public TimeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemView = layoutInflater.inflate(R.layout.time_card, parent, false);
        unbinder = ButterKnife.bind(this, parent);
        return new TimeCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimeCardViewHolder holder, int position) {
        final TimeCard timeCard = timeCardList.get(position);
        if (timeCard != null) {
            if (timeCard.getEvent() != null) {
                switch (timeCard.getEvent()) {
                    case Constants.HeadingText.CHECK_IN:
                        holder.cardBackground.setBackgroundResource(R.drawable.check_in_card_background);
                        break;
                    case Constants.HeadingText.CHECK_LUNCH:
                        holder.cardBackground.setBackgroundResource(R.drawable.lunch_card_background);
                        break;
                    case Constants.HeadingText.CHECK_OUT:
                        holder.cardBackground.setBackgroundResource(R.drawable.check_out_card_background);
                        break;
                    case Constants.HeadingText.CHECK_OFF:
                        holder.cardBackground.setBackgroundResource(R.drawable.check_off_card_background);
                        break;
                    default:
                        holder.cardBackground.setBackgroundResource(R.drawable.empty_card_background);
                        setEmptyCard(holder);
                        holder.eventText.setText("Add Lunch");
                        break;
                }
            } else {
                holder.cardBackground.setBackgroundResource(R.drawable.empty_card_background);
                setEmptyCard(holder);
                holder.eventText.setText("Add Lunch");
            }
            if (timeCard.getTime() != null) {
                holder.timeText.setText(timeCard.getTime().toString());
            }
            if (timeCard.getEvent() != null) {
                holder.eventText.setText(timeCard.getEvent());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        final TimeCard timeCard = timeCardList.get(position);
        if (timeCard.getEvent() != null) {
            switch (timeCard.getEvent()) {
                case Constants.HeadingText.CHECK_IN:
                    return Constants.Event.CHECK_IN;
                case Constants.HeadingText.CHECK_LUNCH:
                    return Constants.Event.CHECK_LUNCH;
                case Constants.HeadingText.CHECK_OUT:
                    return Constants.Event.CHECK_OUT;
                case Constants.HeadingText.CHECK_OFF:
                    return Constants.Event.CHECK_OFF;
                default:
                    return Constants.Event.EMPTY_CARD;
            }
        }
        return Constants.Event.EMPTY_CARD;
    }

    @Override
    public int getItemCount() {
        return timeCardList.size();
    }

    public void setEmptyCard(TimeCardViewHolder timeCardViewHolder) {
        timeCardViewHolder.clockImage.setVisibility(View.INVISIBLE);
        timeCardViewHolder.timeText.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) timeCardViewHolder.eventText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        timeCardViewHolder.eventText.setLayoutParams(layoutParams);
    }

    public static class TimeCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView clockImage;
        private TextView timeText;
        private TextView eventText;
        private RelativeLayout cardBackground;

        public TimeCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clockImage = (ImageView) itemView.findViewById(R.id.clock_image);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
            eventText = (TextView) itemView.findViewById(R.id.event);
            cardBackground = (RelativeLayout) itemView.findViewById(R.id.background);
        }

    }
}
