package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HandleInfo {

    /**
     * costValue : 6
     * handleUnitId : 213
     * handleUnitName : 天桥派出所
     * superiorUnitName : 西城分局
     * handleUnitAddress : 北京市
     * coordinate : null
     */

    private Integer costValue;
    private Integer handleUnitId;
    private String handleUnitName;
    private String superiorUnitName;
    private String handleUnitAddress;
    private Object coordinate;
}
