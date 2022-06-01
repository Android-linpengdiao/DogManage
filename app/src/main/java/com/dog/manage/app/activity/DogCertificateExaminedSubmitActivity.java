package com.dog.manage.app.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.base.BaseData;
import com.base.utils.CommonUtil;
import com.base.utils.LogUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateExaminedSubmitBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogLicenseDetail;
import com.dog.manage.app.model.HandleInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DogCertificateExaminedSubmitActivity extends BaseActivity implements AMapLocationListener {

    private ActivityDogCertificateExaminedSubmitBinding binding;
    private Dog dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_examined_submit);
        addActivity(this);

        dogDetail = (Dog) getIntent().getSerializableExtra("dataBean");
        if (dogDetail != null) {
            binding.dogTypeView.binding.itemContent.setText(dogDetail.getDogType());
            getAnnualDogLicense(dogDetail.getLincenceId());
        }
//        permissionsManager();
    }

    /**
     * 犬证年审-选择我的犬只详情
     */
    private void getAnnualDogLicense(int lincenceId) {
        SendRequest.getAnnualDogLicense(lincenceId,
                new GenericsCallback<ResultClient<HandleInfo>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<HandleInfo> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    binding.handleUnitAddressView.setText(response.getData().getHandleUnitAddress());
                    binding.costValueView.binding.itemContent.setText("￥" + response.getData().getCostValue());

                } else {
                    ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
                }
            }
        });
    }

    public void onClickConfirm(View view) {
        if (dogDetail == null) {
            ToastUtils.showShort(getApplicationContext(), "信息有误");
            return;
        }
        SendRequest.saveAnnualDogLicense(dogDetail.getLincenceId(),
                new GenericsCallback<ResultClient<Boolean>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<Boolean> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", SubmitSuccessActivity.type_examined);
                            openActivity(SubmitSuccessActivity.class, bundle);

                            finishActivity(DogManageWorkflowActivity.class);
                            finishActivity(DogCertificateExaminedActivity.class);
                            finish();

                        } else {
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
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
            Log.i(TAG, "onLocationChanged: getErrorCode " + amapLocation.getErrorCode());
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String address = amapLocation.getAoiName();
                Log.i(TAG, "onLocationChanged: latitude = " + latitude + " ; longitude = " + longitude + " ; address = " + address);
            } else {
            }
        }
    }
}