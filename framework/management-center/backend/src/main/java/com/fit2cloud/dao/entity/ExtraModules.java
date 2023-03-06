package com.fit2cloud.dao.entity;

import lombok.Data;

import java.util.List;

@Data
public class ExtraModules {

    private String version;

    private List<ExtraModule> modules;


}
