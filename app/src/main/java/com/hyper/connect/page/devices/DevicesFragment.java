package com.hyper.connect.page.devices;

import static com.hyper.connect.MainActivity.ACTION_REQUEST_ADD_DEVICE;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyper.connect.R;


public class DevicesFragment extends Fragment{
    private RecyclerView recyclerView;
    private DeviceListAdapter deviceListAdapter;
    private DevicesViewModel devicesViewModel;
    private FloatingActionButton addDeviceButton;
    private LinearLayout emptyPlaceholder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_devices, container, false);

        emptyPlaceholder=view.findViewById(R.id.emptyPlaceholder);
        recyclerView=view.findViewById(R.id.devices_recyclerview);
        deviceListAdapter=new DeviceListAdapter(getContext(), this);
        recyclerView.setAdapter(deviceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        devicesViewModel=ViewModelProviders.of(this).get(DevicesViewModel.class);
        devicesViewModel.getLiveDeviceList().observe(this, deviceList -> {
            deviceListAdapter.setDeviceList(deviceList);
            if(deviceList==null || deviceList.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyPlaceholder.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.setVisibility(View.VISIBLE);
                emptyPlaceholder.setVisibility(View.GONE);
            }
        });

        addDeviceButton=view.findViewById(R.id.addDeviceButton);
        addDeviceButton.setOnClickListener(buttonView -> {
            Intent intent=new Intent(getActivity(), AddDeviceActivity.class);
            getActivity().startActivityForResult(intent, ACTION_REQUEST_ADD_DEVICE);
        });

        return view;
    }

}
