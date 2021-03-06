package com.dog.manage.app.activity;


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
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.AreaSelectAdapter;
import com.dog.manage.app.adapter.CommunitySelectAdapter;
import com.dog.manage.app.adapter.ImageAdapter;
import com.dog.manage.app.area.CityData;
import com.dog.manage.app.area.CityManager;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogOwnerBinding;
import com.dog.manage.app.databinding.DialogAddressBinding;
import com.dog.manage.app.databinding.DialogCommunityBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.AddressBean;
import com.dog.manage.app.model.CommunityBean;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.DogUser;
import com.dog.manage.app.model.PunishRecord;
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
 * ????????????
 */
public class DogCertificateEditDogOwnerActivity extends BaseActivity {

    private ActivityDogCertificateEditDogOwnerBinding binding;

    public static final int type_userInfo = 0;//????????????
    public static final int type_certificate = 1;//????????????
    public static final int type_immune = 2;//???????????????
    public static final int type_examined = 3;//????????????
    public static final int type_adoption = 4;//????????????
    public static final int type_details = 5;//????????????
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
            binding.titleView.binding.itemTitle.setText("????????????");
            binding.stepContainer.setVisibility(View.GONE);
            binding.confirmView.setText("????????????");
            initPersonal();
            getDogUser();
            getHuaweiCloudAuthTokens();

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("????????????");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");
            initPersonal();
            checkingDogUser();

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("???????????????");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");
            initPersonal();
            checkingDogUser();

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("????????????");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");
            initPersonal();
            checkingDogUser();
            paperLicence = getIntent().getStringExtra("paperLicence");
            paperImmuneLicence = getIntent().getStringExtra("paperImmuneLicence");

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("????????????");
            binding.firstStepView.setSelected(true);
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");
            binding.secondStepView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_10), 0, getResources().getDimensionPixelOffset(R.dimen.dp_10), 0);
            initPersonal();

            leaveId = getIntent().getIntExtra("leaveId", 0);
            checkingDogUser();

        } else if (type == type_details) {
            binding.titleView.binding.itemTitle.setText("????????????");
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
                            String address = addressBean.getAreaName();
                            dogUser.setAddress(address);
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
                            ToastUtils.showShort(getApplication(), "?????????????????????");
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
                            ToastUtils.showShort(DogCertificateEditDogOwnerActivity.this, "????????????9?????????");
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
     * ????????????Token??????
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
            user.put("name", "xingchongwangguo");
            user.put("password", "xingchongwangguo123456");

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
        SendRequest.huaweiCloudAuthTokens(jsonObject.toString(), new GenericsCallback(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }

    private DialogCommunityBinding communityBinding;
    private CommunitySelectAdapter communitySelectAdapter;
    private Pager<CommunityBean> communityPager = new Pager<>();
    private CommunityBean communityBean;

    /**
     * ????????????
     *
     * @param isRefresh
     * @param communityName
     */
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

    /**
     * ????????????
     *
     * @param isRefresh
     */
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
//                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
//                            binding.emptyView.setText("???????????????");
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
                    DialogManager.showConfirmDialog(DogCertificateEditDogOwnerActivity.this, "????????????????????????",
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
     * ?????? ??????????????????
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
                        ToastUtils.showShort(getApplicationContext(), "??????????????????");
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
                }
            }
        });
    }


    private void initDogUserView(DogUser dogUser) {
        binding.certificateContainer.setVisibility(View.VISIBLE);
        if (dogUser.getUserType() != null && (dogUser.getUserType() == DogUser.userType_personal || dogUser.getUserType() == DogUser.userType_organ)) {
            if (dogUser.getUserType() == DogUser.userType_personal) {

                //????????????
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
                //????????????????????????;1?????????/????????? 2 ?????????
                if (dogType == 1) {
                    binding.radioButtonDisabled.setChecked(true);
                    binding.oldManContainer.setVisibility(View.GONE);
                    binding.oldManOrDisabledCertificateHintView.setText("????????????");
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
                    binding.oldManOrDisabledCertificateHintView.setText("??????????????????");
                    //??????????????????????????????;0?????? 1??????
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
                        //??????????????????
                        binding.radioButtonDisabled.setVisibility(dogType == 1 ? View.VISIBLE : View.GONE);
                        binding.radioButtonOldMan.setVisibility(dogType == 2 ? View.VISIBLE : View.GONE);
                        binding.radioButtonOldMan0.setChecked(dogUser.getAged() == 0 ? true : false);
                        binding.radioButtonOldMan0.setVisibility(dogUser.getAged() == 0 ? View.VISIBLE : View.GONE);
                        binding.radioButtonOldMan1.setChecked(dogUser.getAged() == 1 ? true : false);
                        binding.radioButtonOldMan1.setVisibility(dogUser.getAged() == 1 ? View.VISIBLE : View.GONE);

                        //??????????????????
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

                //???????????????????????????012/02/31
                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
                //????????????
                binding.communityAddressView.binding.itemContent.setText(dogUser.getVillageName());
                //?????????????????????
                binding.detailedAddressView.setText(dogUser.getDetailedAddress());

                binding.houseNumberView.binding.itemEdit.setText(dogUser.getHouseNum());
//                GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, dogUser.getHousePhoto(), binding.houseProprietaryCertificateView, 6);
                try {
                    //??????????????????????????????
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

                binding.dogOwnerNameView.binding.itemEdit.setText(dogUser.getOrgName());
                binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);

                binding.dogOwnerIDCardView.binding.itemEdit.setText(dogUser.getIdNum());
                binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);


                try {
                    //????????????????????????
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

                //???????????????????????????012/02/31
                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
                //????????????
                binding.communityAddressView.binding.itemContent.setText(dogUser.getVillageName());
                //?????????????????????
                binding.detailedAddressView.setText(dogUser.getDetailedAddress());

                //??????????????????????????????
                GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, dogUser.getDogManagement(), binding.managementSystemView, 6);
                try {
                    //??????????????????????????????
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
     * ????????????
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

        binding.dogOwnerNameView.binding.itemTitle.setText("????????????");
        binding.dogOwnerNameView.binding.itemEdit.setHint("?????????????????????");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("???????????????");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("????????????????????????");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("????????????");

        binding.radioGroupIDCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonHaiWai:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("??????????????????");

                        break;
                    case R.id.radioButtonGanGao:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("?????????????????????");

                        break;
                    case R.id.radioButtonIDCard:
                        binding.IDCardBackContainer.setVisibility(View.VISIBLE);
                        binding.IDCardFrontHintView.setText("????????????????????????");

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
                    case R.id.radioButtonOldMan://?????????
                        binding.oldManContainer.setVisibility(View.VISIBLE);
                        binding.oldManOrDisabledCertificateHintView.setText("??????????????????");
                        binding.oldManOrDisabledCertificateUploadHintView.setText("????????????????????????");
                        binding.oldManOrDisabledCertificateContentUploadHintView.setText("????????????????????????");
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificate, binding.oldManOrDisabledCertificateView, 6);
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, oldManOrDisabledCertificateContent, binding.oldManOrDisabledCertificateContentView, 6);

                        int oldManCheckedRadioButtonId = binding.radioGroupOldMan.getCheckedRadioButtonId();
                        if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//???
                            binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);

                        } else if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan0) {//???
                            binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);

                        }

                        break;
                    case R.id.radioButtonDisabled://?????????/?????????
                        binding.oldManContainer.setVisibility(View.GONE);
                        binding.oldManOrDisabledCertificateHintView.setText("????????????");
                        binding.oldManOrDisabledCertificateUploadHintView.setText("??????????????????");
                        binding.oldManOrDisabledCertificateContentUploadHintView.setText("??????????????????");
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
     * ????????????
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

        binding.dogOwnerNameView.binding.itemTitle.setText("????????????");
        binding.dogOwnerNameView.binding.itemEdit.setHint("?????????????????????");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("?????????????????????");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("??????????????????????????????");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("????????????");


    }

    //??????
    private String personalIDCardFront = null;
    private String personalIDCardBack = null;
    private int dogType = 1;
    private String oldManOrDisabledCertificate = null;
    private String oldManOrDisabledCertificateContent = null;

    //??????
    private String legalPersonIDCardFront = null;
    private String legalPersonIDCardBack = null;
    private String legalFacility1 = null;
    private String legalFacility2 = null;


    /**
     * userType
     * integer
     * ?????????????????????;1????????? 2 ?????????
     * userName
     * string
     * ????????????????????????
     * idType
     * integer
     * ?????????????????????;1:????????? 2 ????????????
     * idPhoto
     * string
     * ????????????/????????????????????????
     * idNum
     * string
     * ????????????????????????/??????????????????
     * dogType
     * integer
     * ????????????????????????;1?????????/????????? 2 ?????????
     * agedProve
     * string
     * ??????????????????????????????
     * aged
     * integer
     * ??????????????????????????????;0?????? 1??????
     * contactPhoneNum
     * string
     * ?????????????????????
     * address
     * string
     * ???????????????????????????012/02/31
     * detailedAddress
     * string
     * ?????????????????????
     * houseNum
     * string
     * ????????????????????????
     * housePhoto
     * string
     * ??????????????????????????????????????????
     * bizLicense
     * string
     * ??????????????????????????????
     * dogManagement
     * string
     * ??????????????????????????????
     * dogDevice
     * string
     * ??????????????????????????????
     * orgName
     * string
     * ??????/?????????????????????
     * busTypeId
     * string
     * ???????????? 1 ???????????? 0 ???????????????
     *
     * @param view
     */
    public void onClickConfirm(View view) {

        if (type == type_userInfo || type == type_certificate || type == type_immune || type == type_examined || type == type_adoption) {

            Map<String, String> map = new HashMap<>();

            //????????????
            int checkedRadioButtonId = binding.radioGroupDogOwner.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.radioButtonOrgan) {
                //????????????
                String orgName = binding.organNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(orgName)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                } else {
                    dogUser.setOrgName(orgName);
                }

                if (CommonUtil.isBlank(dogUser.getBizLicense())) {
                    ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                    return;
                }

                String dogOwnerName = binding.dogOwnerNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(dogOwnerName)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                } else {
                    dogUser.setUserName(dogOwnerName);
                }

                String idNum = binding.dogOwnerIDCardView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(idNum)) {
                    ToastUtils.showShort(getApplicationContext(), "??????????????????????????????");
                    return;
                } else {
                    dogUser.setIdNum(idNum);
                }

                if (CommonUtil.isBlank(legalPersonIDCardFront)) {
                    ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                    return;
                }

                if (CommonUtil.isBlank(legalPersonIDCardBack)) {
                    ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                    return;
                }

                if (CommonUtil.isBlank(dogUser.getAddress())) {
                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
                    return;
                }

                String communityAddress = binding.communityAddressView.binding.itemContent.getText().toString();
                if (CommonUtil.isBlank(communityAddress)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                }

                String detailedAddress = binding.detailedAddressView.getText().toString();
                if (CommonUtil.isBlank(detailedAddress)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                } else {
                    dogUser.setDetailedAddress(detailedAddress);
                }

                if (CommonUtil.isBlank(dogUser.getDogManagement())) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????????????????");
                    return;
                }

                if (CommonUtil.isBlank(legalFacility1)) {
                    ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                    return;
                }

                if (CommonUtil.isBlank(legalFacility2)) {
                    ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                    return;
                }

                map.put("userType", String.valueOf(2));//?????????????????????;1????????? 2 ?????????
                map.put("idType", String.valueOf(2));//?????????????????????;1:????????? 2 ????????????
                map.put("orgName", dogUser.getOrgName());//????????????
                map.put("bizLicense", dogUser.getBizLicense());//??????????????????????????????
                map.put("userName", dogUser.getUserName());//????????????
                map.put("idNum", dogUser.getIdNum());//??????????????????
                map.put("idPhoto", GsonUtils.toJson(Arrays.asList(legalPersonIDCardFront, legalPersonIDCardBack)));//????????????????????????
