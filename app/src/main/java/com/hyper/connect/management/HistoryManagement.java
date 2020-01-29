package com.hyper.connect.management;

import android.content.Context;
import android.se.omapi.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class HistoryManagement{
    private File historyDir;

    public HistoryManagement(Context context){
        historyDir=new File(context.getFilesDir(), "history");
    }

    public Map<String, String> getHistory(int deviceId, String fileName){
        File deviceDir=new File(historyDir, Integer.toString(deviceId));
        File averageFile=new File(deviceDir, fileName);
        Map<String, String> historyMap=null;
        try{
            Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            JsonReader jsonReader=new JsonReader(new FileReader(averageFile));
            Type token=new TypeToken<HashMap<String, String>>(){}.getType();
            historyMap=gson.fromJson(jsonReader, token);
        }
        catch(FileNotFoundException fnfe){
            historyMap=null;
        }
        return historyMap;
    }
}
