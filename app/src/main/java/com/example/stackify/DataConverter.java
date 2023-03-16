package com.example.stackify;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class DataConverter {

    // Converts a List<BoxModel> to a string
    @TypeConverter
    public String fromListToString(List<Segment> list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Segment>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public List<Segment> fromStringToList(String segmentListString) {
        if (segmentListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Segment>>() {}.getType();
        List<Segment> segmentList = gson.fromJson(segmentListString, type);
        return segmentList;
    }

    // Converts a List<Box> to a string
    @TypeConverter
    public String fromBoxListToString(List<Box> list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Box>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public List<Box> fromStringToBoxList(String boxListString) {
        if (boxListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Box>>() {}.getType();
        List<Box> boxList = gson.fromJson(boxListString, type);
        return boxList;
    }

    @TypeConverter
    public LocalDate fromTimestamp(Long epoch) {
        if (epoch == null) {
            return null;
        }
        LocalDate date = Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }
    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        if (date == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = date.atStartOfDay(zoneId).toEpochSecond() * 1000;
        return epoch;
    }
}
