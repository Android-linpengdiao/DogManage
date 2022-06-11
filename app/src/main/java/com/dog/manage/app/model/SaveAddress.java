package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SaveAddress implements Serializable {

    private String acceptUnit;
    private String lincenceId;
    private String unitId;
    private String dogType;
    private String addressId;
}
