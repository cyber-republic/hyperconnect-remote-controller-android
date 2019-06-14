package com.hyper.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.view.MenuItem;

import com.hyper.connect.app.ElastosService;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.page.dashboard.DashboardFragment;
import com.hyper.connect.page.devices.DevicesFragment;
import com.hyper.connect.page.events.EventsFragment;
import com.hyper.connect.page.notifications.NotificationsActivity;

public class MainActivity extends AppCompatActivity{
    public static final int DASHBOARD_TAB_ID=0;
    public static final int DEVICES_TAB_ID=1;
    public static final int EVENTS_TAB_ID=2;
    public static final int ACTION_REQUEST_ADD_DEVICE=0;
    public static final int ACTION_REQUEST_ADD_EVENT=1;
    public static final int PERMISSION_REQUEST_CODE_CAMERA=0;
    public static final String ACTION_RESPONSE_KEYWORD="response";
    public static final int ACTION_RESULT_SUCCESS=0;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalApplication.setAppActive(true);

        initMainNavigation();
        initNotificationsAction();

        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ACTION_REQUEST_ADD_DEVICE && resultCode==RESULT_OK){
            Snackbar.make(viewPager, R.string.snack_device_request, Snackbar.LENGTH_SHORT).show();
        }
        else if(requestCode==ACTION_REQUEST_ADD_EVENT && resultCode==RESULT_OK){
            Snackbar.make(viewPager, R.string.snack_event_added, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent serviceIntent=new Intent(this, ElastosService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        GlobalApplication.setAppActive(false);
    }

    @Override
    protected void onResume(){
        super.onResume();
        GlobalApplication.setAppActive(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        GlobalApplication.setAppActive(false);
    }

    private void initNotificationsAction(){
        AppCompatImageButton notificationsButton=findViewById(R.id.notificationsButton);
        notificationsButton.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });
    }

    private void initMainNavigation(){
        viewPager=findViewById(R.id.view_pager);
        bottomNavigationView=findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(DASHBOARD_TAB_ID);
                        return true;
                    case R.id.navigation_devices:
                        viewPager.setCurrentItem(DEVICES_TAB_ID);
                        return true;
                    case R.id.navigation_events:
                        viewPager.setCurrentItem(EVENTS_TAB_ID);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i1){}

            @Override
            public void onPageSelected(int i){
                switch(i){
                    case DASHBOARD_TAB_ID:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case DEVICES_TAB_ID:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_devices);
                        break;
                    case EVENTS_TAB_ID:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_events);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i){}
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int i){
                Fragment fragment=null;
                switch(i){
                    case DASHBOARD_TAB_ID:
                        fragment=new DashboardFragment();
                        break;
                    case DEVICES_TAB_ID:
                        fragment=new DevicesFragment();
                        break;
                    case EVENTS_TAB_ID:
                        fragment=new EventsFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount(){
                return 3;
            }
        });
    }
}
