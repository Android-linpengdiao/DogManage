package com.dog.manage.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemDogImmuneHospitalBinding;
import com.dog.manage.app.databinding.ItemMessageBinding;
import com.dog.manage.app.model.Hospital;

import java.text.DecimalFormat;

public class DogImmuneHospitalAdapter extends BaseRecyclerAdapter<Hospital, ItemDogImmuneHospitalBinding> {

    private int select = -1;
    private OnClickListener onClickListener;
    private LatLng startLatLng = new LatLng(0, 0);

    public void setStartLatLng(LatLng startLatLng) {
        this.startLatLng = startLatLng;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getSelect() {
        return select;
    }

    public DogImmuneHospitalAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_dog_immune_hospital;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static final String TAG = "DogImmuneHospitalAdapte";

    @Override
    protected void onBindItem(ItemDogImmuneHospitalBinding binding, Hospital dataBean, int position) {

        binding.hospitalNameView.setText(dataBean.getHospitalName());
        binding.hospitalAddressView.setText(dataBean.getHospitalAddress());
        binding.hospitalPhoneView.setText(dataBean.getHospitalPhone());
        binding.selectedView.setSelected(select == position ? true : false);
        LatLng endLatLng = new LatLng(39.993743, 116.472995);
        if (startLatLng.latitude == 0 || startLatLng.longitude == 0 || endLatLng.latitude == 0 || endLatLng.longitude == 0) {
            binding.locationTextView.setVisibility(View.INVISIBLE);
        } else {
            float distance = AMapUtils.calculateLineDistance(startLatLng, endLatLng) / 1000;
            DecimalFormat df = new DecimalFormat("#0.00");
            binding.locationTextView.setText("距离" + df.format(distance) + "km");
            binding.locationTextView.setVisibility(View.VISIBLE);
        }
        binding.selectedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
        binding.navigateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
