package com.dog.manage.app.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PetType {

    private Integer code;
    private DataBean data;
    private String message;
    private Integer status;
    private String serialNo;
    private String reqId;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<PetBean> pet;
        private String petType;
        private String url;

        @NoArgsConstructor
        @Data
        public static class PetBean {
            private List<IdentificationBean> identification;

            @NoArgsConstructor
            @Data
            public static class IdentificationBean {
                private String english_name;
                private String chinese_name;
                private Double confidence;
            }
        }
    }
}
