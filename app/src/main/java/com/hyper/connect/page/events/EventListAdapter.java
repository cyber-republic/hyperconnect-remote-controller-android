package com.hyper.connect.page.events;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.enums.AttributeState;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.Event;
import com.hyper.connect.database.entity.enums.EventEdgeType;
import com.hyper.connect.database.entity.enums.EventState;
import com.hyper.connect.database.entity.enums.EventType;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.page.devices.AttributesViewModel;

import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder>{
    private final LayoutInflater inflater;
    private final Context appContext;
    private List<Event> eventList;
    private static ElastosCarrier elastosCarrier;
    private static LocalRepository localRepository;
    private EventsFragment eventsFragment;
    private AttributesViewModel attributesViewModel;
    private FloatingActionButton addEventButton;

    public EventListAdapter(Context context, EventsFragment eventsFragment, FloatingActionButton addEventButton){
        inflater=LayoutInflater.from(context);
        appContext=context;
        elastosCarrier=GlobalApplication.getElastosCarrier();
        localRepository=GlobalApplication.getLocalRepository();
        this.eventsFragment=eventsFragment;
        this.addEventButton=addEventButton;
        attributesViewModel=ViewModelProviders.of(eventsFragment).get(AttributesViewModel.class);
    }

    public void setEventList(List<Event> newEventList){
        eventList=newEventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView=inflater.inflate(R.layout.item_recyclerview_events, viewGroup, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i){
        if(eventList!=null){
            Event event=eventList.get(i);
            eventViewHolder.bind(event);
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(eventList!=null){
            count=eventList.size();
        }
        return count;
    }

    class EventViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private AppCompatImageView stateImage;
        private TextView stateText;
        private AppCompatButton detailsButton;
        private SwitchCompat stateSwitch;
        private TextView sourceDeviceText;
        private TextView sourceAttributeText;
        private TextView actionDeviceText;
        private TextView actionAttributeText;
        private AppCompatImageView sourceDeviceImage;
        private AppCompatImageView sourceAttributeImage;
        private AppCompatImageView actionDeviceImage;
        private AppCompatImageView actionAttributeImage;
        private Device sourceDevice;
        private Attribute sourceAttribute;
        private Device actionDevice;
        private Attribute actionAttribute;

        EventViewHolder(View itemView){
            super(itemView);
            nameText=itemView.findViewById(R.id.nameText);
            stateImage=itemView.findViewById(R.id.stateImage);
            stateText=itemView.findViewById(R.id.stateText);
            detailsButton=itemView.findViewById(R.id.detailsButton);
            stateSwitch=itemView.findViewById(R.id.stateSwitch);
            sourceDeviceText=itemView.findViewById(R.id.sourceDeviceText);
            sourceAttributeText=itemView.findViewById(R.id.sourceAttributeText);
            actionDeviceText=itemView.findViewById(R.id.actionDeviceText);
            actionAttributeText=itemView.findViewById(R.id.actionAttributeText);
            sourceDeviceImage=itemView.findViewById(R.id.sourceDeviceImage);
            sourceAttributeImage=itemView.findViewById(R.id.sourceAttributeImage);
            actionDeviceImage=itemView.findViewById(R.id.actionDeviceImage);
            actionAttributeImage=itemView.findViewById(R.id.actionAttributeImage);
        }

        void bind(Event event){
            nameText.setText(event.getName());

            EventState eventState=event.getState();
            if(eventState==EventState.ACTIVE){
                stateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                stateText.setText(R.string.text_active);
                stateSwitch.setChecked(true);
            }
            else if(eventState==EventState.DEACTIVATED){
                stateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                stateText.setText(R.string.text_deactivated);
                stateSwitch.setChecked(false);
            }

            attributesViewModel.getLiveDeviceById(event.getSourceDeviceId()).observe(eventsFragment, device -> {
                if(device!=null){
                    sourceDevice=device;
                    sourceDeviceText.setText(device.getName());
                    if(device.getConnectionState()==DeviceConnectionState.ONLINE){
                        sourceDeviceImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                    }
                    else if(device.getConnectionState()==DeviceConnectionState.OFFLINE){
                        sourceDeviceImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                    }
                }
            });
            attributesViewModel.getLiveAttributeByEdgeAttributeIdAndDeviceId(event.getSourceEdgeAttributeId(), event.getSourceDeviceId()).observe(eventsFragment, attribute -> {
                if(attribute!=null){
                    sourceAttribute=attribute;
                    sourceAttributeText.setText(attribute.getName());
                    if(attribute.getState()==AttributeState.ACTIVE){
                        sourceAttributeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                    }
                    else if(attribute.getState()==AttributeState.DEACTIVATED){
                        sourceAttributeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                    }
                }
            });
            attributesViewModel.getLiveDeviceById(event.getActionDeviceId()).observe(eventsFragment, device -> {
                if(device!=null){
                    actionDevice=device;
                    actionDeviceText.setText(device.getName());
                    if(device.getConnectionState()==DeviceConnectionState.ONLINE){
                        actionDeviceImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                    }
                    else if(device.getConnectionState()==DeviceConnectionState.OFFLINE){
                        actionDeviceImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                    }
                }
            });
            attributesViewModel.getLiveAttributeByEdgeAttributeIdAndDeviceId(event.getActionEdgeAttributeId(), event.getActionDeviceId()).observe(eventsFragment, attribute -> {
                if(attribute!=null){
                    actionAttribute=attribute;
                    actionAttributeText.setText(attribute.getName());
                    if(attribute.getState()==AttributeState.ACTIVE){
                        actionAttributeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                    }
                    else if(attribute.getState()==AttributeState.DEACTIVATED){
                        actionAttributeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                    }
                }
            });


            stateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                boolean canContinue=false;
                if(sourceDevice!=null && actionDevice!=null && sourceAttribute!=null && actionAttribute!=null){
                    if(sourceDevice.getConnectionState()==DeviceConnectionState.OFFLINE && actionDevice.getConnectionState()==DeviceConnectionState.OFFLINE){
                        Snackbar.make(addEventButton, R.string.snack_event_source_action_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(sourceDevice.getConnectionState()==DeviceConnectionState.OFFLINE){
                        Snackbar.make(addEventButton, R.string.snack_event_source_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(actionDevice.getConnectionState()==DeviceConnectionState.OFFLINE){
                        Snackbar.make(addEventButton, R.string.snack_event_action_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(sourceAttribute.getState()==AttributeState.DEACTIVATED && actionAttribute.getState()==AttributeState.DEACTIVATED){
                        Snackbar.make(addEventButton, R.string.snack_event_source_action_attribute_must_be_active, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(sourceAttribute.getState()==AttributeState.DEACTIVATED){
                        Snackbar.make(addEventButton, R.string.snack_event_source_attribute_must_be_active, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(actionAttribute.getState()==AttributeState.DEACTIVATED){
                        Snackbar.make(addEventButton, R.string.snack_event_action_attribute_must_be_active, Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        canContinue=true;
                    }
                }
                else{
                    Snackbar.make(addEventButton, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
                }

                if(canContinue){
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("command", "changeEventState");
                    jsonObject.addProperty("globalEventId", event.getGlobalEventId());
                    jsonObject.addProperty("state", isChecked);

                    if(sourceDevice.getConnectionState()==DeviceConnectionState.ONLINE && actionDevice.getConnectionState()==DeviceConnectionState.ONLINE){
                        boolean sourceMessageCheck=true;
                        boolean actionMessageCheck=true;
                        if(event.getType()==EventType.LOCAL){
                            jsonObject.addProperty("edgeType", EventEdgeType.SOURCE_AND_ACTION.getValue());
                            sourceMessageCheck=elastosCarrier.sendFriendMessage(event.getSourceDeviceUserId(), jsonObject.toString());
                        }
                        else if(event.getType()==EventType.GLOBAL){
                            jsonObject.addProperty("edgeType", EventEdgeType.SOURCE.getValue());
                            sourceMessageCheck=elastosCarrier.sendFriendMessage(event.getSourceDeviceUserId(), jsonObject.toString());
                            jsonObject.addProperty("edgeType", EventEdgeType.ACTION.getValue());
                            actionMessageCheck=elastosCarrier.sendFriendMessage(event.getActionDeviceUserId(), jsonObject.toString());
                        }

                        if(sourceMessageCheck && actionMessageCheck){
                            if(isChecked){
                                event.setState(EventState.ACTIVE);
                            }
                            else{
                                event.setState(EventState.DEACTIVATED);
                            }
                            localRepository.updateEvent(event);
                            Snackbar.make(addEventButton, R.string.snack_event_state_changed, Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            Snackbar.make(addEventButton, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Snackbar.make(addEventButton, R.string.snack_event_source_action_device_must_be_online, Snackbar.LENGTH_SHORT).show();
                    }
                }
                else{
                    stateSwitch.setChecked(!isChecked);
                }
            });

            detailsButton.setOnClickListener(buttonView -> {
                final Dialog dialog=new Dialog(appContext);
                dialog.setContentView(R.layout.dialog_event_details);

                TextView typeText=dialog.findViewById(R.id.typeText);
                TextView averageText=dialog.findViewById(R.id.averageText);
                TextView conditionText=dialog.findViewById(R.id.conditionText);
                TextView conditionValueText=dialog.findViewById(R.id.conditionValueText);
                TextView triggerValueText=dialog.findViewById(R.id.triggerValueText);

                typeText.setText(event.getType().toString());
                averageText.setText(event.getAverage().toString());
                conditionText.setText(event.getCondition().toString());
                conditionValueText.setText(event.getConditionValue());
                triggerValueText.setText(event.getTriggerValue());

                AppCompatButton closeButton=dialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(viewButton -> {
                    dialog.dismiss();
                });

                dialog.show();
            });
        }
    }


}
