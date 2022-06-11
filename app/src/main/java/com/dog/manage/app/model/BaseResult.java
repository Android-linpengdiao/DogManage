package com.dog.manage.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseResult {

    /**
     * msg : 操作成功
     * code : 200
     */

    private String msg;
    private Integer code;
}
