package com.dog.manage.app.activity;


import android.app.Activity;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.base.manager.DialogManager;
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
import com.cjt2325.camera.JCameraView;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.AreaSelectAdapter;
import com.dog.manage.app.adapter.CommunitySelectAdapter;
import com.dog.manage.app.adapter.ImageAdapter;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogOwnerBinding;
import com.dog.manage.app.databinding.DialogAddressBinding;
import com.dog.manage.app.databinding.DialogCommunityBinding;
import com.base.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.AddressBean;
import com.dog.manage.app.model.CommunityBean;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.DogUser;
import com.dog.manage.app.model.IdCard;
import com.dog.manage.app.utils.UploadFileManager;
import com.dog.manage.app.view.GridItemDecoration;
import com.gamerole.orcameralib.CameraActivity;
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
import com.okhttp.callbacks.StringCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 犬证办理
 */
public class DogCertificateEditDogOwnerActivity extends BaseActivity {

    private ActivityDogCertificateEditDogOwnerBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    public static final int type_examined = 3;//犬证年审
    public static final int type_adoption = 4;//犬只领养
    public static final int type_details = 5;//犬主信息
    private int type = 0;
    private int dogId = 0;
    private int useId = 0;
    private int leaveId = 0;
    private String paperLicence = null;
    private String paperImmuneLicence = null;
    private DogUser dogUser = new DogUser();

