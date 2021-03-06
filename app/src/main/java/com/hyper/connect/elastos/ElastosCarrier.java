package com.hyper.connect.elastos;


import org.apache.commons.io.FilenameUtils;
import org.elastos.carrier.AbstractCarrierHandler;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.exceptions.CarrierException;
import org.elastos.carrier.filetransfer.FileTransfer;
import org.elastos.carrier.filetransfer.FileTransferHandler;
import org.elastos.carrier.filetransfer.FileTransferInfo;
import org.elastos.carrier.filetransfer.FileTransferState;
import org.elastos.carrier.filetransfer.Manager;
import org.elastos.carrier.filetransfer.ManagerHandler;

import android.content.Context;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.database.entity.Notification;
import com.hyper.connect.database.entity.enums.AttributeDirection;
import com.hyper.connect.database.entity.enums.AttributeScriptState;
import com.hyper.connect.database.entity.enums.AttributeState;
import com.hyper.connect.database.entity.enums.AttributeType;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.enums.DeviceState;
import com.hyper.connect.database.entity.Event;
import com.hyper.connect.database.entity.enums.EventAverage;
import com.hyper.connect.database.entity.enums.EventCondition;
import com.hyper.connect.database.entity.enums.EventState;
import com.hyper.connect.database.entity.enums.EventType;
import com.hyper.connect.database.entity.Sensor;
import com.hyper.connect.database.entity.enums.NotificationCategory;
import com.hyper.connect.database.entity.enums.NotificationType;
import com.hyper.connect.elastos.common.Synchronizer;
import com.hyper.connect.elastos.common.TestOptions;
import com.hyper.connect.page.history.HistoryActivity;
import com.hyper.connect.util.CustomUtil;

