package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecordAdoption {

    //领养记录
    /**
     * "id":3,
     * "adoptNum":"LY16550043156770",
     * "leaveDogId":1,
     * "useId":19,
     * "orderId":null,
     * "acceptUnit":"留检所",
     * "status":1,
     * "rejectionReason":null,
     * "applyTime":"2022-05-28 14:48:36",
     * "dogName":"牧羊犬",
     * "dogColor":"白色",
     * "dogGender":0,
     * "leaveId":null,
     * "price":null,
     * "payType":null,
     * "acceptUnitAddress":"北京市"
     */

    private Integer id;
    private String adoptNum;
    private Integer leaveDogId;
    private Integer useId = 0;
    private Integer dogId = 0;
    private Integer orderId;
    private String acceptUnit;
    private Integer status;
    private String rejectionReason;
    private String applyTime;
    private String userName;
    private String dogType;
    private String dogName;
    private String dogColor;
    private Integer dogGender;
    private String dogAge;
    private String dogPhoto;
    private Integer leaveId;
    private String price;
    private Integer payType;
    private String acceptUnitAddress;

}
