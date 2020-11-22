package com.codecool.stocktrader.service;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class UTCTimeProvider {
    private Map<String, Long> UTCTimeStamps;
    public Map<String, Long> provideUTCTimeStamps(String resolution){
        UTCTimeStamps = new HashMap<>();
        if (resolution.equals("1")){
            return provideUTCTimeStampsPerMin();
        } else if (resolution.equals("D")){
            return provideUTCTimeStampsPerDay();
        }
        return null;
    }

    private Map<String, Long> provideUTCTimeStampsPerDay() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        System.out.println("today is:"+dayOfWeek);

        Calendar calOpen = Calendar.getInstance();
        if (calOpen.get(Calendar.DAY_OF_MONTH) <=2 && calOpen.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            calOpen.set(Calendar.MONTH, calOpen.get(Calendar.MONTH)-1);
        } else if (calOpen.get(Calendar.DAY_OF_MONTH) <=2 && calOpen.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            calOpen.set(Calendar.MONTH, calOpen.get(Calendar.MONTH)-1);
        }
        calOpen.set(Calendar.HOUR_OF_DAY,15);
        calOpen.set(Calendar.MINUTE,30);
        calOpen.set(Calendar.SECOND,0);
        calOpen.set(Calendar.MILLISECOND,0);
        if (calOpen.get(Calendar.DAY_OF_YEAR) > 60) {
            calOpen.set(Calendar.DAY_OF_YEAR, calOpen.get(Calendar.DAY_OF_YEAR) - 60);
        } else {
            int countDaysBack = 365 - calOpen.get(Calendar.DAY_OF_YEAR);
            calOpen.set(Calendar.YEAR, calOpen.get(Calendar.YEAR)-1);
            calOpen.set(Calendar.DAY_OF_YEAR, calOpen.get(Calendar.DAY_OF_YEAR) - countDaysBack);
        }
        Calendar calClose = Calendar.getInstance();
        calClose.set(Calendar.HOUR_OF_DAY,22);
        calClose.set(Calendar.MINUTE,00);
        calClose.set(Calendar.SECOND,0);
        calClose.set(Calendar.MILLISECOND,0);


        UTCTimeStamps.put("from", calOpen.getTime().getTime()/1000);
        UTCTimeStamps.put("to", calClose.getTime().getTime()/1000);
        //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        //Date dateNow = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(dateMorning));
        return UTCTimeStamps;
    }

    private Map<String, Long> provideUTCTimeStampsPerMin(){
        UTCTimeStamps = new HashMap<>();
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        //System.out.println("today is:"+dayOfWeek);

        Calendar calOpen = Calendar.getInstance();
        calOpen.set(Calendar.HOUR_OF_DAY,15);
        calOpen.set(Calendar.MINUTE,30);
        calOpen.set(Calendar.SECOND,0);
        calOpen.set(Calendar.MILLISECOND,0);

        Calendar calClose = Calendar.getInstance();
        calClose.set(Calendar.HOUR_OF_DAY,22);
        calClose.set(Calendar.MINUTE,00);
        calClose.set(Calendar.SECOND,0);
        calClose.set(Calendar.MILLISECOND,0);

        if (dayOfWeek == 7) {
            calOpen.set(Calendar.DAY_OF_WEEK, 6);
            calClose.set(Calendar.DAY_OF_WEEK, 6);
        } else if (dayOfWeek == 1) {
            calOpen.set(Calendar.WEEK_OF_YEAR, calOpen.get(Calendar.WEEK_OF_YEAR)-1);
            calOpen.set(Calendar.DAY_OF_WEEK, 6);
            calClose.set(Calendar.WEEK_OF_YEAR, calClose.get(Calendar.WEEK_OF_YEAR)-1);
            calClose.set(Calendar.DAY_OF_WEEK, 6);
        } else if (dayOfWeek == 2 && today.get(Calendar.HOUR_OF_DAY) <= 15 && today.get(Calendar.MINUTE) <= 30){
            calOpen.set(Calendar.WEEK_OF_YEAR, calOpen.get(Calendar.WEEK_OF_YEAR)-1);
            calOpen.set(Calendar.DAY_OF_WEEK, 6);
            calClose.set(Calendar.WEEK_OF_YEAR, calClose.get(Calendar.WEEK_OF_YEAR)-1);
            calClose.set(Calendar.DAY_OF_WEEK, 6);
        }

        UTCTimeStamps.put("from", calOpen.getTime().getTime()/1000);
        UTCTimeStamps.put("to", calClose.getTime().getTime()/1000);
        //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        //Date dateNow = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(dateMorning));

        return UTCTimeStamps;
    }
}
