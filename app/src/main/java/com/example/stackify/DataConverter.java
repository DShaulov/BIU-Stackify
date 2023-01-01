package com.example.stackify;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
}
