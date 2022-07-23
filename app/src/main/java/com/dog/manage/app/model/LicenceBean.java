package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LicenceBean implements Serializable {


    /**
     * idNum : 20220601160355A00419
     * lincenceId : 33
     * dogType : 金毛寻回犬
     * awardTime : 2022-06-01
     * dogColor : 金黄色
     * dogGender : 1
     * orgName : 李先生
     * userId : 19
     * addressId : 221
     * dogId : 39
     * surplusDate : 364
     * immuneLicenceStatus : 1
     * detailedAddress : 天桥小区
     */

    private String idNum;
    private Integer lincenceId;
    private String dogType;
    private String awardTime;
    private String dogColor;
    private Integer dogGender;
    private String orgName;
    private Integer userId;
    private String addressId;
    private Integer dogId;
    private Integer surplusDate;
    private Integer immuneLicenceStatus;
    private String detailedAddress;
    private String dogPhoto;
    private Integer licenceStatus = 0;//办理状态;1：待审核 2：代缴费  3：审核驳回  4：已办结  5：已过期  6：已注销
    private Integer adoptStatus = 0;//领养状态 0 正常 1 领养
}
