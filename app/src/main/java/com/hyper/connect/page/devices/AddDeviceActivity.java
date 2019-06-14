package com.hyper.connect.page.devices;

import static com.hyper.connect.MainActivity.PERMISSION_REQUEST_CODE_CAMERA;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.enums.DeviceState;
import com.hyper.connect.elastos.ElastosCarrier;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class AddDeviceActivity extends AppCompatActivity{
    private AppCompatButton backButton;
    private TextInputEditText deviceNameInput;
    private TextInputEditText deviceAddressInput;
    private AppCompatButton finishButton;
    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        GlobalApplication.getElastosCarrier().removeFriend("BvDjoMK2XMrNHydTuaGpaMGTYRU4QyVh5sFuivYyVyrc");

        barcodeView=findViewById(R.id.barcodeView);
        openCamera();

        backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        deviceNameInput=findViewById(R.id.deviceNameInput);
        deviceAddressInput=findViewById(R.id.deviceAddressInput);

        finishButton=findViewById(R.id.finishButton);
        finishButton.setOnClickListener(view -> {
            String deviceName=deviceNameInput.getText().toString();
            String deviceAddress=deviceAddressInput.getText().toString();
            if(deviceName.isEmpty()){
                Snackbar.make(finishButton, R.string.snack_device_name_empty, Snackbar.LENGTH_SHORT).show();
            }
            else if(deviceAddress.isEmpty()){
                Snackbar.make(finishButton, R.string.snack_device_address_empty, Snackbar.LENGTH_SHORT).show();
            }
            else{
                addDevice(deviceAddress);
            }
        });
    }

    private void openCamera(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        }
        else{
            startCamera();
        }
    }

    private void requestCameraPermission(){
        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case PERMISSION_REQUEST_CODE_CAMERA:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    startCamera();
                }
                else{
                    //permission denied, do nothing
                }
                return;
            }
        }
    }

    private void startCamera(){
        BarcodeCallback callback=new BarcodeCallback(){
            @Override
            public void barcodeResult(BarcodeResult result){
                String deviceAddress=result.getText();
                addDevice(deviceAddress);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints){}
        };

        Collection<BarcodeFormat> formats=Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(this.getIntent());
        barcodeView.decodeContinuous(callback);
        barcodeView.resume();
    }

    public void stopCamera(){
        barcodeView.pause();
    }

    private void addDevice(String deviceAddress){
        ElastosCarrier elastosCarrier=GlobalApplication.getElastosCarrier();
        String deviceName=deviceNameInput.getText().toString();
        boolean addressCheck=elastosCarrier.isValidAddress(deviceAddress);
        if(addressCheck){
            Device device=new Device(0, "undefined", deviceAddress, deviceName, DeviceState.PENDING, DeviceConnectionState.OFFLINE);
            boolean addCheck=elastosCarrier.addFriend(device);
            if(addCheck){
                stopCamera();
                setResult(RESULT_OK);
                finish();
            }
            else{
                Snackbar.make(barcodeView, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
            }
        }
        else{
            Snackbar.make(barcodeView, R.string.snack_device_address_not_valid, Snackbar.LENGTH_SHORT).show();
        }
    }
}
