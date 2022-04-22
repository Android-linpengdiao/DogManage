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
import com.dog.manage.app.activity.record.AdoptionRecordActivity;
import com.dog.manage.app.activity.record.PunishDetailsActivity;
import com.dog.manage.app.adapter.AdoptionRecordAdapter;
import com.dog.manage.app.adapter.CertificateRecordAdapter;
import com.dog.manage.app.adapter.PunishRecordAdapter;
import com.dog.manage.app.databinding.FragmentAdoptionRecordBinding;
import com.okhttp.Pager;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.Arrays;

import okhttp3.Call;

/**
 * 领养记录
 */
public class AdoptionRecordFragment extends BaseFragment {

    private FragmentAdoptionRecordBinding binding;

    private int type;

    private Pager<BaseData> creationPager = new Pager<>();
    private AdoptionRecordAdapter adoptionRecordAdapter;
    private CertificateRecordAdapter certificateRecordAdapter;
    private PunishRecordAdapter punishRecordAdapter;

    public static AdoptionRecordFragment getInstance(int type, int id) {
        AdoptionRecordFragment fragment = new AdoptionRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_adoption_record, container, false);

        if (getArguments() != null) {

            type = getArguments().getInt("type");

//            GridItemDecoration.Builder builder = new GridItemDecoration.Builder(getActivity());
//            builder.color(R.color.transparent);
//            builder.size(CommonUtil.dip2px(getActivity(), 4));
//            binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
//            binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerView.setNestedScrollingEnabled(false);
            RecycleViewDivider divider = new RecycleViewDivider(getActivity(),
                    LinearLayoutManager.VERTICAL,
                    CommonUtil.dip2px(getActivity(), 10),
                    Color.parseColor("#FAFAFA"));
            binding.recyclerView.addItemDecoration(divider);

            binding.recyclerView.setNestedScrollingEnabled(false);

            if (type == AdoptionRecordActivity.type_certificate) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", (Integer) object);
                        openActivity(CertificateDetailsActivity.class,bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            } else if (type == AdoptionRecordActivity.type_immune) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", (Integer) object);
                        openActivity(ImmuneDetailsActivity.class,bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            } else if (type == AdoptionRecordActivity.type_transfer) {
                certificateRecordAdapter = new CertificateRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(certificateRecordAdapter);
                certificateRecordAdapter.setType(type);
                certificateRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", (Integer) object);
                        openActivity(TransferDetailsActivity.class,bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            } else if (type == AdoptionRecordActivity.type_adoption) {
                adoptionRecordAdapter = new AdoptionRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(adoptionRecordAdapter);
                adoptionRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", (Integer) object);
                        openActivity(AdoptionDetailsActivity.class,bundle);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
                adoptionRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            } else if (type == AdoptionRecordActivity.type_logout) {
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
                certificateRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            } else if (type == AdoptionRecordActivity.type_punish) {
                punishRecordAdapter = new PunishRecordAdapter(getActivity());
                binding.recyclerView.setAdapter(punishRecordAdapter);
                punishRecordAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        openActivity(PunishDetailsActivity.class);

                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
                punishRecordAdapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

            }


        }

        setRefresh();

        return binding.getRoot();
    }


    private void setRefresh() {
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                creationPager = new Pager<>();
//                loadData(true);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                loadData(false);

            }
        });
//        binding.refreshLayout.autoRefresh();

    }

    public void loadData(boolean isRefresh) {
        SendRequest.favorite_getPager(getUserInfo().getToken(), 11, creationPager.getNextCursor(),
                new GenericsCallback<Pager<BaseData>>(new JsonGenericsSerializator()) {

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
                    }

                    @Override
                    public void onResponse(Pager<BaseData> response, int id) {
                        creationPager = response;
                        if (response != null && response.getData() != null) {
//                            if (isRefresh) {
//                                adapter.refreshData(response.getData());
//                            } else {
//                                adapter.loadMoreData(response.getData());
//                            }
//                            if (!response.isHasnext()) {
//                                binding.refreshLayout.setNoMoreData(true);
//                            }
//                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
//                            binding.emptyView.setText("暂无内容～");
                        }
                    }
                });

    }

}
