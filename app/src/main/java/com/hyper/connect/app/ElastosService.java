package com.hyper.connect.app;

import static com.hyper.connect.app.GlobalApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.hyper.connect.MainActivity;
import com.hyper.connect.R;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.management.AttributeManagement;

public class ElastosService extends Service{
    private static boolean DEBUG=false;
    private static boolean isServiceRunning=false;
    private static ElastosCarrier elastosCarrier=null;
    private static AttributeManagement attributeManagement=null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(!isServiceRunning){
            Intent notificationIntent=new Intent(this, MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, notificationIntent, 0);
            Notification notification=new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Hyper Connect")
                    .setContentText("Connection service is running in background.")
                    .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
            startElastos();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);

        if(!GlobalApplication.getStayConnected()){
            stopElastos();
            stopForeground(true);
            stopSelf();
        }
    }

    private void startElastos(){
        if(elastosCarrier==null){
            elastosCarrier=new ElastosCarrier(getApplicationContext());
            attributeManagement=new AttributeManagement();
            if(!DEBUG){
                elastosCarrier.start();
            }
            GlobalApplication.setElastosCarrier(elastosCarrier);
            GlobalApplication.setAttributeManagement(attributeManagement);
            isServiceRunning=true;
        }
    }

    private void stopElastos(){
        if(!DEBUG){
            elastosCarrier.kill();
        }
        elastosCarrier.interrupt();
        elastosCarrier=null;
        isServiceRunning=false;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
