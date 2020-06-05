package com.example.testprogs.platforms.misc.timebetweendates;

import com.example.testprogs.utils.Utils;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TimeBetweenDates {

    static int[] monthDays = {-1,/*0th element not needed*/ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    static boolean isLeapYear(Date d) {
        return ((d.year & 3) == 0) && ((d.year % 100) != 0 || (d.year % 400) == 0);

    }

    public static class Date {
        int day;
        int month;
        int year;

        public Date(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public Date(Date dateStart) {
            this(dateStart.day, dateStart.month, dateStart.year);
        }

        public int getNoOfNewYearsTill(Date newDate) {
            return newDate.year - year;
        }

        static int getNumOfDaysInMonth(Date date) {
            if (date.month == 2) {
                if (isLeapYear(date)) return 29;
            }
            return monthDays[date.month];
        }

        public int getNoOfMonthsTill(Date dateTemp) {
            return dateTemp.month - month;
        }

        public int getNoOfMonthsTillNewYear() {
            return 12 + 1 - month; // CHANGED
        }

        public int getNoOfWeeksTillNewMonth() {
            int daysInThisMonth = getNumOfDaysInMonth(this);
            int weekno = (day - 1) / 7;
            int noOfWeeks = (daysInThisMonth - 1) / 7;
            return noOfWeeks - weekno;
        }

        public int getNoOfWeeksTill(Date dateEnd) {
            //same month
            int weekno = (day - 1) / 7;
            int weeknoEnd = (dateEnd.day - 1) / 7;
            return weeknoEnd - weekno;
        }


        public void addYears(int numOfYears) {
            year += numOfYears;
        }

        public void addMonths(int noOfMonths) {
            month += noOfMonths;
            if (month > 12) {
                addYears(month / 12);
                month = month % 12;
            }

        }

        public void addWeeks(int weeks) {
            for (int i = 0; i < weeks; i++) {
                int daysInThisMonth = getNumOfDaysInMonth(this);
                if (day+7 <= daysInThisMonth)
                    addDays(7);
                else
                    addDays(daysInThisMonth-day);
            }

        }

        public void addDays(int numOfDays) {
            day += numOfDays;
            while (day > getNumOfDaysInMonth(this)) {
                day = day - getNumOfDaysInMonth(this);
                addMonths(1);
            }
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Date date = (Date) o;
            return day == date.day &&
                    month == date.month &&
                    year == date.year;
        }

        public boolean lessThan(Date d2) {
            if (year < d2.year) return true;
            if (year == d2.year) {
                if (month < d2.month) return true;
                if (month == d2.month) {
                    return day < d2.day;
                }
            }
            return false;
        }
    }

    public static boolean tryAddingYear(ArrayList<TimePeriod> timePeriods, Date dateTemp, Date dateEnd) {
        if (dateTemp.day == 1 && dateTemp.month == 1) {
            int noOfYears = dateTemp.getNoOfNewYearsTill(dateEnd);
            if (noOfYears > 0) {
                timePeriods.add(Years.compute(dateTemp, noOfYears));
                dateTemp.addYears(noOfYears);
                return true;
            }
        }
        return false;
    }

    public static boolean tryAddingMonth(ArrayList<TimePeriod> timePeriods, Date dateTemp, Date dateEnd) {
        if (dateTemp.day == 1) {
            int noOfMonths = 0;

            //Check how many months till new year if not same year
            if (dateEnd.year > dateTemp.year) {
                noOfMonths = dateTemp.getNoOfMonthsTillNewYear();
            }
            //Check how many months till required month
            else {
                noOfMonths = dateTemp.getNoOfMonthsTill(dateEnd);
            }

            if (noOfMonths > 0) {
                timePeriods.add(Month.compute(dateTemp, noOfMonths));
                dateTemp.addMonths(noOfMonths);
                return true;
            }
        }
        return false;
    }

    public static boolean tryAddingWeek(ArrayList<TimePeriod> timePeriods, Date dateTemp, Date dateEnd) {
        if ((dateTemp.day - 1) % 7 == 0) {
            int noOfWeeks = 0;

            //Check how many weeks till required week
            if (dateEnd.year == dateTemp.year && dateEnd.month == dateTemp.month) {
                noOfWeeks = dateTemp.getNoOfWeeksTill(dateEnd);
            }
            //Check how many weeks till new month
            else {
                noOfWeeks = dateTemp.getNoOfWeeksTillNewMonth();
            }

            if (noOfWeeks > 0) {
                timePeriods.add(Week.compute(dateTemp, noOfWeeks));
                dateTemp.addWeeks(noOfWeeks);
                return true;
            }
        }
        return false;
    }

    public static void tryAddingDays(ArrayList<TimePeriod> timePeriods, Date dateTemp, Date dateEnd) {
        int noOfDays = 0;
        if(dateEnd.year == dateTemp.year
                && dateEnd.month == dateTemp.month
                && (dateEnd.day-1)/7 == (dateTemp.day-1)/7) {
            noOfDays = dateEnd.day - dateTemp.day;
        }
        else {
            noOfDays = 7-((dateEnd.day-1)%7);
        }
        if(noOfDays > 0) {
            timePeriods.add(Days.compute(dateTemp, noOfDays));
            dateTemp.addDays(noOfDays);
        }

    }

    public static ArrayList<TimePeriod> condense(ArrayList<TimePeriod> timePeriods) {
        if(timePeriods.size() < 2) return timePeriods;
        ArrayList<TimePeriod> timePeriodsCondensed = new ArrayList<>();
        TimePeriod prev = timePeriods.get(0);
        for (int i = 1; i < timePeriods.size(); i++) {
            if(prev.type != timePeriods.get(i).type) {
                timePeriodsCondensed.add(prev);
                prev = timePeriods.get(i);
            }else {
                prev.end = timePeriods.get(i).end;
            }
        }
        timePeriodsCondensed.add(prev);
        return timePeriodsCondensed;
    }

    public static ArrayList<TimePeriod> solve(Date dateStart, Date dateEnd) {
        Date dateTemp = new Date(dateStart);
        ArrayList<TimePeriod> timePeriods = new ArrayList<>();

        while (dateTemp.lessThan(dateEnd)) {
            //check start of year
            if (!tryAddingYear(timePeriods, dateTemp, dateEnd)) {
                if (!tryAddingMonth(timePeriods, dateTemp, dateEnd)) {
                    if (!tryAddingWeek(timePeriods, dateTemp, dateEnd)) {
                        tryAddingDays(timePeriods,dateTemp,dateEnd);
                    }
                }
            }
        }

        return condense(timePeriods);
    }

    public static void main(String[] args) {
        Date dateStart = new Date(1, 4, 2012);
        Date dateEnd = new Date(5, 4, 2013);

        ArrayList<TimePeriod> timePeriods = TimeBetweenDates.solve(dateStart, dateEnd);
        System.out.println(Utils.objToJsonPrettyStr(timePeriods, Logger.getGlobal()));
//        System.out.println(isLeapYear(dateEnd));
//        System.out.println(isLeapYear(dateStart));
    }
}
