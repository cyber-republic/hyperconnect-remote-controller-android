package com.hyper.connect.page.devices;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;
import com.hyper.connect.database.entity.Device;

import java.util.List;


public class DevicesViewModel extends AndroidViewModel{
    private LocalRepository localRepository;

    public DevicesViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
    }

    public LiveData<List<Device>> getLiveDeviceList(){
        return localRepository.getLiveDeviceList();
    }

    public LiveData<Device> getLiveDeviceByUserId(String userId){
        return localRepository.getLiveDeviceByUserId(userId);
    }

    public LiveData<List<Category>> getLiveCategoryListByDeviceId(int deviceId){
        return localRepository.getLiveCategoryListByDeviceId(deviceId);
    }
}
