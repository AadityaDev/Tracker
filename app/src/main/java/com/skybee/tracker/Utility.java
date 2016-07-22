package com.skybee.tracker;

import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.TimeCard;

public class Utility {

    public static TimeCard setEventType(TimeCard timeCard, int size) {
        switch (size) {
            case 1:
                timeCard.setEvent(Constants.HeadingText.CHECK_IN);
                break;
            case 2:
                timeCard.setEvent(Constants.HeadingText.CHECK_LUNCH);
                break;
            case 3:
                timeCard.setEvent(Constants.HeadingText.CHECK_OUT);
                break;
            case 4:
                timeCard.setEvent(Constants.HeadingText.CHECK_OFF);
                break;
        }
        return timeCard;
    }


}
