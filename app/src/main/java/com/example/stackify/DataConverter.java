package com.example.stackify;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    // Converts a List<BoxModel> to a string
    @TypeConverter
    public String fromListToString(List<BoxModel> list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BoxModel>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public List<BoxModel> fromStringToList(String boxListString) {
        if (boxListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BoxModel>>() {}.getType();
        List<BoxModel> countryLangList = gson.fromJson(boxListString, type);
        return countryLangList;
    }
}
