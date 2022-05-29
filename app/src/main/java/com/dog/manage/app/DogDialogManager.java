package com.dog.manage.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.TextAdapter;
import com.base.databinding.ViewRankDialogBinding;
import com.dog.manage.app.adapter.DogColorListAdapter;
import com.dog.manage.app.adapter.DogListAdapter;
import com.dog.manage.app.model.Dog;

import java.util.List;

public class DogDialogManager {

    private static DogDialogManager mInstance;

    private DogDialogManager() {
    }

    public static DogDialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DogDialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DogDialogManager();
                }
            }
        }
        return mInstance;
    }

    public interface Listener {
        void onItemLeft();

        void onItemRight();
    }

    public interface OnClickListener {
        void onClick(View view, Object object);
    }

    public void showDogListDialog(Activity activity, List<Dog> list, int position, OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(activity.getResources().getColor(com.base.R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View contentView = LayoutInflater.from(activity).inflate(com.base.R.layout.view_rank_dialog, null);
        window.setContentView(contentView);
        ViewRankDialogBinding binding = DataBindingUtil.bind(contentView);
        DogListAdapter dogListAdapter = new DogListAdapter(activity);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerView.setAdapter(dogListAdapter);
        dogListAdapter.setDone(position);
        dogListAdapter.refreshData(list);
        dogListAdapter.setOnClickListener(new com.base.view.OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                listener.onClick(view, object);
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void showDogColorDialog(Activity activity, List<String> list, OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(activity.getResources().getColor(com.base.R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_rank_dialog, null);
        window.setContentView(contentView);
        ViewRankDialogBinding binding = DataBindingUtil.bind(contentView);
        DogColorListAdapter dogListAdapter = new DogColorListAdapter(activity);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerView.setAdapter(dogListAdapter);
        dogListAdapter.refreshData(list);
        dogListAdapter.setOnClickListener(new com.base.view.OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                listener.onClick(view, object);
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
