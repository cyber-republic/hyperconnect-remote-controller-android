package com.hyper.connect.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.hyper.connect.database.dao.AttributeDao;
import com.hyper.connect.database.dao.CategoryDao;
import com.hyper.connect.database.dao.CategoryRecordDao;
import com.hyper.connect.database.dao.DataRecordDao;
import com.hyper.connect.database.dao.DeviceDao;
import com.hyper.connect.database.dao.EventDao;
import com.hyper.connect.database.dao.FavoriteChartDao;
import com.hyper.connect.database.dao.NotificationDao;
import com.hyper.connect.database.dao.SensorDao;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;
import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.FavoriteChart;
import com.hyper.connect.database.entity.Notification;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.enums.DeviceState;
import com.hyper.connect.database.entity.Event;
import com.hyper.connect.database.entity.Sensor;


@Database(entities={Device.class, Sensor.class, Attribute.class, Event.class, DataRecord.class, Notification.class, Category.class, CategoryRecord.class, FavoriteChart.class}, version=1)
public abstract class LocalRoomDatabase extends RoomDatabase{
    public abstract DeviceDao deviceDao();
    public abstract SensorDao sensorDao();
    public abstract AttributeDao attributeDao();
    public abstract EventDao eventDao();
    public abstract DataRecordDao dataRecordDao();
    public abstract NotificationDao notificationDao();
    public abstract CategoryDao categoryDao();
    public abstract CategoryRecordDao categoryRecordDao();
    public abstract FavoriteChartDao favoriteChartDao();

    private static volatile LocalRoomDatabase INSTANCE;

    public static LocalRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (LocalRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE=Room.databaseBuilder(context.getApplicationContext(), LocalRoomDatabase.class, "local_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback=new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            //new DummyAsyncTask(INSTANCE).execute();
            new SetDevicesOfflineAsyncTask(INSTANCE).execute();
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
        }
    };

    private static class DummyAsyncTask extends AsyncTask<Void, Void, Void> {
        private final DeviceDao deviceDao;

        DummyAsyncTask(LocalRoomDatabase localDatabase){
            deviceDao=localDatabase.deviceDao();
        }

        @Override
        protected Void doInBackground(final Void... params){
            Device device=new Device(1, "J1s9bhQJnR8gqWdJpNEfvVQ3ZP1KUqBXxSb8KQPwio3X", "eQ525ffnwHko97n9PWXr99Vu91hwfGyDWXjp5u6qauWYqrQCrx75", "Device1", DeviceState.ACTIVE, DeviceConnectionState.OFFLINE);
            deviceDao.insert(device);
            Device device2=new Device(2, "BvDjoMK2XMrNHydTuaGpaMGTYRU4QyVh5sFuivYyVyrc", "QznAmC1SGKdcn3FN5A6hrHgn3RvP7pTo2Ze8xmbEvWvHHHoScd2A", "Device2", DeviceState.ACTIVE, DeviceConnectionState.OFFLINE);
            deviceDao.insert(device2);

            return null;
        }
    }

    private static class SetDevicesOfflineAsyncTask extends AsyncTask<Void, Void, Void> {
        private final DeviceDao deviceDao;

        SetDevicesOfflineAsyncTask(LocalRoomDatabase localDatabase){
            deviceDao=localDatabase.deviceDao();
        }

        @Override
        protected Void doInBackground(final Void... params){
            deviceDao.setDeviceListConnectionState(DeviceConnectionState.OFFLINE);

            return null;
        }
    }
}
