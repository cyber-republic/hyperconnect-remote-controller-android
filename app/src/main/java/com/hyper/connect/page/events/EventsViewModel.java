package com.hyper.connect.page.events;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.Event;

import java.util.List;


public class EventsViewModel extends AndroidViewModel{
    private LocalRepository localRepository;
    private LiveData<List<Event>> liveEventList;

    public EventsViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
        liveEventList=localRepository.getLiveEventList();
    }

    public LiveData<List<Event>> getLiveEventList(){
        return liveEventList;
    }
}