//                map.put("idPhoto", legalPersonIDCardFront);//????????????????????????
//                map.put("idPhoto", legalPersonIDCardBack);//????????????????????????
                map.put("address", dogUser.getAddress());//????????????
                map.put("detailedAddress", dogUser.getDetailedAddress());//????????????
                map.put("dogManagement", dogUser.getDogManagement());//??????????????????
                map.put("dogDevice", GsonUtils.toJson(Arrays.asList(legalFacility1, legalFacility2)));//??????????????????
//                map.put("dogDevice", legalFacility1);//??????????????????

            } else if (checkedRadioButtonId == R.id.radioButtonPersonal) {
                //????????????
                int IDCardCheckedRadioButtonId = binding.radioGroupIDCard.getCheckedRadioButtonId();
                if (IDCardCheckedRadioButtonId == R.id.radioButtonIDCard) {//?????????
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                        return;
                    }
                    if (CommonUtil.isBlank(personalIDCardBack)) {
                        ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                        return;
                    }
                } else if (IDCardCheckedRadioButtonId == R.id.radioButtonGanGao) {//???????????????
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "????????????????????????");
                        return;
                    }

                } else if (IDCardCheckedRadioButtonId == R.id.radioButtonHaiWai) {//????????????
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                        return;
                    }

                }

                String orgName = binding.dogOwnerNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(orgName)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                } else {
                    dogUser.setOrgName(orgName);
                }

                String idNum = binding.dogOwnerIDCardView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(idNum)) {
                    ToastUtils.showShort(getApplicationContext(), "????????????????????????");
                    return;
                } else {
                    dogUser.setIdNum(idNum);
                }

                //????????????????????????;1?????????/????????? 2 ?????????
                int dogTypeCheckedRadioButtonId = binding.radioGroupDogType.getCheckedRadioButtonId();
                //?????????????????????
                int oldManCheckedRadioButtonId = binding.radioGroupOldMan.getCheckedRadioButtonId();
                if (dogTypeCheckedRadioButtonId == R.id.radioButtonOldMan) {//2 ?????????
                    dogType = 2;
                    if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//???
                        if (CommonUtil.isBlank(oldManOrDisabledCertificate) || CommonUtil.isBlank(oldManOrDisabledCertificateContent)) {
                            ToastUtils.showShort(getApplicationContext(), "???????????????????????????");
                            return;
                        }

                    } else if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan0) {//???


                    }

                } else if (dogTypeCheckedRadioButtonId == R.id.radioButtonDisabled) {//1?????????/?????????
                    dogType = 1;
                    if (CommonUtil.isBlank(oldManOrDisabledCertificate) || CommonUtil.isBlank(oldManOrDisabledCertificateContent)) {
                        ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                        return;
                    }

                }

                if (CommonUtil.isBlank(dogUser.getAddress())) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                }

                String communityAddress = binding.communityAddressView.binding.itemContent.getText().toString();
                if (CommonUtil.isBlank(communityAddress)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                }

                String detailedAddress = binding.detailedAddressView.getText().toString();
                if (CommonUtil.isBlank(detailedAddress)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                } else {
                    dogUser.setDetailedAddress(detailedAddress);
                }

                if (type != type_immune) {
                    String personaHouseNumber = binding.houseNumberView.binding.itemEdit.getText().toString();
                    if (CommonUtil.isBlank(personaHouseNumber)) {
                        ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                        return;
                    } else {
                        dogUser.setHouseNum(personaHouseNumber);
                    }

                    if (imageList.size() <= 1) {
                        ToastUtils.showShort(getApplicationContext(), "???????????????????????????????????????");
                        return;
                    }
                    if (imageList.size() > 0 && imageList.indexOf("add") != -1) {
                        imageList.remove("add");
                    }

//                    if (CommonUtil.isBlank(dogUser.getHousePhoto())) {
//                        ToastUtils.showShort(getApplicationContext(), "???????????????????????????????????????");
//                        return;
//                    }
                }

                map.put("userType", String.valueOf(1));//?????????????????????;1????????? 2 ?????????
                map.put("idType", String.valueOf(1));//?????????????????????;1:????????? 2 ????????????
                map.put("idPhoto", GsonUtils.toJson(Arrays.asList(personalIDCardFront, personalIDCardBack)));//????????????
