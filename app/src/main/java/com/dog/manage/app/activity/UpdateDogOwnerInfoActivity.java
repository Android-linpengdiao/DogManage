package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.LogUtil;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.BaseBottomSheetDialog;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.AreaSelectAdapter;
import com.dog.manage.app.adapter.CommunitySelectAdapter;
import com.dog.manage.app.databinding.ActivityUpdateDogOwnerInfoBinding;
import com.dog.manage.app.databinding.DialogAddressBinding;
import com.dog.manage.app.databinding.DialogCommunityBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.AddressBean;
import com.dog.manage.app.model.CommunityBean;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogUser;
import com.dog.manage.app.model.HandleInfo;
import com.dog.manage.app.model.LicenceBean;
import com.dog.manage.app.model.SaveAddress;
import com.dog.manage.app.utils.UploadFileManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 信息变更
 */
public class UpdateDogOwnerInfoActivity extends BaseActivity {

    private ActivityUpdateDogOwnerInfoBinding binding;
    public static final int type_details = 0;//犬主信息
    public static final int type_submit = 1;//提交

    private int type = 0;
    private LicenceBean licenceBean = null;
    private SaveAddress saveAddress = null;
    private DogUser dogUser = new DogUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_owner_info);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);


        if (type == type_details) {
            binding.firstStepView.setSelected(true);
            licenceBean = (LicenceBean) getIntent().getSerializableExtra("LicenceBean");
//                getDogUserById();

        } else if (type == type_submit) {
            binding.secondStepView.setSelected(true);

            binding.confirmView.setText("提交审核");
            binding.submitContainer.setVisibility(View.VISIBLE);
            binding.dogOwnerContainer.setVisibility(View.GONE);

            saveAddress = (SaveAddress) getIntent().getSerializableExtra("saveAddress");
            binding.handleUnitAddressView.setText(saveAddress.getAcceptUnit());
//            getHandleInfo();
        }

        binding.addressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View contentView = LayoutInflater.from(UpdateDogOwnerInfoActivity.this).inflate(R.layout.dialog_address, null);
                addressBinding = DataBindingUtil.bind(contentView);
                BaseBottomSheetDialog bottomSheetDialog = new BaseBottomSheetDialog(UpdateDogOwnerInfoActivity.this) {
                    @Override
                    protected View initContentView() {
                        return addressBinding.getRoot();
                    }
                };
                bottomSheetDialog.show();

                addressBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                addressBinding.recyclerView.setNestedScrollingEnabled(false);
                RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        CommonUtil.dip2px(getApplicationContext(), 0.5f),
                        Color.parseColor("#E1E1E1"));
                addressBinding.recyclerView.addItemDecoration(divider);
                areaSelectAdapter = new AreaSelectAdapter(getApplicationContext());
                addressBinding.recyclerView.setAdapter(areaSelectAdapter);

                addressBinding.refreshLayout.setEnableRefresh(false);
                addressBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {
                        getAddressAreas(false);
                    }
                });
                getAddressAreas(true);

                addressBinding.confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (areaSelectAdapter.getList().size() > 0) {
                            addressBean = areaSelectAdapter.getList().get(areaSelectAdapter.getSelect());
                            String address = addressBean.getAreaName();
                            dogUser.setAddress(address);
                            binding.addressView.binding.itemContent.setText(address);
                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });

        binding.detailedAddressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(UpdateDogOwnerInfoActivity.this).inflate(R.layout.dialog_community, null);
                communityBinding = DataBindingUtil.bind(contentView);
                BaseBottomSheetDialog bottomSheetDialog = new BaseBottomSheetDialog(UpdateDogOwnerInfoActivity.this) {
                    @Override
                    protected View initContentView() {
                        return communityBinding.getRoot();
                    }
                };
                bottomSheetDialog.show();

                communityBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                communityBinding.recyclerView.setNestedScrollingEnabled(false);
                RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        CommonUtil.dip2px(getApplicationContext(), 0.5f),
                        Color.parseColor("#E1E1E1"));
                communityBinding.recyclerView.addItemDecoration(divider);
                communitySelectAdapter = new CommunitySelectAdapter(getApplicationContext());
                communityBinding.recyclerView.setAdapter(communitySelectAdapter);

                communityBinding.refreshLayout.setEnableRefresh(false);
                communityBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {
                        getAddressList(false, "");
                    }
                });
                getAddressList(true, "");

                communityBinding.confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (communitySelectAdapter.getList().size() > 0) {
                            communityBean = communitySelectAdapter.getList().get(communitySelectAdapter.getSelect());
                            if (dogUser != null) {
                                dogUser.setVillageId(communityBean.getId());
                                dogUser.setCommunityDept(communityBean.getCommunityDept());
                            }
                            String detailedAddress = communityBean.getCommunityName();
                            dogUser.setDetailedAddress(detailedAddress);
                            binding.detailedAddressView.binding.itemContent.setText(detailedAddress);
                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });

    }

