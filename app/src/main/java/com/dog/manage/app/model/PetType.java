package com.dog.manage.app.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PetType implements Serializable {

    private Integer code;
    private DataBean data;
    private String message;
    private Integer status;
    private String serialNo;
    private String reqId;

    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable{
        private List<PetBean> pet;
        private String petType;
        private String url;

        @NoArgsConstructor
        @Data
        public static class PetBean implements Serializable{
            private List<IdentificationBean> identification;

            @NoArgsConstructor
            @Data
            public static class IdentificationBean implements Serializable{
                private String english_name;
                private String chinese_name;
                private Double confidence;
            }
        }
    }
}
