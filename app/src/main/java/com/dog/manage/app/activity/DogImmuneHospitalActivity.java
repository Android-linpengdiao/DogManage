package com.dog.manage.app.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 犬只选择免疫医院
 */
public class DogImmuneHospitalActivity extends BaseActivity implements AMap.OnMarkerClickListener {

    private ActivityDogImmuneHospitalBinding binding;
    private DogImmuneHospitalAdapter adapter;
    private Map<String, String> paramsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_immune_hospital);
        addActivity(this);
        binding.thirdStepView.setSelected(true);

        String paramsJson = getIntent().getStringExtra("paramsJson");
        if (!TextUtils.isEmpty(paramsJson)) {
            Gson gson = new Gson();
            paramsMap = gson.fromJson(paramsJson, new TypeToken<Map<String, Object>>() {
            }.getType());
        }

        initView();
        initMapView(savedInstanceState);


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
        adapter.refreshData(Arrays.asList("", "", ""));
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

        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_immune);
        openActivity(SubmitSuccessActivity.class, bundle);

        finishActivity(DogManageWorkflowActivity.class);
        finishActivity(DogCertificateEditDogOwnerActivity.class);
        finishActivity(DogCertificateEditDogActivity.class);
        finish();

        if (LogUtil.isDebug){
            return;
        }
        SendRequest.DogImmuneHospital(getUserInfo().getAuthorization(), paramsMap, new GenericsCallback(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });

    }

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
        List<String> hasMap = new MapNavigationUtil().hasMap(DogImmuneHospitalActivity.this);
        if (hasMap.size() > 0) {
            MapNavigationUtil.showChooseMap(DogImmuneHospitalActivity.this, new LocationBean(null, latLng.latitude, latLng.longitude));
        } else {
            ToastUtils.showLong(DogImmuneHospitalActivity.this, "未找到地图APP，请下载安装高德地图APP");
        }
    }

}