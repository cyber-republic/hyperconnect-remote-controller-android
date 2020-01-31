package com.hyper.connect.page.dashboard;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;

import java.util.List;


public class DashboardViewModel extends AndroidViewModel{
    private LocalRepository localRepository;

    public DashboardViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
    }

    public LiveData<Integer> getLiveDeviceCount(){
        return localRepository.getLiveDeviceCount();
    }

    public LiveData<Integer> getLiveOnlineDeviceCount(){
        return localRepository.getLiveOnlineDeviceCount();
    }

    public LiveData<Integer> getLiveAttributeCount(){
        return localRepository.getLiveAttributeCount();
    }

    public LiveData<Integer> getLiveActiveAttributeCount(){
        return localRepository.getLiveActiveAttributeCount();
    }

    public LiveData<Integer> getLiveEventCount(){
        return localRepository.getLiveEventCount();
    }

    public LiveData<Integer> getLiveActiveEventCount(){
        return localRepository.getLiveActiveEventCount();
    }

    public LiveData<Boolean> getCarrierConnectionState(){
        return localRepository.getCarrierConnectionState();
    }
}
