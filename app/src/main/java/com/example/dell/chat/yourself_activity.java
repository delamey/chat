package com.example.dell.chat;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class yourself_activity extends AppCompatActivity {
    private TextView positionText;
    public LocationClient mlocationClient;
    private MapView mapView;
    private Toolbar toolbar;
    private BaiduMap baiduMap;
    private boolean isFirstLocate=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map);
        //positionText = (TextView) findViewById(R.id.position);
        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MyLocationListener());
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人定位");

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mapView= (MapView) findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(yourself_activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
            if (ContextCompat.checkSelfPermission(yourself_activity.this, android.Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
            }
        if (ContextCompat.checkSelfPermission(yourself_activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] pemissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(yourself_activity.this,pemissions,1);
        }else {
            requestLocation();
        }
    }
   private  void navigateTo(BDLocation location){
       if(isFirstLocate){
           LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
           MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
           baiduMap.animateMapStatus(update);
           update=MapStatusUpdateFactory.zoomTo(17f);
           baiduMap.animateMapStatus(update);
           isFirstLocate=false;
       }
       MyLocationData.Builder builder=new MyLocationData.Builder();
       builder.latitude(location.getLatitude());
       builder.longitude(location.getLongitude());
       MyLocationData myLocationData=builder.build();
       baiduMap.setMyLocationData(myLocationData);
   }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for(int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    private void requestLocation() {
        initLocation();
        mlocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    private void initLocation() {
        LocationClientOption  option=new LocationClientOption();
        option.setScanSpan(3000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mlocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation.getLocType()==BDLocation.TypeGpsLocation||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                    navigateTo(bdLocation);
                }
//                StringBuilder currenPosition=new StringBuilder();
//                currenPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
//                currenPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
//                currenPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
//                currenPosition.append("省：").append(bdLocation.getProvince()).append("\n");
//                currenPosition.append("市：").append(bdLocation.getCity()).append("\n");
//                currenPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
//                currenPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
//                currenPosition.append("定位方式：");
//               // currenPosition.append(bdLocation.getLocType()+"\n");
//                //currenPosition.append(BDLocation.TypeNetWorkLocation+"\n");
//               // currenPosition.append(BDLocation.TypeGpsLocation+"\n");
//                if (bdLocation.getLocType()== BDLocation.TypeGpsLocation){
//                    Log.i("GPS","gps");
//                    currenPosition.append("GPS");
//                }else if (bdLocation.getLocType()== BDLocation.TypeNetWorkLocation){
//                    currenPosition.append("网络");
//                    Log.i("wangluo","internet");
//                }
//                positionText.setText(currenPosition);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        }
    }

