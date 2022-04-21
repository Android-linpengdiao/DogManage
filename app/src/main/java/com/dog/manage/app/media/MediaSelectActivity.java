package com.dog.manage.app.media;

import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.GsonUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.adapter.MediaFileAdapter;
import com.dog.manage.app.databinding.ActivityMediaSelectBinding;
import com.dog.manage.app.view.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MediaSelectActivity extends BaseActivity {

    private ActivityMediaSelectBinding binding;
    private MediaFileAdapter adapter;

    public static final int MEDIA_TYPE_PHOTO = 0;
    public static final int MEDIA_TYPE_VIDEO = 1;
    public static final int MEDIA_TYPE_ALL = 2;

    private int maxNumber = 1;

    private List<MediaFile> selects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_media_select);
        addActivity(this);

        maxNumber = getIntent().getIntExtra("maxNumber", 1);

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 2));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new MediaFileAdapter(getApplication());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                MediaFile mediaFile = (MediaFile) object;
                if (mediaFile.getStatus() == 1) {
                    selects.add(mediaFile);
                } else {
                    selects.remove(mediaFile);
                }
                binding.titleView.binding.itemContent.setText("发送" + (selects.size() > 0 ? "(" + selects.size() + ")" : ""));
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

        MediaUtils.getLocalMedia(getApplicationContext(), MediaUtils.MEDIA_TYPE_ALL,
                new MediaUtils.LocalMediaCallback() {
                    @Override
                    public void onLocalMediaFileUpdate(final List<MediaFile> mediaFiles) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.loadMoreData(mediaFiles);
                            }
                        });
                    }
                });

        binding.titleView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selects.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("imageJson", GsonUtils.toJson(selects));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showShort(MediaSelectActivity.this, "请选择照片");
                }
            }
        });
    }
}