    private ImageAdapter imageAdapter;
    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog_owner);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);
        useId = getUserInfo().getId();
        if (type == type_userInfo) {
            binding.titleView.binding.itemTitle.setText("我的信息");
            binding.stepContainer.setVisibility(View.GONE);
            binding.confirmView.setText("保存信息");
            initPersonal();
            getDogUser();
            getHuaweiCloudAuthTokens();

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交审核");
            initPersonal();
            checkingDogUser();

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③选择医院");
            initPersonal();
            checkingDogUser();

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("年审办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交年审");
            initPersonal();
            checkingDogUser();
            paperLicence = getIntent().getStringExtra("paperLicence");
            paperImmuneLicence = getIntent().getStringExtra("paperImmuneLicence");

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("犬只领养");
            binding.firstStepView.setSelected(true);
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①选择犬只");
            binding.secondStepView.setText("②犬主信息");
            binding.thirdStepView.setText("③提交审核");
            binding.secondStepView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_10), 0, getResources().getDimensionPixelOffset(R.dimen.dp_10), 0);
            initPersonal();

            leaveId = getIntent().getIntExtra("leaveId", 0);
            checkingDogUser();

        } else if (type == type_details) {
            binding.titleView.binding.itemTitle.setText("犬主信息");
            binding.stepContainer.setVisibility(View.GONE);
            binding.confirmView.setVisibility(View.GONE);

            binding.radioButtonOrgan.setEnabled(false);
            binding.radioButtonPersonal.setEnabled(false);

            binding.radioButtonHaiWai.setEnabled(false);
            binding.radioButtonGanGao.setEnabled(false);
            binding.radioButtonIDCard.setEnabled(false);


            binding.organNameView.binding.itemEdit.setEnabled(false);
            binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);
            binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);

            binding.addressView.binding.itemContent.setEnabled(false);
            binding.communityAddressView.binding.itemContent.setEnabled(false);
            binding.detailedAddressView.setEnabled(false);
            binding.houseNumberView.binding.itemEdit.setEnabled(false);
            initPersonal();

            dogId = getIntent().getIntExtra("dogId", 0);
            if (getIntent().getIntExtra("useId", 0) != 0) {
                useId = getIntent().getIntExtra("useId", 0);
                getDogUserById(1);
            } else {
                getDogUserById(0);
            }

        }

        binding.radioGroupDogOwner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOrgan:
                        initOrganView();

                        break;
                    case R.id.radioButtonPersonal:
                        initPersonal();

                        break;
                    default:
                        break;
                }
            }
        });
        binding.addressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View contentView = LayoutInflater.from(DogCertificateEditDogOwnerActivity.this).inflate(R.layout.dialog_address, null);
                addressBinding = DataBindingUtil.bind(contentView);
                BaseBottomSheetDialog bottomSheetDialog = new BaseBottomSheetDialog(DogCertificateEditDogOwnerActivity.this) {
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

                            //重新选择小区
                            dogUser.setVillageId(0);
                            dogUser.setCommunityDept(0);
                            binding.communityAddressView.binding.itemContent.setText(null);

                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });

        binding.communityAddressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == type_details) {
                    return;
                }

                View contentView = LayoutInflater.from(DogCertificateEditDogOwnerActivity.this).inflate(R.layout.dialog_community, null);
                communityBinding = DataBindingUtil.bind(contentView);
                BaseBottomSheetDialog bottomSheetDialog = new BaseBottomSheetDialog(DogCertificateEditDogOwnerActivity.this) {
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
                            ToastUtils.showShort(DogCertificateEditDogOwnerActivity.this, "最多上传9张图片");
                        }
                    }
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        if (type == type_details) {
            imageAdapter.setType(type);
        } else {
            imageList.add("add");
            imageAdapter.refreshData(imageList);
        }


    }

    /**
     * 获取华为Token认证
     */
    private void getHuaweiCloudAuthTokens() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject identity = new JSONObject();

            JSONArray methods = new JSONArray();
            methods.put("password");
            identity.put("methods", methods);

            JSONObject password = new JSONObject();

            JSONObject user = new JSONObject();
            user.put("name", "xingchongwangguo-token");
            user.put("password", "xingchongwangguo12345678");

            JSONObject domain = new JSONObject();
            domain.put("name", "xingchongwangguo");

            user.put("domain", domain);

            password.put("user", user);

            identity.put("password", password);
            jsonObject.put("identity", identity);


            JSONObject scope = new JSONObject();

            JSONObject project = new JSONObject();
            project.put("name", "cn-north-4");
            scope.put("project", project);

            jsonObject.put("scope", scope);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendRequest.huaweiCloudAuthTokens(jsonObject.toString(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG, "onResponse: " + response);
            }
        });
    }

    private DialogCommunityBinding communityBinding;
    private CommunitySelectAdapter communitySelectAdapter;
    private Pager<CommunityBean> communityPager = new Pager<>();
    private CommunityBean communityBean;

    /**
     * 详细地址
     *
     * @param isRefresh
     * @param communityName
     */
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
                            communityBinding.emptyView.setVisibility(communitySelectAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            communityBinding.emptyView.setText("暂无数据～");
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

    /**
     * 居住地址
     *
     * @param isRefresh
     */
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
                            }
                            Log.i(TAG, "onResponse: size = " + areaSelectAdapter.getList().size());
                            Log.i(TAG, "onResponse: getTotal = " + response.getTotal());
                            if (areaSelectAdapter.getList().size() < response.getTotal()) {
                                areasPager.setCursor(areasPager.getCursor() + 1);
                                Log.i(TAG, "onResponse: getCursor = " + areasPager.getCursor());
                            }
                            if (areaSelectAdapter.getList().size() == response.getTotal()) {
                                addressBinding.refreshLayout.setNoMoreData(true);
                            }
                            addressBinding.emptyView.setVisibility(areaSelectAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            addressBinding.emptyView.setText("暂无数据～");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private void checkingDogUser() {
        if (type == type_certificate || type == type_immune) {
            binding.container.setVisibility(View.GONE);
            binding.confirmView.setVisibility(View.GONE);
        }
        SendRequest.checkingDogUser(new GenericsCallback<ResultClient<Boolean>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogCertificateEditDogOwnerActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<Boolean> response, int id) {
                if (response.isSuccess() && response.getData()) {
                    binding.container.setVisibility(View.VISIBLE);
                    binding.confirmView.setVisibility(View.VISIBLE);
                    getDogUser();
                } else if (response.isSuccess() && !response.getData()) {
                    DialogManager.showConfirmDialog(DogCertificateEditDogOwnerActivity.this, "请先完善个人信息",
                            new DialogManager.Listener() {
                                @Override
                                public void onItemLeft() {
                                    finish();
                                }

                                @Override
                                public void onItemRight() {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_userInfo);
                                    openActivity(DogCertificateEditDogOwnerActivity.class, bundle, request_Update);
//                            finish();
                                }
                            });
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
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
                    dogUser = response.getData();
                    initDogUserView(dogUser);
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }

    /**
     * 犬证 获取犬主信息
     */
    private void getDogUserById(int userType) {
        SendRequest.getUserById(useId, dogId, userType, new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogUser> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    dogUser = response.getData();
                    initDogUserView(dogUser);
                    if (dogUser.getUserType() == null) {
                        binding.certificateContainer.setVisibility(View.GONE);
                        ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                }
            }
        });
    }


    private void initDogUserView(DogUser dogUser) {
        binding.certificateContainer.setVisibility(View.VISIBLE);
        if (dogUser.getUserType() != null && (dogUser.getUserType() == DogUser.userType_personal || dogUser.getUserType() == DogUser.userType_organ)) {
            if (dogUser.getUserType() == DogUser.userType_personal) {

                //个人办理
                binding.radioButtonOrgan.setVisibility(View.GONE);
                binding.radioButtonHaiWai.setVisibility(View.GONE);
                binding.radioButtonGanGao.setVisibility(View.GONE);

                binding.dogOwnerNameView.binding.itemEdit.setText(dogUser.getOrgName());
                binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);

                binding.dogOwnerIDCardView.binding.itemEdit.setText(dogUser.getIdNum());
                binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);

                binding.IDCardFrontView.setEnabled(false);
                binding.IDCardBackView.setEnabled(false);

                try {
                    List<String> idPhotos = new Gson().fromJson(dogUser.getIdPhoto(), new TypeToken<List<String>>() {
                    }.getType());
                    if (idPhotos.size() > 0) {
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.IDCardFrontView, 6);
                        personalIDCardFront = idPhotos.get(0);
                    }
                    if (idPhotos.size() > 1) {
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.IDCardBackView, 6);
                        personalIDCardBack = idPhotos.get(1);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                if (dogUser.getDogType() != null)
                    dogType = dogUser.getDogType();
                //养犬类型（个人）;1导盲犬/扶助犬 2 陪伴犬
                if (dogType == 1) {
                    binding.radioButtonDisabled.setChecked(true);
                    binding.oldManContainer.setVisibility(View.GONE);
                    binding.oldManOrDisabledCertificateHintView.setText("残疾人证");
//                    GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, dogUser.getAgedProve(), binding.oldManOrDisabledCertificateView, 6);
                    try {
                        List<String> idPhotos = new Gson().fromJson(dogUser.getAgedProve(), new TypeToken<List<String>>() {
                        }.getType());
                        if (idPhotos.size() > 0) {
                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                    idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.oldManOrDisabledCertificateView, 6);
                            oldManOrDisabledCertificate = idPhotos.get(0);
                        }
                        if (idPhotos.size() > 1) {
                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                    idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.oldManOrDisabledCertificateContentView, 6);
                            oldManOrDisabledCertificateContent = idPhotos.get(1);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    if (type == type_details) {
                        binding.radioButtonOldMan.setVisibility(View.GONE);
                    }

                } else if (dogType == 2) {
                    binding.radioButtonOldMan.setChecked(true);
                    binding.oldManContainer.setVisibility(View.VISIBLE);
                    binding.oldManOrDisabledCertificateHintView.setText("鳏寡老人证明");
                    //是否鳏寡老人（个人）;0：否 1：是
                    Log.i(TAG, "initDogUserView: getAged = " + dogUser.getAged());
                    if (dogUser.getAged() == 0) {
                        binding.radioButtonOldMan0.setChecked(true);
                        binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);

                    } else if (dogUser.getAged() == 1) {
                        binding.radioButtonOldMan1.setChecked(true);
//                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, dogUser.getAgedProve(), binding.oldManOrDisabledCertificateView, 6);
                        try {
                            List<String> idPhotos = new Gson().fromJson(dogUser.getAgedProve(), new TypeToken<List<String>>() {
                            }.getType());
                            if (idPhotos.size() > 0) {
                                GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                        idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.oldManOrDisabledCertificateView, 6);
                                oldManOrDisabledCertificate = idPhotos.get(0);
                            }
                            if (idPhotos.size() > 1) {
                                GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                        idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.oldManOrDisabledCertificateContentView, 6);
                                oldManOrDisabledCertificateContent = idPhotos.get(1);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);

                    }

                    if (type == type_details) {
                        //犬只类型提示
                        binding.radioButtonDisabled.setVisibility(dogType == 1 ? View.VISIBLE : View.GONE);
                        binding.radioButtonOldMan.setVisibility(dogType == 2 ? View.VISIBLE : View.GONE);
                        binding.radioButtonOldMan0.setChecked(dogUser.getAged() == 0 ? true : false);
                        binding.radioButtonOldMan0.setVisibility(dogUser.getAged() == 0 ? View.VISIBLE : View.GONE);
                        binding.radioButtonOldMan1.setChecked(dogUser.getAged() == 1 ? true : false);
                        binding.radioButtonOldMan1.setVisibility(dogUser.getAged() == 1 ? View.VISIBLE : View.GONE);

                        //犬只类型提示
                        binding.dogTypeHintView.setVisibility(View.GONE);
                        binding.oldManHintView.setVisibility(View.GONE);
                        binding.oldManOrDisabledCertificateContainer.setVisibility(dogType == 2 && dogUser.getAged() == 0 ? View.GONE : View.VISIBLE);
                    }

                }

                if (type == type_details) {
                    binding.dogTypeHintView.setVisibility(View.GONE);
                    binding.oldManHintView.setVisibility(View.GONE);
                    binding.addressView.binding.itemArrow.setVisibility(View.GONE);
                    binding.communityAddressView.binding.itemArrow.setVisibility(View.GONE);
                }

                //居住地址（全）例：012/02/31
