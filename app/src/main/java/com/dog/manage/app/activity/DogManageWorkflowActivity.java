package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogManageWorkflowBinding;

/**
 * 犬只办理
 */
public class DogManageWorkflowActivity extends BaseActivity {

    private ActivityDogManageWorkflowBinding binding;
    private int type = 0;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_manage_workflow);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);
        title = getIntent().getStringExtra("title");
        binding.titleView.binding.itemTitle.setText(title);

        if (type == 0) {
            binding.firstStepView.setText("填写犬主身份信息、地址信息上传证件照片等");
            binding.secondStepView.setText("填写犬只相关信息、犬只照片进行鼻纹识别等操作");
            binding.thirdStepView.setText("确认操作结果,查看业务归属地派出所,完成提交审核");

        } else if (type == 1) {
            binding.firstStepView.setText("填写犬主身份信息、地址信息上传证件照片等");
            binding.secondStepView.setText("填写犬只相关信息、犬只照片进行鼻纹识别等操作");
            binding.thirdStepView.setText("选择医院进行疫苗接种,完成后提交,即可前往接种");

        } else if (type == 2) {
            binding.firstStepView.setText("查看当前犬证到期时间,与是否提示年审通知");
            binding.secondStepView.setText("填写并复查犬主相关信息、犬只相关信息");
            binding.thirdStepView.setText("填写犬只相关信息、犬只照片等信息并提交年审");

        } else if (type == 3) {
            binding.firstStepView.setText("选择要进行过户操作的犬只信息并确定");
            binding.secondStepView.setText("填写犬只新犬主的姓名、与手机号码信息并确定");
            binding.thirdStepView.setText("点击提交,完成犬只过户申请的提交,审核后完成过户 ");

        } else if (type == 4) {
            binding.firstStepView.setText("浏览可领养的犬只信息,选择并确定要领养的犬只");
            binding.secondStepView.setText("填写领养犬主的相关信息、以及相关证件");
            binding.thirdStepView.setText("确认领养犬只的信息,提交领养申请,等待审核 ");

        } else if (type == 5) {
            binding.firstStepView.setText("选择犬只的注销方式并填写信息资料");
            binding.secondStepView.setText("提交申请等待审核结果完成注销 ");
            binding.thirdStepView.setVisibility(View.GONE);

        } else if (type == 6) {
            binding.firstStepView.setText("选择您要变更的犬证,确认后点击开始变更");
            binding.secondStepView.setText("按照要求有序重新填写犬证相关信息,并提交");
            binding.thirdStepView.setText("等待审核通过后,将会为您生成新的犬证 ");


        } else if (type == 7) {


        } else if (type == 8) {


        }
    }

    public void onClickConfirm(View view) {

        if (type == 0) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_certificate);
            openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

        } else if (type == 1) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_immune);
            openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

        } else if (type == 2) {
            openActivity(DogCertificateExaminedActivity.class);


        } else if (type == 3) {
            openActivity(DogTransferActivity.class);

        } else if (type == 4) {
            openActivity(DogAdoptionActivity.class);

        } else if (type == 5) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", DogLogoutDetailsActivity.type_submit);
            openActivity(DogLogoutDetailsActivity.class, bundle);

        } else if (type == 6) {
            openActivity(UpdateDogCertificateActivity.class);

        } else if (type == 7) {
            openActivity(MainDogManageWorkflowActivity.class);


        } else if (type == 8) {


        }
    }
}