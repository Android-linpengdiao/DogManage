package com.dog.manage.app.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogOwnerBinding;

/**
 * 犬证办理
 */
public class DogCertificateEditDogOwnerActivity extends BaseActivity {

    private ActivityDogCertificateEditDogOwnerBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog_owner);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);

        if (type == type_userInfo) {
            binding.titleView.binding.itemTitle.setText("我的信息");
            binding.stepContainer.setVisibility(View.GONE);

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("犬主信息");
            binding.secondStepView.setText("犬只信息");
            binding.thirdStepView.setText("提交信息");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("犬主信息");
            binding.secondStepView.setText("犬只信息");
            binding.thirdStepView.setText("提交信息");

        }

        initPersonal();
        binding.radioGroupDogOwner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOrgan:
                        initOrganView();

                        break;
                    case R.id.radioButtonPersonal:
                        initPersonal();

                        break;
                    default:
                        break;
                }
            }
        });


    }

    /**
     * 个人办理
     */
    private void initPersonal() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.VISIBLE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogTypeContainer.setVisibility(View.VISIBLE);
        if (binding.radioGroupDogType.getCheckedRadioButtonId() == R.id.radioButtonOldMan) {
            binding.oldManContainer.setVisibility(View.VISIBLE);
        } else {
            binding.oldManContainer.setVisibility(View.GONE);
        }
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);
        binding.houseNumberView.setVisibility(View.VISIBLE);

        binding.organNameView.setVisibility(View.GONE);
        binding.organCertificateContainer.setVisibility(View.GONE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.GONE);
        binding.facilityContainer.setVisibility(View.GONE);

        binding.dogOwnerNameView.binding.itemTitle.setText("犬主姓名");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入犬主姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("身份证号码");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入身份证号码");

        binding.addressView.binding.itemTitle.setText("居住地址");
        binding.houseHintView.setText("房产证或房屋租赁合同");

        binding.radioGroupIDCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonHaiWai:


                        break;
                    case R.id.radioButtonGanGao:


                        break;
                    case R.id.radioButtonIDCard:


                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupDogType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan://陪伴犬
                        Log.i(TAG, "onCheckedChanged: radioButtonOldMan");
                        binding.oldManContainer.setVisibility(View.VISIBLE);
                        binding.oldManOrDisabledCertificateHintView.setText("螺寡老人证明");

                        break;
                    case R.id.radioButtonDisabled://导盲犬/扶助犬
                        Log.i(TAG, "onCheckedChanged: radioButtonDisabled");
                        binding.oldManContainer.setVisibility(View.GONE);
                        binding.oldManOrDisabledCertificateHintView.setText("残疾人证");

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupOldMan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan0:
                        binding.oldManHintView.setVisibility(View.GONE);

                        break;
                    case R.id.radioButtonOldMan1:
                        binding.oldManHintView.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 单位办理
     */
    private void initOrganView() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.GONE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.GONE);
        binding.dogTypeContainer.setVisibility(View.GONE);
        binding.oldManContainer.setVisibility(View.GONE);
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);
        binding.houseNumberView.setVisibility(View.GONE);

        binding.organNameView.setVisibility(View.VISIBLE);
        binding.organCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.VISIBLE);
        binding.facilityContainer.setVisibility(View.VISIBLE);

        binding.dogOwnerNameView.binding.itemTitle.setText("法人姓名");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入法人姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("法人身份证号码");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入法人身份证号码");

        binding.addressView.binding.itemTitle.setText("养犬地址");
        binding.houseHintView.setText("养犬管理制度");


    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        openActivity(DogCertificateEditDogActivity.class, bundle);
    }
}