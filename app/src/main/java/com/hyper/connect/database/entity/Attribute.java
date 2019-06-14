package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.hyper.connect.database.entity.converter.AttributeDirectionConverter;
import com.hyper.connect.database.entity.converter.AttributeScriptStateConverter;
import com.hyper.connect.database.entity.converter.AttributeStateConverter;
import com.hyper.connect.database.entity.converter.AttributeTypeConverter;
import com.hyper.connect.database.entity.enums.AttributeDirection;
import com.hyper.connect.database.entity.enums.AttributeScriptState;
import com.hyper.connect.database.entity.enums.AttributeState;
import com.hyper.connect.database.entity.enums.AttributeType;

@Entity(tableName="attribute_table")
public class Attribute{

    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="edgeAttributeId")
    private int edgeAttributeId;

    @ColumnInfo(name="edgeSensorId")
    private int edgeSensorId;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    @NonNull
    @TypeConverters(AttributeDirectionConverter.class)
    @ColumnInfo(name="direction")
    private AttributeDirection direction; /*** input, output ***/

    @NonNull
    @TypeConverters(AttributeTypeConverter.class)
    @ColumnInfo(name="type")
    private AttributeType type; /*** string, boolean, integer, double ***/

    @ColumnInfo(name="interval")
    private int interval;

    @NonNull
    @TypeConverters(AttributeScriptStateConverter.class)
    @ColumnInfo(name="scriptState")
    private AttributeScriptState scriptState; /*** valid, invalid ***/

    @NonNull
    @TypeConverters(AttributeStateConverter.class)
    @ColumnInfo(name="state")
    private AttributeState state; /*** active, deactivated ***/

    @ColumnInfo(name="deviceId")
    private int deviceId;

    @Ignore
    public Attribute(@NonNull String name){
        this.name=name;
    }

    public Attribute(int id, int edgeAttributeId, int edgeSensorId, @NonNull String name, @NonNull AttributeDirection direction, @NonNull AttributeType type, int interval, @NonNull AttributeScriptState scriptState, @NonNull AttributeState state, int deviceId){
        this.id=id;
        this.edgeAttributeId=edgeAttributeId;
        this.edgeSensorId=edgeSensorId;
        this.name=name;
        this.direction=direction;
        this.type=type;
        this.interval=interval;
        this.scriptState=scriptState;
        this.state=state;
        this.deviceId=deviceId;
    }

    public int getId(){
        return id;
    }

    public int getEdgeAttributeId(){
        return edgeAttributeId;
    }

    public int getEdgeSensorId(){
        return edgeSensorId;
    }

    @NonNull
    public String getName(){
        return name;
    }

    @NonNull
    public AttributeDirection getDirection(){
        return direction;
    }

    @NonNull
    public AttributeType getType(){
        return type;
    }

    public int getInterval(){
        return interval;
    }

    @NonNull
    public AttributeScriptState getScriptState(){
        return scriptState;
    }

    @NonNull
    public AttributeState getState(){
        return state;
    }

    public int getDeviceId(){
        return deviceId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setEdgeAttributeId(int edgeAttributeId){
        this.edgeAttributeId=edgeAttributeId;
    }

    public void setEdgeSensorId(int edgeSensorId){
        this.edgeSensorId=edgeSensorId;
    }

    public void setName(@NonNull String name){
        this.name=name;
    }

    public void setDirection(@NonNull AttributeDirection direction){
        this.direction=direction;
    }

    public void setType(@NonNull AttributeType type){
        this.type=type;
    }

    public void setInterval(int interval){
        this.interval=interval;
    }

    public void setScriptState(@NonNull AttributeScriptState scriptState){
        this.scriptState=scriptState;
    }

    public void setState(@NonNull AttributeState state){
        this.state=state;
    }

    public void setDeviceId(int deviceId){
        this.deviceId=deviceId;
    }

    @Override
    public String toString(){
        return "Attribute{"+
                "id="+id+
                ", edgeAttributeId="+edgeAttributeId+
                ", edgeSensorId="+edgeSensorId+
                ", name='"+name+'\''+
                ", direction="+direction+
                ", type="+type+
                ", interval="+interval+
                ", scriptState="+scriptState+
                ", state="+state+
                ", deviceId="+deviceId+
                '}';
    }
}
