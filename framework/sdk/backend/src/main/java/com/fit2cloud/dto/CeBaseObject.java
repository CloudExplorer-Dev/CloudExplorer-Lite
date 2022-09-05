package com.fit2cloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CeBaseObject implements Serializable {

    @Serial
    private static final long serialVersionUID = -178376305901134099L;

    @Getter
    @Setter
    private String module;


}
