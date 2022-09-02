package com.fit2cloud.dto.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Accessors(chain = true)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Module implements Serializable {

    @Serial
    private static final long serialVersionUID = 1570448947064332552L;

    private String id;
    private String name;
    private String basePath;
    private String icon;
    private int order;

}
