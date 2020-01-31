package com.hyper.connect.page.devices;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.enums.DeviceState;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.page.notifications.NotificationsActivity;

import java.util.List;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>{
    private final LayoutInflater inflater;
    private final Context appContext;
    private List<Device> deviceList;
    private static ElastosCarrier elastosCarrier;
    private static LocalRepository localRepository;
    private DevicesFragment devicesFragment;
    private DevicesViewModel devicesViewModel;
    private CategoriesViewModel categoriesViewModel;

    public DeviceListAdapter(Context context, DevicesFragment devicesFragment){
        inflater=LayoutInflater.from(context);
        appContext=context;
        elastosCarrier=GlobalApplication.getElastosCarrier();
        localRepository=GlobalApplication.getLocalRepository();
        this.devicesFragment=devicesFragment;
        devicesViewModel=ViewModelProviders.of(devicesFragment).get(DevicesViewModel.class);
        categoriesViewModel=ViewModelProviders.of(devicesFragment).get(CategoriesViewModel.class);
    }

    public void setDeviceList(List<Device> newDeviceList){
        deviceList=newDeviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView=inflater.inflate(R.layout.item_recyclerview_devices, viewGroup, false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int i){
        if(deviceList!=null){
            Device device=deviceList.get(i);
                deviceViewHolder.bind(device);
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(deviceList!=null){
            count=deviceList.size();
        }
        return count;
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private AppCompatImageView statusImage;
        private TextView statusText;
        private AppCompatButton sensorsButton;
        private AppCompatButton notificationsButton;
        private AppCompatImageButton moreButton;
        private TextView categoryPlaceholder;
        private LinearLayout categoryLayout;

        DeviceViewHolder(View itemView){
            super(itemView);
            nameText=itemView.findViewById(R.id.nameText);
            statusImage=itemView.findViewById(R.id.statusImage);
            statusText=itemView.findViewById(R.id.statusText);
            sensorsButton=itemView.findViewById(R.id.sensorsButton);
            notificationsButton=itemView.findViewById(R.id.notificationsButton);
            moreButton=itemView.findViewById(R.id.moreButton);
            categoryPlaceholder=itemView.findViewById(R.id.categoryPlaceholder);
            categoryLayout=itemView.findViewById(R.id.categoryLayout);
        }

        void bind(Device device){
            nameText.setText(device.getName());
            if(device.getState()==DeviceState.ACTIVE){
                if(device.getConnectionState()==DeviceConnectionState.ONLINE){
                    statusText.setText(R.string.text_online);
                    statusImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                }
                else if(device.getConnectionState()==DeviceConnectionState.OFFLINE){
                    statusText.setText(R.string.text_offline);
                    statusImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                }

                sensorsButton.setBackgroundResource(R.drawable.button_background);
                sensorsButton.setOnClickListener(viewButton -> {
                    Intent intent=new Intent(appContext, SensorsActivity.class);
                    intent.putExtra("deviceId", device.getId());
                    intent.putExtra("deviceName", device.getName());
                    intent.putExtra("deviceUserId", device.getUserId());
                    appContext.startActivity(intent);
                });
                notificationsButton.setBackgroundResource(R.drawable.button_background);
                notificationsButton.setOnClickListener(viewButton -> {
                    Intent intent=new Intent(appContext, NotificationsActivity.class);
                    intent.putExtra("deviceUserId", device.getUserId());
                    intent.putExtra("deviceName", device.getName());
                    appContext.startActivity(intent);
                });
            }
            else if(device.getState()==DeviceState.PENDING || device.getState()==DeviceState.DEACTIVATED){
                //statusText.setText(R.string.text_pending);
                statusText.setText(device.getState().toString());
                statusImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorOrange)));
                sensorsButton.setBackgroundResource(R.drawable.button_disabled_background);
                sensorsButton.setOnClickListener(viewButton -> Snackbar.make(sensorsButton, R.string.text_activate_device, Snackbar.LENGTH_SHORT).show());
                notificationsButton.setBackgroundResource(R.drawable.button_disabled_background);
                notificationsButton.setOnClickListener(viewButton -> Snackbar.make(notificationsButton, R.string.text_activate_device, Snackbar.LENGTH_SHORT).show());
            }

            devicesViewModel.getLiveCategoryListByDeviceId(device.getId()).observe(devicesFragment, categoryList -> {
                if(categoryList==null || categoryList.isEmpty()){
                    categoryLayout.setVisibility(View.GONE);
                    categoryPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    categoryLayout.removeAllViews();
                    for(Category category : categoryList){
                        TextView categoryText=new TextView(appContext);
                        categoryText.setText(category.getName());
                        categoryText.setTextSize(14);
                        categoryText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorBlack)));
                        categoryText.setBackgroundResource(R.drawable.button_category_background);
                        categoryText.setClickable(true);
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMarginEnd(30);
                        categoryText.setLayoutParams(layoutParams);
                        categoryText.setOnClickListener(buttonView -> {
                            Intent intent=new Intent(appContext, SensorsActivity.class);
                            intent.putExtra("deviceId", device.getId());
                            intent.putExtra("deviceName", device.getName());
                            intent.putExtra("deviceUserId", device.getUserId());
                            intent.putExtra("categoryId", category.getId());
                            appContext.startActivity(intent);
                        });
                        categoryLayout.addView(categoryText);
                    }
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryPlaceholder.setVisibility(View.GONE);
                }
            });

            moreButton.setOnClickListener(buttonView -> new QuickAction(device, moreButton));
        }
    }

    private class QuickAction extends PopupWindow{
        private QuickAction(Device device, AppCompatImageButton moreButton){
            PopupWindow popupWindow=new PopupWindow(appContext);
            LayoutInflater inflater=(LayoutInflater)appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView=inflater.inflate(R.layout.popup_device_actions, null);
            ImageView arrowUpImage=(ImageView)popupView.findViewById(R.id.arrowUpImage);
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)arrowUpImage.getLayoutParams();
            layoutParams.gravity=Gravity.TOP;
            arrowUpImage.setLayoutParams(layoutParams);

            AppCompatButton editButton=popupView.findViewById(R.id.editButton);
            AppCompatButton removeButton=popupView.findViewById(R.id.removeButton);

            editButton.setOnClickListener(buttonView -> {
                popupWindow.dismiss();
                final Dialog dialog=new Dialog(appContext);
                dialog.setContentView(R.layout.dialog_edit_device);

                TextInputEditText deviceNameInput=dialog.findViewById(R.id.deviceNameInput);
                deviceNameInput.setText(device.getName());

                AppCompatButton cancelButton=dialog.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(viewButton -> {
                    dialog.dismiss();
                });

                AppCompatButton updateButton=dialog.findViewById(R.id.updateButton);
                updateButton.setOnClickListener(viewButton -> {
                    String deviceName=deviceNameInput.getText().toString();
                    if(deviceName.isEmpty()){
                        Snackbar.make(updateButton, R.string.snack_device_name_empty, Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        dialog.dismiss();
                        device.setName(deviceName);
                        localRepository.updateDevice(device);
                        Snackbar.make(moreButton, R.string.snack_edit_device, Snackbar.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            });

            removeButton.setOnClickListener(buttonView -> {
                popupWindow.dismiss();
                final Dialog dialog=new Dialog(appContext);
                dialog.setContentView(R.layout.dialog_remove_device);

                AppCompatButton cancelButton=dialog.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(viewButton -> {
                    dialog.dismiss();
                });

                AppCompatButton confirmButton=dialog.findViewById(R.id.confirmButton);
                confirmButton.setOnClickListener(viewButton -> {
                    dialog.dismiss();

                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("command", "removeMe");
                    elastosCarrier.sendFriendMessage(device.getUserId(), jsonObject.toString());

                    boolean removeCheck=elastosCarrier.removeFriend(device.getUserId());
                    if(removeCheck){
                        localRepository.deleteDevice(device);
                        Snackbar.make(moreButton, R.string.snack_remove_device, Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        Snackbar.make(moreButton, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            });

            popupWindow.setContentView(popupView);
            popupWindow.setBackgroundDrawable(null);
            popupWindow.setFocusable(true);
            popupWindow.showAsDropDown(moreButton);
        }
    }
}
