package com.dog.manage.app.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.FrameItemAdapter;
import com.dog.manage.app.databinding.ActivityMainBinding;
import com.dog.manage.app.login.ConfigUtils;
import com.dog.manage.app.model.BannerBean;
import com.dog.manage.app.model.PoliciesBean;
import com.dog.manage.app.utils.GlideBannerLoader;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.xw.banner.BannerConfig;
import com.xw.banner.Transformer;
import com.xw.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_main);
        setStatusBarHeight();
        addActivity(this);
        String registrationID = JPushInterface.getRegistrationID(this);
        Log.i(TAG, "onClickLogin: registrationID = "+registrationID);
        if (checkUserRank(getApplicationContext())) {
            JPushInterface.setAlias(this, getUserInfo().getId(), String.valueOf(getUserInfo().getId()));
        }

        initView();
        noticeList();

        Log.i(TAG, "onCreate: dp_16 = " + getResources().getDimensionPixelSize(R.dimen.dp_14));
        Log.i(TAG, "onCreate: sp_16 = " + getResources().getDimensionPixelSize(R.dimen.dp_14));

    }

    /**
     * http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp
     * http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/ef02820b-5d40-457d-97e2-adb29ccbecba.jpg
     * http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/aaef08a2-e4ee-4802-9875-14755e6b4af8.webp
     * <p>
     * http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/54577243b9b38770.jpg
     */

    @Override
    protected void onResume() {
        if (checkUserRank(getApplicationContext())) {
            binding.topView.setVisibility(View.VISIBLE);
        } else {
            binding.topView.setVisibility(View.GONE);
        }
        binding.topView.setVisibility(View.VISIBLE);
        super.onResume();
    }

    private void initView() {

        intBanner();

        binding.frameItemRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.frameItemRecyclerView.setNestedScrollingEnabled(false);
        FrameItemAdapter frameItemAdapter = new FrameItemAdapter(getApplicationContext());
        binding.frameItemRecyclerView.setAdapter(frameItemAdapter);
        frameItemAdapter.refreshData(Arrays.asList("犬证办理", "免疫证办理", "犬证年审", "犬只过户", "犬只领养", "犬只注销", "信息变更", "办理流程", "政策法规"));
        frameItemAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                int position = (int) object;
                if (position == 7) {
                    openActivity(MainDogManageWorkflowActivity.class);
                } else if (position == 8) {
                    openActivity(PoliciesActivity.class);
                } else {
                    if (checkUserRank(getApplicationContext(), true)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", position);
                        bundle.putString("title", frameItemAdapter.getList().get(position));
                        openActivity(DogManageWorkflowActivity.class, bundle);
                    }
                }

            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
    }

    private void intBanner() {
        binding.mainBanner.setImageLoader(new GlideBannerLoader(0));
        binding.mainBanner.isAutoPlay(true);
        binding.mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.mainBanner.setBannerAnimation(Transformer.Default);
        binding.mainBanner.setIndicatorGravity(BannerConfig.CENTER);
        binding.mainBanner.setDelayTime(5000);
        binding.mainBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });

        binding.banner.setImageLoader(new GlideBannerLoader(8));
        binding.banner.isAutoPlay(true);
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.banner.setBannerAnimation(Transformer.Default);
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setDelayTime(5000);
//        binding.banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                openActivity(AdvertiseActivity.class);
//            }
//        });

        bannerInfoList();
        getForbiddenById();
    }

    public void bannerInfoList() {
        SendRequest.bannerInfoList(0, 20, new GenericsCallback<Pager<BannerBean>>(new JsonGenericsSerializator()) {


            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Pager<BannerBean> response, int id) {
                if (response != null && response.getRows() != null && response.getRows().size() > 0) {
                    List<String> imageList = new ArrayList<>();
                    for (BannerBean bannerBean : response.getRows()) {
                        imageList.add(bannerBean.getImageUrl());
                    }
                    binding.mainBanner.setImages(imageList).start();
                } else {
                    binding.mainBanner.setImages(Arrays.asList("")).start();
                }
            }
        });

    }

    public void getForbiddenById() {
        SendRequest.getForbiddenById(new GenericsCallback<ResultClient<PoliciesBean>>(new JsonGenericsSerializator()) {


            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<PoliciesBean> response, int id) {
                if (response != null && response.getData() != null) {
                    binding.banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("policiesBean",response.getData());
                            openActivity(AdvertiseActivity.class,bundle);
                        }
                    });
                    List<String> imageList = new ArrayList<>();
                    imageList.add(response.getData().getImageUrl());
                    binding.banner.setImages(imageList).start();
//                    try {
//                        List<String> imageList = new Gson().fromJson(response.getData().getImageUrl(), new TypeToken<List<String>>() {
//                        }.getType());
//                        binding.banner.setImages(imageList).start();
//                    } catch (Exception e) {
//                        e.getMessage();
//                    }
                }
            }
        });

    }

    public void noticeList() {
        SendRequest.noticeList(1, 10,
                new GenericsCallback<Pager<PoliciesBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(Pager<PoliciesBean> response, int id) {
                        if (response != null && response.getRows() != null && response.getRows().size() > 0) {
                            StringBuffer buffer = new StringBuffer();
                            for (PoliciesBean policiesBean : response.getRows()) {
                                buffer.append(policiesBean.getNoticeTitle()).append("       ");
                            }
                            binding.noticeView.setText(buffer);
                        }
                    }
                });

    }

    public void onClickMessage(View view) {
        if (checkUserRank(getApplicationContext(), true)) {
            openActivity(CameraActivity.class);

//            openActivity(UpdateDogOwnerInfoActivity.class);
//            if (checkPermissions(PermissionUtils.CAMERA, 100)) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("type", CameraActivity.type_petArchives);
//                openActivity(CameraActivity.class, bundle);
//            }
        }
    }

    public void onClickUser(View view) {
        if (checkUserRank(getApplicationContext(), true)) {
            openActivity(UserHomeActivity.class);
        }

    }


    private static final int requestCode = 1100;
    private String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @AfterPermissionGranted(requestCode)
    private void permissionsManager() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), permissions)) {
            OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(getApplicationContext()), ConfigUtils.getCJSConfig(getApplicationContext()));
            openLoginActivity(true);

        } else {
            EasyPermissions.requestPermissions(this, "请同意下面的权限", requestCode, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * ***************************** 一键登录 **********************************
     */

    private void openLoginActivity(boolean show) {
        //拉取授权页方法
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                Log.i(TAG, "getOpenLoginAuthStatus: " + result);
                if (1000 == code) {
                    //拉起授权页成功
                } else {
                    openActivity(LoginActivity.class);
                    //拉起授权页失败
                    try {
                        JSONObject object = new JSONObject(result);
                        if (show) {
                            ToastUtils.showShort(MainActivity.this, object.optString("innerDesc"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                if (1011 == code) {

                    return;
                } else if (1000 == code) {
                    OneKeyLoginManager.getInstance().finishAuthActivity();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (!CommonUtil.isBlank(jsonObject.optString("token"))) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}