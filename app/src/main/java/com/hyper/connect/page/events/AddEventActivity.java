package com.hyper.connect.page.events;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.AttributeDirection;
import com.hyper.connect.database.entity.enums.AttributeType;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.Event;
import com.hyper.connect.database.entity.enums.EventAverage;
import com.hyper.connect.database.entity.enums.EventCondition;
import com.hyper.connect.database.entity.enums.EventEdgeType;
import com.hyper.connect.database.entity.enums.EventState;
import com.hyper.connect.database.entity.enums.EventType;
import com.hyper.connect.database.entity.Sensor;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.page.devices.AttributesViewModel;
import com.hyper.connect.page.devices.DevicesViewModel;
import com.hyper.connect.page.devices.SensorsViewModel;
import com.hyper.connect.util.CustomUtil;

import java.util.ArrayList;
import java.util.List;


public class AddEventActivity extends AppCompatActivity{
    private DevicesViewModel devicesViewModel;
    private SensorsViewModel sensorsViewModel;
    private AttributesViewModel attributesViewModel;
    private AppCompatButton backButton;
    private AppCompatSpinner sourceDeviceSpinner;
    private AppCompatSpinner sourceSensorSpinner;
    private AppCompatSpinner sourceAttributeSpinner;
    private AppCompatSpinner averageSpinner;
    private AppCompatSpinner eventConditionSpinner;
    private AppCompatSpinner eventValueSpinner;
    private EditText eventValueInput;
    private AppCompatSpinner actionDeviceSpinner;
    private AppCompatSpinner actionSensorSpinner;
    private AppCompatSpinner actionAttributeSpinner;
    private AppCompatSpinner triggerValueSpinner;
    private EditText triggerValueInput;
    private EditText eventNameInput;
    private AppCompatButton addEventButton;
    private Event newEvent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        newEvent=new Event(0);
        newEvent.setState(EventState.DEACTIVATED);

        devicesViewModel=ViewModelProviders.of(this).get(DevicesViewModel.class);
        sensorsViewModel=ViewModelProviders.of(this).get(SensorsViewModel.class);
        attributesViewModel=ViewModelProviders.of(this).get(AttributesViewModel.class);

        sourceDeviceSpinner=findViewById(R.id.sourceDeviceSpinner);
        sourceSensorSpinner=findViewById(R.id.sourceSensorSpinner);
        sourceAttributeSpinner=findViewById(R.id.sourceAttributeSpinner);
        averageSpinner=findViewById(R.id.averageSpinner);
        eventConditionSpinner=findViewById(R.id.eventConditionSpinner);
        eventValueSpinner=findViewById(R.id.eventValueSpinner);
        eventValueInput=findViewById(R.id.eventValueInput);
        sourceSensorSpinner.setEnabled(false);
        sourceAttributeSpinner.setEnabled(false);
        averageSpinner.setEnabled(false);
        eventConditionSpinner.setEnabled(false);
        eventValueSpinner.setEnabled(false);
        eventValueInput.setEnabled(false);

        actionDeviceSpinner=findViewById(R.id.actionDeviceSpinner);
        actionSensorSpinner=findViewById(R.id.actionSensorSpinner);
        actionAttributeSpinner=findViewById(R.id.actionAttributeSpinner);
        triggerValueSpinner=findViewById(R.id.triggerValueSpinner);
        triggerValueInput=findViewById(R.id.triggerValueInput);
        actionSensorSpinner.setEnabled(false);
        actionAttributeSpinner.setEnabled(false);
        triggerValueSpinner.setEnabled(false);
        triggerValueInput.setEnabled(false);

        eventNameInput=findViewById(R.id.eventNameInput);
        addEventButton=findViewById(R.id.addEventButton);


        Device emptyDevice=new Device("-- Select Device --");
        List<Device> emptyDeviceList=new ArrayList<Device>();
        emptyDeviceList.add(emptyDevice);

        Sensor emptySensor=new Sensor("-- Select Sensor --");
        List<Sensor> emptySensorList=new ArrayList<Sensor>();
        emptySensorList.add(emptySensor);

        Attribute emptyAttribute=new Attribute("-- Select Attribute --");
        List<Attribute> emptyAttributeList=new ArrayList<Attribute>();
        emptyAttributeList.add(emptyAttribute);

