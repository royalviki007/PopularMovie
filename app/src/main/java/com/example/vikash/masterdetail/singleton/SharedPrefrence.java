package com.example.vikash.masterdetail.singleton;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vikash on 16/03/2016.
 */
public class SharedPrefrence {
    private static SharedPrefrence newInstance;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;


    private SharedPrefrence(Context context) {
        settings = context.getSharedPreferences("erosnow", Context.MODE_PRIVATE);
        editor = settings.edit();

    }

    public static SharedPrefrence getInstance(Context context) {

        if(newInstance==null) {
            newInstance = new SharedPrefrence(context);
        }
        return newInstance;
    }


    public void put(String key, int value) {
        String storedValue = settings.getString(key, null);
        if (storedValue != null && !storedValue.equalsIgnoreCase("")) {
            List<String> list = new ArrayList<String>(Arrays.asList(storedValue.split(",")));
            list.add(String.valueOf(value));
            storedValue = android.text.TextUtils.join(",", list);
        }else {
            storedValue = String.valueOf(value);
        }
        commitChanges(key, storedValue);

    }

    public void delete(String key, int value) {
        String storedValue = settings.getString(key, null);
        if (storedValue != null && !storedValue.equalsIgnoreCase("")) {
            List<String> list = new ArrayList<String>(Arrays.asList(storedValue.split(",")));
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equalsIgnoreCase(String.valueOf(value))) {
                    list.remove(i);
                    storedValue = android.text.TextUtils.join(",", list);
                    commitChanges(key, storedValue);
                }
            }

        }
    }

    private void commitChanges(String key, String storedValue) {
        editor.clear();
        editor.putString(key, storedValue);
        editor.commit();
    }

    public List<String> getFavMovieList(String key) {
        String storedValue = settings.getString(key, null);
        List<String> list = null;
        if (storedValue != null && !storedValue.equalsIgnoreCase("")) {
            list = new ArrayList<String>(Arrays.asList(storedValue.split(",")));
        }
        return list;
    }
}
