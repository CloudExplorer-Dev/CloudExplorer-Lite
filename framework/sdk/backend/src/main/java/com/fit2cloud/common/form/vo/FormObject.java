package com.fit2cloud.common.form.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FormObject {

    List<? extends com.fit2cloud.common.form.vo.Form> forms;

    Map<Integer, Map<String, Object>> stepAnnotationMap;

    Map<Integer, Map<String, Object>> groupAnnotationMap;

}
