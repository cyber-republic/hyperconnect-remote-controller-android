package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.hyper.connect.database.entity.converter.NotificationCategoryConverter;
import com.hyper.connect.database.entity.converter.NotificationTypeConverter;
import com.hyper.connect.database.entity.enums.NotificationCategory;
import com.hyper.connect.database.entity.enums.NotificationType;

@Entity(tableName="notification_table")
public class Notification{
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @NonNull
    @ColumnInfo(name="deviceUserId")
    private String deviceUserId;

    @ColumnInfo(name="edgeNotificationId")
    private int edgeNotificationId;

    @NonNull
    @TypeConverters(NotificationTypeConverter.class)
    @ColumnInfo(name="type")
    private NotificationType type; /*** success, warning, error ***/

    @NonNull
    @TypeConverters(NotificationCategoryConverter.class)
    @ColumnInfo(name="category")
    private NotificationCategory category; /*** device, sensor, attribute, event, system ***/

    @NonNull
    @ColumnInfo(name="edgeThingId")
    private String edgeThingId; /*** device(deviceUserId), sensor(sensorId), attribute(attributeId), event(globalEventId) ***/

    @NonNull
    @ColumnInfo(name="message")
    private String message;

    @NonNull
    @ColumnInfo(name="dateTime")
    private String dateTime;

    public Notification(int id, @NonNull String deviceUserId, int edgeNotificationId, @NonNull NotificationType type, @NonNull NotificationCategory category, @NonNull String edgeThingId, @NonNull String message, @NonNull String dateTime){
        this.id=id;
        this.deviceUserId=deviceUserId;
        this.edgeNotificationId=edgeNotificationId;
        this.type=type;
        this.category=category;
        this.edgeThingId=edgeThingId;
        this.message=message;
        this.dateTime=dateTime;
    }

    public int getId(){
        return id;
    }

    @NonNull
    public String getDeviceUserId(){
        return deviceUserId;
    }

    public int getEdgeNotificationId(){
        return edgeNotificationId;
    }

    @NonNull
    public NotificationType getType(){
        return type;
    }

    @NonNull
    public NotificationCategory getCategory(){
        return category;
    }

    @NonNull
    public String getEdgeThingId(){
        return edgeThingId;
    }

    @NonNull
    public String getMessage(){
        return message;
    }

    @NonNull
    public String getDateTime(){
        return dateTime;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setDeviceUserId(@NonNull String deviceUserId){
        this.deviceUserId=deviceUserId;
    }

    public void setEdgeNotificationId(int edgeNotificationId){
        this.edgeNotificationId=edgeNotificationId;
    }

    public void setType(@NonNull NotificationType type){
        this.type=type;
    }

    public void setCategory(@NonNull NotificationCategory category){
        this.category=category;
    }

    public void setEdgeThingId(@NonNull String edgeThingId){
        this.edgeThingId=edgeThingId;
    }

    public void setMessage(@NonNull String message){
        this.message=message;
    }

    public void setDateTime(@NonNull String dateTime){
        this.dateTime=dateTime;
    }
}
