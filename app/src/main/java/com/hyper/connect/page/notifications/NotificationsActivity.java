package com.hyper.connect.page.notifications;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.database.entity.enums.NotificationCategory;
import com.hyper.connect.database.entity.enums.NotificationType;

import java.util.ArrayList;
import java.util.List;


public class NotificationsActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private NotificationListAdapter notificationListAdapter;
    private NotificationsViewModel notificationsViewModel;
    private LinearLayout emptyPlaceholder;
    private TextView titleText;
    private AppCompatSpinner typeSpinner;
    private AppCompatSpinner categorySpinner;
    private NotificationType selectedType;
    private NotificationCategory selectedCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        String deviceUserId=getIntent().getStringExtra("deviceUserId");
        String deviceName=getIntent().getStringExtra("deviceName");

        AppCompatButton backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        titleText=findViewById(R.id.titleText);
        emptyPlaceholder=findViewById(R.id.emptyPlaceholder);
        recyclerView=findViewById(R.id.notifications_recyclerview);
        notificationListAdapter=new NotificationListAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(notificationListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        notificationsViewModel=ViewModelProviders.of(NotificationsActivity.this).get(NotificationsViewModel.class);
        if(deviceUserId==null){
            titleText.setText(R.string.title_notifications);
            notificationsViewModel.getLiveNotificationList().observe(NotificationsActivity.this, notificationList -> {
                notificationListAdapter.setNotificationList(notificationList);
                if(notificationList==null || notificationList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyPlaceholder.setVisibility(View.GONE);
                }
            });
        }
        else{
            String title=((String)getString(R.string.title_notifications_of_device))+" "+deviceName;
            titleText.setText(title);
            notificationsViewModel.getLiveNotificationListByDeviceUserId(deviceUserId).observe(NotificationsActivity.this, notificationList -> {
                notificationListAdapter.setNotificationList(notificationList);
                if(notificationList==null || notificationList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyPlaceholder.setVisibility(View.GONE);
                }
            });
        }

        typeSpinner=findViewById(R.id.typeSpinner);
        categorySpinner=findViewById(R.id.categorySpinner);

        List<String> typeList=new ArrayList<String>();
        typeList.add("-- Select Type --");
        typeList.add(NotificationType.SUCCESS.toString());
        typeList.add(NotificationType.WARNING.toString());
        typeList.add(NotificationType.ERROR.toString());

        List<String> categoryList=new ArrayList<String>();
        categoryList.add("-- Select Category --");
        categoryList.add(NotificationCategory.DEVICE.toString());
        categoryList.add(NotificationCategory.SENSOR.toString());
        categoryList.add(NotificationCategory.ATTRIBUTE.toString());
        categoryList.add(NotificationCategory.EVENT.toString());

        ArrayAdapter<String> typeAdapter=new ArrayAdapter<String>(NotificationsActivity.this, R.layout.item_spinner_text, R.id.textView, typeList);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> categoryAdapter=new ArrayAdapter<String>(NotificationsActivity.this, R.layout.item_spinner_text, R.id.textView, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position==0){
                    selectedType=null;
                }
                else{
                    selectedType=NotificationType.stringValueOf((String)parent.getItemAtPosition(position));
                }
                executeFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position==0){
                    selectedCategory=null;
                }
                else{
                    selectedCategory=NotificationCategory.stringValueOf((String)parent.getItemAtPosition(position));
                }
                executeFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    public void setEmptyPlaceholderVisible(boolean state){
        if(state){
            emptyPlaceholder.setVisibility(View.VISIBLE);
        }
        else{
            emptyPlaceholder.setVisibility(View.GONE);
        }
    }

    private void executeFilter(){
        JsonObject filterObject=new JsonObject();
        if(selectedType==null && selectedCategory==null){
            notificationListAdapter.getFilter().filter(null);
        }
        else if(selectedType==null){
            filterObject.addProperty("command", "category");
            filterObject.addProperty("category", selectedCategory.getValue());
            notificationListAdapter.getFilter().filter(filterObject.toString());
        }
        else if(selectedCategory==null){
            filterObject.addProperty("command", "type");
            filterObject.addProperty("type", selectedType.getValue());
            notificationListAdapter.getFilter().filter(filterObject.toString());
        }
        else{
            filterObject.addProperty("command", "both");
            filterObject.addProperty("type", selectedType.getValue());
            filterObject.addProperty("category", selectedCategory.getValue());
            notificationListAdapter.getFilter().filter(filterObject.toString());
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
