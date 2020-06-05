package com.example.testprogs.platforms.misc.timebetweendates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class TimePeriod {
    public enum Type {
        YEAR,
        MONTH,
        WEEK,
        DAY,
        UNKNOWN
    }

    public enum MonthEnum {
        JAN(1),
        FEB(2),
        MAR(3),
        APR(4),
        MAY(5),
        JUN(6),
        JUL(7),
        AUG(8),
        SEP(9),
        OCT(10),
        NOV(11),
        DEC(12),
        UNKNOWN(13);

        int monthCode;
        MonthEnum(int m) {
            monthCode = m;
        }

        private static final Map<Integer, MonthEnum> monthmap = Collections.unmodifiableMap(initializeMapping());

        private static Map<Integer, MonthEnum> initializeMapping() {
            HashMap<Integer, MonthEnum> codeVsTypes = new HashMap<>();
            for (MonthEnum eachAttrib : MonthEnum.values())
                codeVsTypes.put(eachAttrib.monthCode, eachAttrib);
            return codeVsTypes;
        }

        public static MonthEnum getMonthFromCode(int monthCode) {
            MonthEnum mod = monthmap.get(monthCode);
            if(mod!=null)
                return mod;
            else
                return UNKNOWN;
        }
    }

    public TimePeriod.Type type = Type.UNKNOWN;
    public TimeBetweenDates.Date start;
    public TimeBetweenDates.Date end;

    public TimePeriod(TimePeriod.Type type,TimeBetweenDates.Date start, TimeBetweenDates.Date end) {
        this.type = type;
        this.start= start;
        this.end = end;
    }
}
