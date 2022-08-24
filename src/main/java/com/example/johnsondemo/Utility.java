package com.example.johnsondemo;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
public class Utility {

    public String defaultFormat(Timestamp timestamp){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(timestamp);

    }

    public String isoFormat(Timestamp timestamp){

        DateFormat isoFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSSXXX");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(timestamp);

    }

    public Map<String,String> updateNewTime(Timestamp timestamp){

        Map<String, String> newTime = new HashMap<>();
        newTime.put("updated", defaultFormat(timestamp));
        newTime.put("updatedISO",isoFormat(timestamp));
        newTime.put("updateduk",defaultFormat(timestamp));
        return newTime;

    }
}
