package com.hyper.connect.page.notifications;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Notification;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel{
    private LocalRepository localRepository;
    private LiveData<List<Notification>> liveNotificationList;

    public NotificationsViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
        liveNotificationList=localRepository.getLiveNotificationList();
    }

    public LiveData<List<Notification>> getLiveNotificationList(){
        return liveNotificationList;
    }

    public LiveData<List<Notification>> getLiveNotificationListByDeviceUserId(String deviceUserId){
        return localRepository.getLiveNotificationListByDeviceUserId(deviceUserId);
    }
}
