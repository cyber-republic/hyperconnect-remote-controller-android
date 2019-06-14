package com.hyper.connect.page.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hyper.connect.R;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.page.devices.AttributeListAdapter;
import com.hyper.connect.page.devices.AttributesViewModel;
import com.hyper.connect.page.devices.CategoriesViewModel;
import com.hyper.connect.page.devices.SensorsActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DashboardFragment extends Fragment{
    private AppCompatImageView carrierStateImage;
    private TextView carrierStateText;
    private TextView onlineDeviceCountText;
    private TextView deviceCountText;
    private TextView activeAttributeCountText;
    private TextView attributeCountText;
    private TextView activeEventCountText;
    private TextView eventCountText;
    private DashboardViewModel dashboardViewModel;
    /*private WebView webView;
    private ArrayList<DataRecord> dataRecordList;*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);

        carrierStateImage=view.findViewById(R.id.carrierStateImage);
        carrierStateText=view.findViewById(R.id.carrierStateText);
        onlineDeviceCountText=view.findViewById(R.id.onlineDeviceCountText);
        deviceCountText=view.findViewById(R.id.deviceCountText);
        activeAttributeCountText=view.findViewById(R.id.activeAttributeCountText);
        attributeCountText=view.findViewById(R.id.attributeCountText);
        activeEventCountText=view.findViewById(R.id.activeEventCountText);
        eventCountText=view.findViewById(R.id.eventCountText);

        dashboardViewModel=ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.getCarrierConnectionState().observe(this, state -> {
            if(state!=null){
                if(state){
                    carrierStateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorGreen)));
                    carrierStateText.setText(R.string.text_connected);
                    carrierStateText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorGreen)));
                }
                else{
                    carrierStateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorRed)));
                    carrierStateText.setText(R.string.text_disconnected);
                    carrierStateText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorRed)));
                }
            }
        });
        dashboardViewModel.getLiveOnlineDeviceCount().observe(this, onlineDeviceCount -> {
            if(onlineDeviceCount!=null){
                onlineDeviceCountText.setText(Integer.toString(onlineDeviceCount));

            }
        });
        dashboardViewModel.getLiveDeviceCount().observe(this, deviceCount -> {
            if(deviceCount!=null){
                deviceCountText.setText(Integer.toString(deviceCount));
            }
        });
        dashboardViewModel.getLiveActiveAttributeCount().observe(this, activeAttributeCount -> {
            if(activeAttributeCount!=null){
                activeAttributeCountText.setText(Integer.toString(activeAttributeCount));
            }
        });
        dashboardViewModel.getLiveAttributeCount().observe(this, attributeCount -> {
            if(attributeCount!=null){
                attributeCountText.setText(Integer.toString(attributeCount));
            }
        });
        dashboardViewModel.getLiveActiveEventCount().observe(this, activeEventCount -> {
            if(activeEventCount!=null){
                activeEventCountText.setText(Integer.toString(activeEventCount));
            }
        });
        dashboardViewModel.getLiveEventCount().observe(this, eventCount -> {
            if(eventCount!=null){
                eventCountText.setText(Integer.toString(eventCount));
            }
        });

        /*webView=view.findViewById(R.id.webView);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        String chartUrl="file:///android_asset/chart.html";
        webView.loadUrl(chartUrl);

        dashboardViewModel.getLiveFavoriteChartList().observe(this, favoriteChartList -> {

        });*/

        return view;
    }

    /*public class WebAppInterface{
        @JavascriptInterface
        public String getDataList(){
            dataRecordList=new ArrayList<DataRecord>();
            dataRecordList.add(new DataRecord("2019/06/14 07:11", "10"));
            dataRecordList.add(new DataRecord("2019/06/14 07:11", "20"));
            dataRecordList.add(new DataRecord("2019/06/14 07:11", "30"));
            dataRecordList.add(new DataRecord("2019/06/14 07:11", "40"));
            dataRecordList.add(new DataRecord("2019/06/14 07:11", "50"));
            JsonElement jsonElement=new Gson().toJsonTree(dataRecordList);
            return jsonElement.toString();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("onPause - Dashboard");
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("onResume - Dashboard");
    }*/
}
