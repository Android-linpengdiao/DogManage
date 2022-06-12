package com.dog.manage.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.BaseData;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.CertificateDetailsActivity;
import com.dog.manage.app.activity.DogLogoutDetailsActivity;
import com.dog.manage.app.activity.ImmuneDetailsActivity;
import com.dog.manage.app.activity.TransferDetailsActivity;
import com.dog.manage.app.activity.record.AdoptionDetailsActivity;
import com.dog.manage.app.activity.record.RecordActivity;
import com.dog.manage.app.adapter.AdoptionRecordAdapter;
import com.dog.manage.app.adapter.CertificateRecordAdapter;
import com.dog.manage.app.adapter.PunishRecordAdapter;
import com.dog.manage.app.databinding.FragmentRecordBinding;
import com.dog.manage.app.model.PunishRecord;
import com.dog.manage.app.model.RecordAdoption;
import com.dog.manage.app.model.RecordImmune;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 记录
 */
public class RecordFragment extends BaseFragment {

    private FragmentRecordBinding binding;

    private int type;
    private int id;

    private AdoptionRecordAdapter adoptionRecordAdapter;
    private CertificateRecordAdapter certificateRecordAdapter;

    public static RecordFragment getInstance(int type, int id) {
        RecordFragment fragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false);

        if (getArguments() != null) {

            type = getArguments().getInt("type");
            id = getArguments().getInt("id");

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerView.setNestedScrollingEnabled(false);
            RecycleViewDivider divider = new RecycleViewDivider(getActivity(),
                    LinearLayoutManager.VERTICAL,
                    CommonUtil.dip2px(getActivity(), 10),
                    Color.parseColor("#FAFAFA"));
            binding.recyclerView.addItemDecoration(divider);

            binding.recyclerView.setNestedScrollingEnabled(false);

            if (type == RecordActivity.type_certificate) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        RecordImmune dataBean = (RecordImmune) object;
                        Bundle bundle = new Bundle();
                        bundle.putInt("lincenceId", dataBean.getLincenceId());
                        openActivity(CertificateDetailsActivity.class, bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });

            } else if (type == RecordActivity.type_immune) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        RecordImmune dataBean = (RecordImmune) object;
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 0);
                        bundle.putInt("immuneId", dataBean.getImmuneId());
                        openActivity(ImmuneDetailsActivity.class, bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });

            } else if (type == RecordActivity.type_transfer) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        RecordImmune dataBean = (RecordImmune) object;
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", dataBean.getId());
                        openActivity(TransferDetailsActivity.class, bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });

            } else if (type == RecordActivity.type_adoption) {
                adoptionRecordAdapter = new AdoptionRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(adoptionRecordAdapter);
                adoptionRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", (Integer) object);
                        openActivity(AdoptionDetailsActivity.class, bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });

            } else if (type == RecordActivity.type_logout) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", DogLogoutDetailsActivity.type_details);
                        bundle.putInt("auditType", (Integer) object);
                        openActivity(DogLogoutDetailsActivity.class, bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
                certificateRecordAdapter.refreshData(Arrays.asList(new RecordImmune(), new RecordImmune(), new RecordImmune(), new RecordImmune(), new RecordImmune(), new RecordImmune(), new RecordImmune(), new RecordImmune(), new RecordImmune()));

            }


        }

        setRefresh();

        return binding.getRoot();
    }

    private Pager<RecordImmune> transferPager = new Pager<>();
    private Pager<RecordAdoption> adoptionPager = new Pager<>();

    private void setRefresh() {
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (type == RecordActivity.type_certificate) {
                    getUserDogLicence();

                } else if (type == RecordActivity.type_immune) {
                    getDogImmuneStatusList();

                } else if (type == RecordActivity.type_transfer) {
                    transferPager = new Pager<>();
                    transferDogList(true);

                } else if (type == RecordActivity.type_adoption) {
                    adoptionPager = new Pager<>();
                    getCancelDogList(true);

                }
            }
        });

        if (type == RecordActivity.type_certificate || type == RecordActivity.type_immune) {
            binding.refreshLayout.setEnableLoadMore(false);

        } else if (type == RecordActivity.type_transfer) {
            binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                    if (type == RecordActivity.type_transfer) {
                        transferDogList(false);

                    } else if (type == RecordActivity.type_adoption) {
                        getCancelDogList(false);

                    }
                }
            });
        }
        binding.refreshLayout.autoRefresh();

    }

    /**
     * 犬证办理记录
     */
    public void getUserDogLicence() {
        SendRequest.getUserDogLicence(id, new GenericsCallback<ResultClient<List<RecordImmune>>>(new JsonGenericsSerializator()) {

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                binding.refreshLayout.finishRefresh();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                binding.refreshLayout.finishRefresh(false);
            }

            @Override
            public void onResponse(ResultClient<List<RecordImmune>> response, int id) {
                if (response != null && response.getData() != null) {
                    certificateRecordAdapter.refreshData(response.getData());
                    binding.emptyView.setVisibility(certificateRecordAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                    binding.emptyView.setText("暂无内容～");
                } else {
                    ToastUtils.showShort(getActivity(), response.getMsg());
                }
            }
        });

    }

    /**
     * 免疫证办理记录列表
     */
    public void getDogImmuneStatusList() {
        SendRequest.getDogImmuneStatusList(id, new GenericsCallback<ResultClient<List<RecordImmune>>>(new JsonGenericsSerializator()) {

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                binding.refreshLayout.finishRefresh();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                binding.refreshLayout.finishRefresh(false);
            }

            @Override
            public void onResponse(ResultClient<List<RecordImmune>> response, int id) {
                if (response != null && response.getData() != null) {
                    certificateRecordAdapter.refreshData(response.getData());
                    binding.emptyView.setVisibility(certificateRecordAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                    binding.emptyView.setText("暂无内容～");
                } else {
                    ToastUtils.showShort(getActivity(), response.getMsg());
                }
            }
        });

    }

    /**
     * 犬只过户-狗证过户列表
     */
    public void transferDogList(boolean isRefresh) {
        SendRequest.transferDogList(id, transferPager.getCursor(), transferPager.getSize(),
                new GenericsCallback<Pager<RecordImmune>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            binding.refreshLayout.finishRefresh();
                        } else {
                            binding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            binding.refreshLayout.finishRefresh(false);
                        } else {
                            binding.refreshLayout.finishLoadMore(false);
                        }
                        ToastUtils.showShort(getActivity(), "获取信息失败");
                    }

                    @Override
                    public void onResponse(Pager<RecordImmune> response, int id) {
                        transferPager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                certificateRecordAdapter.refreshData(response.getRows());
                            } else {
                                certificateRecordAdapter.loadMoreData(response.getRows());
                                if (certificateRecordAdapter.getList().size() < response.getTotal()) {
                                    transferPager.setCursor(transferPager.getCursor() + 1);
                                }
                            }
                            if (certificateRecordAdapter.getList().size() == response.getTotal()) {
                                binding.refreshLayout.setNoMoreData(true);
                            }
                            binding.emptyView.setVisibility(certificateRecordAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            binding.emptyView.setText("暂无内容～");
                        } else {
                            ToastUtils.showShort(getActivity(), response.getMessage());
                        }
                    }
                });

    }


    /**
     * 犬只过户-狗证过户列表
     */
    public void getCancelDogList(boolean isRefresh) {
        SendRequest.getCancelDogList(id, adoptionPager.getCursor(), adoptionPager.getSize(),
                new GenericsCallback<Pager<RecordAdoption>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            binding.refreshLayout.finishRefresh();
                        } else {
                            binding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            binding.refreshLayout.finishRefresh(false);
                        } else {
                            binding.refreshLayout.finishLoadMore(false);
                        }
                        ToastUtils.showShort(getActivity(), "获取信息失败");
                    }

                    @Override
                    public void onResponse(Pager<RecordAdoption> response, int id) {
                        adoptionPager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                adoptionRecordAdapter.refreshData(response.getRows());
                            } else {
                                adoptionRecordAdapter.loadMoreData(response.getRows());
                                if (adoptionRecordAdapter.getList().size() < response.getTotal()) {
                                    adoptionPager.setCursor(adoptionPager.getCursor() + 1);
                                }
                            }
                            if (adoptionRecordAdapter.getList().size() == response.getTotal()) {
                                binding.refreshLayout.setNoMoreData(true);
                            }
                            binding.emptyView.setVisibility(adoptionRecordAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            binding.emptyView.setText("暂无内容～");
                        } else {
                            ToastUtils.showShort(getActivity(), response.getMessage());
                        }
                    }
                });

    }

}
