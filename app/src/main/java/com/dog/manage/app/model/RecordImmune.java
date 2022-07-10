package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecordImmune implements Serializable {

    //犬证办理
    /**
     * "lincenceId":6,
     * "licenceStatus":1,
     * "dogType":"柴犬",
     * "dogAge":1,
     * "createdTime":"2022-05-09"
     */
    private Integer lincenceId;
    private Integer licenceStatus;
    private String createdTime;

    //犬只免疫证
    /**
     * immuneId : 1
     * dogType : 哈士奇
     * createTime : null
     * dogAge : 1
     * lincenceStatus : 2
     */

    private Integer immuneId;
    private String dogType;
    private String createTime;
    private Integer dogAge;
    private Integer lincenceStatus;

    //犬只过户
    /**
     * id
     * integer
     * 主键
     * dogLicenceId
     * integer
     * 犬证id
     * dogLicenceNum
     * string
     * 犬证编号
     * oldOwnerId
     * integer
     * 老用户
     * oldUserName
     * string
     * 老用户名称
     * newOwnerId
     * integer
     * 新用户
     * newUserName
     * string
     * 新用户名称
     * newUserPhone
     * null
     * 新用户手机号
     * acceptUnit
     * string
     * 受理单位
     * status
     * integer
     * 状态
     * applyTime
     * null
     * 受理时间
     * dogAge
     * integer
     * 犬只年龄
     */

    private Integer id;
    private Integer dogLicenceId;
    private String dogLicenceNum;
    private Integer oldOwnerId;
    private String oldUserName;
    private Integer newOwnerId;
    private String newUserName;
    private String newUserPhone;
    private String acceptUnit;
    private Integer status;
    private String applyTime;
    private String rejectionReason;
//    private Integer dogAge;

    //犬只注销
    private Integer cancelType;
    private String cancelReason;
    private String cancelImageUrl;

}
