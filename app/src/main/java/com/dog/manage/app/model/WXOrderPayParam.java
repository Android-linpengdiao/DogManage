package com.dog.manage.app.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WXOrderPayParam {

    /**
     * orderInfo : {"package":"Sign=WXPay","sign":"46194D11DF23A892C80E6CC37933F066","noncestr":"WgtnkS6B0mTPIEU8","appid":"wx3c296fc08c23b4c8","partnerid":"1628224372","prepayid":"wx0709254622953968448147e96ed8a80000","timestamp":1659835546}
     * state : 1
     */

    private OrderInfoBean orderInfo;
    private Integer state;

    @NoArgsConstructor
    @Data
    public static class OrderInfoBean {
        /**
         * package : Sign=WXPay
         * sign : 46194D11DF23A892C80E6CC37933F066
         * noncestr : WgtnkS6B0mTPIEU8
         * appid : wx3c296fc08c23b4c8
         * partnerid : 1628224372
         * prepayid : wx0709254622953968448147e96ed8a80000
         * timestamp : 1659835546
         */

        private String sign;
        private String noncestr;
        private String appid;
        private String partnerid;
        private String prepayid;
        private Integer timestamp;
    }
}
