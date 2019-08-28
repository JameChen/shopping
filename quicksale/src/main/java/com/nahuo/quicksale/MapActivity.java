package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.NaviPara;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.nahuo.quicksale.adapter.MapAddressAdapter;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapActivity extends BaseActivity implements LocationSource, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
        AMapLocationListener, AMap.OnMapClickListener, PoiSearch.OnPoiSearchListener, TextWatcher, Inputtips.InputtipsListener, OnClickListener {

    private Context mContext = this;
    public static String resultKey = "com.nahuo.quicksale.MapActivity.resultKey";
    private SupportMapFragment mapView;
    private ListView lvAddress;
    private TextView tvSearch;
    private AutoCompleteTextView searchText;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocation amapLocation;
    private List<Map> adds = new ArrayList<Map>();
    private MapAddressAdapter mAddressAdapter = null;
    private double latitude = 0;
    private double longitude = 0;
    private String city = "";
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        isFirst = true;
        initViews();
    }

    private void initViews() {
        this.findViewById(R.id.iv_left1).setOnClickListener(this);

        lvAddress = (ListView) this.findViewById(R.id.lvAddress);
        tvSearch = (TextView) this.findViewById(R.id.tvSearch);
        tvSearch.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        initSlidingdrawer();
    }

    private void initSlidingdrawer() {
        mAddressAdapter = new MapAddressAdapter(this, adds);
        lvAddress.setAdapter(mAddressAdapter);
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AuthInfoModel.AIModel aiData = (AuthInfoModel.AIModel) mAddressAdapter.getItem(position);
                Intent data = new Intent();
                data.putExtra(resultKey, aiData);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));// 地图缩放比例越大越清晰
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(getResources().getColor(R.color.map_alpha));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(Const.Alpha, Const.Red,
                Const.Green, Const.Blue));// 设置圆形的填充颜色
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.curr_position));// 设置小蓝点的图标
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        this.amapLocation = amapLocation;
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                if (isFirst) {
                    isFirst = false;
                    LatLng ll = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    onMapClick(ll);
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    /**
     * 激活定位
     */
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

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            //            mapView.onDestroy();
            mapView.onDestroyView();
        }
        deactivate();
        System.gc();
    }


    private void addMarker(LatLng mLatLng) {
        aMap.clear();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.curr_position));// 设置小蓝点的图标
        aMap.setMyLocationStyle(myLocationStyle);
        mListener.onLocationChanged(amapLocation);
        LatLngBounds bounds = new LatLngBounds.Builder().include(mLatLng)
                .build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(mLatLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.map_go_position)));
        searchText.setText(amapLocation.getAddress());
        getAddress(amapLocation);
    }

    private void getAddress(AMapLocation amapLocation) {
       // Geocoder mGeocoder = new Geocoder(this);
//        try {
//            List<Address> listAddress = mGeocoder.getFromLocation(latitude, longitude, 5);
//            if (listAddress != null && listAddress.size() > 0) {
//                Address mAddress = listAddress.get(0);
//                 city = mAddress.getLocality();
//                keyWord = amapLocation.getAddress();
//                doSearchQuery(city);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        city= amapLocation.getCity();
        keyWord = amapLocation.getAddress();
        doSearchQuery(city);
    }

    @Override
    public void onMapClick(LatLng mLatLng) {
        latitude = mLatLng.latitude;
        longitude = mLatLng.longitude;

//        Location lo = new Location();
//        lo.setLatitude(latitude);
//        lo.setLongitude(longitude);
//        this.amapLocation = new AMapLocation();

        addMarker(mLatLng);
    }

    /**
     * 搜索后返回
     *
     * @param result
     * @param rCode
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    List<AuthInfoModel.AIModel> addsList = new ArrayList();
                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiItem p = poiItems.get(i);
                            AuthInfoModel.AIModel m = (new AuthInfoModel()).new AIModel();
                            m.setAddress(p.getTitle());
                            m.setArea(p.getAdName());
                            m.setCity(p.getCityName());
                            m.setProvince(p.getProvinceName());
                            m.setStreet(p.getSnippet());// 街道
                            m.setBusinessArea(p.getBusinessArea());
                            m.setDimension(p.getLatLonPoint().getLatitude() + "");
                            m.setLongitude(p.getLatLonPoint().getLongitude() + "");
                            if (m != null) {
                                addsList.add(m);
                            }
                        }
                        mAddressAdapter.setData(addsList);
                        mAddressAdapter.notifyDataSetChanged();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(MapActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(MapActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MapActivity.this, rCode + "", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(MapActivity.this, infomation, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MapActivity.this, rCode + "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (newText!=null && newText.length()>0) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "");
            Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        keyWord = searchText.getText().toString();
        if ("".equals(keyWord)) {
            Toast.makeText(MapActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            return;
        } else {
            doSearchQuery(city);
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String city) {
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1: {// 返回
                finish();
            }
            break;
            case R.id.tvSearch: {// 搜索
                searchButton();
            }
            break;
        }
    }

    /**
     * 点击泡泡
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        LinearLayout llPanel1 = (LinearLayout) view.findViewById(R.id.llPanel1);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        llPanel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps2d.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }

    }
}
