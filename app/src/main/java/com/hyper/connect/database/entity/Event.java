package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.hyper.connect.database.entity.converter.EventAverageConverter;
import com.hyper.connect.database.entity.converter.EventConditionConverter;
import com.hyper.connect.database.entity.converter.EventStateConverter;
import com.hyper.connect.database.entity.converter.EventTypeConverter;
import com.hyper.connect.database.entity.enums.EventAverage;
import com.hyper.connect.database.entity.enums.EventCondition;
import com.hyper.connect.database.entity.enums.EventState;
import com.hyper.connect.database.entity.enums.EventType;

@Entity(tableName="event_table")
public class Event{
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @NonNull
    @ColumnInfo(name="globalEventId")
    private String globalEventId;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    @NonNull
    @TypeConverters(EventTypeConverter.class)
    @ColumnInfo(name="type")
    private EventType type; /*** local, global ***/

    @NonNull
    @TypeConverters(EventStateConverter.class)
    @ColumnInfo(name="state")
    private EventState state; /*** active, deactivated ***/

    @NonNull
    @TypeConverters(EventAverageConverter.class)
    @ColumnInfo(name="average")
    private EventAverage average; /*** real-time, 1m, 5m, 15m, 1h, 3h, 6h, 1d ***/

    @NonNull
    @TypeConverters(EventConditionConverter.class)
    @ColumnInfo(name="condition")
    private EventCondition condition; /*** equal to, not equal to, greater than, less than ***/

    @NonNull
    @ColumnInfo(name="conditionValue")
    private String conditionValue;

    @NonNull
    @ColumnInfo(name="triggerValue")
    private String triggerValue;

    @NonNull
    @ColumnInfo(name="sourceDeviceUserId")
    private String sourceDeviceUserId;

    @ColumnInfo(name="sourceDeviceId")
    private int sourceDeviceId;

    @ColumnInfo(name="sourceEdgeSensorId")
    private int sourceEdgeSensorId;

    @ColumnInfo(name="sourceEdgeAttributeId")
    private int sourceEdgeAttributeId;

    @NonNull
    @ColumnInfo(name="actionDeviceUserId")
    private String actionDeviceUserId;

    @ColumnInfo(name="actionDeviceId")
    private int actionDeviceId;

    @ColumnInfo(name="actionEdgeSensorId")
    private int actionEdgeSensorId;

    @ColumnInfo(name="actionEdgeAttributeId")
    private int actionEdgeAttributeId;

    @Ignore
    public Event(int id){
        this.id=id;
    }

    public Event(int id, @NonNull String globalEventId, @NonNull String name, @NonNull EventType type, @NonNull EventState state, @NonNull EventAverage average, @NonNull EventCondition condition, @NonNull String conditionValue, @NonNull String triggerValue, @NonNull String sourceDeviceUserId, int sourceDeviceId, int sourceEdgeSensorId, int sourceEdgeAttributeId, @NonNull String actionDeviceUserId, int actionDeviceId, int actionEdgeSensorId, int actionEdgeAttributeId){
        this.id=id;
        this.globalEventId=globalEventId;
        this.name=name;
        this.type=type;
        this.state=state;
        this.average=average;
        this.condition=condition;
        this.conditionValue=conditionValue;
        this.triggerValue=triggerValue;
        this.sourceDeviceUserId=sourceDeviceUserId;
        this.sourceDeviceId=sourceDeviceId;
        this.sourceEdgeSensorId=sourceEdgeSensorId;
        this.sourceEdgeAttributeId=sourceEdgeAttributeId;
        this.actionDeviceUserId=actionDeviceUserId;
        this.actionDeviceId=actionDeviceId;
        this.actionEdgeSensorId=actionEdgeSensorId;
        this.actionEdgeAttributeId=actionEdgeAttributeId;
    }

    public int getId(){
        return id;
    }

    @NonNull
    public String getGlobalEventId(){
        return globalEventId;
    }

    @NonNull
    public String getName(){
        return name;
    }

    @NonNull
    public EventType getType(){
        return type;
    }

