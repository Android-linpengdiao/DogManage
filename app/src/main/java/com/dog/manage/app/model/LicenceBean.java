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
}
