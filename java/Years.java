package com.example.testprogs.platforms.misc.timebetweendates;

public class Years extends TimePeriod {


    public Years(TimeBetweenDates.Date start, TimeBetweenDates.Date end) {
        super(Type.YEAR,start, end);
    }

    public static Years compute(TimeBetweenDates.Date date, int years) {
        TimeBetweenDates.Date start = new TimeBetweenDates.Date(date);
        TimeBetweenDates.Date end = new TimeBetweenDates.Date(start);
        end.addYears(years);
        return new Years(start,end);
    }
}
