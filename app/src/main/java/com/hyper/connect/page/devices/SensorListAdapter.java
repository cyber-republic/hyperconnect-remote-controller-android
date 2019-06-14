package com.hyper.connect.page.devices;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.CategoryRecord;
import com.hyper.connect.database.entity.Sensor;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.management.AttributeManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensorViewHolder> implements Filterable{
    private final LayoutInflater inflater;
    private final Context appContext;
    private List<Sensor> sensorList;
    private List<Sensor> fullSensorList;
    private static ElastosCarrier elastosCarrier;
    private static LocalRepository localRepository;
    private static AttributeManagement attributeManagement;
    private SensorsActivity sensorsActivity;
    private AttributesViewModel attributesViewModel;
    private CategoriesViewModel categoriesViewModel;
    private String deviceUserId;
    private Map<Integer, Boolean> filteredCategoryMap;

    public SensorListAdapter(Context context, SensorsActivity sensorsActivity, String deviceUserId){
        inflater=LayoutInflater.from(context);
        appContext=context;
        elastosCarrier=GlobalApplication.getElastosCarrier();
        localRepository=GlobalApplication.getLocalRepository();
        attributeManagement=GlobalApplication.getAttributeManagement();
        this.sensorsActivity=sensorsActivity;
        attributesViewModel=ViewModelProviders.of(sensorsActivity).get(AttributesViewModel.class);
        categoriesViewModel=ViewModelProviders.of(sensorsActivity).get(CategoriesViewModel.class);
        this.deviceUserId=deviceUserId;
    }

    public void setSensorList(List<Sensor> newSensorList){
        sensorList=newSensorList;
        fullSensorList=new ArrayList<Sensor>(sensorList);
        notifyDataSetChanged();
    }

    public void setFilteredCategoryMap(Map<Integer, Boolean> filteredCategoryMap){
        this.filteredCategoryMap=filteredCategoryMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView=inflater.inflate(R.layout.item_recyclerview_sensors, viewGroup, false);
        return new SensorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder sensorViewHolder, int i){
        if(sensorList!=null){
            Sensor sensor=sensorList.get(i);
            sensorViewHolder.bind(sensor);
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(sensorList!=null){
            count=sensorList.size();
        }
        return count;
    }

    class SensorViewHolder extends RecyclerView.ViewHolder{
        private TextView sensorNameText;
        private RecyclerView recyclerView;

        SensorViewHolder(View itemView){
            super(itemView);
            sensorNameText=itemView.findViewById(R.id.sensorNameText);
            recyclerView=itemView.findViewById(R.id.attributes_recyclerview);
        }

        void bind(Sensor sensor){
            sensorNameText.setText(sensor.getName());

            AttributeListAdapter attributeListAdapter=new AttributeListAdapter(appContext, sensorsActivity, deviceUserId);
            attributeListAdapter.setFilteredCategoryMap(filteredCategoryMap);
            recyclerView.setAdapter(attributeListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(appContext));

            attributesViewModel.getLiveDeviceById(sensor.getDeviceId()).observe(sensorsActivity, device -> {
                attributeListAdapter.setDevice(device);
            });

            attributesViewModel.getLiveAttributeListByEdgeSensorIdAndDeviceId(sensor.getEdgeSensorId(), sensor.getDeviceId()).observe(sensorsActivity, attributeList -> {
                attributeListAdapter.setAttributeList(attributeList);
                if(filteredCategoryMap!=null && !filteredCategoryMap.isEmpty()){
                    attributeListAdapter.getFilter().filter("true");
                }
            });
        }
    }

    @Override
    public Filter getFilter(){
        return sensorFilter;
    }

    private Filter sensorFilter=new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            List<Sensor> filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredList.addAll(fullSensorList);
            }
            else{
                int filteredSensorId=Integer.parseInt(constraint.toString());
                for(Sensor sensor : fullSensorList){
                    if(sensor.getId()==filteredSensorId){
                        filteredList.add(sensor);
                        break;
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            sensorList.clear();
            sensorList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
