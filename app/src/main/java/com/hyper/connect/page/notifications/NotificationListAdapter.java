package com.hyper.connect.page.notifications;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.Notification;
import com.hyper.connect.database.entity.enums.NotificationCategory;
import com.hyper.connect.database.entity.enums.NotificationType;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.page.devices.DevicesViewModel;

import java.util.ArrayList;
import java.util.List;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> implements Filterable{
    private final LayoutInflater inflater;
    private final Context appContext;
    private List<Notification> notificationList;
    private List<Notification> fullNotificationList;
    private static ElastosCarrier elastosCarrier;
    private static LocalRepository localRepository;
    private NotificationsActivity notificationsActivity;
    private DevicesViewModel devicesViewModel;

    public NotificationListAdapter(Context context, NotificationsActivity notificationsActivity){
        inflater=LayoutInflater.from(context);
        appContext=context;
        elastosCarrier=GlobalApplication.getElastosCarrier();
        localRepository=GlobalApplication.getLocalRepository();
        this.notificationsActivity=notificationsActivity;
        devicesViewModel=ViewModelProviders.of(notificationsActivity).get(DevicesViewModel.class);
    }

    public void setNotificationList(List<Notification> newNotificationList){
        notificationList=newNotificationList;
        fullNotificationList=new ArrayList<Notification>(notificationList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView=inflater.inflate(R.layout.item_recyclerview_notifications, viewGroup, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i){
        if(notificationList!=null){
            Notification notification=notificationList.get(i);
            notificationViewHolder.bind(notification);
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(notificationList!=null){
            count=notificationList.size();
        }
        return count;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView typeImage;
        private AppCompatImageView categoryImage;
        private TextView messageText;
        private TextView dateTimeText;
        private TextView deviceNameText;

        NotificationViewHolder(View itemView){
            super(itemView);
            typeImage=itemView.findViewById(R.id.typeImage);
            categoryImage=itemView.findViewById(R.id.categoryImage);
            messageText=itemView.findViewById(R.id.messageText);
            dateTimeText=itemView.findViewById(R.id.dateTimeText);
            deviceNameText=itemView.findViewById(R.id.deviceNameText);
        }

        void bind(Notification notification){
            NotificationType type=notification.getType();
            if(type==NotificationType.SUCCESS){
                typeImage.setImageResource(R.drawable.ic_done_black_24dp);
                typeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
            }
            else if(type==NotificationType.WARNING){
                typeImage.setImageResource(R.drawable.ic_warning_black_24dp);
                typeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorOrange)));
            }
            else if(type==NotificationType.ERROR){
                typeImage.setImageResource(R.drawable.ic_error_black_24dp);
                typeImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
            }

            NotificationCategory category=notification.getCategory();
            if(category==NotificationCategory.DEVICE){
                categoryImage.setImageResource(R.drawable.ic_router_black_24dp);
            }
            else if(category==NotificationCategory.SENSOR){
                categoryImage.setImageResource(R.drawable.ic_developer_board_black_24dp);
            }
            else if(category==NotificationCategory.ATTRIBUTE){
                categoryImage.setImageResource(R.drawable.ic_memory_black_24dp);
            }
            else if(category==NotificationCategory.EVENT){
                categoryImage.setImageResource(R.drawable.ic_event_black_24dp);
            }
            else if(category==NotificationCategory.SYSTEM){
                //TODO: system notification
            }

            messageText.setText(notification.getMessage());
            dateTimeText.setText(notification.getDateTime());

            devicesViewModel.getLiveDeviceByUserId(notification.getDeviceUserId()).observe(notificationsActivity, device -> {
                deviceNameText.setText(device.getName());
            });

        }
    }

    @Override
    public Filter getFilter(){
        return notificationFilter;
    }

    private Filter notificationFilter=new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            List<Notification> filteredList=new ArrayList<Notification>();
            if(constraint==null || constraint.length()==0){
                filteredList.addAll(fullNotificationList);
            }
            else{
                JsonObject filterObject=new Gson().fromJson(constraint.toString(), JsonObject.class);
                String command=filterObject.get("command").getAsString();
                if(command.equals("both")){
                    NotificationType type=NotificationType.valueOf(filterObject.get("type").getAsInt());
                    NotificationCategory category=NotificationCategory.valueOf(filterObject.get("category").getAsInt());
                    for(Notification notification : fullNotificationList){
                        if(notification.getType()==type && notification.getCategory()==category){
                            filteredList.add(notification);
                        }
                    }
                }
                else if(command.equals("type")){
                    NotificationType type=NotificationType.valueOf(filterObject.get("type").getAsInt());
                    for(Notification notification : fullNotificationList){
                        if(notification.getType()==type){
                            filteredList.add(notification);
                        }
                    }
                }
                else if(command.equals("category")){
                    NotificationCategory category=NotificationCategory.valueOf(filterObject.get("category").getAsInt());
                    for(Notification notification : fullNotificationList){
                        if(notification.getCategory()==category){
                            filteredList.add(notification);
                        }
                    }
                }
            }


            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            notificationList.clear();
            notificationList.addAll((List)results.values);

            if(notificationList.isEmpty()){
                notificationsActivity.setEmptyPlaceholderVisible(true);
            }
            else{
                notificationsActivity.setEmptyPlaceholderVisible(false);
            }

            notifyDataSetChanged();
        }
    };
}
