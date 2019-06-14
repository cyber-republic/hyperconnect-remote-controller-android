package com.hyper.connect.page.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hyper.connect.R;
import com.hyper.connect.database.entity.Device;

import java.util.List;

public class ActionDeviceSpinnerAdapter extends ArrayAdapter<Device>{
    private Context context;
    private int groupId;
    private List<Device> deviceList;

    public ActionDeviceSpinnerAdapter(Context context, int groupId, int id, List<Device> deviceList){
        super(context, id, deviceList);
        this.context=context;
        this.groupId=groupId;
        this.deviceList=deviceList;
    }

    public void setDeviceList(List<Device> deviceList){
        clear();
        addAll(deviceList);
        this.deviceList=deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View itemView=LayoutInflater.from(context).inflate(groupId, parent, false);
        Device device=deviceList.get(position);
        TextView textView=itemView.findViewById(R.id.textView);
        textView.setText(device.getName());
        return itemView;
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
        return getView(position, convertView, parent);
    }
}
