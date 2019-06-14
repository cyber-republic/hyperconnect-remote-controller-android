package com.hyper.connect.management;

import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.management.concurrent.AttributeThread;
import com.hyper.connect.management.concurrent.HistoryThread;

import java.util.HashMap;
import java.util.Map;

public class AttributeManagement{
    private Map<Integer, AttributeThread> attributeThreadMap;
    private HistoryThread historyThread=null;

    public AttributeManagement(){
        attributeThreadMap=new HashMap<Integer, AttributeThread>();
    }

    public void startAttribute(String deviceUserId, int attributeId, int edgeAttributeId){
        AttributeThread attributeThread=new AttributeThread(deviceUserId, edgeAttributeId);
        attributeThread.start();
        attributeThreadMap.put(attributeId, attributeThread);
    }

    public void stopAttribute(int attributeId){
        AttributeThread attributeThread=attributeThreadMap.get(attributeId);
        if(attributeThread!=null){
            attributeThread.stop();
            attributeThreadMap.remove(attributeId);
        }
    }

    public boolean isAttributeRunning(int attributeId){
        boolean isRunning=true;
        AttributeThread attributeThread=attributeThreadMap.get(attributeId);
        if(attributeThread==null){
            isRunning=false;
        }
        return isRunning;
    }

    public void stopAllAttributes(){
        for(Map.Entry<Integer, AttributeThread> entry : attributeThreadMap.entrySet()){
            int attributeId=entry.getKey();
            AttributeThread attributeThread=entry.getValue();
            attributeThread.stop();
        }
        attributeThreadMap.clear();
    }

    public void pauseAllAttributes(){
        for(Map.Entry<Integer, AttributeThread> entry : attributeThreadMap.entrySet()){
            AttributeThread attributeThread=entry.getValue();
            attributeThread.stop();
        }
    }

    public void resumeAllAttributes(){
        for(Map.Entry<Integer, AttributeThread> entry : attributeThreadMap.entrySet()){
            AttributeThread attributeThread=entry.getValue();
            attributeThread.start();
        }
    }

    public void startHistory(String deviceUserId, int edgeAttributeId){
        historyThread=new HistoryThread(deviceUserId, edgeAttributeId);
        historyThread.start();
    }

    public void stopHistory(){
        if(historyThread!=null){
            historyThread.stop();
            historyThread=null;
        }
    }

    public boolean isHistoryRunning(){
        boolean isRunning=true;
        if(historyThread==null){
            isRunning=false;
        }
        return isRunning;
    }
}
