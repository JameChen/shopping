/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nahuo.quicksale.hyphenate.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;


public class EaseBaiduMapActivity extends EaseBaseAppCompatActivity implements LocationSource,AMap.OnMarkerClickListener,
        AMapLocationListener, AMap.OnMapClickListener {

    private final static String TAG = AMapLocation.class.getSimpleName();

    Button sendButton = null;

    EditText indexText = null;
    int index = 0;
    // LocationData locData = null;
    public static EaseBaiduMapActivity instance = null;
    ProgressDialog progressDialog;
    static MapView mMapView = null;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private AMapLocation location;
    private double latitude = 0;
    private double longitude = 0;
    private String address = "";
     private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient;
    private boolean isLocation = true;
    private UiSettings mUiSettings;
    private CameraUpdate mUpdata;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.pinhuo_ease_activity_baidumap);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onCreate(savedInstanceState);
        sendButton = (Button) findViewById(R.id.btn_location_send);
        sendButton.setVisibility(View.GONE);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        address = intent.getStringExtra("address");
        if (longitude > 0 || latitude > 0 || !TextUtils.isEmpty(address)) {
            isLocation = false;
        } else {
            isLocation = true;
        }
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        if (!isLocation) {
            mUpdata = CameraUpdateFactory.newCameraPosition(
                    //15是缩放比例，0是倾斜度，30显示比例
                    new CameraPosition(new LatLng(latitude , longitude), 15, 0, 30));
            //这是地理位置，就是经纬度。
            aMap.moveCamera(mUpdata); //定位的方法
            drawMarkers();
        }
}



    public void drawMarkers() {

        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title("该位置")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        if (isLocation) {
            mUiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);
        }else {
            mUiSettings.setMyLocationButtonEnabled(false);
        }
        setupLocationStyle();
    }

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.curr_position));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
            location = aMapLocation;
            if (location != null) {
                sendButton.setVisibility(View.VISIBLE);
                sendButton.setEnabled(true);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
                Bundle bundle = location.getExtras();
                if (bundle != null) {
                    int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                    String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                    // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                    int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                /*
                errorCode
                errorInfo
                locationType
                */
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    onMapClick(ll);
                    Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
                } else {
                    Log.e("amap", "定位信息， bundle is null ");

                }

            } else {
                Log.e("amap", "定位失败");
            }
    }

    @Override
    public void onMapClick(LatLng mLatLng) {
//        latitude = mLatLng.latitude;
//        longitude = mLatLng.longitude;
        addMarker(mLatLng);
    }

    private void addMarker(LatLng mLatLng) {
            aMap.clear();
            mListener.onLocationChanged(location);
            LatLngBounds bounds = new LatLngBounds.Builder().include(mLatLng)
                    .build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
            aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(mLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.curr_position)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(getApplicationContext());
                mLocationOption = new AMapLocationClientOption();
                //设置定位监听
                mlocationClient.setLocationListener(this);
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    public void back(View v) {
        finish();
    }

    public void sendLocation(View view) {
        if (this.location != null) {
            Intent intent = this.getIntent();
            intent.putExtra("latitude", this.location.getLatitude());
            intent.putExtra("longitude", this.location.getLongitude());
            intent.putExtra("address", this.location.getAddress());
            this.setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(com.hyphenate.easeui.R.anim.slide_in_from_left, com.hyphenate.easeui.R.anim.slide_out_to_right);
        }else {
            ViewHub.showLongToast(this,"数据为空");
        }
    }

}
