package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecordImmune {

    /**
     * immuneId : 1
     * dogType : 哈士奇
     * createTime : null
     * dogAge : 1
     * lincenceStatus : 2
     */

    private Integer immuneId;
    private String dogType;
    private Object createTime;
    private Integer dogAge;
    private Integer lincenceStatus;
}
