package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PetArchives implements Serializable {

    private Integer code;
    private DataBean data;
    private String message;
    private Integer status;
    private String serialNo;
    private String reqId;

    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable{
        private Integer compareResult;
        private String url;
        private String petId;
        private String imgIndex;
        private Integer id;
    }
}
