package com.fit2cloud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class DemoObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 7938329873497218384L;

    private String key;
    private String value;


}