//                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
                updateAddressView(binding.addressView.binding.itemContent, dogUser.getAddress());
                //所属小区
                binding.communityAddressView.binding.itemContent.setText(dogUser.getVillageName());
                //详细地址（全）
                binding.detailedAddressView.setText(dogUser.getDetailedAddress());

                binding.houseNumberView.binding.itemEdit.setText(dogUser.getHouseNum());
//                GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, dogUser.getHousePhoto(), binding.houseProprietaryCertificateView, 6);
                try {
                    //房产证或房屋租赁合同
                    List<String> housePhotos = new Gson().fromJson(dogUser.getHousePhoto(), new TypeToken<List<String>>() {
                    }.getType());
                    Log.i(TAG, "initDogUserView: " + housePhotos.size());
                    if (housePhotos != null) {
                        if (imageAdapter.getList().size() > 0) {
                            imageList.addAll(imageAdapter.getList().size() - 1, housePhotos);
                        } else {
                            imageList.addAll(housePhotos);
                        }
                    }
                    imageAdapter.refreshData(imageList);
                } catch (Exception e) {
                    e.getMessage();
                }


            } else if (dogUser.getUserType() == DogUser.userType_organ) {
                binding.radioButtonOrgan.setChecked(true);

                binding.radioButtonPersonal.setVisibility(View.GONE);

                binding.organNameView.binding.itemEdit.setText(dogUser.getOrgName());
                binding.organNameView.binding.itemEdit.setEnabled(false);
                GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, dogUser.getBizLicense(), binding.businessLicenseView, 6);
                binding.businessLicenseView.setEnabled(false);

                binding.dogOwnerNameView.binding.itemEdit.setText(dogUser.getUserName());
                binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);

                binding.dogOwnerIDCardView.binding.itemEdit.setText(dogUser.getIdNum());
                binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);


                try {
                    //法人证件照（全）
                    List<String> idPhotos = new Gson().fromJson(dogUser.getIdPhoto(), new TypeToken<List<String>>() {
                    }.getType());
                    if (idPhotos.size() > 0) {
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.legalPersonIDCardFrontView, 6);
                        legalPersonIDCardFront = idPhotos.get(0);
                    }
                    if (idPhotos.size() > 1) {
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.legalPersonIDCardBackView, 6);
                        legalPersonIDCardBack = idPhotos.get(1);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                //居住地址（全）例：012/02/31
//                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
                updateAddressView(binding.addressView.binding.itemContent, dogUser.getAddress());
                //所属小区
                binding.communityAddressView.binding.itemContent.setText(dogUser.getVillageName());
                //详细地址（全）
                binding.detailedAddressView.setText(dogUser.getDetailedAddress());

                //养犬管理制度（单位）
                GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, dogUser.getDogManagement(), binding.managementSystemView, 6);
                try {
                    //养犬设施图片（单位）
                    List<String> idPhotos = new Gson().fromJson(dogUser.getDogDevice(), new TypeToken<List<String>>() {
                    }.getType());
                    if (idPhotos.size() > 0) {
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.facility1View, 6);
                        legalFacility1 = idPhotos.get(0);
                    }
                    if (idPhotos.size() > 1) {
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.facility2View, 6);
                        legalFacility2 = idPhotos.get(1);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        }
    }

    /**
     * 个人办理
     */
    private void initPersonal() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.VISIBLE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogTypeContainer.setVisibility(View.VISIBLE);
        if (binding.radioGroupDogType.getCheckedRadioButtonId() == R.id.radioButtonOldMan) {
            binding.oldManContainer.setVisibility(View.VISIBLE);
        } else {
            binding.oldManContainer.setVisibility(View.GONE);
        }
