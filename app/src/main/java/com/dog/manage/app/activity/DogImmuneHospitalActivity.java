package com.dog.manage.app.activity;


import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.base.LocationBean;
import com.base.utils.CommonUtil;
import com.base.utils.LogUtil;
import com.base.utils.MapNavigationUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.DogImmuneHospitalAdapter;
import com.dog.manage.app.databinding.ActivityDogImmuneHospitalBinding;
import com.dog.manage.app.model.Hospital;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 犬只选择免疫医院
 */
public class DogImmuneHospitalActivity extends BaseActivity implements AMap.OnMarkerClickListener, AMapLocationListener {

    private ActivityDogImmuneHospitalBinding binding;
    private DogImmuneHospitalAdapter adapter;
    private int dogId;
    private int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_immune_hospital);
        addActivity(this);
        binding.thirdStepView.setSelected(true);

        dogId = getIntent().getIntExtra("dogId", 0);
        addressId = getIntent().getIntExtra("addressId", 0);

        initView();
        initMapView(savedInstanceState);
        permissionsLocation();

        getHospitalPosition();


    }

    /**
     * 根据坐标获取宠物医院信息
     */
    private void getHospitalPosition() {
        SendRequest.getHospitalPosition("39.90403,116.407525",
                new GenericsCallback<ResultClient<List<Hospital>>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<List<Hospital>> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            adapter.refreshData(response.getData());
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 1),
                Color.parseColor("#FAFAFA"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogImmuneHospitalAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                int position = (int) object;
                switch (view.getId()) {
                    case R.id.navigateView:
                        openMapNavigate(latLngs.get(position));

                        break;
                    case R.id.selectedView:
                        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                latLngs.get(position), 15, 30, 30)));
                        binding.hospitalNameView.setText(adapter.getList().get(position).getHospitalName());

                        break;
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
    }

    public void onClickConfirm(View view) {

        if (adapter.getSelect() < 0) {
            ToastUtils.showShort(getApplicationContext(), "请选择1个医院");
            return;
        }
        Hospital dataBean = adapter.getList().get(adapter.getSelect());
        if (dataBean == null) {
            ToastUtils.showShort(getApplicationContext(), "提交失败");
            return;
        }
        SendRequest.saveImmune(dogId, addressId, dataBean.getId(), dataBean.getHospitalName(),
                new GenericsCallback<ResultClient<Boolean>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<Boolean> response, int id) {
                        if (response.isSuccess() && response.getData()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", SubmitSuccessActivity.type_immune);
                            openActivity(SubmitSuccessActivity.class, bundle);

                            finishActivity(DogManageWorkflowActivity.class);
                            finishActivity(DogCertificateEditDogOwnerActivity.class);
                            finishActivity(DogCertificateEditDogActivity.class);
                            finish();
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });

    }

    //==================================     定位     ==============================================

    private void changeCamera(CameraUpdate update) {
        binding.mapView.getMap().moveCamera(update);
    }

    public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
    public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度

    private void initMapView(Bundle savedInstanceState) {
        binding.mapView.onCreate(savedInstanceState);// 此方法必须重写
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        binding.mapView.getMap().setMapType(AMap.MAP_TYPE_NORMAL);
        binding.mapView.getMap().setOnMarkerClickListener(this);
//        binding.mapView.getMap().getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        binding.mapView.getMap().getUiSettings().setZoomControlsEnabled(false);
        addMarkersToMap();
    }

    List<LatLng> latLngs = new ArrayList<>();

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        latLngs.add(BEIJING);
        latLngs.add(ZHONGGUANCUN);
        latLngs.add(new LatLng(39.993743, 116.472995));

        for (int i = 0; i < latLngs.size(); i++) {
            MarkerOptions markerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .position(latLngs.get(i))
//                    .title("北京爱牧家动物医院(西直门店)" + i)
//                    .snippet("详细信息")
                    .draggable(true);
            Marker marker = binding.mapView.getMap().addMarker(markerOption);
            marker.showInfoWindow();
        }
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLngs.get(0), 12, 30, 30)));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    private void openMapNavigate(LatLng latLng) {
        if (CommonUtil.openLocation(DogImmuneHospitalActivity.this, request_Location)) {
            List<String> hasMap = new MapNavigationUtil().hasMap(DogImmuneHospitalActivity.this);
            if (hasMap.size() > 0) {
                MapNavigationUtil.showChooseMap(DogImmuneHospitalActivity.this, new LocationBean(null, latLng.latitude, latLng.longitude));
            } else {
                ToastUtils.showLong(DogImmuneHospitalActivity.this, "未找到地图APP，请下载安装高德地图APP");
            }
        }
    }

    private final int request_Location = 100;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == request_Location) {
            permissionsLocation();
        }
    }

    //==================================     定位     ==============================================

    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private final int requestCode = 100;

    @AfterPermissionGranted(requestCode)
    private void permissionsLocation() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), permissions)) {
            initLocation();

        } else {
            EasyPermissions.requestPermissions(this, "请同意下面的权限", requestCode, permissions);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 定位
     */
    private void initLocation() {

        AMapLocationClient mlocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        // 启动定位
        mlocationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.i(TAG, "onLocationChanged: ");
        if (amapLocation != null) {
            Log.i(TAG, "onLocationChanged: getErrorCode = " + amapLocation.getErrorCode());
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String address = amapLocation.getAoiName();
                Log.i(TAG, "onLocationChanged: latitude " + latitude);
                Log.i(TAG, "onLocationChanged: longitude " + longitude);
                LatLng startLatLng = new LatLng(latitude, longitude);
                if (adapter != null)
                    adapter.setStartLatLng(startLatLng);
            } else {

            }
        }
    }

}