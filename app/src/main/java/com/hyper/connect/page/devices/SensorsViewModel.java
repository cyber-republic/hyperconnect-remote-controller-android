package com.hyper.connect.page.devices;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.Sensor;

import java.util.List;


public class SensorsViewModel extends AndroidViewModel{
    private LocalRepository localRepository;
    private LiveData<List<Sensor>> liveSensorList;

    public SensorsViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
        liveSensorList=localRepository.getLiveSensorList();
    }

    public LiveData<List<Sensor>> getLiveSensorList(){
        return liveSensorList;
    }

    public LiveData<List<Sensor>> getLiveSensorListByDeviceId(int deviceId){
        return localRepository.getLiveSensorListByDeviceId(deviceId);
    }
}
