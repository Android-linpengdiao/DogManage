package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogTransferBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.PunishRecord;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 犬只过户
 */
public class DogTransferActivity extends BaseActivity {

    private ActivityDogTransferBinding binding;
    private List<String> dogList = Arrays.asList("萨摩耶", "柯基", "泰迪", "哈士奇");
    private Dog dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_transfer);
        addActivity(this);

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogTransferActivity.this, null, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
//                        String content = (String) object;
//                        dogCertificate = dogList.indexOf(content);
//                        binding.dogCertificateView.binding.itemContent.setText(content);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
            }
        });

        loadData(true);
    }

    private Pager<PunishRecord> pager = new Pager<>();

//    private void setRefresh() {
//        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                pager = new Pager<>();
//                loadData(true);
//            }
//        });
//        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                loadData(false);
//
//            }
//        });
//        binding.refreshLayout.autoRefresh();
//
//    }

    public void loadData(boolean isRefresh) {
        SendRequest.saveCancelDogInfo(pager.getCursor(), pager.getSize(),
                new GenericsCallback<Pager<PunishRecord>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
//                        if (isRefresh) {
//                            binding.refreshLayout.finishRefresh();
//                        } else {
//                            binding.refreshLayout.finishLoadMore();
//                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        if (isRefresh) {
//                            binding.refreshLayout.finishRefresh(false);
//                        } else {
//                            binding.refreshLayout.finishLoadMore(false);
//                        }
                        ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                    }

                    @Override
                    public void onResponse(Pager<PunishRecord> response, int id) {
//                        pager = response;
//                        if (response != null && response.getRows() != null) {
//                            if (isRefresh) {
//                                adapter.refreshData(response.getRows());
//                            } else {
//                                adapter.loadMoreData(response.getRows());
//                                if (adapter.getList().size() < response.getTotal()) {
//                                    pager.setCursor(pager.getCursor() + 1);
//                                }
//                            }
//                            if (adapter.getList().size() == response.getTotal()) {
//                                binding.refreshLayout.setNoMoreData(true);
//                            }
//                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
//                            binding.emptyView.setText("暂无犬只～");
//                        } else {
//                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
//                        }
                    }
                });

    }

    public void onClickConfirm(View view) {
        if (dogDetail == null) {
            ToastUtils.showShort(getApplicationContext(), "请选择犬只");
            return;
        }

        String dogOwnerName = binding.dogOwnerName.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogOwnerName)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新犬主的姓名");
            return;
        }
        String dogOwnerPhone = binding.dogOwnerPhoneView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogOwnerPhone)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新犬主的手机号码");
            return;
        }

        SendRequest.saveTransferDog(dogDetail.getLincenceId(), dogOwnerName, dogOwnerPhone,
                new GenericsCallback<ResultClient<BaseData>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<BaseData> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", SubmitSuccessActivity.type_transfer);
                            openActivity(SubmitSuccessActivity.class, bundle);

                            finishActivity(DogManageWorkflowActivity.class);
                            finish();

                        } else {
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");

                        }
                    }
                });
    }
}