    @NonNull
    public EventState getState(){
        return state;
    }

    @NonNull
    public EventAverage getAverage(){
        return average;
    }

    @NonNull
    public EventCondition getCondition(){
        return condition;
    }

    @NonNull
    public String getConditionValue(){
        return conditionValue;
    }

    @NonNull
    public String getTriggerValue(){
        return triggerValue;
    }

    @NonNull
    public String getSourceDeviceUserId(){
        return sourceDeviceUserId;
    }

    public int getSourceDeviceId(){
        return sourceDeviceId;
    }

    public int getSourceEdgeSensorId(){
        return sourceEdgeSensorId;
    }

    public int getSourceEdgeAttributeId(){
        return sourceEdgeAttributeId;
    }

    @NonNull
    public String getActionDeviceUserId(){
        return actionDeviceUserId;
    }

    public int getActionDeviceId(){
        return actionDeviceId;
    }

    public int getActionEdgeSensorId(){
        return actionEdgeSensorId;
    }

    public int getActionEdgeAttributeId(){
        return actionEdgeAttributeId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setGlobalEventId(@NonNull String globalEventId){
        this.globalEventId=globalEventId;
    }

    public void setName(@NonNull String name){
        this.name=name;
    }

    public void setType(@NonNull EventType type){
        this.type=type;
    }

    public void setState(@NonNull EventState state){
        this.state=state;
    }

    public void setAverage(@NonNull EventAverage average){
        this.average=average;
    }

    public void setCondition(@NonNull EventCondition condition){
        this.condition=condition;
    }

    public void setConditionValue(@NonNull String conditionValue){
        this.conditionValue=conditionValue;
    }

    public void setTriggerValue(@NonNull String triggerValue){
        this.triggerValue=triggerValue;
    }

    public void setSourceDeviceUserId(@NonNull String sourceDeviceUserId){
        this.sourceDeviceUserId=sourceDeviceUserId;
    }

    public void setSourceDeviceId(int sourceDeviceId){
        this.sourceDeviceId=sourceDeviceId;
    }

    public void setSourceEdgeSensorId(int sourceEdgeSensorId){
        this.sourceEdgeSensorId=sourceEdgeSensorId;
    }

    public void setSourceEdgeAttributeId(int sourceEdgeAttributeId){
        this.sourceEdgeAttributeId=sourceEdgeAttributeId;
    }

    public void setActionDeviceUserId(@NonNull String actionDeviceUserId){
        this.actionDeviceUserId=actionDeviceUserId;
    }

    public void setActionDeviceId(int actionDeviceId){
        this.actionDeviceId=actionDeviceId;
    }

    public void setActionEdgeSensorId(int actionEdgeSensorId){
        this.actionEdgeSensorId=actionEdgeSensorId;
    }

    public void setActionEdgeAttributeId(int actionEdgeAttributeId){
        this.actionEdgeAttributeId=actionEdgeAttributeId;
    }

    @Override
    public String toString(){
        return "Event{"+
                "id="+id+'\n'+
                ", globalEventId='"+globalEventId+'\''+'\n'+
                ", name='"+name+'\''+'\n'+
                ", type="+type+'\n'+
                ", state="+state+'\n'+
                ", average="+average+'\n'+
                ", condition="+condition+'\n'+
                ", conditionValue='"+conditionValue+'\''+'\n'+
                ", triggerValue='"+triggerValue+'\''+'\n'+
                ", sourceDeviceUserId='"+sourceDeviceUserId+'\''+'\n'+
                ", sourceDeviceId="+sourceDeviceId+'\n'+
                ", sourceEdgeSensorId="+sourceEdgeSensorId+'\n'+
                ", sourceEdgeAttributeId="+sourceEdgeAttributeId+'\n'+
                ", actionDeviceUserId='"+actionDeviceUserId+'\''+'\n'+
                ", actionDeviceId="+actionDeviceId+'\n'+
                ", actionEdgeSensorId="+actionEdgeSensorId+'\n'+
                ", actionEdgeAttributeId="+actionEdgeAttributeId+'\n'+
                '}';
    }
}
