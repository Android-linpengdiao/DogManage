package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Dog implements Serializable {

    private Integer dogId;
    private Integer lincenceId;
    private Integer licenceStatus;
    private String dogType;
    private Integer dogAge;
    private String createdTime;
    private Integer userId;
    private String userName;
    private String acceptUnit;
    private Integer price;
    private Object rejectionReason;
    private String payTypeId;

    private String idNum;
    private String immuneId;
    private String dogName;
    private String dogColor;
    private Integer dogGender;//0-雌性 1-雄性
    private Integer sterilization;
    private String sterilizationProve;
    private String dogPhoto;
    private String noseprint;
    private Integer leaveId;

    public Dog(Integer dogId, String dogType, String idNum) {
        this.dogId = dogId;
        this.dogType = dogType;
        this.idNum = idNum;
    }
}
