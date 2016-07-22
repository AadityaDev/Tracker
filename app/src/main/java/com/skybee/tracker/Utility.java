package com.skybee.tracker;

import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.TimeCard;

public class Utility {

    public void setEventType(TimeCard timeCard,String eventType){
        switch (eventType){
            case Constants.HeadingText.CHECK_IN:
                timeCard.setEvent(Constants.HeadingText.CHECK_IN);
            case Constants.HeadingText.CHECK_LUNCH:
                timeCard.setEvent(Constants.HeadingText.CHECK_LUNCH);
            case Constants.HeadingText.CHECK_OUT:
                timeCard.setEvent(Constants.HeadingText.CHECK_OUT);
            case Constants.HeadingText.CHECK_OFF:
                timeCard.setEvent(Constants.HeadingText.CHECK_OFF);
        }
    }


}
