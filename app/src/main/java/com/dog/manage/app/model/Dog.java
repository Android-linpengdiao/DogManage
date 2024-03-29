package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Dog implements Serializable {

    private Integer dogId  = 0;
    private Integer lincenceId = 0;
    private Integer licenceStatus;
    private String dogType;
    private String dogAge;
    private String createdTime;
    private Integer userId;
    private String userName;
    private String acceptUnit;
    private Integer price;
    private Object rejectionReason;
    private String payTypeId;

    private String idNum;
    private String immuneId;
    private String dogNum;
    private String dogName;
    private String dogColor;
    private Integer dogGender;//0-雌性 1-雄性
    private Integer sterilization;
    private String sterilizationProve;
    private String dogPhoto;
    private String noseprint;
    private Integer leaveId = 0;
    private Integer id = 0;
    private Integer adoptStatus = 0;//领养状态 0 正常 1 领养
    private String adoptNum;
    private Integer type = 1;//1 免疫证 2 领养

    public Dog(Integer dogId, String dogType, String idNum) {
        this.dogId = dogId;
        this.dogType = dogType;
        this.idNum = idNum;
    }

    public Dog(Integer dogId, Integer lincenceId, String dogType) {
        this.dogId = dogId;
        this.lincenceId = lincenceId;
        this.dogType = dogType;
    }
}
