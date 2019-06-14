package com.hyper.connect.page.history;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.database.entity.FavoriteChart;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.management.AttributeManagement;
import com.hyper.connect.page.dashboard.DashboardViewModel;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity{
    private AppCompatButton backButton;
    private TextView chartTitleText;
    private WebView webView;
    private AppCompatImageButton favoriteButton;
    private ArrayList<DataRecord> dataRecordList;
    private AttributeManagement attributeManagement;
    private String deviceUserId;
    private int edgeAttributeId;
    private DashboardViewModel dashboardViewModel;
    private LocalRepository localRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        deviceUserId=getIntent().getStringExtra("deviceUserId");
        edgeAttributeId=getIntent().getIntExtra("edgeAttributeId", -1);
        String attributeName=getIntent().getStringExtra("attributeName");
        int attributeId=getIntent().getIntExtra("attributeId", -1);

        ElastosCarrier elastosCarrier=GlobalApplication.getElastosCarrier();
        elastosCarrier.setHistoryActivity(this);
        attributeManagement=GlobalApplication.getAttributeManagement();

        if(!attributeManagement.isHistoryRunning()){
            attributeManagement.startHistory(deviceUserId, edgeAttributeId);
        }

        backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            attributeManagement.stopHistory();
            setResult(RESULT_CANCELED);
            finish();
        });

        chartTitleText=findViewById(R.id.chartTitleText);
        chartTitleText.setText(attributeName);

        /*favoriteButton=findViewById(R.id.favoriteButton);
        localRepository=GlobalApplication.getLocalRepository();
        dashboardViewModel=ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.getLiveFavoriteChartList().observe(HistoryActivity.this, favoriteChartList -> {
            if(favoriteChartList!=null){
                if(favoriteChartList.isEmpty()){
                    favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                    favoriteButton.setOnClickListener(buttonView -> {
                        localRepository.insertFavoriteChart(new FavoriteChart(0, attributeId));
                        Snackbar.make(favoriteButton, R.string.text_chart_pinned, Snackbar.LENGTH_SHORT).show();
                    });
                }
                else{
                    FavoriteChart favoriteChart=favoriteChartList.get(0);
                    if(favoriteChart.getAttributeId()==attributeId){
                        favoriteButton.setImageResource(R.drawable.ic_star_black_24dp);
                        favoriteButton.setOnClickListener(buttonView -> {
                            localRepository.deleteFavoriteChart();
                            Snackbar.make(favoriteButton, R.string.text_chart_unpinned, Snackbar.LENGTH_SHORT).show();
                        });
                    }
                    else{
                        favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                        favoriteButton.setOnClickListener(buttonView -> {
                            localRepository.insertFavoriteChart(new FavoriteChart(0, attributeId));
                            Snackbar.make(favoriteButton, R.string.text_chart_pinned, Snackbar.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });*/

        webView=findViewById(R.id.webView);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        String chartUrl="file:///android_asset/chart.html";
        webView.loadUrl(chartUrl);
    }

    public void setDataRecordList(ArrayList<DataRecord> dataRecordList){
        this.dataRecordList=dataRecordList;
        webView.post(new Runnable(){
            @Override
            public void run(){
                webView.reload();
            }
        });
    }

    public class WebAppInterface{
        @JavascriptInterface
        public String getDataList(){
            JsonElement jsonElement=new Gson().toJsonTree(dataRecordList);
            return jsonElement.toString();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        attributeManagement.stopHistory();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!attributeManagement.isHistoryRunning()){
            attributeManagement.startHistory(deviceUserId, edgeAttributeId);
        }
    }




}