        List<String> averageListFull=new ArrayList<String>();
        averageListFull.add("-- Select Average --");
        averageListFull.add(EventAverage.REAL_TIME.toString());
        averageListFull.add(EventAverage.ONE_MINUTE.toString());
        averageListFull.add(EventAverage.FIVE_MINUTES.toString());
        averageListFull.add(EventAverage.FIFTEEN_MINUTES.toString());
        averageListFull.add(EventAverage.ONE_HOUR.toString());
        averageListFull.add(EventAverage.THREE_HOURS.toString());
        averageListFull.add(EventAverage.SIX_HOURS.toString());
        averageListFull.add(EventAverage.ONE_DAY.toString());

        List<String> averageListRealTime=new ArrayList<String>();
        averageListRealTime.add("-- Select Average --");
        averageListRealTime.add(EventAverage.REAL_TIME.toString());

        List<String> eventConditionListFull=new ArrayList<String>();
        eventConditionListFull.add("-- Select Condition --");
        eventConditionListFull.add(EventCondition.EQUAL_TO.toString());
        eventConditionListFull.add(EventCondition.NOT_EQUAL_TO.toString());
        eventConditionListFull.add(EventCondition.GREATER_THAN.toString());
        eventConditionListFull.add(EventCondition.LESS_THAN.toString());

        List<String> eventConditionListHalf=new ArrayList<String>();
        eventConditionListHalf.add("-- Select Condition --");
        eventConditionListHalf.add(EventCondition.EQUAL_TO.toString());
        eventConditionListHalf.add(EventCondition.NOT_EQUAL_TO.toString());

        List<String> eventValueList=new ArrayList<String>();
        eventValueList.add("-- Select Value --");
        eventValueList.add("True");
        eventValueList.add("False");

        List<String> triggerValueList=new ArrayList<String>();
        triggerValueList.add("-- Select Trigger Value --");
        triggerValueList.add("True");
        triggerValueList.add("False");


        SourceDeviceSpinnerAdapter sourceDeviceSpinnerAdapter=new SourceDeviceSpinnerAdapter(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, emptyDeviceList);
        sourceDeviceSpinner.setAdapter(sourceDeviceSpinnerAdapter);

        SourceSensorSpinnerAdapter sourceSensorSpinnerAdapter=new SourceSensorSpinnerAdapter(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, emptySensorList);
        sourceSensorSpinner.setAdapter(sourceSensorSpinnerAdapter);

        SourceAttributeSpinnerAdapter sourceAttributeSpinnerAdapter=new SourceAttributeSpinnerAdapter(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, emptyAttributeList);
        sourceAttributeSpinner.setAdapter(sourceAttributeSpinnerAdapter);

