package com.hyper.connect.management.concurrent;

import com.google.gson.JsonObject;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.elastos.ElastosCarrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class AttributeThread{
    private String deviceUserId;
    private int edgeAttributeId;
    private ExecutorService executorService;
    private Future<?> future;
    private ElastosCarrier elastosCarrier;

    public AttributeThread(String deviceUserId, int edgeAttributeId){
        this.deviceUserId=deviceUserId;
        this.edgeAttributeId=edgeAttributeId;
        elastosCarrier=GlobalApplication.getElastosCarrier();
    }

    public void start(){
        executorService=Executors.newSingleThreadExecutor();
        Runnable runnable=new Runnable(){
            public void run(){
                while(true){
                    try{
                        JsonObject commandObject=new JsonObject();
                        commandObject.addProperty("command", "getValue");
                        commandObject.addProperty("attributeId", edgeAttributeId);
                        elastosCarrier.sendFriendMessage(deviceUserId, commandObject.toString());
                        TimeUnit.SECONDS.sleep(3);
                    }
                    catch(InterruptedException e){
                        break;
                    }
                }
            }
        };
        future=executorService.submit(runnable);
    }

    public void stop(){
        future.cancel(true);
        executorService.shutdown();
    }
}