import java.lang.Thread;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ElastosCarrier extends Thread{
	private final static String CONTROLLER_CONNECTION_KEYWORD="hyperconnect_controller";
	private final static String DEVICE_CONNECTION_KEYWORD="hyperconnect_device";
	private static Synchronizer syncher=new Synchronizer();
	private static TestOptions options;
	private static CarrierHandler carrierHandler;
	private static Carrier carrier;
	private boolean isConnected=false;
	private static LocalRepository localRepository;
	private HistoryActivity historyActivity;

	private static Manager fileTransferManager;
	private static File historyDir=null;
	private static FileTransfer currentFileTransfer=null;
	private static FileTransferState fileTransferState=null;
	private static String currentFileTransferUserId="";
	private static int transferCount=0;

	public void setHistoryActivity(HistoryActivity historyActivity){
		this.historyActivity=historyActivity;
	}

	public ElastosCarrier(Context context){
		String appPath=getAppPath(context);
		localRepository=new LocalRepository(context);
		options=new TestOptions(appPath);
		carrierHandler=new CarrierHandler();
		historyDir=new File(appPath, "history");
		if(!historyDir.exists()){
			historyDir.mkdir();
		}
	}

	public LocalRepository getLocalRepository(){
		return localRepository;
	}

	public void run(){
		try{
			Carrier.initializeInstance(options, carrierHandler);
			carrier=Carrier.getInstance();
			carrier.start(1000);

			FileTransferManagerHandler fileTransferManagerHandler=new FileTransferManagerHandler();
			Manager.initializeInstance(carrier, fileTransferManagerHandler);
			fileTransferManager=Manager.getInstance();

			synchronized(carrier){
				carrier.wait();
			}
		}
		catch(CarrierException | InterruptedException e){}
	}

	public void kill(){
		carrier.kill();
	}

	public boolean isConnected(){
		return isConnected;
	}

	public String getAddress(){
		String address="undefined";
		try{
			address=carrier.getAddress();
		}
		catch(CarrierException ce){
			address="undefined";
		}
		syncher.wakeup();
		return address;
	}

	public String getUserId(){
		String userId="undefined";
		try{
			userId=carrier.getUserId();
		}
		catch(CarrierException ce){
			userId="undefined";
		}
		syncher.wakeup();
		return userId;
	}

	public boolean isValidAddress(String address){
		return Carrier.isValidAddress(address);
	}

	public boolean isValidUserId(String userId){
		return Carrier.isValidId(userId);
	}

	public boolean addFriend(Device device){
		boolean response=true;
		try{
			localRepository.setTempDevice(device);
			carrier.addFriend(device.getAddress(), CONTROLLER_CONNECTION_KEYWORD);
		}
		catch(CarrierException ce){
			response=false;
		}
		syncher.wakeup();
		return response;
	}

	public boolean removeFriend(String userId){
		boolean response=true;
		try{
			carrier.removeFriend(userId);
		}
		catch(CarrierException ce){
			response=false;
		}
		syncher.wakeup();
		return response;
	}

	public boolean sendFriendMessage(String userId, String message){
		boolean response=true;
		try{
			carrier.sendFriendMessage(userId, message);
		}
		catch(CarrierException ce){
			response=false;
		}
		syncher.wakeup();
		return response;
	}

	private class CarrierHandler extends AbstractCarrierHandler{
		@Override
		public void onReady(Carrier carrier){
			synchronized(carrier){
				carrier.notify();
			}
		}

		@Override
		public void onConnection(Carrier carrier, ConnectionStatus status){
			System.out.println("CarrierHandler - onConnection - "+status);
			if(status==ConnectionStatus.Connected){
				isConnected=true;
				String address=getAddress();
				String userId=getUserId();
				System.out.println("address: "+address);
				System.out.println("userId: "+userId);
				localRepository.setCarrierConnectionState(true);
			}
			else if(status==ConnectionStatus.Disconnected){
				isConnected=false;
				localRepository.setCarrierConnectionState(false);
			}
			syncher.wakeup();
		}

		@Override
		public void onFriends(Carrier carrier, List<FriendInfo> friends){
			System.out.println("CarrierHandler - onFriends - "+friends);
			syncher.wakeup();
		}

		@Override
		public void onFriendConnection(Carrier carrier, String friendId, ConnectionStatus status){
			System.out.println("CarrierHandler - onFriendConnection - "+friendId+" - "+status);
			Device device=localRepository.getDeviceByUserId(friendId);
			if(device!=null){
				if(status==ConnectionStatus.Connected){
					device.setConnectionState(DeviceConnectionState.ONLINE);
					if(device.getState()==DeviceState.PENDING){
						device.setState(DeviceState.ACTIVE);
						List<Device> deviceList=localRepository.getDeviceList();
						for(Device savedDevice : deviceList){
							if(!savedDevice.getUserId().equals(friendId)){
								JsonObject jsonObject=new JsonObject();
								jsonObject.addProperty("command", "addDevice");
								jsonObject.addProperty("address", savedDevice.getAddress());
								jsonObject.addProperty("userId", savedDevice.getUserId());
								String jsonString=jsonObject.toString();
								sendFriendMessage(friendId, jsonString);
							}
						}
					}

					if(device.getState()==DeviceState.ACTIVE){
						JsonObject jsonObject=new JsonObject();
						jsonObject.addProperty("command", "getData");
						String jsonString=jsonObject.toString();
						sendFriendMessage(friendId, jsonString);
					}
				}
				else if(status==ConnectionStatus.Disconnected){
					device.setConnectionState(DeviceConnectionState.OFFLINE);
				}
				localRepository.updateDevice(device);
			}
			syncher.wakeup();
		}

		@Override
		public void onFriendAdded(Carrier carrier, FriendInfo info){
			System.out.println("CarrierHandler - onFriendAdded - "+info);
			Device device=localRepository.getDeviceByUserId(info.getUserId());
			if(device==null){
				Device newDevice=localRepository.getTempDevice();
				newDevice.setUserId(info.getUserId());
				localRepository.insertDevice(newDevice);
			}
			syncher.wakeup();
		}

		@Override
		public void onFriendRemoved(Carrier carrier, String friendId){
			System.out.println("CarrierHandler - onFriendRemoved - "+friendId);
			syncher.wakeup();
		}

		@Override
		public void onFriendMessage(Carrier carrier, String from, byte[] message, boolean isOffline){
			String messageText=new String(message);
			System.out.println("CarrierHandler - onFriendMessage - "+from+" - "+messageText);
			Device device=localRepository.getDeviceByUserId(from);
			Gson gson=new Gson();
			JsonObject resultObject=gson.fromJson(messageText, JsonObject.class);
			String command=resultObject.get("command").getAsString();
			if(command.equals("addSensor")){
				JsonObject sensorObject=resultObject.getAsJsonObject("sensor");
				int edgeSensorId=sensorObject.get("id").getAsInt();
				String name=sensorObject.get("name").getAsString();
				String type=sensorObject.get("type").getAsString();

				Sensor sensor=localRepository.getSensorByEdgeSensorIdAndDeviceId(edgeSensorId, device.getId());
				if(sensor==null){
					Sensor newSensor=new Sensor(0, edgeSensorId, name, type, device.getId());
					localRepository.insertSensor(newSensor);
				}
			}
			else if(command.equals("deleteSensor")){
				int edgeSensorId=resultObject.get("id").getAsInt();
				Sensor sensor=localRepository.getSensorByEdgeSensorIdAndDeviceId(edgeSensorId, device.getId());
				if(sensor!=null){
					localRepository.deleteSensor(sensor);
				}
			}
			else if(command.equals("addAttribute")){
				JsonObject attributeObject=resultObject.getAsJsonObject("attribute");
				int edgeAttributeId=attributeObject.get("id").getAsInt();
				int edgeSensorId=attributeObject.get("sensorId").getAsInt();
				String name=attributeObject.get("name").getAsString();
				AttributeDirection direction=AttributeDirection.valueOf(attributeObject.get("direction").getAsInt());
				AttributeType type=AttributeType.valueOf(attributeObject.get("type").getAsInt());
				int interval=attributeObject.get("interval").getAsInt();
				AttributeScriptState scriptState=AttributeScriptState.valueOf(attributeObject.get("scriptState").getAsInt());
				AttributeState state=AttributeState.valueOf(attributeObject.get("state").getAsInt());

				Attribute attribute=localRepository.getAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, device.getId());
				if(attribute==null){
					Attribute newAttribute=new Attribute(0, edgeAttributeId, edgeSensorId, name, direction, type, interval, scriptState, state, device.getId());
					localRepository.insertAttribute(newAttribute);
				}
				else{
					attribute.setState(state);
					attribute.setScriptState(scriptState);
					localRepository.updateAttribute(attribute);
				}
			}
			else if(command.equals("deleteAttribute")){
				int edgeAttributeId=resultObject.get("id").getAsInt();
				Attribute attribute=localRepository.getAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, device.getId());
				if(attribute!=null){
					localRepository.deleteAttribute(attribute);
				}
			}
			else if(command.equals("addEvent")){
				JsonObject eventObject=resultObject.getAsJsonObject("event");
				String globalEventId=eventObject.get("globalEventId").getAsString();
				String name=eventObject.get("name").getAsString();
				EventType type=EventType.valueOf(eventObject.get("type").getAsInt());
				EventState state=EventState.valueOf(eventObject.get("state").getAsInt());
				EventAverage average=EventAverage.valueOf(eventObject.get("average").getAsInt());
				EventCondition condition=EventCondition.valueOf(eventObject.get("condition").getAsInt());
				String conditionValue=eventObject.get("conditionValue").getAsString();
				String triggerValue=eventObject.get("triggerValue").getAsString();
				String sourceDeviceUserId=eventObject.get("sourceDeviceUserId").getAsString();
				int sourceEdgeSensorId=eventObject.get("sourceEdgeSensorId").getAsInt();
				int sourceEdgeAttributeId=eventObject.get("sourceEdgeAttributeId").getAsInt();
				String actionDeviceUserId=eventObject.get("actionDeviceUserId").getAsString();
				int actionEdgeSensorId=eventObject.get("actionEdgeSensorId").getAsInt();
				int actionEdgeAttributeId=eventObject.get("actionEdgeAttributeId").getAsInt();

				Event event=localRepository.getEventByGlobalEventId(globalEventId);
				if(event==null){
					int sourceDeviceId=device.getId();
					int actionDeviceId=localRepository.getDeviceByUserId(actionDeviceUserId).getId();
					Event newEvent=new Event(0, globalEventId, name, type, state, average, condition, conditionValue, triggerValue, sourceDeviceUserId, sourceDeviceId, sourceEdgeSensorId, sourceEdgeAttributeId, actionDeviceUserId, actionDeviceId, actionEdgeSensorId, actionEdgeAttributeId);
					localRepository.insertEvent(newEvent);
				}
				else{
					event.setState(state);
					localRepository.updateEvent(event);
				}
			}
			else if(command.equals("deleteEvent")){
				String globalEventId=resultObject.get("globalEventId").getAsString();
				Event event=localRepository.getEventByGlobalEventId(globalEventId);
				if(event!=null){
					localRepository.deleteEvent(event);
				}
			}
			else if(command.equals("addNotification")){
				JsonObject notificationObject=resultObject.getAsJsonObject("notification");

				int edgeNotificationId=notificationObject.get("id").getAsInt();
				NotificationType type=NotificationType.valueOf(notificationObject.get("type").getAsInt());
				NotificationCategory category=NotificationCategory.valueOf(notificationObject.get("category").getAsInt());
				String edgeThingId=notificationObject.get("edgeThingId").getAsString();
				String notificationMessage=notificationObject.get("message").getAsString();
				String dateTime=notificationObject.get("dateTime").getAsString();
				Notification notification=new Notification(0, device.getUserId(), edgeNotificationId, type, category, edgeThingId, notificationMessage, dateTime);
				localRepository.insertNotification(notification);
			}
			else if(command.equals("changeAttributeState")){
				int edgeAttributeId=resultObject.get("id").getAsInt();
				boolean state=resultObject.get("state").getAsBoolean();
				Attribute attribute=localRepository.getAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, device.getId());
				if(attribute!=null){
					if(state){
						attribute.setState(AttributeState.ACTIVE);
					}
					else{
						attribute.setState(AttributeState.DEACTIVATED);
					}
					localRepository.updateAttribute(attribute);
				}
			}
			else if(command.equals("changeAttributeScriptState")){
				int edgeAttributeId=resultObject.get("id").getAsInt();
				boolean scriptState=resultObject.get("scriptState").getAsBoolean();
				Attribute attribute=localRepository.getAttributeByEdgeAttributeIdAndDeviceId(edgeAttributeId, device.getId());
				if(attribute!=null){
					if(scriptState){
						attribute.setScriptState(AttributeScriptState.VALID);
					}
					else{
						attribute.setScriptState(AttributeScriptState.INVALID);
					}
					localRepository.updateAttribute(attribute);
				}
			}
			else if(command.equals("changeEventState")){
				String globalEventId=resultObject.get("globalEventId").getAsString();
				boolean state=resultObject.get("state").getAsBoolean();
				Event event=localRepository.getEventByGlobalEventId(globalEventId);
				if(event!=null){
					if(state){
						event.setState(EventState.ACTIVE);
					}
					else{
						event.setState(EventState.DEACTIVATED);
					}
					localRepository.updateEvent(event);
				}
			}
			else if(command.equals("dataValue")){
				JsonObject dataRecordObject=resultObject.getAsJsonObject("dataRecord");

				int edgeAttributeId=dataRecordObject.get("attributeId").getAsInt();
				String dateTime=dataRecordObject.get("dateTime").getAsString();
				String value=dataRecordObject.get("value").getAsString();
				DataRecord dataRecord=localRepository.getDataRecordByDeviceUserIdAndEdgeAttributeId(device.getUserId(), edgeAttributeId);
				if(dataRecord==null){
					dataRecord=new DataRecord(0, device.getUserId(), edgeAttributeId, dateTime, value);
					localRepository.insertDataRecord(dataRecord);
				}
				else{
					dataRecord.setDateTime(dateTime);
					dataRecord.setValue(value);
					localRepository.updateDataRecord(dataRecord);
				}
			}
			else if(command.equals("historyValue")){
				JsonArray dataList=resultObject.getAsJsonArray("dataRecordList");
				ArrayList<DataRecord> dataRecordList=new ArrayList<DataRecord>();
				for(JsonElement jsonElement : dataList){
					JsonObject jsonObject=jsonElement.getAsJsonObject();
					String dateTime=jsonObject.get("dateTime").getAsString();
					String value=jsonObject.get("value").getAsString();
					DataRecord dataRecord=new DataRecord(dateTime, value);
					dataRecordList.add(dataRecord);
				}

				historyActivity.setLiveDataRecordList(dataRecordList);
			}
			else if(command.equals("changeControllerState")){
				boolean state=resultObject.get("state").getAsBoolean();
				if(state){
					device.setState(DeviceState.ACTIVE);
					JsonObject jsonObject=new JsonObject();
					jsonObject.addProperty("command", "getData");
					sendFriendMessage(from, jsonObject.toString());
				}
				else{
					device.setState(DeviceState.DEACTIVATED);
					GlobalApplication.getAttributeManagement().stopAllAttributes();
				}
				localRepository.updateDevice(device);
			}

			syncher.wakeup();
		}
	}

	private class FileTransferManagerHandler implements ManagerHandler{
		@Override
		public void onConnectRequest(Carrier carrier, String from, FileTransferInfo info){
			System.out.println("FileTransferManagerHandler - onConnectRequest - from: "+from);
			try{
				if(!(from.equals(currentFileTransferUserId) && transferCount==0)){
					currentFileTransferUserId=from;
					currentFileTransfer=fileTransferManager.newFileTransfer(from, info, new TransferHandler(from));
					currentFileTransfer.acceptConnect();
				}
			}
			catch(CarrierException e){}
		}
	}

	public void closeFileTransfer(){
		fileTransferState=FileTransferState.Closed;
	}

	private class TransferHandler implements FileTransferHandler{
		private String userId="";
		private String localFilename="";
		private String filePath="";
		private long receiveDataLen=0;
		private long receiveFileSize=0;
		private int tempPercent=-1;

		private TransferHandler(String userId){
			this.userId=userId;
		}

		@Override
		public void onStateChanged(FileTransfer filetransfer, FileTransferState state){
			System.out.println("TransferHandler - onStateChanged - "+state);
			if(state==FileTransferState.Failed){
				JsonObject jsonObject=new JsonObject();
				jsonObject.addProperty("command", "cleanFileTransfer");
				jsonObject.addProperty("state", "Closed");
				sendFriendMessage(userId, jsonObject.toString());
				historyActivity.showError();
			}
		}

		@Override
		public void onFileRequest(FileTransfer filetransfer, String fileId, String filename, long size){
			System.out.println("TransferHandler - onFileRequest - "+fileId+" - "+filename);
			receiveDataLen=0;
			receiveFileSize=size;

			localFilename=FilenameUtils.getName(filename);

			Device device=localRepository.getDeviceByUserId(userId);
			File deviceDir=new File(historyDir, Integer.toString(device.getId()));
			if(!deviceDir.exists()){
			    deviceDir.mkdir();
            }
			File file=new File(deviceDir, localFilename);
			filePath=file.getAbsolutePath();

			try{
				filetransfer.pullData(fileId, 0);
			}
			catch(Exception e){}
		}

		@Override
		public void onPullRequest(FileTransfer filetransfer, String fileId, long offset){
			System.out.println("TransferHandler - onPullRequest - "+fileId);
		}

		@Override
		public boolean onData(FileTransfer filetransfer, String fileId, byte[] data){
			receiveDataLen+=data.length;
			int percent=(int)(receiveDataLen*100/receiveFileSize);

			if(tempPercent!=percent){
				//TODO: update file percent
				System.out.println("onData "+percent+"%");
			}
			tempPercent=percent;

			CustomUtil.byte2File(data, filePath);
			if(receiveFileSize==receiveDataLen){
				//TODO: finished
				System.out.println("Receiving finished");
				historyActivity.showData();

				transferCount++;
				if(transferCount==5){
					currentFileTransferUserId="";
					transferCount=0;
					JsonObject jsonObject=new JsonObject();
					jsonObject.addProperty("command", "cleanFileTransfer");
					jsonObject.addProperty("state", "Closed");
					sendFriendMessage(userId, jsonObject.toString());
				}
			}

			return true;
		}

		@Override
		public void onDataFinished(FileTransfer filetransfer, String fileId){
			System.out.println("TransferHandler - onDataFinished - "+fileId);
		}

		@Override
		public void onPending(FileTransfer filetransfer, String fileId){
			System.out.println("TransferHandler - onPending - "+fileId);
		}

		@Override
		public void onResume(FileTransfer filetransfer, String fileId){
			System.out.println("TransferHandler - onResume - "+fileId);
		}

		@Override
		public void onCancel(FileTransfer filetransfer, String fileId, int status, String reason){
			System.out.println("TransferHandler - onCancel - "+fileId);
		}
	}

	private String getAppPath(Context context){
        File file=context.getFilesDir();
        return file.getAbsolutePath();
    }

}