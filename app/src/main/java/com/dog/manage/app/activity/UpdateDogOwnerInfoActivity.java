package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.AreaSelectAdapter;
import com.dog.manage.app.adapter.CommunitySelectAdapter;
import com.dog.manage.app.adapter.ImageAdapter;
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
import com.dog.manage.app.view.GridItemDecoration;
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
import java.util.ArrayList;
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
 * ????????????
 */
public class UpdateDogOwnerInfoActivity extends BaseActivity {

    private ActivityUpdateDogOwnerInfoBinding binding;
    public static final int type_details = 0;//????????????
    public static final int type_submit = 1;//??????

    private int type = 0;
    private LicenceBean licenceBean = null;
    private SaveAddress saveAddress = null;
    private DogUser dogUser = new DogUser();

    private ImageAdapter imageAdapter;
    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_owner_info);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.houseNumberView.binding.itemEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});


        if (type == type_details) {
            binding.firstStepView.setSelected(true);
            licenceBean = (LicenceBean) getIntent().getSerializableExtra("LicenceBean");
//                getDogUserById();
            imageAdapter = new ImageAdapter(this);
            GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
            builder.color(R.color.transparent);
            builder.size(CommonUtil.dip2px(this, 2));
            binding.imageRecyclerView.addItemDecoration(new GridItemDecoration(builder));
            binding.imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            binding.imageRecyclerView.setAdapter(imageAdapter);
            imageAdapter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view, Object object) {
                    if (object instanceof Integer) {
                        int position = (int) object;
                        if (position == (imageList.size() - 1)) {
                            if (imageList.size() < 5) {
                                onClickHouseProprietaryCertificate();
                            } else {
                                ToastUtils.showShort(UpdateDogOwnerInfoActivity.this, "????????????9?????????");
                            }
                        }
                    }
                }

                @Override
                public void onLongClick(View view, Object object) {

                }
            });
            imageList.add("add");
            imageAdapter.refreshData(imageList);

        } else if (type == type_submit) {
            binding.secondStepView.setSelected(true);

            binding.confirmView.setText("????????????");
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
                addressBinding.refreshLayout.setEnableLoadMore(false);
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
                communityBinding.refreshLayout.setEnableLoadMore(false);
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
//     * ?????? ??????????????????
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
//                        ToastUtils.showShort(getApplicationContext(), "??????????????????");
//                    }
//                } else {
//                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
//                }
//            }
//        });
//    }

//    private void initDogUserView(DogUser dogUser) {
//        if (dogUser.getUserType() != null && (dogUser.getUserType() == DogUser.userType_personal || dogUser.getUserType() == DogUser.userType_organ)) {
//            if (dogUser.getUserType() == DogUser.userType_personal) {
//                //???????????????????????????012/02/31
//                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
//                //?????????????????????
//                binding.detailedAddressView.binding.itemContent.setText(dogUser.getDetailedAddress());
//
//                binding.houseNumberView.binding.itemEdit.setText(dogUser.getHouseNum());
//                GlideLoader.LoderUploadImage(UpdateDogOwnerInfoActivity.this, dogUser.getHousePhoto(), binding.houseProprietaryCertificateView, 6);
//
//            } else if (dogUser.getUserType() == DogUser.userType_organ) {
//
//
////                //???????????????????????????012/02/31
////                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
////                //?????????????????????
////                binding.detailedAddressView.binding.itemContent.setText(dogUser.getDetailedAddress());
////
////                //??????????????????????????????
////                GlideLoader.LoderUploadImage(UpdateDogOwnerInfoActivity.this, dogUser.getDogManagement(), binding.managementSystemView, 6);
////                try {
////                    //??????????????????????????????
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

    //===============================  ????????????  ===================================

    private DialogCommunityBinding communityBinding;
    private CommunitySelectAdapter communitySelectAdapter;
    private Pager<CommunityBean> communityPager = new Pager<>();
    private CommunityBean communityBean;

    private void getAddressList(boolean isRefresh, String communityName) {
        if (addressBean == null) {
            ToastUtils.showShort(getApplicationContext(), "????????????????????????");
            return;
        }
        //???110000 ??????110100
        SendRequest.getAddressList(communityName, 110000, 110100, addressBean.getId(),
                communityPager.getCursor(), 100,
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
//                            binding.emptyView.setText("???????????????");
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
        //???110000 ??????110100
        SendRequest.getAddressAreas(3, 110100,
                areasPager.getCursor(), 100,
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
                        ToastUtils.showShort(getApplicationContext(), "??????????????????");
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
//                            binding.emptyView.setText("???????????????");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }


    //===============================  ???????????????????????????????????????  ===================================

//    private HandleInfo handleInfo;
//
//    private void getHandleInfo() {
//        if (saveAddress == null) {
//            ToastUtils.showShort(getApplicationContext(), "??????????????????");
//        }
//        /**
//         * dogId
//         * integer
//         * ??????id
//         * addressId
//         * integer
//         * ??????id
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
                ToastUtils.showShort(getApplicationContext(), "??????????????????");
                return;
            }

            if (CommonUtil.isBlank(dogUser.getAddress())) {
                ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                return;
            }

            if (CommonUtil.isBlank(dogUser.getDetailedAddress())) {
                ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                return;
            }

            String houseNum = binding.houseNumberView.binding.itemEdit.getText().toString();
            if (CommonUtil.isBlank(houseNum)) {
                ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                return;
            } else {
                dogUser.setHouseNum(houseNum);
            }

            if (imageList.size() <= 1) {
                ToastUtils.showShort(getApplicationContext(), "???????????????????????????????????????");
                return;
            }
            if (imageList.size() > 0 && imageList.indexOf("add") != -1) {
                imageList.remove("add");
            }

//            if (CommonUtil.isBlank(dogUser.getHousePhoto())) {
//                ToastUtils.showShort(getApplicationContext(), "???????????????????????????????????????");
//                return;
//            }

            /**
             * lincenceId
             * integer
             * ????????????
             * address
             * string
             * ????????????
             * detailedAddress
             * string
             * ????????????
             * houseNum
             * string
             * ????????????
             * housePhoto
             * string
             * ??????????????????????????????
             * addressArea
             * string
             * ????????????
             */
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("lincenceId", String.valueOf(licenceBean.getLincenceId()));
            paramsMap.put("address", dogUser.getAddress());
            paramsMap.put("detailedAddress", dogUser.getDetailedAddress());
            paramsMap.put("houseNum", dogUser.getHouseNum());
            paramsMap.put("housePhoto", GsonUtils.toJson(imageList));
//            paramsMap.put("housePhoto", dogUser.getHousePhoto());
            paramsMap.put("addressArea", dogUser.getCommunityDept() + "");//??????????????????????????????
            paramsMap.put("villageId", dogUser.getVillageId() + "");//??????id

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
                ToastUtils.showShort(getApplicationContext(), "????????????,????????????");
                return;
            }

            /**
             * lincenceId
             * integer
             * ??????id
             * newAddressId
             * integer
             * ?????????id
             * acceptUnit
             * string
             * ????????????
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
     * ????????????????????????????????????
     */
    public void onClickHouseProprietaryCertificate() {
        if (checkPermissions(PermissionUtils.STORAGE, request_HouseProprietaryCertificate)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 5 - imageAdapter.getList().size());
            openActivity(MediaSelectActivity.class, bundle, request_HouseProprietaryCertificate);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_HouseProprietaryCertificate:
                    compressImageMulti(data, request_HouseProprietaryCertificate);

                    break;
            }
        }
    }

    private void compressImageMulti(Intent data, int requestCode) {
        try {
            if (data != null) {
                String imageJson = data.getStringExtra("imageJson");
                if (!TextUtils.isEmpty(imageJson)) {
                    Gson gson = new Gson();
                    List<MediaFile> mediaFiles = gson.fromJson(imageJson, new TypeToken<List<MediaFile>>() {
                    }.getType());
                    if (mediaFiles != null && mediaFiles.size() > 0) {
                        List<String> mediaFileList = new ArrayList<>();
                        for (int i = 0; i < mediaFiles.size(); i++) {
                            MediaFile mediaFile = mediaFiles.get(i);
                            String path = mediaFile.getPath();
                            int finalI = i;
                            Luban.with(this)
                                    .load(path)// ??????????????????????????????
                                    .ignoreBy(500)// ??????????????????????????????
                                    .setTargetDir(FileUtils.getMediaPath())// ?????????????????????????????????
                                    .setCompressListener(new OnCompressListener() { //????????????
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            mediaFileList.add(file.getAbsolutePath());
                                            if (finalI == mediaFiles.size() - 1) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        uploadFileMulti(mediaFileList);
                                                    }
                                                }).start();
                                            }
//                                                }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }
                                    }).launch();//????????????
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * ????????? ????????????
     *
     * @param mediaFileList
     */
    private void uploadFileMulti(List<String> mediaFileList) {
        LoadingManager.showLoadingDialog(UpdateDogOwnerInfoActivity.this, "?????????...");
        for (int i = 0; i < mediaFileList.size(); i++) {
            String filePath = mediaFileList.get(i);
            Log.i(TAG, "uploadFile: filePath = " + filePath);
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(Config.huaweiBucketName);
            request.setObjectKey(fileName);
            request.setFile(new File(filePath));
            request.setProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressStatus status) {
                }
            });
            //?????????1MB????????????????????????
            request.setProgressInterval(1024 * 1024L);
            PutObjectResult result = UploadFileManager.getInstance().getObsClient().putObject(request);
            String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;
            Log.i(TAG, "uploadFile: url = " + url);
            imageList.add(imageAdapter.getList().size() - 1, url);
            LoadingManager.hideLoadingDialog(UpdateDogOwnerInfoActivity.this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageAdapter.notifyDataSetChanged();
                }
            });
        }
    }

}