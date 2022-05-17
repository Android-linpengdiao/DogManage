package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogDetail {

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private String updateTime;
    private String remark;
    private ParamsBean params;
    private Integer id;
    private String idNum;
    private String dogName;
    private String dogColor;
    private Integer dogGender;//犬只性别;0:雌性， 1：雄性
    private Integer sterilization;//是否绝育;0：否 1：是
    private Object sterilizationProve;
    private Integer dogAge;
    private String dogPhoto;
    private String dogType;
    private String noseprint;
    private Integer status;
    private Integer createdBy;
    private String createdTime;
    private Integer updatedBy;
    private String updatedTime;

    private Integer leaveCenter;//留检所信息
    private Integer dogShape;//犬只体型 0 小型 1 中型
    private Integer immuneStatus;//免疫状态 0未免疫 1 已免疫
    private String immuneExprie;//免疫到期时间

    @NoArgsConstructor
    @Data
    public static class ParamsBean {
    }
}
