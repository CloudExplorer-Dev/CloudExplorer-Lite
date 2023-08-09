package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bill_currency")
public class BillCurrency implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种code
     */
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    /**
     * 提示
     */
    @TableField("message")
    private String message;

    /**
     * 符号
     */
    @TableField("symbol")
    private String symbol;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 汇率
     */
    @TableField("exchangeRate")
    private Double exchangeRate;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
