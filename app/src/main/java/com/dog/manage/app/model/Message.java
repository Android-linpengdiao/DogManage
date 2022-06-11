package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Message implements Serializable {

    /**
     * createBy
     * string
     * 创建人
     * createTime
     * string
     * 创建时间
     * updateBy
     * string
     * 更新人
     * updateTime
     * string
     * 更新时间
     * remark
     * string
     * 备注
     * imageUrl
     * string
     * 图片地址 多个图片以“，”分开
     * params
     * object
     * {0}
     * noticeId
     * integer
     * id
     * noticeTitle
     * string
     * 标题
     * noticeType
     * string
     * noticeContent
     * string
     * 内容
     * status
     * string
     * 状态
     * sonType
     * string
     * 消息类型
     */

    private Integer noticeId;
    private String noticeTitle;
    private String noticeContent;
    private String imageUrl;
    private String createTime;
    private String updateTime;
}