//        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);
        if (type == type_immune) {
            binding.houseNumberView.setVisibility(View.GONE);
            binding.houseProprietaryCertificateContainer.setVisibility(View.GONE);
        } else {
            binding.houseNumberView.setVisibility(View.VISIBLE);
            binding.houseProprietaryCertificateContainer.setVisibility(View.VISIBLE);
        }
        binding.houseNumberView.binding.itemEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});

        binding.organNameView.setVisibility(View.GONE);
        binding.organCertificateContainer.setVisibility(View.GONE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.GONE);
        binding.facilityContainer.setVisibility(View.GONE);
        binding.managementSystemContainer.setVisibility(View.GONE);

        binding.dogOwnerNameView.binding.itemTitle.setText("犬主姓名");
        binding.dogOwnerNameView.binding.itemEdit.setHint("请输入犬主姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("身份证号码");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("请输入身份证号码");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("居住地址");

        binding.radioGroupIDCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonHaiWai:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("上传外国护照");

                        break;
                    case R.id.radioButtonGanGao:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("上传港澳通信证");

                        break;
                    case R.id.radioButtonIDCard:
                        binding.IDCardBackContainer.setVisibility(View.VISIBLE);
                        binding.IDCardFrontHintView.setText("上传身份证人像面");

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupDogType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan://陪伴犬
                        binding.oldManContainer.setVisibility(View.VISIBLE);
                        binding.oldManOrDisabledCertificateHintView.setText("鳏寡老人证明");
                        binding.oldManOrDisabledCertificateUploadHintView.setText("上传鳏寡老人证明");
                        binding.oldManOrDisabledCertificateContentUploadHintView.setText("上传鳏寡老人证明");
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificate, binding.oldManOrDisabledCertificateView, 6);
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificateContent, binding.oldManOrDisabledCertificateContentView, 6);

                        int oldManCheckedRadioButtonId = binding.radioGroupOldMan.getCheckedRadioButtonId();
                        if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//是
                            binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);

                        } else if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan0) {//否
                            binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);

                        }

                        break;
                    case R.id.radioButtonDisabled://导盲犬/扶助犬
                        binding.oldManContainer.setVisibility(View.GONE);
                        binding.oldManOrDisabledCertificateHintView.setText("残疾人证");
                        binding.oldManOrDisabledCertificateUploadHintView.setText("上传残疾人证");
                        binding.oldManOrDisabledCertificateContentUploadHintView.setText("上传残疾人证");
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificate, binding.oldManOrDisabledCertificateView, 6);
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificateContent, binding.oldManOrDisabledCertificateContentView, 6);
                        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupOldMan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan0:
                        binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);

                        break;
                    case R.id.radioButtonOldMan1:
                        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 单位办理
     */
    private void initOrganView() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.GONE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.GONE);
        binding.dogTypeContainer.setVisibility(View.GONE);
        binding.oldManContainer.setVisibility(View.GONE);
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);
        binding.houseNumberView.setVisibility(View.GONE);
        binding.houseProprietaryCertificateContainer.setVisibility(View.GONE);

        binding.organNameView.setVisibility(View.VISIBLE);
        binding.organCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.VISIBLE);
        binding.facilityContainer.setVisibility(View.VISIBLE);
        binding.managementSystemContainer.setVisibility(View.VISIBLE);

        binding.dogOwnerNameView.binding.itemTitle.setText("法人姓名");
        binding.dogOwnerNameView.binding.itemEdit.setHint("请输入法人姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("法人身份证号码");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("请输入法人身份证号码");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("养犬地址");


    }

    //个人
    private String personalIDCardFront = null;
    private String personalIDCardBack = null;
    private int dogType = 1;
    private String oldManOrDisabledCertificate = null;
    private String oldManOrDisabledCertificateContent = null;

    //单位
    private String legalPersonIDCardFront = null;
    private String legalPersonIDCardBack = null;
    private String legalFacility1 = null;
    private String legalFacility2 = null;


    /**
     * userType
     * integer
     * 用户类型（全）;1：个人 2 ：单位
     * userName
     * string
     * 法人姓名（单位）
     * idType
     * integer
     * 证件类型（全）;1:身份证 2 营业执照
     * idPhoto
     * string
     * 证件照片/法人证件照（全）
     * idNum
     * string
     * 身份证号（个人）/法人身份证号
     * dogType
     * integer
     * 养犬类型（个人）;1导盲犬/扶助犬 2 陪伴犬
     * agedProve
     * string
     * 鳏寡老人证明（个人）
     * aged
     * integer
     * 是否鳏寡老人（个人）;0：否 1：是
     * contactPhoneNum
     * string
     * 联系电话（全）
     * address
     * string
     * 居住地址（全）例：012/02/31
     * detailedAddress
     * string
     * 详细地址（全）
     * houseNum
     * string
     * 房本编号（个人）
     * housePhoto
     * string
     * 房产证或租赁合同照片（个人）
     * bizLicense
     * string
     * 单位营业执照（单位）
     * dogManagement
     * string
     * 养犬管理制度（单位）
     * dogDevice
     * string
     * 养犬设施图片（单位）
     * orgName
     * string
     * 个人/企业名称（全）
     * busTypeId
     * string
     * 业务类型 1 个人信息 0 犬证、疫苗
     *
     * @param view
     */
    public void onClickConfirm(View view) {

        if (type == type_userInfo || type == type_certificate || type == type_immune || type == type_examined || type == type_adoption) {

            Map<String, String> map = new HashMap<>();

            //犬主类型
            int checkedRadioButtonId = binding.radioGroupDogOwner.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.radioButtonOrgan) {
                //单位办理
                String orgName = binding.organNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(orgName)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入单位名称");
                    return;
                } else {
                    dogUser.setOrgName(orgName);
                }

                if (CommonUtil.isBlank(dogUser.getBizLicense())) {
                    ToastUtils.showShort(getApplicationContext(), "请上传单位营业执照");
                    return;
                }

                String dogOwnerName = binding.dogOwnerNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(dogOwnerName)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入法人姓名");
                    return;
                } else {
                    dogUser.setUserName(dogOwnerName);
                }

                String idNum = binding.dogOwnerIDCardView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(idNum)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入法人身份证号码");
                    return;
                } else {
                    dogUser.setIdNum(idNum);
                }

                if (CommonUtil.isBlank(legalPersonIDCardFront)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传身份证人像面");
                    return;
                }

                if (CommonUtil.isBlank(legalPersonIDCardBack)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传身份证国徽面");
                    return;
                }

                if (CommonUtil.isBlank(dogUser.getAddress())) {
                    ToastUtils.showShort(getApplicationContext(), "请选择省市区");
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

                if (CommonUtil.isBlank(dogUser.getDogManagement())) {
                    ToastUtils.showShort(getApplicationContext(), "请上传管理制度文件照片");
                    return;
                }

                if (CommonUtil.isBlank(legalFacility1)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传养犬设施图片");
                    return;
                }

                if (CommonUtil.isBlank(legalFacility2)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传养犬设施图片");
                    return;
                }

                map.put("userType", String.valueOf(2));//用户类型（全）;1：个人 2 ：单位
                map.put("idType", String.valueOf(2));//证件类型（全）;1:身份证 2 营业执照
                map.put("orgName", dogUser.getOrgName());//企业名称
                map.put("bizLicense", dogUser.getBizLicense());//单位营业执照（单位）
                map.put("userName", dogUser.getUserName());//法人姓名
                map.put("idNum", dogUser.getIdNum());//法人身份证号
                map.put("idPhoto", GsonUtils.toJson(Arrays.asList(legalPersonIDCardFront, legalPersonIDCardBack)));//法人证件照（全）
//                map.put("idPhoto", legalPersonIDCardFront);//法人证件照（全）
//                map.put("idPhoto", legalPersonIDCardBack);//法人证件照（全）
                map.put("address", dogUser.getAddress());//居住地址
                map.put("detailedAddress", dogUser.getDetailedAddress());//详细地址
                map.put("dogManagement", dogUser.getDogManagement());//养犬管理制度
                map.put("dogDevice", GsonUtils.toJson(Arrays.asList(legalFacility1, legalFacility2)));//养犬设施图片
//                map.put("dogDevice", legalFacility1);//养犬设施图片

            } else if (checkedRadioButtonId == R.id.radioButtonPersonal) {
                //个人办理
                int IDCardCheckedRadioButtonId = binding.radioGroupIDCard.getCheckedRadioButtonId();
                if (IDCardCheckedRadioButtonId == R.id.radioButtonIDCard) {//身份证
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传身份证人像面");
                        return;
                    }
                    if (CommonUtil.isBlank(personalIDCardBack)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传身份证国徽面");
                        return;
                    }
                } else if (IDCardCheckedRadioButtonId == R.id.radioButtonGanGao) {//港澳通信证
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传港澳通信证");
                        return;
                    }

                } else if (IDCardCheckedRadioButtonId == R.id.radioButtonHaiWai) {//外国护照
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传外国护照");
                        return;
                    }

                }

                String orgName = binding.dogOwnerNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(orgName)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入犬主姓名");
                    return;
                } else {
                    dogUser.setOrgName(orgName);
                }

                String idNum = binding.dogOwnerIDCardView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(idNum)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入身份证号码");
                    return;
                } else {
                    dogUser.setIdNum(idNum);
                }

                //养犬类型（个人）;1导盲犬/扶助犬 2 陪伴犬
                int dogTypeCheckedRadioButtonId = binding.radioGroupDogType.getCheckedRadioButtonId();
                //是否为鳏寡老人
                int oldManCheckedRadioButtonId = binding.radioGroupOldMan.getCheckedRadioButtonId();
                if (dogTypeCheckedRadioButtonId == R.id.radioButtonOldMan) {//2 陪伴犬
                    dogType = 2;
                    if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//是
                        if (CommonUtil.isBlank(oldManOrDisabledCertificate) || CommonUtil.isBlank(oldManOrDisabledCertificateContent)) {
                            ToastUtils.showShort(getApplicationContext(), "请上传鳏寡老人证明");
                            return;
                        }

                    } else if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan0) {//否


                    }

                } else if (dogTypeCheckedRadioButtonId == R.id.radioButtonDisabled) {//1导盲犬/扶助犬
                    dogType = 1;
                    if (CommonUtil.isBlank(oldManOrDisabledCertificate) || CommonUtil.isBlank(oldManOrDisabledCertificateContent)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传残疾人证");
                        return;
                    }

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

                List<String> housePhotos = new ArrayList<>();
                if (type != type_immune) {
                    String personaHouseNumber = binding.houseNumberView.binding.itemEdit.getText().toString();
                    if (CommonUtil.isBlank(personaHouseNumber)) {
                        ToastUtils.showShort(getApplicationContext(), "请输入房本编号");
                        return;
                    } else {
                        dogUser.setHouseNum(personaHouseNumber);
                    }

                    if (imageList.size() <= 1) {
                        ToastUtils.showShort(getApplicationContext(), "请上传房产证或房屋租赁合同");
                        return;
                    }
//                    if (imageList.size() > 0 && imageList.indexOf("add") != -1) {
//                        imageList.remove("add");
//                    }

                    for (String url : imageList) {
                        if (!CommonUtil.isBlank(url) && !url.equals("add")) {
                            housePhotos.add(url);
                        }
                    }

//                    if (CommonUtil.isBlank(dogUser.getHousePhoto())) {
//                        ToastUtils.showShort(getApplicationContext(), "请上传房产证或房屋租赁合同");
//                        return;
//                    }
                }

                map.put("userType", String.valueOf(1));//用户类型（全）;1：个人 2 ：单位
                map.put("idType", String.valueOf(1));//证件类型（全）;1:身份证 2 营业执照
                map.put("idPhoto", GsonUtils.toJson(Arrays.asList(personalIDCardFront, personalIDCardBack)));//证件照片
//                map.put("idPhoto", personalIDCardBack);//证件照片
                map.put("orgName", dogUser.getOrgName());//个人名称
                map.put("idNum", dogUser.getIdNum());//身份证号
                map.put("dogType", String.valueOf(dogType));//养犬类型（个人）;1导盲犬/扶助犬 2 陪伴犬
                if (dogType == 1) {//1导盲犬/扶助犬
//                    map.put("agedProve", dogUser.getAgedProve());
                    map.put("agedProve", GsonUtils.toJson(Arrays.asList(oldManOrDisabledCertificate, oldManOrDisabledCertificateContent)));
                } else if (dogType == 2) {//2 陪伴犬
                    if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//是
//                        map.put("agedProve", dogUser.getAgedProve());//鳏寡老人证明（个人）
                        map.put("agedProve", GsonUtils.toJson(Arrays.asList(oldManOrDisabledCertificate, oldManOrDisabledCertificateContent)));//鳏寡老人证明（个人）
                    }
                    map.put("aged", oldManCheckedRadioButtonId == R.id.radioButtonOldMan1 ? "1" : "0");//是否鳏寡老人（个人）;0：否 1：是
                }
                map.put("address", dogUser.getAddress());//居住地址
                map.put("detailedAddress", dogUser.getDetailedAddress());//详细地址
                if (type != type_immune) {
                    map.put("houseNum", dogUser.getHouseNum());//房本编号
                    map.put("housePhoto", GsonUtils.toJson(housePhotos));//房产证或租赁合同照片
//                    map.put("housePhoto", dogUser.getHousePhoto());//房产证或租赁合同照片
                }

            }

            map.put("communityDept", dogUser.getCommunityDept() + "");//社区所属机构（新增）
            map.put("villageId", dogUser.getVillageId() + "");//社区id

            if (type == type_certificate || type == type_immune || type == type_examined || type == type_adoption) {
                map.put("busTypeId", String.valueOf(0));//业务类型 1 个人信息 0 犬证、疫苗

            } else if (type == type_userInfo) {
                map.put("busTypeId", String.valueOf(1));//业务类型 1 个人信息 0 犬证、疫苗

            }

            if (type == type_adoption) {
                map.put("leaveId", String.valueOf(leaveId));//领养id
            }

            if (type == type_adoption) {
                SendRequest.saveLeaveDogUser(map, new GenericsCallback<ResultClient<DogDetail>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(DogCertificateEditDogOwnerActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<DogDetail> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            response.getData().setLeaveId(leaveId);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("dogDetail", response.getData());
                            openActivity(DogAdoptionSubmitActivity.class, bundle);

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMsg());
                        }
                    }
                });
            } else {
                SendRequest.editDogUser(map, new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(DogCertificateEditDogOwnerActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<DogUser> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", type);
                            if (type == type_certificate || type == type_immune || type == type_examined) {
                                bundle.putInt("addressId", response.getData().getAddressId());
                                if (type == type_examined) {
                                    bundle.putString("paperLicence", paperLicence);
                                    bundle.putString("paperImmuneLicence", paperImmuneLicence);
                                }
                                openActivity(DogCertificateEditDogActivity.class, bundle);

                            } else if (type == type_adoption) {
                                bundle.putInt("leaveId", leaveId);
                                bundle.putInt("addressId", response.getData().getAddressId());
                                openActivity(DogAdoptionSubmitActivity.class);


                            } else if (type == type_userInfo) {
                                ToastUtils.showShort(getApplicationContext(), "保存成功");
                                setResult(RESULT_OK);
                                finish();

                            }
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMsg());
                        }
                    }
                });
            }

        }

    }


    private final int request_IDCardFront = 100;
    private final int request_IDCardBack = 200;
    private final int request_LegalPersonIDCardFront = 300;
    private final int request_LegalPersonIDCardBack = 400;
    private final int request_BusinessLicense = 500;
    private final int request_OldManOrDisabledCertificate = 600;
    private final int request_OldManOrDisabledCertificateContent = 1200;
    private final int request_HouseProprietaryCertificate = 700;
    private final int request_ManagementSystem = 800;
    private final int request_Facility1 = 900;
    private final int request_Facility2 = 1000;
    private final int request_City = 1100;
    private final int request_Update = 1300;

    /**
     * 个人证件 上传身份证人像面
     *
     * @param view
     */
    public void onClickIDCardFront(View view) {
        if (!isEnabled()) {
            return;
        }
        if (type == type_userInfo) {
            if (checkPermissions(PermissionUtils.CAMERA, request_IDCardFront)) {

                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtils.createTempFile(System.currentTimeMillis() + ".jpg").getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, request_IDCardFront);

//                int type = JCameraView.BUTTON_STATE_ONLY_CAPTURE;
//                int minTime = 0;
//                int maxTime = 60;
//                CameraActivity.startCameraActivity(DogCertificateEditDogOwnerActivity.this, minTime, maxTime, "#44bf19", type, request_IDCardFront);

            }
        }
