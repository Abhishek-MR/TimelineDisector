package com.example.testprogs.platforms.misc.timebetweendates;

public class Days extends TimePeriod {


    public Days(TimeBetweenDates.Date start, TimeBetweenDates.Date end) {
        super(Type.DAY,start, end);
    }

    public static Days compute(TimeBetweenDates.Date date, int days) {
        TimeBetweenDates.Date start = new TimeBetweenDates.Date(date);
        TimeBetweenDates.Date end = new TimeBetweenDates.Date(start);
        end.addDays(days);
        return new Days(start,end);
    }
}
