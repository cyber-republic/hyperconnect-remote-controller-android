package com.hyper.connect.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.hyper.connect.database.LocalRoomDatabase;
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
import com.hyper.connect.database.entity.Event;
import com.hyper.connect.database.entity.FavoriteChart;
import com.hyper.connect.database.entity.Notification;
import com.hyper.connect.database.entity.Sensor;
import com.hyper.connect.database.entity.enums.AttributeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocalRepository{
    private DeviceDao deviceDao;
    private SensorDao sensorDao;
    private AttributeDao attributeDao;
    private EventDao eventDao;
    private DataRecordDao dataRecordDao;
    private NotificationDao notificationDao;
    private CategoryDao categoryDao;
    private CategoryRecordDao categoryRecordDao;
    private FavoriteChartDao favoriteChartDao;
    private Device tempDevice;
    private MutableLiveData<Boolean> carrierConnectionState;

    public LocalRepository(Context context){
        LocalRoomDatabase localRoomDatabase=LocalRoomDatabase.getDatabase(context);
        deviceDao=localRoomDatabase.deviceDao();
        sensorDao=localRoomDatabase.sensorDao();
        attributeDao=localRoomDatabase.attributeDao();
        eventDao=localRoomDatabase.eventDao();
        dataRecordDao=localRoomDatabase.dataRecordDao();
        notificationDao=localRoomDatabase.notificationDao();
        categoryDao=localRoomDatabase.categoryDao();
        categoryRecordDao=localRoomDatabase.categoryRecordDao();
        favoriteChartDao=localRoomDatabase.favoriteChartDao();
        carrierConnectionState=new MutableLiveData<Boolean>();
        carrierConnectionState.postValue(false);
    }

    public LiveData<Boolean> getCarrierConnectionState(){
        return carrierConnectionState;
    }

    public void setCarrierConnectionState(boolean state){
        carrierConnectionState.postValue(state);
    }

    public Device getTempDevice(){
        return tempDevice;
    }

    public void setTempDevice(Device device){
        tempDevice=device;
    }

    public List<Device> getDeviceList(){
        return deviceDao.getDeviceList();
    }

    public LiveData<List<Device>> getLiveDeviceList(){
        return deviceDao.getLiveDeviceList();
    }

    public LiveData<List<Sensor>> getLiveSensorList(){
        return sensorDao.getLiveSensorList();
    }

    public LiveData<List<Sensor>> getLiveSensorListByDeviceId(int deviceId){
        return sensorDao.getLiveSensorListByDeviceId(deviceId);
    }

    public LiveData<List<Attribute>> getLiveAttributeList(){
        return attributeDao.getLiveAttributeList();
    }

    public LiveData<List<Event>> getLiveEventList(){
        return eventDao.getLiveEventList();
    }

    public LiveData<List<Attribute>> getLiveAttributeListByEdgeSensorIdAndDeviceId(int edgeSensorId, int deviceId){
        return attributeDao.getLiveAttributeListByEdgeSensorIdAndDeviceId(edgeSensorId, deviceId);
    }

    public LiveData<List<Attribute>> getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(int edgeSensorId, int deviceId, AttributeDirection direction){
        return attributeDao.getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(edgeSensorId, deviceId, direction.getValue());
    }

    public Device getDeviceById(int id){
        return deviceDao.getDeviceById(id);
    }

    public LiveData<Device> getLiveDeviceById(int id){
        return deviceDao.getLiveDeviceById(id);
    }

    public Device getDeviceByUserId(String userId){
        return deviceDao.getDeviceByUserId(userId);
    }

    public Device getDeviceByAddress(String address){
        Device device=null;
        try{
            device=new GetDeviceByAddressAsyncTask(deviceDao).execute(address).get();
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return device;
    }

    private static class GetDeviceByAddressAsyncTask extends AsyncTask<String, Void, Device>{
        private DeviceDao deviceDao;

        private GetDeviceByAddressAsyncTask(DeviceDao deviceDao){
            this.deviceDao=deviceDao;
        }

        @Override
        protected Device doInBackground(String... params){
            String address=params[0];
            Device device=deviceDao.getDeviceByAddress(address);
            return device;
        }
    }

    public LiveData<Device> getLiveDeviceByUserId(String userId){
        return deviceDao.getLiveDeviceByUserId(userId);
    }

    public Sensor getSensorByEdgeSensorIdAndDeviceId(int edgeSensorId, int deviceId){
        return sensorDao.getSensorByEdgeSensorIdAndDeviceId(edgeSensorId, deviceId);
    }

    public Event getEventByGlobalEventId(String globalEventId){
        return eventDao.getEventByGlobalEventId(globalEventId);
    }

    public Event getEventByGlobalEventIdAndSourceDeviceId(String globalEventId, int sourceDeviceId){
        return eventDao.getEventByGlobalEventIdAndSourceDeviceId(globalEventId, sourceDeviceId);
    }

    public Attribute getAttributeByEdgeAttributeIdAndDeviceId(int edgeAttributeId, int deviceId){
        return attributeDao.getAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, deviceId);
    }

    public LiveData<Attribute> getLiveAttributeByEdgeAttributeIdAndDeviceId(int edgeAttributeId, int deviceId){
        return attributeDao.getLiveAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, deviceId);
    }

    public void insertDevice(Device device){
        new InsertDeviceAsyncTask(deviceDao).execute(device);
    }

    private static class InsertDeviceAsyncTask extends AsyncTask<Device, Void, Void>{
        private DeviceDao deviceDao;

        private InsertDeviceAsyncTask(DeviceDao deviceDao){
            this.deviceDao=deviceDao;
        }

        @Override
        protected Void doInBackground(Device... params){
            Device device=params[0];
            deviceDao.insert(device);
            return null;
        }
    }

    public void deleteDevice(Device device){
        new DeleteDeviceAsyncTask(deviceDao).execute(device);
    }

    private static class DeleteDeviceAsyncTask extends AsyncTask<Device, Void, Void>{
        private DeviceDao deviceDao;

        private DeleteDeviceAsyncTask(DeviceDao deviceDao){
            this.deviceDao=deviceDao;
        }

        @Override
        protected Void doInBackground(Device... params){
            Device device=params[0];
            deviceDao.deleteDeviceById(device.getId());
            return null;
        }
    }

    public void updateDevice(Device device){
        new UpdateDeviceAsyncTask(deviceDao).execute(device);
    }

    private static class UpdateDeviceAsyncTask extends AsyncTask<Device, Void, Void>{
        private DeviceDao deviceDao;

        private UpdateDeviceAsyncTask(DeviceDao deviceDao){
            this.deviceDao=deviceDao;
        }

        @Override
        protected Void doInBackground(Device... params){
            Device device=params[0];
            deviceDao.update(device);
            return null;
        }
    }

    public void insertSensor(Sensor sensor){
        new InsertSensorAsyncTask(sensorDao).execute(sensor);
    }

    private static class InsertSensorAsyncTask extends AsyncTask<Sensor, Void, Void>{
        private SensorDao sensorDao;

        private InsertSensorAsyncTask(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected Void doInBackground(Sensor... params){
            Sensor sensor=params[0];
            sensorDao.insert(sensor);
            return null;
        }
    }

    public void insertAttribute(Attribute attribute){
        new InsertAttributeAsyncTask(attributeDao).execute(attribute);
    }

    private static class InsertAttributeAsyncTask extends AsyncTask<Attribute, Void, Void>{
        private AttributeDao attributeDao;

        private InsertAttributeAsyncTask(AttributeDao attributeDao){
            this.attributeDao=attributeDao;
        }

        @Override
        protected Void doInBackground(Attribute... params){
            Attribute attribute=params[0];
            attributeDao.insert(attribute);
            return null;
        }
    }

    public void updateAttribute(Attribute attribute){
        new UpdateAttributeAsyncTask(attributeDao).execute(attribute);
    }

    private static class UpdateAttributeAsyncTask extends AsyncTask<Attribute, Void, Void>{
        private AttributeDao attributeDao;

        private UpdateAttributeAsyncTask(AttributeDao attributeDao){
            this.attributeDao=attributeDao;
        }

        @Override
        protected Void doInBackground(Attribute... params){
            Attribute attribute=params[0];
            attributeDao.update(attribute);
            return null;
        }
    }

    public void insertEvent(Event event){
        new InsertEventAsyncTask(eventDao).execute(event);
    }

    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, Void>{
        private EventDao eventDao;

        private InsertEventAsyncTask(EventDao eventDao){
            this.eventDao=eventDao;
        }

        @Override
        protected Void doInBackground(Event... params){
            Event event=params[0];
            eventDao.insert(event);
            return null;
        }
    }

    public void updateEvent(Event event){
        new UpdateEventAsyncTask(eventDao).execute(event);
    }

    private static class UpdateEventAsyncTask extends AsyncTask<Event, Void, Void>{
        private EventDao eventDao;

        private UpdateEventAsyncTask(EventDao eventDao){
            this.eventDao=eventDao;
        }

        @Override
        protected Void doInBackground(Event... params){
            Event event=params[0];
            eventDao.update(event);
            return null;
        }
    }

    public void insertDataRecord(DataRecord dataRecord){
        new InsertDataRecordAsyncTask(dataRecordDao).execute(dataRecord);
    }

    private static class InsertDataRecordAsyncTask extends AsyncTask<DataRecord, Void, Void>{
        private DataRecordDao dataRecordDao;

        private InsertDataRecordAsyncTask(DataRecordDao dataRecordDao){
            this.dataRecordDao=dataRecordDao;
        }

        @Override
        protected Void doInBackground(DataRecord... params){
            DataRecord dataRecord=params[0];
            dataRecordDao.insert(dataRecord);
            return null;
        }
    }

    public void updateDataRecord(DataRecord dataRecord){
        new UpdateDataRecordAsyncTask(dataRecordDao).execute(dataRecord);
    }

    private static class UpdateDataRecordAsyncTask extends AsyncTask<DataRecord, Void, Void>{
        private DataRecordDao dataRecordDao;

        private UpdateDataRecordAsyncTask(DataRecordDao dataRecordDao){
            this.dataRecordDao=dataRecordDao;
        }

        @Override
        protected Void doInBackground(DataRecord... params){
            DataRecord dataRecord=params[0];
            dataRecordDao.update(dataRecord);
            return null;
        }
    }

    public DataRecord getDataRecordByDeviceUserIdAndEdgeAttributeId(String deviceUserId, int edgeAttributeId){
        return dataRecordDao.getDataRecordByDeviceUserIdAndEdgeAttributeId(deviceUserId, edgeAttributeId);
    }

    public LiveData<DataRecord> getLiveDataRecordByDeviceUserIdAndEdgeAttributeId(String deviceUserId, int edgeAttributeId){
        return dataRecordDao.getLiveDataRecordByDeviceUserIdAndEdgeAttributeId(deviceUserId, edgeAttributeId);
    }

    public LiveData<List<Notification>> getLiveNotificationList(){
        return notificationDao.getLiveNotificationList();
    }

    public LiveData<List<Notification>> getLiveNotificationListByDeviceUserId(String deviceUserId){
        return notificationDao.getLiveNotificationListByDeviceUserId(deviceUserId);
    }

    public void insertNotification(Notification notification){
        new InsertNotificationAsyncTask(notificationDao).execute(notification);
    }

    private static class InsertNotificationAsyncTask extends AsyncTask<Notification, Void, Void>{
        private NotificationDao notificationDao;

        private InsertNotificationAsyncTask(NotificationDao notificationDao){
            this.notificationDao=notificationDao;
        }

        @Override
        protected Void doInBackground(Notification... params){
            Notification notification=params[0];
            notificationDao.insert(notification);
            return null;
        }
    }

    public void deleteAttribute(Attribute attribute){
        new DeleteAttributeAsyncTask(attributeDao).execute(attribute);
    }

    private static class DeleteAttributeAsyncTask extends AsyncTask<Attribute, Void, Void>{
        private AttributeDao attributeDao;

        private DeleteAttributeAsyncTask(AttributeDao attributeDao){
            this.attributeDao=attributeDao;
        }

        @Override
        protected Void doInBackground(Attribute... params){
            Attribute attribute=params[0];
            attributeDao.deleteAttributeById(attribute.getId());
            return null;
        }
    }

    public void deleteSensor(Sensor sensor){
        new DeleteSensorAsyncTask(sensorDao).execute(sensor);
    }

    private static class DeleteSensorAsyncTask extends AsyncTask<Sensor, Void, Void>{
        private SensorDao sensorDao;

        private DeleteSensorAsyncTask(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected Void doInBackground(Sensor... params){
            Sensor sensor=params[0];
            sensorDao.deleteSensorById(sensor.getId());
            return null;
        }
    }

    public void deleteEvent(Event event){
        new DeleteEventAsyncTask(eventDao).execute(event);
    }

    private static class DeleteEventAsyncTask extends AsyncTask<Event, Void, Void>{
        private EventDao eventDao;

        private DeleteEventAsyncTask(EventDao eventDao){
            this.eventDao=eventDao;
        }

        @Override
        protected Void doInBackground(Event... params){
            Event event=params[0];
            eventDao.deleteEventById(event.getId());
            return null;
        }
    }

    public LiveData<List<Category>> getLiveCategoryList(){
        return categoryDao.getLiveCategoryList();
    }

    public LiveData<Category> getLiveCategoryById(int id){
        return categoryDao.getLiveCategoryById(id);
    }

    public Category getCategoryById(int id){
        return categoryDao.getCategoryById(id);
    }

    public void insertCategory(Category category){
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }

    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void>{
        private CategoryDao categoryDao;

        private InsertCategoryAsyncTask(CategoryDao categoryDao){
            this.categoryDao=categoryDao;
        }

        @Override
        protected Void doInBackground(Category... params){
            Category category=params[0];
            categoryDao.insert(category);
            return null;
        }
    }

    public LiveData<List<CategoryRecord>> getLiveCategoryRecordList(){
        return categoryRecordDao.getLiveCategoryRecordList();
    }

    public CategoryRecord getCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId){
        return categoryRecordDao.getCategoryRecordByCategoryIdAndAttributeId(categoryId, attributeId);
    }

    public LiveData<CategoryRecord> getLiveCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId){
        return categoryRecordDao.getLiveCategoryRecordByCategoryIdAndAttributeId(categoryId, attributeId);
    }

    public LiveData<List<CategoryRecord>> getLiveCategoryRecordListByAttributeId(int attributeId){
        return categoryRecordDao.getLiveCategoryRecordListByAttributeId(attributeId);
    }

    public List<CategoryRecord> getCategoryRecordListByAttributeId(int attributeId){
        return categoryRecordDao.getCategoryRecordListByAttributeId(attributeId);
    }

    public LiveData<List<Category>> getLiveCategoryListByDeviceId(int deviceId){
        return categoryDao.getLiveCategoryListByDeviceId(deviceId);
    }

    public void insertCategoryRecord(CategoryRecord categoryRecord){
        new InsertCategoryRecordAsyncTask(categoryRecordDao).execute(categoryRecord);
    }

    private static class InsertCategoryRecordAsyncTask extends AsyncTask<CategoryRecord, Void, Void>{
        private CategoryRecordDao categoryRecordDao;

        private InsertCategoryRecordAsyncTask(CategoryRecordDao categoryRecordDao){
            this.categoryRecordDao=categoryRecordDao;
        }

        @Override
        protected Void doInBackground(CategoryRecord... params){
            CategoryRecord newCategoryRecord=params[0];
            CategoryRecord categoryRecord=categoryRecordDao.getCategoryRecordByCategoryIdAndAttributeId(newCategoryRecord.getCategoryId(), newCategoryRecord.getAttributeId());
            if(categoryRecord==null){
                categoryRecordDao.insert(newCategoryRecord);
            }
            return null;
        }
    }

    public void deleteCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId){
        new DeleteCategoryRecordAsyncTask(categoryRecordDao).execute(categoryId, attributeId);
    }

    private static class DeleteCategoryRecordAsyncTask extends AsyncTask<Integer, Void, Void>{
        private CategoryRecordDao categoryRecordDao;

        private DeleteCategoryRecordAsyncTask(CategoryRecordDao categoryRecordDao){
            this.categoryRecordDao=categoryRecordDao;
        }

        @Override
        protected Void doInBackground(Integer... params){
            int categoryId=params[0];
            int attributeId=params[1];
            categoryRecordDao.deleteCategoryRecordByCategoryIdAndAttributeId(categoryId, attributeId);
            return null;
        }
    }

    public LiveData<Integer> getLiveDeviceCount(){
        return deviceDao.getLiveDeviceCount();
    }

    public LiveData<Integer> getLiveOnlineDeviceCount(){
        return deviceDao.getLiveOnlineDeviceCount();
    }

    public LiveData<Integer> getLiveAttributeCount(){
        return attributeDao.getLiveAttributeCount();
    }

    public LiveData<Integer> getLiveActiveAttributeCount(){
        return attributeDao.getLiveActiveAttributeCount();
    }

    public LiveData<Integer> getLiveEventCount(){
        return eventDao.getLiveEventCount();
    }

    public LiveData<Integer> getLiveActiveEventCount(){
        return eventDao.getLiveActiveEventCount();
    }

    public LiveData<List<FavoriteChart>> getLiveFavoriteChartList(){
        return favoriteChartDao.getLiveFavoriteChartList();
    }

    public void insertFavoriteChart(FavoriteChart favoriteChart){
        new InsertFavoriteChartAsyncTask(favoriteChartDao).execute(favoriteChart);
    }

    private static class InsertFavoriteChartAsyncTask extends AsyncTask<FavoriteChart, Void, Void>{
        private FavoriteChartDao favoriteChartDao;

        private InsertFavoriteChartAsyncTask(FavoriteChartDao favoriteChartDao){
            this.favoriteChartDao=favoriteChartDao;
        }

        @Override
        protected Void doInBackground(FavoriteChart... params){
            FavoriteChart favoriteChart=params[0];
            favoriteChartDao.insert(favoriteChart);

            return null;
        }
    }

    public void deleteFavoriteChart(){
        new DeleteFavoriteChartAsyncTask(favoriteChartDao).execute();
    }

    private static class DeleteFavoriteChartAsyncTask extends AsyncTask<Void, Void, Void>{
        private FavoriteChartDao favoriteChartDao;

        private DeleteFavoriteChartAsyncTask(FavoriteChartDao favoriteChartDao){
            this.favoriteChartDao=favoriteChartDao;
        }

        @Override
        protected Void doInBackground(Void... params){
            favoriteChartDao.deleteFavoriteChart();

            return null;
        }
    }
}
