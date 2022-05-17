package com.dog.manage.app.activity;

import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.DogAdapter;
import com.dog.manage.app.databinding.ActivityDogAdoptionBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.view.GridItemDecoration;
import com.okhttp.Pager;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


import okhttp3.Call;

/**
 * 犬只领养
 */
public class DogAdoptionActivity extends BaseActivity {

    private ActivityDogAdoptionBinding binding;
    private DogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_adoption);
        addActivity(this);

        binding.firstStepView.setSelected(true);
        binding.firstStepView.setText("①选择犬只");
        binding.secondStepView.setText("②犬主信息");
        binding.thirdStepView.setText("③提交审核");

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 12));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogAdapter(getApplication());
        adapter.setType(0);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                Dog dataBean = (Dog) object;
                Bundle bundle = new Bundle();
                bundle.putInt("leaveId", dataBean.getLeaveId());
                openActivity(DogDetailsThemeActivity.class, bundle);

            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        setRefresh();

    }

    private Pager<Dog> pager = new Pager<>();

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
        SendRequest.getLeaveDogPageList(pager.getCursor(), pager.getSize(),
                new GenericsCallback<Pager<Dog>>(new JsonGenericsSerializator()) {

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
                    public void onResponse(Pager<Dog> response, int id) {
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