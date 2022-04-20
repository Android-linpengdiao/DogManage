package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogLogoutDetailsBinding;

/**
 * 注销记录详情
 */
public class DogLogoutDetailsActivity extends BaseActivity {

    private ActivityDogLogoutDetailsBinding binding;
    public static final int type_submit = 0;//0-提交注销
    public static final int type_details = 1;//1-注销详情
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_logout_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);

        if (type == type_submit) {
            initSubmitView();
        }

    }

    private void initSubmitView() {
        binding.dogInfoView.setItemArrowVisible(true);
        binding.radioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        binding.pictureHintView.setText("上传照片(非必填)");
                        binding.descriptionHintView.setText("简要说明");
                        binding.pictureContainer2.setVisibility(View.INVISIBLE);
                        binding.pictureContainer3.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.radioButton2:
                        binding.pictureHintView.setText("上传无害化处理过程图片3张");
                        binding.descriptionHintView.setText("简要说明(非必填)");
                        binding.pictureContainer2.setVisibility(View.VISIBLE);
                        binding.pictureContainer3.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_logout);
        openActivity(SubmitSuccessActivity.class, bundle);
    }
}