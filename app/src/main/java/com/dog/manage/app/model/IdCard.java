package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class IdCard {

    /**
     * result : {"name":"华晓芸","sex":"女","ethnicity":"汉","birth":"1982-04-15","address":"广东省深圳市龙岗区坂田街道华为基地","number":"328957198204156517"}
     */

    private ResultBean result;
    private String error_code;
    private String error_msg;

    @NoArgsConstructor
    @Data
    public static class ResultBean {
        /**
         * name : 华晓芸
         * sex : 女
         * ethnicity : 汉
         * birth : 1982-04-15
         * address : 广东省深圳市龙岗区坂田街道华为基地
         * number : 328957198204156517
         */

        private String name;
        private String sex;
        private String ethnicity;
        private String birth;
        private String address;
        private String number;
    }
}
