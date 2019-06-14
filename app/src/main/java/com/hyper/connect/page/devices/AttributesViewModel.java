package com.hyper.connect.page.devices;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.AttributeDirection;

import java.util.List;


public class AttributesViewModel extends AndroidViewModel{
    private LocalRepository localRepository;

    public AttributesViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
    }

    public LiveData<List<Attribute>> getLiveAttributeListByEdgeSensorIdAndDeviceId(int edgeSensorId, int deviceId){
        return localRepository.getLiveAttributeListByEdgeSensorIdAndDeviceId(edgeSensorId, deviceId);
    }

    public LiveData<List<Attribute>> getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(int edgeSensorId, int deviceId, AttributeDirection direction){
        return localRepository.getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(edgeSensorId, deviceId, direction);
    }

    public LiveData<Device> getLiveDeviceById(int id){
        return localRepository.getLiveDeviceById(id);
    }

    public LiveData<Attribute> getLiveAttributeByEdgeAttributeIdAndDeviceId(int edgeAttributeId, int deviceId){
        return localRepository.getLiveAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, deviceId);
    }

    public LiveData<DataRecord> getLiveDataRecordByDeviceUserIdAndEdgeAttributeId(String deviceUserId, int edgeAttributeId){
        return localRepository.getLiveDataRecordByDeviceUserIdAndEdgeAttributeId(deviceUserId, edgeAttributeId);
    }
}
