package com.example.testprogs.platforms.misc.timebetweendates;

public class Month extends TimePeriod {


    public Month(TimeBetweenDates.Date start, TimeBetweenDates.Date end) {
        super(Type.MONTH,start, end);
    }

    public static Month compute(TimeBetweenDates.Date date, int months) {
        TimeBetweenDates.Date start = new TimeBetweenDates.Date(date);
        TimeBetweenDates.Date end = new TimeBetweenDates.Date(start);
        end.addMonths(months);
        return new Month(start,end);
    }
}