//    /**
//     * 犬证 获取犬主信息
//     */
//    private void getDogUserById() {
//        SendRequest.getUserById(getUserInfo().getId(), licenceBean.getDogId(), new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(ResultClient<DogUser> response, int id) {
//                if (response.isSuccess() && response.getData() != null) {
//                    dogUser = response.getData();
//                    initDogUserView(dogUser);
//                    if (dogUser.getUserType() == null) {
//                        ToastUtils.showShort(getApplicationContext(), "获取信息失败");
//                    }
//                } else {
//                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
//                }
//            }
//        });
//    }

//    private void initDogUserView(DogUser dogUser) {
//        if (dogUser.getUserType() != null && (dogUser.getUserType() == DogUser.userType_personal || dogUser.getUserType() == DogUser.userType_organ)) {
//            if (dogUser.getUserType() == DogUser.userType_personal) {
//                //居住地址（全）例：012/02/31
//                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
//                //详细地址（全）
//                binding.detailedAddressView.binding.itemContent.setText(dogUser.getDetailedAddress());
//
//                binding.houseNumberView.binding.itemEdit.setText(dogUser.getHouseNum());
//                GlideLoader.LoderUploadImage(UpdateDogOwnerInfoActivity.this, dogUser.getHousePhoto(), binding.houseProprietaryCertificateView, 6);
//
//            } else if (dogUser.getUserType() == DogUser.userType_organ) {
//
//
////                //居住地址（全）例：012/02/31
////                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
////                //详细地址（全）
////                binding.detailedAddressView.binding.itemContent.setText(dogUser.getDetailedAddress());
////
////                //养犬管理制度（单位）
////                GlideLoader.LoderUploadImage(UpdateDogOwnerInfoActivity.this, dogUser.getDogManagement(), binding.managementSystemView, 6);
////                try {
////                    //养犬设施图片（单位）
////                    List<String> idPhotos = new Gson().fromJson(dogUser.getDogDevice(), new TypeToken<List<String>>() {
////                    }.getType());
////                    if (idPhotos.size() > 0) {
////                        GlideLoader.LoderImage(UpdateDogOwnerInfoActivity.this,
////                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.facility1View, 6);
////                        legalFacility1 = idPhotos.get(0);
////                    }
////                    if (idPhotos.size() > 1) {
////                        GlideLoader.LoderImage(UpdateDogOwnerInfoActivity.this,
////                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.facility2View, 6);
////                        legalFacility2 = idPhotos.get(1);
////                    }
////                } catch (Exception e) {
////                    e.getMessage();
////                }
//
//            }
//        }
//    }

    //===============================  选择地址  ===================================

    private DialogCommunityBinding communityBinding;
    private CommunitySelectAdapter communitySelectAdapter;
    private Pager<CommunityBean> communityPager = new Pager<>();
    private CommunityBean communityBean;

    private void getAddressList(boolean isRefresh, String communityName) {
        if (addressBean == null) {
            ToastUtils.showShort(getApplicationContext(), "请先选择居住地址");
            return;
        }
        //省110000 、市110100
        SendRequest.getAddressList(communityName, 110000, 110100, addressBean.getId(),
                communityPager.getCursor(), communityPager.getSize(),
                new GenericsCallback<Pager<CommunityBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            communityBinding.refreshLayout.finishRefresh();
                        } else {
                            communityBinding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            communityBinding.refreshLayout.finishRefresh(false);
                        } else {
                            communityBinding.refreshLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onResponse(Pager<CommunityBean> response, int id) {
                        communityPager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                communitySelectAdapter.refreshData(response.getRows());
                            } else {
                                communitySelectAdapter.loadMoreData(response.getRows());
                                if (communitySelectAdapter.getList().size() < response.getTotal()) {
                                    communityPager.setCursor(communityPager.getCursor() + 1);
                                }
                            }
                            if (communitySelectAdapter.getList().size() == response.getTotal()) {
                                communityBinding.refreshLayout.setNoMoreData(true);
                            }
//                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
//                            binding.emptyView.setText("暂无犬只～");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private DialogAddressBinding addressBinding;
    private AreaSelectAdapter areaSelectAdapter;
    private Pager<AddressBean> areasPager = new Pager<>();
    private AddressBean addressBean;

    private void getAddressAreas(boolean isRefresh) {
        //省110000 、市110100
        SendRequest.getAddressAreas(3, 110100, areasPager.getCursor(), areasPager.getSize(),
                new GenericsCallback<Pager<AddressBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            addressBinding.refreshLayout.finishRefresh();
                        } else {
                            addressBinding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            addressBinding.refreshLayout.finishRefresh(false);
                        } else {
                            addressBinding.refreshLayout.finishLoadMore(false);
                        }
                        ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                    }

                    @Override
                    public void onResponse(Pager<AddressBean> response, int id) {
                        areasPager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                areaSelectAdapter.refreshData(response.getRows());
                            } else {
                                areaSelectAdapter.loadMoreData(response.getRows());
                                if (areaSelectAdapter.getList().size() < response.getTotal()) {
                                    areasPager.setCursor(areasPager.getCursor() + 1);
                                }
                            }
                            if (areaSelectAdapter.getList().size() == response.getTotal()) {
                                addressBinding.refreshLayout.setNoMoreData(true);
                            }
//                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
//                            binding.emptyView.setText("暂无犬只～");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }


    //===============================  获取价格、手里单位信息接口  ===================================

//    private HandleInfo handleInfo;
//
//    private void getHandleInfo() {
//        if (saveAddress == null) {
//            ToastUtils.showShort(getApplicationContext(), "获取信息失败");
//        }
//        /**
//         * dogId
//         * integer
//         * 犬只id
//         * addressId
//         * integer
//         * 地址id
//         */
//        Map<String, String> map = new HashMap<>();
//        map.put("dogId", String.valueOf(licenceBean.getDogId()));
//        map.put("addressId", String.valueOf(saveAddress.getAddressId()));
//        SendRequest.getHandleInfo(map, new GenericsCallback<ResultClient<HandleInfo>>(new JsonGenericsSerializator()) {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(ResultClient<HandleInfo> response, int id) {
//                if (response.isSuccess() && response.getData() != null) {
//                    handleInfo = response.getData();
//                    binding.handleUnitAddressView.setText(response.getData().getHandleUnitAddress());
//                } else {
//                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
//                }
//            }
//        });
//    }

    public void onClickConfirm(View view) {
        if (type == type_details) {
            if (licenceBean == null) {
                ToastUtils.showShort(getApplicationContext(), "保存信息失败");
                return;
            }

            if (CommonUtil.isBlank(dogUser.getAddress())) {
                ToastUtils.showShort(getApplicationContext(), "请选择居住地址");
                return;
            }

            if (CommonUtil.isBlank(dogUser.getDetailedAddress())) {
                ToastUtils.showShort(getApplicationContext(), "请输入详细地址");
                return;
            }

            String houseNum = binding.houseNumberView.binding.itemEdit.getText().toString();
            if (CommonUtil.isBlank(houseNum)) {
                ToastUtils.showShort(getApplicationContext(), "请输入房本编号");
                return;
            } else {
                dogUser.setHouseNum(houseNum);
            }

            if (CommonUtil.isBlank(dogUser.getHousePhoto())) {
                ToastUtils.showShort(getApplicationContext(), "请上传房产证或房屋租赁合同");
                return;
            }

            /**
             * lincenceId
             * integer
             * 犬证编号
             * address
             * string
             * 居住地址
             * detailedAddress
             * string
             * 详细地址
             * houseNum
             * string
             * 房本编号
             * housePhoto
             * string
             * 房产证或租赁合同照片
             * addressArea
             * string
             * 所在区域
             */
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("lincenceId", String.valueOf(licenceBean.getLincenceId()));
            paramsMap.put("address", dogUser.getAddress());
            paramsMap.put("detailedAddress", dogUser.getDetailedAddress());
            paramsMap.put("houseNum", dogUser.getHouseNum());
            paramsMap.put("housePhoto", dogUser.getHousePhoto());
            paramsMap.put("addressArea", dogUser.getCommunityDept() + "");//社区所属机构（新增）
            paramsMap.put("villageId", dogUser.getVillageId() + "");//社区id

            SendRequest.saveCancelAddress(paramsMap, new GenericsCallback<ResultClient<SaveAddress>>(new JsonGenericsSerializator()) {

                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                    LoadingManager.showLoadingDialog(UpdateDogOwnerInfoActivity.this);
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    LoadingManager.hideLoadingDialog(UpdateDogOwnerInfoActivity.this);
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                }

                @Override
                public void onResponse(ResultClient<SaveAddress> response, int id) {
                    if (response.isSuccess()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", type_submit);
                        bundle.putSerializable("saveAddress", response.getData());
                        openActivity(UpdateDogOwnerInfoActivity.class, bundle);

                    } else {
                        ToastUtils.showShort(getApplicationContext(), response.getMsg());

                    }

                }
            });

        } else if (type == type_submit) {
            if (saveAddress == null) {
                ToastUtils.showShort(getApplicationContext(), "提交失败,信息有误");
                return;
            }

            /**
             * lincenceId
             * integer
             * 犬只id
             * newAddressId
             * integer
             * 新地址id
             * acceptUnit
             * string
             * 受理单位
             * unitId
             * integer
             */
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("lincenceId", String.valueOf(saveAddress.getLincenceId()));
            paramsMap.put("newAddressId", String.valueOf(saveAddress.getAddressId()));
            paramsMap.put("acceptUnit", saveAddress.getAcceptUnit());
            paramsMap.put("unitId", saveAddress.getUnitId());
            SendRequest.approveCancelAddress(paramsMap, new GenericsCallback<ResultClient<Integer>>(new JsonGenericsSerializator()) {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(ResultClient<Integer> response, int id) {
                    if (response.isSuccess()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", SubmitSuccessActivity.type_update);
                        openActivity(SubmitSuccessActivity.class, bundle);

                        finishActivity(DogManageWorkflowActivity.class);
                        finishActivity(UpdateDogCertificateActivity.class);
                        finish();

                    } else {
                        ToastUtils.showShort(getApplicationContext(), response.getMsg());
                    }

                }
            });

        }
    }

    private final int request_HouseProprietaryCertificate = 100;

    /**
     * 上传房产证或房屋租赁合同
     *
     * @param view
     */
    public void onClickHouseProprietaryCertificate(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_HouseProprietaryCertificate)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_HouseProprietaryCertificate);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_HouseProprietaryCertificate:
                    compressImage(data, request_HouseProprietaryCertificate);

                    break;
            }
        }
    }

    private void compressImage(Intent data, int requestCode) {
        try {
            if (data != null) {
                String imageJson = data.getStringExtra("imageJson");
                if (!TextUtils.isEmpty(imageJson)) {
                    Gson gson = new Gson();
                    List<MediaFile> imageList = gson.fromJson(imageJson, new TypeToken<List<MediaFile>>() {
                    }.getType());
                    if (imageList != null && imageList.size() > 0) {
                        String path = imageList.get(0).getPath();
                        Luban.with(this)
                                .load(path)// 传人要压缩的图片列表
                                .ignoreBy(500)// 忽略不压缩图片的大小
                                .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        if (requestCode == request_HouseProprietaryCertificate) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    uploadFile(file.getAbsolutePath());
                                                }
                                            }).start();

//                                            housePhoto = file.getAbsolutePath();
//                                            GlideLoader.LoderImage(UpdateDogOwnerInfoActivity.this, file.getAbsolutePath(), binding.houseProprietaryCertificateView, 6);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                }).launch();//启动压缩
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 华为云 上传文件
     *
     * @param filePath
     */
    private void uploadFile(String filePath) {
        Log.i(TAG, "uploadFile: filePath = " + filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        Log.i(TAG, "uploadFile: fileName = " + fileName);
        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName(Config.huaweiBucketName);
        request.setObjectKey(fileName);
        request.setFile(new File(filePath));
        request.setProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus status) {
                // 获取上传平均速率
                Log.i(TAG, "uploadFile: AverageSpeed:" + status.getAverageSpeed());
                // 获取上传进度百分比
                Log.i(TAG, "uploadFile: TransferPercentage:" + status.getTransferPercentage());
            }
        });
        //每上传1MB数据反馈上传进度
        request.setProgressInterval(1024 * 1024L);
        PutObjectResult result = UploadFileManager.getInstance().getObsClient().putObject(request);
        Log.i(TAG, "uploadFile: getObjectUrl = " + result.getObjectUrl());
        String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;
        Log.i(TAG, "uploadFile: url = " + url);
        //http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/54577243b9b38770.jpg

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dogUser.setHousePhoto(url);
                GlideLoader.LoderImage(UpdateDogOwnerInfoActivity.this, url, binding.houseProprietaryCertificateView, 6);


            }
        });


    }
}