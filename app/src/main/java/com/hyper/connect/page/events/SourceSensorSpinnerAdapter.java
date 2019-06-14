package com.hyper.connect.page.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hyper.connect.R;
import com.hyper.connect.database.entity.Sensor;

import java.util.List;

public class SourceSensorSpinnerAdapter extends ArrayAdapter<Sensor>{
    private Context context;
    private int groupId;
    private List<Sensor> sensorList;

    public SourceSensorSpinnerAdapter(Context context, int groupId, int id, List<Sensor> sensorList){
        super(context, id, sensorList);
        this.context=context;
        this.groupId=groupId;
        this.sensorList=sensorList;
    }

    public void setSensorList(List<Sensor> sensorList){
        clear();
        addAll(sensorList);
        this.sensorList=sensorList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View itemView=LayoutInflater.from(context).inflate(groupId, parent, false);
        Sensor sensor=sensorList.get(position);
        TextView textView=itemView.findViewById(R.id.textView);
        textView.setText(sensor.getName());
        return itemView;
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
        return getView(position, convertView, parent);
    }
}
