package com.dog.manage.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Dog implements Serializable {
    private Integer dogId;
}
