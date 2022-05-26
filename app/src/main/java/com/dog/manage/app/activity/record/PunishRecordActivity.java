package com.dog.manage.app.activity.record;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.BaseData;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.adapter.PunishRecordAdapter;
import com.dog.manage.app.databinding.ActivityPunishRecordBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.PunishRecord;
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
 * 处罚记录
 */
public class PunishRecordActivity extends BaseActivity {

    private ActivityPunishRecordBinding binding;
    private PunishRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_punish_record);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 14),
                Color.parseColor("#FAFAFA"));
        binding.recyclerView.addItemDecoration(divider);
        adapter = new PunishRecordAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                PunishRecord dataBean = (PunishRecord) object;
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean",dataBean);
                openActivity(PunishDetailsActivity.class,bundle);
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

        setRefresh();
    }

    private Pager<PunishRecord> pager = new Pager<>();

    private void setRefresh() {
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = new Pager<>();
                loadData(true);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadData(false);

            }
        });
        binding.refreshLayout.autoRefresh();

    }

    public void loadData(boolean isRefresh) {
        SendRequest.getIllegalList(pager.getCursor(), pager.getSize(),
                new GenericsCallback<Pager<PunishRecord>>(new JsonGenericsSerializator()) {

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
                        ToastUtils.showShort(getApplicationContext(),"获取信息失败");
                    }

                    @Override
                    public void onResponse(Pager<PunishRecord> response, int id) {
                        pager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                adapter.refreshData(response.getRows());
                            } else {
                                adapter.loadMoreData(response.getRows());
                                if (adapter.getList().size() < response.getTotal()) {
                                    pager.setCursor(pager.getCursor() + 1);
                                }
                            }
                            if (adapter.getList().size() == response.getTotal()) {
                                binding.refreshLayout.setNoMoreData(true);
                            }
                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            binding.emptyView.setText("暂无犬只～");
                        }
                    }
                });

    }
}