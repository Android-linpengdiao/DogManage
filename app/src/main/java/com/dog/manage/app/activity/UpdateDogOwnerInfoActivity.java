package com.dog.manage.app.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
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
import com.base.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.AddressBean;
import com.dog.manage.app.model.CommunityBean;
import com.dog.manage.app.model.DogUser;
import com.dog.manage.app.model.LicenceBean;
import com.dog.manage.app.model.SaveAddress;
import com.dog.manage.app.utils.UploadFileManager;
import com.dog.manage.app.view.GridItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                ToastUtils.showShort(UpdateDogOwnerInfoActivity.this, "最多上传9张图片");
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
            getDogUser();

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
                            dogUser.setAddress("110000/110100/" + addressBean.getId());
                            String address = addressBean.getAreaName();
                            binding.addressView.binding.itemContent.setText(address);
                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });

        binding.communityAddressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
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

                communityBinding.detailedAddressView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (CommonUtil.isBlank(charSequence.toString())) {
                            getAddressList(true, "");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                communityBinding.searchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String content = communityBinding.detailedAddressView.getText().toString();
                        if (CommonUtil.isBlank(content)) {
                            ToastUtils.showShort(getApplication(), "请输入小区名称");
                        } else {
                            getAddressList(true, content);
                        }
                    }
                });
                communityBinding.confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (communitySelectAdapter.getList().size() > 0) {
                            communityBean = communitySelectAdapter.getList().get(communitySelectAdapter.getSelect());
                            if (dogUser != null) {
                                dogUser.setVillageId(communityBean.getId());
                                dogUser.setCommunityDept(communityBean.getCommunityDept());
                            }
                            String communityAddress = communityBean.getCommunityName();
//                            dogUser.setDetailedAddress(detailedAddress);
                            binding.communityAddressView.binding.itemContent.setText(communityAddress);
                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });

    }


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

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private void getDogUser() {
        SendRequest.getDogUser(new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogUser> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().getUserType() != null &&
                            (response.getData().getUserType() == DogUser.userType_personal || response.getData().getUserType() == DogUser.userType_organ)) {
                        if (response.getData().getUserType() == DogUser.userType_personal) {
                            binding.houseNumberView.setVisibility(View.VISIBLE);
                            binding.houseContainer.setVisibility(View.VISIBLE);

                        } else if (response.getData().getUserType() == DogUser.userType_organ) {

                        }
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }


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

            String communityAddress = binding.communityAddressView.binding.itemContent.getText().toString();
            if (CommonUtil.isBlank(communityAddress)) {
                ToastUtils.showShort(getApplicationContext(), "请选择所属小区");
                return;
            }

            String detailedAddress = binding.detailedAddressView.getText().toString();
            if (CommonUtil.isBlank(detailedAddress)) {
                ToastUtils.showShort(getApplicationContext(), "请输入详细地址");
                return;
            } else {
                dogUser.setDetailedAddress(detailedAddress);
            }

            String houseNum = binding.houseNumberView.binding.itemEdit.getText().toString();
            if (CommonUtil.isBlank(houseNum)) {
                ToastUtils.showShort(getApplicationContext(), "请输入房本编号");
                return;
            } else {
                dogUser.setHouseNum(houseNum);
            }

            if (imageList.size() <= 1) {
                ToastUtils.showShort(getApplicationContext(), "请上传房产证或房屋租赁合同");
                return;
            }
            if (imageList.size() > 0 && imageList.indexOf("add") != -1) {
                imageList.remove("add");
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
            paramsMap.put("housePhoto", GsonUtils.toJson(imageList));
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
                                    .load(path)// 传人要压缩的图片列表
                                    .ignoreBy(500)// 忽略不压缩图片的大小
                                    .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                    .setCompressListener(new OnCompressListener() { //设置回调
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
                                    }).launch();//启动压缩
                        }
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
     * @param mediaFileList
     */
    private void uploadFileMulti(List<String> mediaFileList) {
        try {
            LoadingManager.showLoadingDialog(UpdateDogOwnerInfoActivity.this, "上传中...");
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
                //每上传1MB数据反馈上传进度
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
        } catch (Exception e) {
            ToastUtils.showShort(getApplication(), "上传失败");
        }
    }

    public void onClickShowHouse(View view) {
        showHouseImageView();
    }

    public void showHouseImageView() {
        AlertDialog dialog = new AlertDialog.Builder(UpdateDogOwnerInfoActivity.this, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setBackgroundColor(getResources().getColor(R.color.transparent));
        window.setAttributes(lp);
        window.setContentView(R.layout.house_image_view);
        ImageView back = window.findViewById(R.id.back);
        ImageView imageView = window.findViewById(R.id.imageView);
        GlideLoader.LoaderHouse(UpdateDogOwnerInfoActivity.this, imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

}