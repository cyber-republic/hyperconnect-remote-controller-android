package com.hyper.connect.page.history;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.database.entity.enums.EventAverage;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.management.AttributeManagement;
import com.hyper.connect.management.HistoryManagement;
import com.hyper.connect.page.dashboard.DashboardViewModel;
import com.hyper.connect.util.CustomUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity{
    private AppCompatButton backButton;
    private AppCompatImageButton liveButton;
    private AppCompatImageButton advancedButton;
    private TextView chartTitleText;
    private TextView typeText;
    private LinearLayout filterLayout;
    private AppCompatSpinner windowSpinner;
    private AppCompatSpinner averageSpinner;
    private LinearLayout dateLayout;
    private TextView dateText;
    private AppCompatImageButton dateButton;
    private AppCompatSpinner hourSpinner;
    private AppCompatSpinner monthSpinner;
    private AppCompatImageButton searchButton;
    private WebView liveWebView;
    private WebView advancedWebView;
    private LinearLayout liveEmptyPlaceholder;
    private LinearLayout advancedEmptyPlaceholder;
    private boolean isHistoryLive=true;
    private ArrayList<DataRecord> liveDataRecordList;
    private ArrayList<DataRecord> advancedDataRecordList;
    private AttributeManagement attributeManagement;
    private int deviceId;
    private String deviceUserId;
    private int edgeAttributeId;
    private DashboardViewModel dashboardViewModel;
    private LocalRepository localRepository;
    private ElastosCarrier elastosCarrier;
    private HistoryManagement historyManagement;

    private String searchFileName;
    private String searchDateTime;
    private String searchPattern;
    private String searchWindow;
    private EventAverage searchAverage;
    private boolean isTransferLoading=false;
    private RelativeLayout fadeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        deviceId=getIntent().getIntExtra("deviceId", -1);
        deviceUserId=getIntent().getStringExtra("deviceUserId");
        edgeAttributeId=getIntent().getIntExtra("edgeAttributeId", -1);
        String attributeName=getIntent().getStringExtra("attributeName");
        int attributeId=getIntent().getIntExtra("attributeId", -1);

        isTransferLoading=false;
        fadeLayout=findViewById(R.id.fadeLayout);
        fadeLayout.setVisibility(View.GONE);

        elastosCarrier=GlobalApplication.getElastosCarrier();
        elastosCarrier.setHistoryActivity(this);
        attributeManagement=GlobalApplication.getAttributeManagement();
        historyManagement=new HistoryManagement(getApplicationContext());

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

        String chartUrl="file:///android_asset/chart.html";
        liveWebView=findViewById(R.id.liveWebView);
        liveWebView.addJavascriptInterface(new WebAppInterfaceLive(), "Android");
        liveWebView.getSettings().setJavaScriptEnabled(true);
        liveWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        liveWebView.getSettings().setLoadWithOverviewMode(true);
        liveWebView.loadUrl(chartUrl);
        liveEmptyPlaceholder=findViewById(R.id.liveEmptyPlaceholder);
        liveEmptyPlaceholder.setVisibility(View.VISIBLE);

        advancedWebView=findViewById(R.id.advancedWebView);
        advancedWebView.addJavascriptInterface(new WebAppInterfaceAdvanced(), "Android");
        advancedWebView.getSettings().setJavaScriptEnabled(true);
        advancedWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        advancedWebView.getSettings().setLoadWithOverviewMode(true);
        advancedWebView.loadUrl(chartUrl);
        advancedEmptyPlaceholder=findViewById(R.id.advancedEmptyPlaceholder);
        advancedEmptyPlaceholder.setVisibility(View.GONE);

        typeText=findViewById(R.id.typeText);
        filterLayout=findViewById(R.id.filterLayout);
        filterLayout.setVisibility(View.GONE);
        liveButton=findViewById(R.id.liveButton);
        advancedButton=findViewById(R.id.advancedButton);
        liveButton.setOnClickListener(buttonView -> {
            if(!attributeManagement.isHistoryRunning()){
                attributeManagement.startHistory(deviceUserId, edgeAttributeId);
            }
            liveButton.setClickable(false);
            liveButton.setBackgroundResource(R.drawable.button_disabled_background);
            advancedButton.setClickable(true);
            advancedButton.setBackgroundResource(R.drawable.button_background);
            typeText.setText(R.string.button_live);
            filterLayout.setVisibility(View.GONE);
            advancedWebView.setVisibility(View.GONE);
            advancedEmptyPlaceholder.setVisibility(View.GONE);
            isHistoryLive=true;
            setLiveDataRecordList(liveDataRecordList);
        });
        advancedButton.setOnClickListener(buttonView -> {
            attributeManagement.stopHistory();
            advancedButton.setClickable(false);
            advancedButton.setBackgroundResource(R.drawable.button_disabled_background);
            liveButton.setClickable(true);
            liveButton.setBackgroundResource(R.drawable.button_background);
            typeText.setText(R.string.button_advanced);
            filterLayout.setVisibility(View.VISIBLE);
            liveWebView.setVisibility(View.GONE);
            liveEmptyPlaceholder.setVisibility(View.GONE);
            isHistoryLive=false;
            setAdvancedDataRecordList(advancedDataRecordList);
        });

        windowSpinner=findViewById(R.id.windowSpinner);
        averageSpinner=findViewById(R.id.averageSpinner);
        dateLayout=findViewById(R.id.dateLayout);
        dateText=findViewById(R.id.dateText);
        dateButton=findViewById(R.id.dateButton);
        hourSpinner=findViewById(R.id.hourSpinner);
        monthSpinner=findViewById(R.id.monthSpinner);
        averageSpinner.setEnabled(false);
        dateLayout.setVisibility(View.GONE);
        dateText.setText(R.string.hint_select_date);
        dateButton.setEnabled(false);
        dateButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorGray)));
        hourSpinner.setVisibility(View.GONE);
        hourSpinner.setEnabled(false);
        monthSpinner.setVisibility(View.GONE);
        monthSpinner.setEnabled(false);

        List<String> windowList=new ArrayList<String>();
        windowList.add("<Window>");
        windowList.add("Hour");
        windowList.add("Day");
        windowList.add("Month");
        ArrayAdapter<String> windowAdapter=new ArrayAdapter<String>(HistoryActivity.this, R.layout.item_spinner_text, R.id.textView, windowList);
        windowSpinner.setAdapter(windowAdapter);

        List<String> averageList=new ArrayList<String>();
        averageList.add("<Average>");
        averageList.add(EventAverage.ONE_MINUTE.toString());
        averageList.add(EventAverage.FIVE_MINUTES.toString());
        ArrayAdapter<String> averageAdapter=new ArrayAdapter<String>(HistoryActivity.this, R.layout.item_spinner_text, R.id.textView, averageList);
        averageSpinner.setAdapter(averageAdapter);

        windowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                dateLayout.setVisibility(View.GONE);
                hourSpinner.setVisibility(View.GONE);
                monthSpinner.setVisibility(View.GONE);
                if(position==0){
                    averageSpinner.setEnabled(false);
                }
                else{
                    String window=(String)parent.getItemAtPosition(position);
                    List<String> averageList=new ArrayList<String>();
                    averageList.add("<Average>");
                    if(window.equals("Hour")){
                        averageList.add(EventAverage.ONE_MINUTE.toString());
                        averageList.add(EventAverage.FIVE_MINUTES.toString());
                        dateLayout.setVisibility(View.VISIBLE);
                        hourSpinner.setVisibility(View.VISIBLE);
                    }
                    else if(window.equals("Day")){
                        averageList.add(EventAverage.ONE_MINUTE.toString());
                        averageList.add(EventAverage.FIVE_MINUTES.toString());
                        averageList.add(EventAverage.FIFTEEN_MINUTES.toString());
                        averageList.add(EventAverage.ONE_HOUR.toString());
                        dateLayout.setVisibility(View.VISIBLE);
                    }
                    else if(window.equals("Month")){
                        averageList.add(EventAverage.ONE_HOUR.toString());
                        averageList.add(EventAverage.THREE_HOURS.toString());
                        averageList.add(EventAverage.SIX_HOURS.toString());
                        averageList.add(EventAverage.ONE_DAY.toString());
                        monthSpinner.setVisibility(View.VISIBLE);
                    }
                    ArrayAdapter<String> averageAdapter=new ArrayAdapter<String>(HistoryActivity.this, R.layout.item_spinner_text, R.id.textView, averageList);
                    averageSpinner.setAdapter(averageAdapter);
                    averageSpinner.setEnabled(true);
                }
                averageSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        averageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                dateText.setText(R.string.hint_select_date);
                hourSpinner.setEnabled(false);
                if(position==0){
                    dateButton.setEnabled(false);
                    dateButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorGray)));
                    monthSpinner.setEnabled(false);
                }
                else{
                    dateButton.setEnabled(true);
                    dateButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorMetal)));
                    monthSpinner.setEnabled(true);
                }
                hourSpinner.setSelection(0);
                monthSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        dateButton.setOnClickListener(buttonView -> {
            DatePickerDialog datePickerDialog=new DatePickerDialog(HistoryActivity.this);
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                    month++;
                    String date=year+"/"+String.format("%02d", month)+"/"+String.format("%02d", dayOfMonth);
                    dateText.setText(date);
                    hourSpinner.setEnabled(true);
                }
            });
        });

        List<String> hourList=new ArrayList<String>();
        hourList.add("<Hour>");
        for(int i=0;i<24;i++){
            String hour=String.format("%02d", i)+" h";
            hourList.add(hour);
        }
        ArrayAdapter<String> hourAdapter=new ArrayAdapter<String>(HistoryActivity.this, R.layout.item_spinner_text, R.id.textView, hourList);
        hourSpinner.setAdapter(hourAdapter);

        List<String> monthList=new ArrayList<String>();
        monthList.add("<Month>");
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        ArrayAdapter<String> monthAdapter=new ArrayAdapter<String>(HistoryActivity.this, R.layout.item_spinner_text, R.id.textView, monthList);
        monthSpinner.setAdapter(monthAdapter);

        searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(buttonView -> {
            if(isTransferLoading){
                Snackbar.make(searchButton, R.string.snack_chart_is_loading, Snackbar.LENGTH_SHORT).show();
            }
            else{
                int errorCount=0;
                String fileName="";
                String dateTime="";
                String pattern="";
                String window="";
                EventAverage average=null;
                int windowIndex=windowSpinner.getSelectedItemPosition();
                int averageIndex=averageSpinner.getSelectedItemPosition();
                if(windowIndex!=0 && averageIndex!=0){
                    window=(String)windowSpinner.getSelectedItem();
                    average=EventAverage.stringValueOf((String)averageSpinner.getSelectedItem());
                    fileName=edgeAttributeId+"_"+average.getShortFilename()+".json";
                    if(window.equals("Hour")){
                        String date=dateText.getText().toString();
                        if(!date.equals("-- Select Date --")){
                            int hourIndex=hourSpinner.getSelectedItemPosition();
                            if(hourIndex!=0){
                                String hour=((String)hourSpinner.getSelectedItem()).substring(0, 2);
                                dateTime=date+" "+hour;
                            }
                            else{
                                errorCount++;
                            }
                        }
                        else{
                            errorCount++;
                        }
                    }
                    else if(window.equals("Day")){
                        String date=dateText.getText().toString();
                        if(!date.equals("-- Select Date --")){
                            dateTime=date;
                        }
                        else{
                            errorCount++;
                        }
                    }
                    else if(window.equals("Month")){
                        int monthIndex=monthSpinner.getSelectedItemPosition();
                        if(monthIndex!=0){
                            String month=getMonthByKey((String)monthSpinner.getSelectedItem());
                            int year=Calendar.getInstance().get(Calendar.YEAR);
                            dateTime=year+"/"+month;
                        }
                        else{
                            errorCount++;
                        }
                    }

                    if(average==EventAverage.ONE_MINUTE || average==EventAverage.FIVE_MINUTES || average==EventAverage.FIFTEEN_MINUTES){
                        pattern="yyyy/MM/dd HH:mm";
                    }
                    else if(average==EventAverage.ONE_HOUR || average==EventAverage.THREE_HOURS || average==EventAverage.SIX_HOURS){
                        pattern="yyyy/MM/dd HH";
                    }
                    else if(average==EventAverage.ONE_DAY){
                        pattern="yyyy/MM/dd";
                    }
                }
                else{
                    errorCount++;
                }

                if(errorCount==0){
                    searchFileName=fileName;
                    searchDateTime=dateTime;
                    searchPattern=pattern;
                    searchWindow=window;
                    searchAverage=average;

                    showSpinner();

                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("command", "getHistoryFile");
                    jsonObject.addProperty("fileName", fileName);
                    elastosCarrier.sendFriendMessage(deviceUserId, jsonObject.toString());
                }
                else{
                    Snackbar.make(searchButton, R.string.snack_all_options_must_be_selected, Snackbar.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void showData(){
        Map<String, String> valueMap=historyManagement.getHistory(deviceId, searchFileName);
        if(valueMap!=null){
            ArrayList<DataRecord> sortedList=valueMap.entrySet().stream().map(
                    r -> new DataRecord(CustomUtil.getDateTimeByPattern(r.getKey(), searchPattern), r.getValue())
            ).filter(
                    object -> object.getDateTime().contains(searchDateTime)
            ).reduce(
                    new ArrayList<DataRecord>(),
                    (objectList, record) -> {
                        objectList.add(record);
                        return objectList;
                    },
                    (objectList, otherObjectList) -> {
                        objectList.addAll(otherObjectList);
                        return objectList;
                    }
            );
            sortedList.sort(new Comparator<DataRecord>(){
                SimpleDateFormat f=new SimpleDateFormat(searchPattern);
                public int compare(DataRecord o1, DataRecord o2){
                    try{
                        return f.parse(o1.getDateTime()).before(f.parse(o2.getDateTime())) ? -1 : 1;
                    }
                    catch(ParseException pe){
                        return 0;
                    }
                }
            });
            setAdvancedDataRecordList(sortedList);
        }
        else{
            advancedWebView.setVisibility(View.GONE);
            advancedEmptyPlaceholder.setVisibility(View.VISIBLE);
        }
        removeSpinner();
    }

    public void showError(){
        removeSpinner();
        Snackbar.make(searchButton, R.string.snack_data_transfer_error, Snackbar.LENGTH_SHORT).show();
    }

    public void showSpinner(){
        isTransferLoading=true;
        runOnUiThread(() -> {
            fadeLayout.setVisibility(View.VISIBLE);
        });
    }

    public void removeSpinner(){
        isTransferLoading=false;
        runOnUiThread(() -> {
            fadeLayout.setVisibility(View.GONE);
        });
    }

    public void setLiveDataRecordList(ArrayList<DataRecord> dataRecordList){
        liveDataRecordList=dataRecordList;
        if(isHistoryLive){
            liveWebView.post(() -> {
                if(liveDataRecordList==null || liveDataRecordList.size()==0){
                    liveWebView.setVisibility(View.GONE);
                    liveEmptyPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    liveWebView.setVisibility(View.VISIBLE);
                    liveEmptyPlaceholder.setVisibility(View.GONE);
                    liveWebView.reload();
                }
            });
        }
    }

    public void setAdvancedDataRecordList(ArrayList<DataRecord> dataRecordList){
        advancedDataRecordList=dataRecordList;
        advancedWebView.post(() -> {
            if(advancedDataRecordList==null || advancedDataRecordList.size()==0){
                advancedWebView.setVisibility(View.GONE);
                advancedEmptyPlaceholder.setVisibility(View.VISIBLE);
            }
            else{
                advancedWebView.setVisibility(View.VISIBLE);
                advancedEmptyPlaceholder.setVisibility(View.GONE);
                advancedWebView.post(() -> advancedWebView.reload());
            }
        });
    }

    public class WebAppInterfaceLive{
        @JavascriptInterface
        public String getDataList(){
            JsonElement jsonElement=new Gson().toJsonTree(liveDataRecordList);
            return jsonElement.toString();
        }
    }

    public class WebAppInterfaceAdvanced{
        @JavascriptInterface
        public String getDataList(){
            JsonElement jsonElement=new Gson().toJsonTree(advancedDataRecordList);
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

    private String getMonthByKey(String monthKey){
        String month="";
        if(monthKey.equals("January")){
            month="01";
        }
        else if(monthKey.equals("February")){
            month="02";
        }
        else if(monthKey.equals("March")){
            month="03";
        }
        else if(monthKey.equals("April")){
            month="04";
        }
        else if(monthKey.equals("May")){
            month="05";
        }
        else if(monthKey.equals("June")){
            month="06";
        }
        else if(monthKey.equals("July")){
            month="07";
        }
        else if(monthKey.equals("August")){
            month="08";
        }
        else if(monthKey.equals("September")){
            month="09";
        }
        else if(monthKey.equals("October")){
            month="10";
        }
        else if(monthKey.equals("November")){
            month="11";
        }
        else if(monthKey.equals("December")){
            month="12";
        }
        return month;
    }


}
