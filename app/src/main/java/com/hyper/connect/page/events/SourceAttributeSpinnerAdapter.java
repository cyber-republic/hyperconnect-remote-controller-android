package com.hyper.connect.page.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hyper.connect.R;
import com.hyper.connect.database.entity.Attribute;

import java.util.List;

public class SourceAttributeSpinnerAdapter extends ArrayAdapter<Attribute>{
    private Context context;
    private int groupId;
    private List<Attribute> attributeList;

    public SourceAttributeSpinnerAdapter(Context context, int groupId, int id, List<Attribute> attributeList){
        super(context, id, attributeList);
        this.context=context;
        this.groupId=groupId;
        this.attributeList=attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList){
        clear();
        addAll(attributeList);
        this.attributeList=attributeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View itemView=LayoutInflater.from(context).inflate(groupId, parent, false);
        Attribute attribute=attributeList.get(position);
        TextView textView=itemView.findViewById(R.id.textView);
        textView.setText(attribute.getName());
        return itemView;
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
        return getView(position, convertView, parent);
    }
}
