package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogLicenseDetail implements Serializable {

    /**
     * detailResultRespVo : {"lincenceId":6,"idNum":"20220509164930A0016","awardTime":null,"dogColor":"addadad","dogGender":0,"dogType":"柴犬","orgName":"张三","detailedAddress":"北京昌平aa啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿","bizImmuneStatus":false}
     * dogLicenseUserDetailVo : {"userName":"张三","idNum":"1234567890","contactPhoneNum":null,"detailedAddress":"北京昌平aa啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿"}
     * bizLicenseApproveDetailVo : {"expireTime":null,"acceptUnit":"北京市西城区天桥派出所","approveUserId":0,"approveUserName":null}
     */

    private DetailResultRespVoBean detailResultRespVo;
    private DogLicenseUserDetailVoBean dogLicenseUserDetailVo;
    private BizLicenseApproveDetailVoBean bizLicenseApproveDetailVo;

    @NoArgsConstructor
    @Data
    public static class DetailResultRespVoBean implements Serializable {
        /**
         * lincenceId : 6
         * idNum : 20220509164930A0016
         * awardTime : null
         * dogColor : addadad
         * dogGender : 0
         * dogType : 柴犬
         * orgName : 张三
         * detailedAddress : 北京昌平aa啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿
         * bizImmuneStatus : false
         */

        private Integer lincenceId;
        private String idNum;
        private String awardTime;
        private String dogColor;
        private Integer dogGender;
        private String dogType;
        private String orgName;
        private String immunePhoto;
        private String detailedAddress;
        private Boolean bizImmuneStatus;
    }

    @NoArgsConstructor
    @Data
    public static class DogLicenseUserDetailVoBean implements Serializable {
        /**
         * userName : 张三
         * idNum : 1234567890
         * contactPhoneNum : null
         * detailedAddress : 北京昌平aa啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿
         */

        private String userName;
        private String idNum;
        private String contactPhoneNum;
        private String detailedAddress;
    }

    @NoArgsConstructor
    @Data
    public static class BizLicenseApproveDetailVoBean implements Serializable {
        /**
         * expireTime : null
         * acceptUnit : 北京市西城区天桥派出所
         * approveUserId : 0
         * approveUserName : null
         */

        private String expireTime;
        private String acceptUnit;
        private Integer approveUserId;
        private String approveUserName;
    }
}
