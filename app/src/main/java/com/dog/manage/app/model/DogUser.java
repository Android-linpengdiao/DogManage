package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogUser {

    ////用户类型（全）;1：个人 2 ：单位
    public final static int userType_personal = 1;
    public final static int userType_organ = 2;

    private Integer id;
    private Integer userType;
    private String bizLicense;
    private String userName;
    private String orgName;
    private Integer idType;
    private String idPhoto;
    private String idNum;
    private Integer dogType;
    private Integer aged;
    private String agedProve;
    private String contactPhoneNum;
    private String address;
    private String detailedAddress;
    private String houseNum;
    private String housePhoto;
    private String dogManagement;
    private String dogDevice;

    private Integer addressId;
}