//                map.put("idPhoto", personalIDCardBack);//????????????
                map.put("orgName", dogUser.getOrgName());//????????????
                map.put("idNum", dogUser.getIdNum());//????????????
                map.put("dogType", String.valueOf(dogType));//????????????????????????;1?????????/????????? 2 ?????????
                if (dogType == 1) {//1?????????/?????????
//                    map.put("agedProve", dogUser.getAgedProve());
                    map.put("agedProve", GsonUtils.toJson(Arrays.asList(oldManOrDisabledCertificate, oldManOrDisabledCertificateContent)));
                } else if (dogType == 2) {//2 ?????????
                    if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//???
//                        map.put("agedProve", dogUser.getAgedProve());//??????????????????????????????
                        map.put("agedProve", GsonUtils.toJson(Arrays.asList(oldManOrDisabledCertificate, oldManOrDisabledCertificateContent)));//??????????????????????????????
                    }
                    map.put("aged", oldManCheckedRadioButtonId == R.id.radioButtonOldMan1 ? "1" : "0");//??????????????????????????????;0?????? 1??????
                }
                map.put("address", dogUser.getAddress());//????????????
                map.put("detailedAddress", dogUser.getDetailedAddress());//????????????
                if (type != type_immune) {
                    map.put("houseNum", dogUser.getHouseNum());//????????????
                    map.put("housePhoto", GsonUtils.toJson(imageList));//??????????????????????????????
//                    map.put("housePhoto", dogUser.getHousePhoto());//??????????????????????????????
                }

            }

            map.put("communityDept", dogUser.getCommunityDept() + "");//??????????????????????????????
            map.put("villageId", dogUser.getVillageId() + "");//??????id

            if (type == type_certificate || type == type_immune || type == type_examined || type == type_adoption) {
                map.put("busTypeId", String.valueOf(0));//???????????? 1 ???????????? 0 ???????????????

            } else if (type == type_userInfo) {
                map.put("busTypeId", String.valueOf(1));//???????????? 1 ???????????? 0 ???????????????

            }

            if (type == type_adoption) {
                map.put("leaveId", String.valueOf(leaveId));//??????id
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
                                ToastUtils.showShort(getApplicationContext(), "????????????");
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
     * ???????????? ????????????????????????
     *
     * @param view
     */
    public void onClickIDCardFront(View view) {
        if (!isEnabled()) {
            return;
        }
        if (type == type_userInfo) {
            if (checkPermissions(PermissionUtils.STORAGE, request_IDCardFront)) {
                Bundle bundle = new Bundle();
                bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
                bundle.putInt("maxNumber", 1);
                openActivity(MediaSelectActivity.class, bundle, request_IDCardFront);
            }
        }
    }

    /**
     * ???????????? ????????????????????????
     *
     * @param view
     */
    public void onClickIDCardBack(View view) {
        if (!isEnabled()) {
            return;
        }
        if (type == type_userInfo) {
            if (checkPermissions(PermissionUtils.STORAGE, request_IDCardBack)) {
                Bundle bundle = new Bundle();
                bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
                bundle.putInt("maxNumber", 1);
                openActivity(MediaSelectActivity.class, bundle, request_IDCardBack);
            }
        }
    }

    /**
     * ???????????? ????????????????????????
     *
     * @param view
     */
    public void onClickLegalPersonIDCardFront(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_LegalPersonIDCardFront)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_LegalPersonIDCardFront);
        }
    }

    /**
     * ???????????? ????????????????????????
     *
     * @param view
     */
    public void onClickLegalPersonIDCardBack(View view) {
        if (!isEnabled()) {
            return;
        }
        if (checkPermissions(PermissionUtils.STORAGE, request_LegalPersonIDCardBack)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_LegalPersonIDCardBack);
        }
    }

    /**
     * ????????????????????????
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
     * ???????????????????????????????????????
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
     * ???????????????????????????????????????
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
     * ????????????????????????????????????
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
     * ??????????????????????????????
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
     * ??????????????????1??????
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
     * ??????????????????2??????
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
                                .load(path)// ??????????????????????????????
                                .ignoreBy(500)// ??????????????????????????????
                                .setTargetDir(FileUtils.getMediaPath())// ?????????????????????????????????
                                .setCompressListener(new OnCompressListener() { //????????????
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadFile(requestCode, file.getAbsolutePath());
                                            }
                                        }).start();

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                }).launch();//????????????
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
     * @param requestCode
     * @param filePath
     */
    private void uploadFile(int requestCode, String filePath) {
        LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this, "?????????...");
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
                // ????????????????????????
                Log.i(TAG, "uploadFile: AverageSpeed:" + status.getAverageSpeed());
                // ???????????????????????????
                Log.i(TAG, "uploadFile: TransferPercentage:" + status.getTransferPercentage());
            }
        });
        //?????????1MB????????????????????????
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
        LoadingManager.showLoadingDialog(DogCertificateEditDogOwnerActivity.this, "?????????...");
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
            LoadingManager.hideLoadingDialog(DogCertificateEditDogOwnerActivity.this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    private boolean isEnabled() {
        if (type == type_details) {
            return false;
        } else {
            return true;
        }
    }
}