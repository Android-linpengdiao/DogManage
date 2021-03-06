package com.dog.manage.app.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.base.LocationBean;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.MapNavigationUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityImmuneDetailsBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.ImmuneApproveDetail;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ImmuneDetailsActivity extends BaseActivity implements AMapLocationListener {

    private ActivityImmuneDetailsBinding binding;
    private int immuneId;
    private ImmuneApproveDetail immuneApproveDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_details);
        addActivity(this);

        immuneId = getIntent().getIntExtra("immuneId", 0);

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (immuneApproveDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", immuneApproveDetail.getDogId());
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (immuneApproveDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", immuneApproveDetail.getDogId());
                openActivity(DogInfoActivity.class, bundle);
            }
        });

        getDogImmuneApproveDetail();
        permissionsLocation();

    }

    /**
     * ???????????????????????????
     */
    private void getDogImmuneApproveDetail() {
        SendRequest.getDogImmuneApproveDetail(immuneId, new GenericsCallback<ResultClient<ImmuneApproveDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(ImmuneDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(ImmuneDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<ImmuneApproveDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());

                } else {
                    ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "??????????????????");
                }
            }
        });
    }

    private void initView(ImmuneApproveDetail data) {
        binding.container.setVisibility(View.VISIBLE);
        binding.bottomView.setVisibility(View.VISIBLE);
        immuneApproveDetail = data;
        //???????????? 0 ?????? 1???????????? 2???????????? 3: ???????????? 4: ?????????
        Integer licenceStatus = data.getLincenceStatus();
        binding.auditStatusView.setText(licenceStatus == 1 ? "?????????" : licenceStatus == 2 ? "?????????" : licenceStatus == 3 ? "????????????" : licenceStatus == 4 ? "?????????" : "?????????");
        binding.hintView.setVisibility(licenceStatus == 1 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(licenceStatus == 1 ? View.GONE : View.VISIBLE);
        binding.confirmView.setText(licenceStatus == 2 ? "???????????????" : "????????????");
        if (data.getDogAge() != null) {
            binding.contentView.setText(data.getDogType() + "-" + CommonUtil.getDogAge(data.getDogAge()));
        }
        binding.createTimeView.setText(data.getCreateTime());

        binding.dogOwnerInfoView.binding.itemContent.setText(data.getUserName());
        binding.dogDetailsView.binding.itemContent.setText(data.getDogType());

        binding.hospitalNameView.setText(data.getHospitalName());
        binding.hospitalAddressView.setText(data.getHospitalAddress());
        binding.hospitalPhoneView.setText(data.getHospitalPhone());
    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", MyDogCertificateOrImmuneActivity.type_immune);
        openActivity(MyDogCertificateOrImmuneActivity.class, bundle);
    }

    public void onClickNavigate(View view) {
        if (CommonUtil.openLocation(ImmuneDetailsActivity.this, request_Location)) {
            LatLng latLng = new LatLng(39.993743, 116.472995);
            List<String> hasMap = new MapNavigationUtil().hasMap(ImmuneDetailsActivity.this);
            if (hasMap.size() > 0) {
                MapNavigationUtil.showChooseMap(ImmuneDetailsActivity.this, new LocationBean(null, latLng.latitude, latLng.longitude));
            } else {
                ToastUtils.showLong(ImmuneDetailsActivity.this, "???????????????APP??????????????????????????????APP");
            }
        }
    }

    private final int request_Location = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode);
        if (resultCode == RESULT_OK && requestCode == request_Location) {
            permissionsLocation();
        }
    }

    //==================================     ??????     ==============================================

    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private final int requestCode = 100;

    @AfterPermissionGranted(requestCode)
    private void permissionsLocation() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), permissions)) {
            initLocation();

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
    private void initLocation() {
        Log.i(TAG, "initLocation: ");
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
        Log.i(TAG, "onLocationChanged: ");
        if (amapLocation != null) {
            Log.i(TAG, "onLocationChanged: getErrorCode = " + amapLocation.getErrorCode());
            if (amapLocation.getErrorCode() == 0) {
                //?????????????????????????????????????????????
                amapLocation.getLocationType();//??????????????????????????????????????????????????????????????????????????????
                double latitude = amapLocation.getLatitude();//????????????
                double longitude = amapLocation.getLongitude();//????????????
                String address = amapLocation.getAoiName();
                Log.i(TAG, "onLocationChanged: latitude " + latitude);
                Log.i(TAG, "onLocationChanged: longitude " + longitude);
                LatLng startLatLng = new LatLng(latitude, longitude);
                LatLng endLatLng = new LatLng(39.993743, 116.472995);
                if (startLatLng.latitude == 0 || startLatLng.longitude == 0 || endLatLng.latitude == 0 || endLatLng.longitude == 0) {
                    binding.locationTextView.setVisibility(View.INVISIBLE);
                } else {
                    float distance = AMapUtils.calculateLineDistance(startLatLng, endLatLng) / 1000;
                    DecimalFormat df = new DecimalFormat("#0.00");
                    binding.locationTextView.setText("??????" + df.format(distance) + "km");
                    binding.locationTextView.setVisibility(View.VISIBLE);
                }
            } else {

            }
        }
    }
}