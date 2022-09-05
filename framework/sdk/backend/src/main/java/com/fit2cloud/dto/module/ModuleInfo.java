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
public class ModuleInfo implements Serializable {


    @Serial
    private static final long serialVersionUID = 107150029872824142L;

    private String name;
    private String nameZhTw;
    private String nameEn;
    private String basePath;
    private String icon;
    private int order;

}
