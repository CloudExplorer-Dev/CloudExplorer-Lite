package com.fit2cloud.dto;

/**
 * 优化建议
 * @author jianneng
 * @date 2023/1/16 10:25
 **/
public enum OptimizeSuggestEnum {
    Reduce("Reduce","降配"),
    Improve("Improve","升配"),
    Recovery("Recovery","回收"),
    ConvertByCycle("ConvertByCycle","转包年包月"),
    ConvertByVolume("ConvertByVolume","转按需按量"),
    ;
    private String value;
    private String name;

    public String getValue(){
        return value;
    }

    public String getName(){
        return name;
    }

    OptimizeSuggestEnum(String value,String name){
        this.value = value;
        this.name = name;
    }
}
