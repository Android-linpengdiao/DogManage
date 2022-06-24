package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Hospital {


    /**
     * id : 102
     * hospitalName : 北京市动物疾控中心
     * hospitalAddress : admin
     * hospitalPhone : 15888888888
     * coordinate : 115.22519738192517,40.21952491712514
     * distance : 105.81
     */

    private Integer id;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;
    private String coordinate;
    private Double distance;
}
