package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogLicenceDetail {

    private Integer orderId;
    private Integer lincenceId;
    private Integer licenceStatus;
    private String dogType;
    private String dogAge;
    private String createdTime;
    private Integer userId;
    private Integer dogId;
    private String userName;
    private String acceptUnit;
    private Integer price;
    private String rejectionReason;
    private Integer payTypeId;
}
