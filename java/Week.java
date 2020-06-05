package com.example.testprogs.platforms.misc.timebetweendates;

public class Week extends TimePeriod {


    public Week(TimeBetweenDates.Date start, TimeBetweenDates.Date end) {
        super(Type.WEEK,start, end);
    }

    public static Week compute(TimeBetweenDates.Date date, int weeks) {
        TimeBetweenDates.Date start = new TimeBetweenDates.Date(date);
        TimeBetweenDates.Date end = new TimeBetweenDates.Date(start);
        end.addWeeks(weeks);
        return new Week(start,end);
    }
}
