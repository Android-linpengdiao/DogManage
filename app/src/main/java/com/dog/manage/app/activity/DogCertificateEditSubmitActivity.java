package com.dog.manage.app.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.base.manager.LoadingManager;
import com.base.utils.GsonUtils;
import com.base.utils.LogUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditSubmitBinding;
import com.dog.manage.app.model.HandleInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DogCertificateEditSubmitActivity extends BaseActivity implements AMapLocationListener {

    private ActivityDogCertificateEditSubmitBinding binding;
    private Map<String, String> paramsMap = new HashMap<>();
    private int dogId;
    private int addressId;
    private String dogType = null;
    private String immunePhoto = null;
    private boolean isAdopt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_submit);
        addActivity(this);

        isAdopt = getIntent().getBooleanExtra("isAdopt", false);
        dogId = getIntent().getIntExtra("dogId", 0);
        addressId = getIntent().getIntExtra("addressId", 0);
        dogType = getIntent().getStringExtra("dogType");
        immunePhoto = getIntent().getStringExtra("immunePhoto");
        binding.dogTypeView.binding.itemContent.setText(dogType + "");
//        String paramsJson = getIntent().getStringExtra("paramsJson");
//        if (!TextUtils.isEmpty(paramsJson)) {
//            Gson gson = new Gson();
//            paramsMap = gson.fromJson(paramsJson, new TypeToken<Map<String, Object>>() {
//            }.getType());
//        }

        setTypeface(binding.acceptUnitsHintView);
        binding.thirdStepView.setSelected(true);

        getHandleInfo();
//        permissionsManager();

    }

    private HandleInfo handleInfo;

    private void getHandleInfo() {
        /**
         * dogId
         * integer
         * 犬只id
         * addressId
         * integer
         * 地址id
         */
        Map<String, String> map = new HashMap<>();
        map.put("dogId", String.valueOf(dogId));
        map.put("addressId", String.valueOf(addressId));
        SendRequest.getHandleInfo(map, new GenericsCallback<ResultClient<HandleInfo>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<HandleInfo> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    handleInfo = response.getData();
                    binding.handleUnitAddressView.setText(response.getData().getSuperiorUnitName() + response.getData().getHandleUnitName());
                    binding.costValueView.binding.itemContent.setText("￥" + response.getData().getCostValue());
                    binding.costValueView.setVisibility(isAdopt ? View.GONE : View.VISIBLE);
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    public void onClickConfirm(View view) {
        if (handleInfo == null) {
            ToastUtils.showShort(getApplicationContext(), "提交失败");
            return;
        }
        /**
         * addressId
         * integer
         * 住址id
         * dogId
         * integer
         * 犬只id
         * acceptUnit
         * string
         * 受理单位
         * unitId
         * integer
         * 受理单位id
         * immunePhoto
         * string
         * 犬只犬证照片 ，正面照
         */

        paramsMap.put("addressId", String.valueOf(addressId));
        paramsMap.put("dogId", String.valueOf(dogId));
        paramsMap.put("acceptUnit", handleInfo.getSuperiorUnitName() + handleInfo.getHandleUnitName());
        paramsMap.put("unitId", String.valueOf(handleInfo.getHandleUnitId()));
        paramsMap.put("immunePhoto", immunePhoto);
        SendRequest.approveDogLicence(paramsMap, new GenericsCallback<ResultClient<Boolean>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogCertificateEditSubmitActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogCertificateEditSubmitActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<Boolean> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", SubmitSuccessActivity.type_certificate);
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

    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private final int requestCode = 100;

    @AfterPermissionGranted(requestCode)
    private void permissionsManager() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), permissions)) {
            initMapView();

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
    private void initMapView() {

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
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String address = amapLocation.getAoiName();

            } else {
            }
        }
    }
}