//        if (type == type_userInfo) {
//            if (checkPermissions(PermissionUtils.STORAGE, request_IDCardFront)) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
//                bundle.putInt("maxNumber", 1);
//                openActivity(MediaSelectActivity.class, bundle, request_IDCardFront);
//            }
//        }
    }

    /**
     * 个人证件 上传身份证国徽面
     *
     * @param view
     */
    public void onClickIDCardBack(View view) {
        if (!isEnabled()) {
            return;
        }
        if (type == type_userInfo) {
            if (type == type_userInfo) {
                if (checkPermissions(PermissionUtils.CAMERA, request_IDCardBack)) {

                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtils.createTempFile(System.currentTimeMillis() + ".jpg").getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                    startActivityForResult(intent, request_IDCardBack);

//                    int type = JCameraView.BUTTON_STATE_ONLY_CAPTURE;
//                    int minTime = 0;
//                    int maxTime = 60;
//                    CameraActivity.startCameraActivity(DogCertificateEditDogOwnerActivity.this, minTime, maxTime, "#44bf19", type, request_IDCardBack);

                }
            }

//            if (checkPermissions(PermissionUtils.STORAGE, request_IDCardBack)) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
//                bundle.putInt("maxNumber", 1);
//                openActivity(MediaSelectActivity.class, bundle, request_IDCardBack);
//            }
        }
    }

    /**
     * 法人证件 上传身份证人像面
     *
     * @param view
     */
    public void onClickLegalPersonIDCardFront(View view) {
        if (!isEnabled()) {
            return;
        }

        if (checkPermissions(PermissionUtils.CAMERA, request_LegalPersonIDCardFront)) {

            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtils.createTempFile(System.currentTimeMillis() + ".jpg").getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
            startActivityForResult(intent, request_LegalPersonIDCardFront);

//            int type = JCameraView.BUTTON_STATE_ONLY_CAPTURE;
//            int minTime = 0;
//            int maxTime = 60;
//            CameraActivity.startCameraActivity(DogCertificateEditDogOwnerActivity.this, minTime, maxTime, "#44bf19", type, request_LegalPersonIDCardFront);

        }

//        if (checkPermissions(PermissionUtils.STORAGE, request_LegalPersonIDCardFront)) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
//            bundle.putInt("maxNumber", 1);
//            openActivity(MediaSelectActivity.class, bundle, request_LegalPersonIDCardFront);
//        }
    }

    /**
     * 法人证件 上传身份证国徽面
     *
     * @param view
     */
    public void onClickLegalPersonIDCardBack(View view) {
        if (!isEnabled()) {
            return;
        }

        if (checkPermissions(PermissionUtils.CAMERA, request_LegalPersonIDCardBack)) {

            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtils.createTempFile(System.currentTimeMillis() + ".jpg").getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
            startActivityForResult(intent, request_LegalPersonIDCardBack);

//            int type = JCameraView.BUTTON_STATE_ONLY_CAPTURE;
//            int minTime = 0;
//            int maxTime = 60;
//            CameraActivity.startCameraActivity(DogCertificateEditDogOwnerActivity.this, minTime, maxTime, "#44bf19", type, request_LegalPersonIDCardBack);

        }

//        if (checkPermissions(PermissionUtils.STORAGE, request_LegalPersonIDCardBack)) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
//            bundle.putInt("maxNumber", 1);
//            openActivity(MediaSelectActivity.class, bundle, request_LegalPersonIDCardBack);
//        }
    }

    /**
     * 上传单位营业执照
     *
     * @param view
     */
    public void onClickBusinessLicense(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_BusinessLicense)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_BusinessLicense);
        }
    }

    /**
     * 上传残疾人证或鳏寡老人证明
     *
     * @param view
     */
    public void onClickOldManOrDisabledCertificate(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_OldManOrDisabledCertificate)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_OldManOrDisabledCertificate);
        }
    }

    /**
     * 上传残疾人证或鳏寡老人证明
     *
     * @param view
     */
    public void onClickOldManOrDisabledCertificateContent(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_OldManOrDisabledCertificateContent)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_OldManOrDisabledCertificateContent);
        }
    }

    /**
     * 上传房产证或房屋租赁合同
     */
    public void onClickHouseProprietaryCertificate() {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_HouseProprietaryCertificate)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 5 - imageAdapter.getList().size());
            openActivity(MediaSelectActivity.class, bundle, request_HouseProprietaryCertificate);
        }
    }

    /**
     * 上传管理制度文件照片
     *
     * @param view
     */
    public void onClickManagementSystem(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_ManagementSystem)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_ManagementSystem);
        }
    }

    /**
     * 上传养犬设施1图片
     *
     * @param view
     */
    public void onClickFacility1(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_Facility1)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Facility1);
        }
    }

    /**
     * 上传养犬设施2图片
     *
     * @param view
     */
    public void onClickFacility2(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_Facility2)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Facility2);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_IDCardFront:
                    compressImage(data, request_IDCardFront);

                    break;
                case request_IDCardBack:
                    compressImage(data, request_IDCardBack);

                    break;
                case request_LegalPersonIDCardFront:
                    compressImage(data, request_LegalPersonIDCardFront);

                    break;
                case request_LegalPersonIDCardBack:
                    compressImage(data, request_LegalPersonIDCardBack);

                    break;
                case request_BusinessLicense:
                    compressImage(data, request_BusinessLicense);

                    break;
                case request_OldManOrDisabledCertificate:
                    compressImage(data, request_OldManOrDisabledCertificate);

                    break;
                case request_OldManOrDisabledCertificateContent:
                    compressImage(data, request_OldManOrDisabledCertificateContent);

                    break;
                case request_HouseProprietaryCertificate:
                    compressImageMulti(data, request_HouseProprietaryCertificate);

                    break;
                case request_ManagementSystem:
                    compressImage(data, request_ManagementSystem);

                    break;
                case request_Facility1:
                    compressImage(data, request_Facility1);

                    break;
                case request_Facility2:
                    compressImage(data, request_Facility2);

                    break;
                case request_City:
                    if (data != null) {
                        String cityName = data.getStringExtra("cityName");
                        dogUser.setAddress(cityName);
                        binding.addressView.binding.itemContent.setText(cityName);
                    }
                    break;
                case request_Update:
                    checkingDogUser();

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
                                        try {
                                            if (requestCode == request_IDCardFront ||
                                                    requestCode == request_IDCardBack ||
                                                    requestCode == request_LegalPersonIDCardFront ||
                                                    requestCode == request_LegalPersonIDCardBack) {
                                                SendRequest.huaweiCloudIdCard(CommonUtil.encodeBase64File(file.getAbsolutePath()),
                                                        new GenericsCallback<IdCard>(new JsonGenericsSerializator()) {
                                                            @Override
                                                            public void onError(Call call, Exception e, int id) {
                                                                Log.i(TAG, "onError: " + e.getMessage());
                                                                ToastUtils.showShort(getApplicationContext(), "识别有误,请重新请求");
                                                            }

                                                            @Override
                                                            public void onResponse(IdCard response, int id) {
                                                                Log.i(TAG, "onResponse: " + GsonUtils.toJson(requestCode));
                                                                if (response != null) {
                                                                    if (response != null && response.getResult() != null) {

                                                                        if (requestCode == request_IDCardFront ||
                                                                                requestCode == request_LegalPersonIDCardFront) {
                                                                            String name = response.getResult().getName();
                                                                            binding.dogOwnerNameView.binding.itemEdit.setText(name);
                                                                            dogUser.setUserName(name);
                                                                            dogUser.setOrgName(name);
                                                                            String number = response.getResult().getNumber();
                                                                            binding.dogOwnerIDCardView.binding.itemEdit.setText(number);
                                                                            dogUser.setIdNum(number);
                                                                        }

                                                                        new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                uploadFile(requestCode, file.getAbsolutePath());
                                                                            }
                                                                        }).start();

                                                                    } else if (CommonUtil.isBlank(response.getError_code()) &&
                                                                            response.getError_code().equals("AIS.0101")) {
                                                                        ToastUtils.showShort(getApplicationContext(), "输入参数无效");

                                                                    } else if (CommonUtil.isBlank(response.getError_code()) &&
                                                                            response.getError_code().equals("AIS.0103")) {
                                                                        ToastUtils.showShort(getApplicationContext(), "图像尺寸不符合要求");

                                                                    } else if (CommonUtil.isBlank(response.getError_code()) &&
                                                                            response.getError_code().equals("AIS.0104")) {
                                                                        ToastUtils.showShort(getApplicationContext(), "图像不支持或图像质量较差");

                                                                    } else {
                                                                        ToastUtils.showShort(getApplicationContext(), "识别有误,请重新请求");
                                                                    }
                                                                } else {
                                                                    ToastUtils.showShort(getApplicationContext(), "识别有误,请重新请求");
                                                                }
                                                            }
                                                        });
                                            } else {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        uploadFile(requestCode, file.getAbsolutePath());
                                                    }
                                                }).start();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
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
     * @param requestCode
     * @param filePath
     */
    private void uploadFile(int requestCode, String filePath) {
        try {
            LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this, "上传中...");
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
            LoadingManager.hideLoadingDialog(DogCertificateEditDogOwnerActivity.this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (requestCode == request_IDCardFront) {
                        personalIDCardFront = url;
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, personalIDCardFront, binding.IDCardFrontView, 6);

                    } else if (requestCode == request_IDCardBack) {
                        personalIDCardBack = url;
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, personalIDCardBack, binding.IDCardBackView, 6);

                    } else if (requestCode == request_LegalPersonIDCardFront) {
                        legalPersonIDCardFront = url;
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, legalPersonIDCardFront, binding.legalPersonIDCardFrontView, 6);

                    } else if (requestCode == request_LegalPersonIDCardBack) {
                        legalPersonIDCardBack = url;
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, legalPersonIDCardBack, binding.legalPersonIDCardBackView, 6);

                    } else if (requestCode == request_BusinessLicense) {
                        String businessLicense = url;
                        dogUser.setBizLicense(businessLicense);
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, businessLicense, binding.businessLicenseView, 6);

                    } else if (requestCode == request_OldManOrDisabledCertificate) {
                        oldManOrDisabledCertificate = url;
//                    dogUser.setAgedProve(oldManOrDisabledCertificate);
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificate, binding.oldManOrDisabledCertificateView, 6);

                    } else if (requestCode == request_OldManOrDisabledCertificateContent) {
                        oldManOrDisabledCertificateContent = url;
//                    dogUser.setAgedProve(oldManOrDisabledCertificate);
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificateContent, binding.oldManOrDisabledCertificateContentView, 6);

                    } else if (requestCode == request_HouseProprietaryCertificate) {
//                    String personaHouseProprietaryCertificate = url;
//                    dogUser.setHousePhoto(personaHouseProprietaryCertificate);
//                    GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, personaHouseProprietaryCertificate, binding.houseProprietaryCertificateView, 6);

                    } else if (requestCode == request_ManagementSystem) {
                        String legalManagementSystem = url;
                        dogUser.setDogManagement(legalManagementSystem);
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, legalManagementSystem, binding.managementSystemView, 6);

                    } else if (requestCode == request_Facility1) {
                        legalFacility1 = url;
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, legalFacility1, binding.facility1View, 6);

                    } else if (requestCode == request_Facility2) {
                        legalFacility2 = url;
                        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, legalFacility2, binding.facility2View, 6);

                    }

                }
            });
        } catch (Exception e) {
            ToastUtils.showShort(getApplication(), "上传失败");
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
            LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this, "上传中...");
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
                LoadingManager.hideLoadingDialog(DogCertificateEditDogOwnerActivity.this);
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


    private boolean isEnabled() {
        if (type == type_details) {
            return false;
        } else {
            return true;
        }
    }

    public void onClickShowHouse(View view) {
        showHouseImageView();
    }

    public void showHouseImageView() {
        AlertDialog dialog = new AlertDialog.Builder(DogCertificateEditDogOwnerActivity.this, AlertDialog.THEME_HOLO_DARK).create();
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
        GlideLoader.LoaderHouse(DogCertificateEditDogOwnerActivity.this,imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

}