        ArrayAdapter<String> averageAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, averageListFull);
        averageSpinner.setAdapter(averageAdapter);

        ArrayAdapter<String> eventConditionAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, eventConditionListFull);
        eventConditionSpinner.setAdapter(eventConditionAdapter);

        ArrayAdapter<String> eventValueAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, eventValueList);
        eventValueSpinner.setAdapter(eventValueAdapter);

        ActionDeviceSpinnerAdapter actionDeviceSpinnerAdapter=new ActionDeviceSpinnerAdapter(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, emptyDeviceList);
        actionDeviceSpinner.setAdapter(actionDeviceSpinnerAdapter);

        ActionSensorSpinnerAdapter actionSensorSpinnerAdapter=new ActionSensorSpinnerAdapter(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, emptySensorList);
        actionSensorSpinner.setAdapter(actionSensorSpinnerAdapter);

        ActionAttributeSpinnerAdapter actionAttributeSpinnerAdapter=new ActionAttributeSpinnerAdapter(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, emptyAttributeList);
        actionAttributeSpinner.setAdapter(actionAttributeSpinnerAdapter);

        ArrayAdapter<String> triggerValueAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, triggerValueList);
        triggerValueSpinner.setAdapter(triggerValueAdapter);


        devicesViewModel.getLiveDeviceList().observe(AddEventActivity.this, deviceList -> {
            if(deviceList.isEmpty() || !(deviceList.get(0).getName().equals(emptyDevice.getName()))){
                deviceList.add(0, emptyDevice);
            }
            sourceDeviceSpinnerAdapter.setDeviceList(deviceList);
            actionDeviceSpinnerAdapter.setDeviceList(deviceList);
        });
        sourceDeviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Device device=(Device)parent.getItemAtPosition(position);
                if(position==0){
                    sourceSensorSpinner.setEnabled(false);
                }
                else{
                    newEvent.setSourceDeviceUserId(device.getUserId());
                    newEvent.setSourceDeviceId(device.getId());
                    sourceSensorSpinner.setEnabled(true);
                    sensorsViewModel.getLiveSensorListByDeviceId(device.getId()).observe(AddEventActivity.this, sensorList -> {
                        sensorList.add(0, emptySensor);
                        sourceSensorSpinnerAdapter.setSensorList(sensorList);
                    });
                }
                sourceSensorSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        sourceSensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Sensor sensor=(Sensor)parent.getItemAtPosition(position);
                if(position==0){
                    sourceAttributeSpinner.setEnabled(false);
                }
                else{
                    newEvent.setSourceEdgeSensorId(sensor.getEdgeSensorId());
                    sourceAttributeSpinner.setEnabled(true);
                    attributesViewModel.getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(sensor.getEdgeSensorId(), sensor.getDeviceId(), AttributeDirection.INPUT).observe(AddEventActivity.this, attributeList -> {
                        attributeList.add(0, emptyAttribute);
                        sourceAttributeSpinnerAdapter.setAttributeList(attributeList);
                    });
                }
                sourceAttributeSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        sourceAttributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Attribute attribute=(Attribute)parent.getItemAtPosition(position);
                if(position==0){
                    averageSpinner.setEnabled(false);
                }
                else{
                    newEvent.setSourceEdgeAttributeId(attribute.getEdgeAttributeId());
                    averageSpinner.setEnabled(true);
                    if(attribute.getType()==AttributeType.STRING || attribute.getType()==AttributeType.BOOLEAN){
                        ArrayAdapter<String> averageAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, averageListRealTime);
                        averageSpinner.setAdapter(averageAdapter);

                        ArrayAdapter<String> eventConditionAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, eventConditionListHalf);
                        eventConditionSpinner.setAdapter(eventConditionAdapter);
                    }
                    else{
                        ArrayAdapter<String> averageAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, averageListFull);
                        averageSpinner.setAdapter(averageAdapter);

                        ArrayAdapter<String> eventConditionAdapter=new ArrayAdapter<String>(AddEventActivity.this, R.layout.item_spinner_text, R.id.textView, eventConditionListFull);
                        eventConditionSpinner.setAdapter(eventConditionAdapter);
                    }

                    if(attribute.getType()==AttributeType.BOOLEAN){
                        eventValueInput.setVisibility(View.GONE);
                        eventValueSpinner.setVisibility(View.VISIBLE);
                    }
                    else{
                        eventValueInput.setVisibility(View.VISIBLE);
                        eventValueSpinner.setVisibility(View.GONE);
                    }
                }
                averageSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        averageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position==0){
                    eventConditionSpinner.setEnabled(false);
                }
                else{
                    EventAverage eventAverage=EventAverage.stringValueOf((String)parent.getItemAtPosition(position));
                    newEvent.setAverage(eventAverage);
                    eventConditionSpinner.setEnabled(true);
                }
                eventConditionSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        eventConditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position==0){
                    eventValueInput.setEnabled(false);
                    eventValueSpinner.setEnabled(false);
                }
                else{
                    EventCondition eventCondition=EventCondition.stringValueOf((String)parent.getItemAtPosition(position));
                    newEvent.setCondition(eventCondition);
                    eventValueInput.setEnabled(true);
                    eventValueSpinner.setEnabled(true);
                }
                eventValueInput.setText("");
                eventValueSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        eventValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position!=0){
                    String value=(String)parent.getItemAtPosition(position);
                    eventValueInput.setText(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });


        actionDeviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Device device=(Device)parent.getItemAtPosition(position);
                if(position==0){
                    actionSensorSpinner.setEnabled(false);
                }
                else{
                    newEvent.setActionDeviceUserId(device.getUserId());
                    newEvent.setActionDeviceId(device.getId());
                    actionSensorSpinner.setEnabled(true);
                    sensorsViewModel.getLiveSensorListByDeviceId(device.getId()).observe(AddEventActivity.this, sensorList -> {
                        sensorList.add(0, emptySensor);
                        actionSensorSpinnerAdapter.setSensorList(sensorList);
                    });
                }
                actionSensorSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        actionSensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Sensor sensor=(Sensor)parent.getItemAtPosition(position);
                if(position==0){
                    actionAttributeSpinner.setEnabled(false);
                }
                else{
                    newEvent.setActionEdgeSensorId(sensor.getEdgeSensorId());
                    actionAttributeSpinner.setEnabled(true);
                    attributesViewModel.getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(sensor.getEdgeSensorId(), sensor.getDeviceId(), AttributeDirection.OUTPUT).observe(AddEventActivity.this, attributeList -> {
                        attributeList.add(0, emptyAttribute);
                        actionAttributeSpinnerAdapter.setAttributeList(attributeList);
                    });
                }
                actionAttributeSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        actionAttributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position==0){
                    triggerValueInput.setEnabled(false);
                    triggerValueSpinner.setEnabled(false);
                }
                else{
                    Attribute attribute=(Attribute)parent.getItemAtPosition(position);
                    newEvent.setActionEdgeAttributeId(attribute.getEdgeAttributeId());
                    if(attribute.getType()==AttributeType.BOOLEAN){
                        triggerValueInput.setVisibility(View.GONE);
                        triggerValueSpinner.setVisibility(View.VISIBLE);
                    }
                    else{
                        triggerValueInput.setVisibility(View.VISIBLE);
                        triggerValueSpinner.setVisibility(View.GONE);
                    }
                    triggerValueInput.setEnabled(true);
                    triggerValueSpinner.setEnabled(true);
                }
                triggerValueInput.setText("");
                triggerValueSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        triggerValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position!=0){
                    String value=(String)parent.getItemAtPosition(position);
                    triggerValueInput.setText(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });


        addEventButton.setOnClickListener(buttonView -> {
            boolean canContinue=true;

            Device sourceDevice=(Device)sourceDeviceSpinner.getSelectedItem();
            Device actionDevice=(Device)actionDeviceSpinner.getSelectedItem();
            String eventValue=eventValueInput.getText().toString();
            String triggerValue=triggerValueInput.getText().toString();
            String eventName=eventNameInput.getText().toString();
            newEvent.setConditionValue(eventValue);
            newEvent.setTriggerValue(triggerValue);
            newEvent.setName(eventName);

            if(eventValue.isEmpty()){
                Snackbar.make(addEventButton, R.string.snack_event_value_is_empty, Snackbar.LENGTH_SHORT).show();
                canContinue=false;
            }
            else if(triggerValue.isEmpty()){
                Snackbar.make(addEventButton, R.string.snack_event_trigger_value_is_empty, Snackbar.LENGTH_SHORT).show();
                canContinue=false;
            }
            else if(eventName.isEmpty()){
                Snackbar.make(addEventButton, R.string.snack_event_name_is_empty, Snackbar.LENGTH_SHORT).show();
                canContinue=false;
            }
            else if(sourceDevice.getConnectionState()==DeviceConnectionState.OFFLINE && actionDevice.getConnectionState()==DeviceConnectionState.OFFLINE){
                Snackbar.make(addEventButton, R.string.snack_event_source_action_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                canContinue=false;
            }
            else if(sourceDevice.getConnectionState()==DeviceConnectionState.OFFLINE){
                Snackbar.make(addEventButton, R.string.snack_event_source_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                canContinue=false;
            }
            else if(actionDevice.getConnectionState()==DeviceConnectionState.OFFLINE){
                Snackbar.make(addEventButton, R.string.snack_event_action_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                canContinue=false;
            }

            if(canContinue){
                EventType eventType=null;
                if(newEvent.getSourceDeviceUserId().equals(newEvent.getActionDeviceUserId())){
                    eventType=EventType.LOCAL;
                }
                else{
                    eventType=EventType.GLOBAL;
                }
                newEvent.setType(eventType);
                String globalEventId=CustomUtil.getRandomGlobalEventId();
                newEvent.setGlobalEventId(globalEventId);

                JsonElement jsonEvent=new Gson().toJsonTree(newEvent);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("command", "addEvent");
                jsonObject.add("event", jsonEvent);

                ElastosCarrier elastosCarrier=GlobalApplication.getElastosCarrier();

                boolean sourceMessageCheck=true;
                boolean actionMessageCheck=true;
                if(eventType==EventType.LOCAL){
                    jsonObject.addProperty("edgeType", EventEdgeType.SOURCE_AND_ACTION.getValue());
                    String jsonString=jsonObject.toString();
                    sourceMessageCheck=elastosCarrier.sendFriendMessage(newEvent.getSourceDeviceUserId(), jsonString);
                }
                else if(eventType==EventType.GLOBAL){
                    jsonObject.addProperty("edgeType", EventEdgeType.SOURCE.getValue());
                    sourceMessageCheck=elastosCarrier.sendFriendMessage(newEvent.getSourceDeviceUserId(), jsonObject.toString());
                    jsonObject.addProperty("edgeType", EventEdgeType.ACTION.getValue());
                    actionMessageCheck=elastosCarrier.sendFriendMessage(newEvent.getActionDeviceUserId(), jsonObject.toString());
                }

                LocalRepository localRepository=elastosCarrier.getLocalRepository();
                localRepository.insertEvent(newEvent);

                if(sourceMessageCheck && actionMessageCheck){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
