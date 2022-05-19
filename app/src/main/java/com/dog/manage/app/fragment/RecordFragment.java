package com.dog.manage.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.BaseData;
import com.base.utils.CommonUtil;
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
import com.dog.manage.app.model.RecordImmune;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

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
//                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

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
//                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            } else if (type == RecordActivity.type_transfer) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", (Integer) object);
                        openActivity(TransferDetailsActivity.class, bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
//                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

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
                adoptionRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

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
//                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            }


        }

        setRefresh();

        return binding.getRoot();
    }


    private void setRefresh() {
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (type == RecordActivity.type_certificate) {
                    getUserDogLicence();

                } else if (type == RecordActivity.type_immune) {
                    getDogImmuneStatusList();

                }
            }
        });
        binding.refreshLayout.setEnableLoadMore(false);
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
                }
            }
        });

    }

}
