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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_submit);
        addActivity(this);

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
         * ??????id
         * addressId
         * integer
         * ??????id
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
                    binding.handleUnitAddressView.setText(response.getData().getHandleUnitAddress());
                    binding.costValueView.binding.itemContent.setText("???" + response.getData().getCostValue());
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    public void onClickConfirm(View view) {
        if (handleInfo == null) {
            ToastUtils.showShort(getApplicationContext(), "????????????");
            return;
        }
        /**
         * addressId
         * integer
         * ??????id
         * dogId
         * integer
         * ??????id
         * acceptUnit
         * string
         * ????????????
         * unitId
         * integer
         * ????????????id
         * immunePhoto
         * string
         * ?????????????????? ????????????
         */

//        addressId = 112;
//        dogId = 8;
//        String acceptUnit = "?????????????????????????????????";
//        int unitId = 1;
//        paramsMap.put("acceptUnit", acceptUnit);
//        paramsMap.put("unitId", String.valueOf(unitId));

        paramsMap.put("addressId", String.valueOf(addressId));
        paramsMap.put("dogId", String.valueOf(dogId));
        paramsMap.put("acceptUnit", handleInfo.getHandleUnitAddress());
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
            EasyPermissions.requestPermissions(this, "????????????????????????", requestCode, permissions);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * ??????
     */
    private void initMapView() {

        AMapLocationClient mlocationClient = new AMapLocationClient(getApplicationContext());
        //?????????????????????
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //??????????????????
        mlocationClient.setLocationListener(this);
        //???????????????????????????????????????Battery_Saving?????????????????????Device_Sensors??????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????,????????????,?????????2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        //??????????????????
        mlocationClient.setLocationOption(mLocationOption);
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????1000ms?????????????????????????????????stopLocation()???????????????????????????
        // ???????????????????????????????????????????????????onDestroy()??????
        // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
        // ????????????
        mlocationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //?????????????????????????????????????????????
                amapLocation.getLocationType();//??????????????????????????????????????????????????????????????????????????????
                double latitude = amapLocation.getLatitude();//????????????
                double longitude = amapLocation.getLongitude();//????????????
                String address = amapLocation.getAoiName();

            } else {
            }
        }
    }
}