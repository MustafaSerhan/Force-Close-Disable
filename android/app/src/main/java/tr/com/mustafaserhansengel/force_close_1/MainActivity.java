package tr.com.mustafaserhansengel.force_close_1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

  private static final int REQUEST_CODE = 3458;
  private DevicePolicyManager dpm;
  private ComponentName admin;

  List<String> requestPermissionsList = new ArrayList<>();
  private int fineLocationPermission;
  private int deviceAdmin;
  private int coarseLocationPermission;
  private static final int MULTIPLE_PERMISSIONS  = 99;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);


    new MethodChannel(getFlutterView(), "tr.com.ifilo.force_stop")
            .setMethodCallHandler(new MethodChannel.MethodCallHandler() {
              @SuppressLint("MissingPermission")
              @Override
              //Butona tıklandığında aktifleşen method
              public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                if (call.method.equals("servisBaslat")) {
                  hookForceStop();
                  //checkandRequestPermissions();
                  result.success("çalişti");
                }
              }
            });

  }


  public void hookForceStop(){
    try
    {
      dpm = (DevicePolicyManager)getBaseContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
      admin = new ComponentName(this, DeviceAdmin.class);

      if (!dpm.isAdminActive(admin)) {

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
        startActivityForResult(intent, 3458);
      }
      else
      {
        dpm.lockNow();
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void checkandRequestPermissions(){

    requestPermissionsList.clear();

    fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    deviceAdmin = ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_DEVICE_ADMIN);


    if(coarseLocationPermission != PackageManager.PERMISSION_GRANTED){
      requestPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    requestPermissionsList.add(Manifest.permission.BIND_DEVICE_ADMIN);

    ActivityCompat.requestPermissions(MainActivity.this,
            requestPermissionsList.toArray(new String[requestPermissionsList.size()]),
            MULTIPLE_PERMISSIONS);
    hookForceStop();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


}
