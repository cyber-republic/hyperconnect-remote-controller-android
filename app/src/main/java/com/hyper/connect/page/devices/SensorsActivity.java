package com.hyper.connect.page.devices;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;
import com.hyper.connect.database.entity.Sensor;
import com.hyper.connect.management.AttributeManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SensorsActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private SensorListAdapter sensorListAdapter;
    private SensorsViewModel sensorsViewModel;
    private CategoriesViewModel categoriesViewModel;
    private LinearLayout emptyPlaceholder;
    private TextView deviceNameText;
    private AppCompatSpinner sensorSpinner;
    private TextView categoryPlaceholder;
    private LinearLayout categoryLayout;
    private AppCompatImageButton categoryFilterButton;
    private Map<Integer, Boolean> selectedMap;
    private static AttributeManagement attributeManagement;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        attributeManagement=GlobalApplication.getAttributeManagement();

        selectedMap=new HashMap<Integer, Boolean>();
        categoryPlaceholder=findViewById(R.id.categoryPlaceholder);
        categoryLayout=findViewById(R.id.categoryLayout);
        categoriesViewModel=ViewModelProviders.of(SensorsActivity.this).get(CategoriesViewModel.class);

        int deviceId=getIntent().getIntExtra("deviceId", -1);
        String deviceName=getIntent().getStringExtra("deviceName");
        String deviceUserId=getIntent().getStringExtra("deviceUserId");
        int categoryId=getIntent().getIntExtra("categoryId", -1);

        AppCompatButton backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            attributeManagement.stopAllAttributes();
            setResult(RESULT_CANCELED);
            finish();
        });

        deviceNameText=findViewById(R.id.deviceNameText);
        String title=((String)getString(R.string.title_sensors))+" "+deviceName;
        deviceNameText.setText(title);

        emptyPlaceholder=findViewById(R.id.emptyPlaceholder);
        recyclerView=findViewById(R.id.sensors_recyclerview);
        sensorListAdapter=new SensorListAdapter(getApplicationContext(), this, deviceUserId);
        recyclerView.setAdapter(sensorListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Sensor emptySensor=new Sensor("-- All Sensors --");
        List<Sensor> emptySensorList=new ArrayList<Sensor>();
        emptySensorList.add(emptySensor);
        sensorSpinner=findViewById(R.id.sensorSpinner);
        SensorSpinnerAdapter sensorSpinnerAdapter=new SensorSpinnerAdapter(SensorsActivity.this, R.layout.item_spinner_text, R.id.textView, emptySensorList);
        sensorSpinner.setAdapter(sensorSpinnerAdapter);

        sensorsViewModel=ViewModelProviders.of(SensorsActivity.this).get(SensorsViewModel.class);
        sensorsViewModel.getLiveSensorListByDeviceId(deviceId).observe(SensorsActivity.this, sensorList -> {
            sensorListAdapter.setSensorList(sensorList);
            if(sensorList==null || sensorList.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyPlaceholder.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.setVisibility(View.VISIBLE);
                emptyPlaceholder.setVisibility(View.GONE);

                List<Sensor> spinnerSensorList=new ArrayList<Sensor>();
                spinnerSensorList.addAll(sensorList);
                spinnerSensorList.add(0, emptySensor);
                sensorSpinnerAdapter.setSensorList(spinnerSensorList);
            }
        });

        sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                attributeManagement.stopAllAttributes();
                if(position!=0){
                    Sensor sensor=(Sensor)parent.getItemAtPosition(position);
                    sensorListAdapter.getFilter().filter(Integer.toString(sensor.getId()));
                }
                else{
                    sensorListAdapter.getFilter().filter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        categoryFilterButton=findViewById(R.id.categoryFilterButton);
        categoryFilterButton.setOnClickListener(buttonView -> {
            FragmentManager fragmentManager=getSupportFragmentManager();
            CategoryFilterDialog categoryDialog=new CategoryFilterDialog();
            categoryDialog.init(SensorsActivity.this);
            categoryDialog.show(fragmentManager, "CategoryFilterDialog");
        });

        if(categoryId!=-1){
            selectedMap.put(categoryId, true);
            setSelectedMap(selectedMap);
        }
    }

    public Map<Integer, Boolean> getSelectedMap(){
        return selectedMap;
    }

    public void setSelectedMap(Map<Integer, Boolean> selectedMap){
        attributeManagement.stopAllAttributes();
        this.selectedMap=selectedMap;
        int counter=0;
        categoryLayout.removeAllViews();
        for(Map.Entry<Integer, Boolean> entry : selectedMap.entrySet()){
            int categoryId=entry.getKey();
            boolean selected=entry.getValue();
            if(selected){
                TextView categoryText=new TextView(this);
                categoriesViewModel.getLiveCategoryById(categoryId).observe(SensorsActivity.this, category -> {
                    categoryText.setText(category.getName());
                    categoryText.setTextSize(14);
                    categoryText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack)));
                    categoryText.setBackgroundResource(R.drawable.button_category_background);
                    categoryText.setClickable(false);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMarginEnd(30);
                    categoryText.setLayoutParams(layoutParams);
                });
                categoryLayout.addView(categoryText);
                counter++;
            }
        }
        if(counter==0){
            categoryLayout.setVisibility(View.GONE);
            categoryPlaceholder.setVisibility(View.VISIBLE);
        }
        else{
            categoryLayout.setVisibility(View.VISIBLE);
            categoryPlaceholder.setVisibility(View.GONE);
        }
        sensorListAdapter.setFilteredCategoryMap(selectedMap);
        Snackbar.make(categoryFilterButton, R.string.snack_category_filter_updated, Snackbar.LENGTH_SHORT).show();
    }

    public static class CategoryFilterDialog extends DialogFragment{
        private LocalRepository localRepository;
        private SensorsActivity sensorsActivity;
        private RecyclerView recyclerView;
        private CategoryFilterListAdapter categoryFilterListAdapter;
        private CategoriesViewModel categoriesViewModel;
        private LinearLayout emptyPlaceholder;
        private AppCompatButton closeButton;
        private AppCompatButton updateButton;

        public void init(SensorsActivity sensorsActivity){
            this.sensorsActivity=sensorsActivity;
            localRepository=GlobalApplication.getLocalRepository();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            View view=inflater.inflate(R.layout.dialog_filter_category, container, false);

            emptyPlaceholder=view.findViewById(R.id.emptyPlaceholder);
            recyclerView=view.findViewById(R.id.categories_recyclerview);
            categoryFilterListAdapter=new CategoryFilterListAdapter(getContext(), sensorsActivity);
            recyclerView.setAdapter(categoryFilterListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            categoryFilterListAdapter.setSelectedMap(sensorsActivity.getSelectedMap());

            categoriesViewModel=ViewModelProviders.of(this).get(CategoriesViewModel.class);
            categoriesViewModel.getLiveCategoryList().observe(this, categoryList -> {
                categoryFilterListAdapter.setCategoryList(categoryList);
                if(categoryList==null || categoryList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyPlaceholder.setVisibility(View.GONE);
                }
            });

            closeButton=view.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(buttonView -> {
                dismiss();
            });

            updateButton=view.findViewById(R.id.updateButton);
            updateButton.setOnClickListener(buttonView -> {
                dismiss();
                Map<Integer, Boolean> selectedMap=categoryFilterListAdapter.getSelectedMap();
                sensorsActivity.setSelectedMap(selectedMap);
            });

            return view;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        attributeManagement.pauseAllAttributes();
    }

    @Override
    protected void onResume(){
        super.onResume();
        attributeManagement.resumeAllAttributes();
    }
}
