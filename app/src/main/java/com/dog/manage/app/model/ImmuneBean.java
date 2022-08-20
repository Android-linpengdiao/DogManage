package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImmuneBean implements Serializable {

    /**
     * idNum : 2022060106202144627878
     * immuneId : 15
     * dogType : 金毛寻回犬
     * dogId : 38
     */

    private String idNum;
    private Integer immuneId;
    private String dogType;
    private Integer dogId;
    private Integer type;
}
